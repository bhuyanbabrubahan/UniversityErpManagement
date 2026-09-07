import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { StorageService } from '../services/storage.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const storage = inject(StorageService);

  // ---------------------------------------------------------
  // PUBLIC AUTH APIs
  // ---------------------------------------------------------

  const publicUrls = [
    '/api/login',
    '/api/auth/register',
    '/api/auth/forgot-password',
    '/api/auth/verify-forgot-password-otp',
    '/api/auth/reset-password',
    '/api/auth/refresh-token',
    '/api/auth/resend-verification',
    '/api/auth/mobile/send',
    '/api/auth/mobile/verify',
    '/api/auth/verify-email'
  ];

  const isPublic = publicUrls.some(url =>
    req.url.includes(url)
  );

  if (isPublic) {
    return next(req);
  }

  // ---------------------------------------------------------
  // ACCESS TOKEN
  // ---------------------------------------------------------

  const accessToken = storage.getAccessToken();

  if (!accessToken) {

    console.warn(
      '[AUTH] Access token missing:',
      req.method,
      req.url
    );

    return next(req);
  }

  // ---------------------------------------------------------
  // DO NOT ADD DUPLICATE AUTHORIZATION
  // ---------------------------------------------------------

  if (req.headers.has('Authorization')) {
    return next(req);
  }

  // ---------------------------------------------------------
  // ATTACH JWT
  // ---------------------------------------------------------

  const authenticatedRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${accessToken}`
    }
  });

  console.log(
    '[AUTH] JWT attached:',
    req.method,
    req.url
  );

  return next(authenticatedRequest);
};