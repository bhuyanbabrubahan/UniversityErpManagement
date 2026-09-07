import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from '../../core/guards/auth.guard';
import { resetPasswordGuard } from '../../core/guards/reset-password.guard';

export const AUTH_ROUTES: Routes = [

  {
    path: 'login',
    component: LoginComponent,
    canActivate: [authGuard],
    data: {
      guestOnly: true
    }
  },

  {
    path: 'forgot-password',
    loadComponent: () =>
      import('./pages/forgot-password/forgot-password.component')
        .then(
          m => m.ForgotPasswordComponent
        )
  },

  {
    path: 'verify-forgot-password-otp',
    loadComponent: () =>
      import('./pages/verify-forgot-password-otp/verify-forgot-password-otp.component')
        .then(
          m => m.VerifyForgotPasswordOtpComponent
        )
  },

  {
    path: 'reset-password',
    canActivate: [
      resetPasswordGuard
    ],
    loadComponent: () =>
      import('./pages/reset-password/reset-password.component')
        .then(
          m => m.ResetPasswordComponent
        )
  },

  {
    path: 'verify-account',
    loadComponent: () =>
      import('./pages/verify-account/verify-account.component')
        .then(
          m => m.VerifyAccountComponent
        )
  },

  {
    path: 'verify-email',
    loadComponent: () =>
      import('./pages/verify-email/verify-email.component')
        .then(
          m => m.VerifyEmailComponent
        )
  }

];