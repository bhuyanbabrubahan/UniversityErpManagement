import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const resetPasswordGuard: CanActivateFn = () => {

    const router = inject(Router);

    const resetToken = sessionStorage.getItem('resetToken');

    if (resetToken) {
        return true;
    }

    return router.createUrlTree([
        '/forgot-password'
    ]);

};