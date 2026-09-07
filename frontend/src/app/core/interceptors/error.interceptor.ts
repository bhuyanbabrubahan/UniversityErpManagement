import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';

import { inject } from '@angular/core';

import {
  catchError,
  throwError
} from 'rxjs';

import { NotificationService }
  from '../services/notification.service';

export const errorInterceptor: HttpInterceptorFn =
  (req, next) => {

    const notification =
      inject(NotificationService);

    return next(req).pipe(

      catchError((error: HttpErrorResponse) => {

        const apiMessage =
          error.error?.message;

        // --------------------------------------------------
        // 401
        // --------------------------------------------------
        // Refresh interceptor already handles 401.
        // Do not show another error/toast here.

        if (error.status === 401) {

          console.log(
            '[ERROR] 401 passed through:',
            req.url
          );

          return throwError(
            () => error
          );
        }

        // --------------------------------------------------
        // 403
        // --------------------------------------------------

        if (error.status === 403) {

          notification.error(
            apiMessage ??
            'You are not authorized.'
          );
        }

        // --------------------------------------------------
        // 404
        // --------------------------------------------------

        else if (error.status === 404) {

          notification.error(
            apiMessage ??
            'Requested resource not found.'
          );
        }

        // --------------------------------------------------
        // 500+
        // --------------------------------------------------

        else if (error.status >= 500) {

          notification.error(
            apiMessage ??
            'Internal server error.'
          );
        }

        return throwError(
          () => error
        );
      })
    );
  };