import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { StorageService } from '../services/storage.service';

export const authGuard: CanActivateFn = (route) => {

    const storageService = inject(StorageService);
    const router = inject(Router);

    const token = storageService.getAccessToken();

    const guestOnly = route.data?.['guestOnly'];

    // Guest pages
    // Login/Register pages
    // Logged-in user ko allow nahi karna

    if (guestOnly) {

        if (token) {

            return router.createUrlTree([
                '/dashboard'
            ]);

        }

        return true;
    }


    // Protected pages
    // Dashboard/Profile/Students

    if (token) {

        return true;

    }


    return router.createUrlTree([
        '/login'
    ]);

};