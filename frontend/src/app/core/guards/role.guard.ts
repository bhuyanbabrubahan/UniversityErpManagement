import { inject } from '@angular/core';
import {
  CanActivateFn,
  Router
} from '@angular/router';

import { StorageService } from '../services/storage.service';

export const roleGuard = (
  allowedRoles: readonly string[]
): CanActivateFn => {

  return () => {

    const storageService =
      inject(StorageService);

    const router =
      inject(Router);

    const userRole =
      storageService.getUserRole();

    // No role
    if (!userRole) {

      return router.createUrlTree([
        '/dashboard'
      ]);
    }

    // Role allowed
    if (
      allowedRoles.includes(userRole)
    ) {

      return true;
    }

    // Role not allowed

    return router.createUrlTree([
      '/dashboard'
    ]);
  };
};