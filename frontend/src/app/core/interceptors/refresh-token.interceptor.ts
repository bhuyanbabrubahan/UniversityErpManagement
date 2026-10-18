import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";

import { inject } from "@angular/core";

import {
  BehaviorSubject,
  catchError,
  filter,
  switchMap,
  take,
  throwError,
} from "rxjs";

import { AuthService } from "../../features/auth/services/auth.service";
import { StorageService } from "../services/storage.service";

let isRefreshing = false;

const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const refreshTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const storage = inject(StorageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // ---------------------------------------------------
      // Only handle 401
      // ---------------------------------------------------

      if (error.status !== 401) {
        return throwError(() => error);
      }

      // ---------------------------------------------------
      // NEVER refresh login / refresh API itself
      // ---------------------------------------------------

      if (
        req.url.includes("/api/auth/") ||
        req.url.includes("/api/login") ||
        req.url.includes("/api/otp/")
      ) {
        return throwError(() => error);
      }

      const refreshToken = storage.getRefreshToken();

      if (!refreshToken) {
        console.error("[REFRESH] No refresh token available");

        return throwError(() => error);
      }

      // ---------------------------------------------------
      // Another refresh already running
      // ---------------------------------------------------

      if (isRefreshing) {
        return refreshTokenSubject.pipe(
          filter((token) => token !== null),

          take(1),

          switchMap((token) => {
            const retryRequest = req.clone({
              setHeaders: {
                Authorization: `Bearer ${token}`,
              },
            });

            return next(retryRequest);
          }),
        );
      }

      // ---------------------------------------------------
      // START REFRESH
      // ---------------------------------------------------

      isRefreshing = true;

      refreshTokenSubject.next(null);

      console.log("[REFRESH] Access token expired. Refreshing...");

      return authService.refreshToken(refreshToken).pipe(
        switchMap((response) => {
          const data = response?.data;

          const newAccessToken = data?.accessToken;

          const newRefreshToken = data?.refreshToken;

          if (!newAccessToken) {
            throw new Error("Refresh API did not return accessToken.");
          }

          // Save new access token
          storage.saveAccessToken(newAccessToken);

          // Save refresh token if backend returns one
          if (newRefreshToken) {
            storage.saveRefreshToken(newRefreshToken);
          }

          isRefreshing = false;

          refreshTokenSubject.next(newAccessToken);

          console.log("[REFRESH] Token refreshed successfully");

          const retryRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newAccessToken}`,
            },
          });

          return next(retryRequest);
        }),

        catchError((refreshError) => {
          isRefreshing = false;

          refreshTokenSubject.next(null);

          const status =
            refreshError instanceof HttpErrorResponse ? refreshError.status : 0;

          // A rejected/expired/revoked refresh token cannot recover the
          // session. Clear stale credentials only for definitive auth
          // failures; retain them for temporary network/server failures.
          if ([400, 401, 403, 404].includes(status)) {
            authService.logout();
          }

          return throwError(() => refreshError);
        }),
      );
    }),
  );
};
