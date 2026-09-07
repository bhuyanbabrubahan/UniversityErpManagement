import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

import { StorageService } from '../../../../core/services/storage.service';
import { CurrentUserService } from '../../../../core/services/current-user.service';
import { ToastService } from '../../../../shared/services/toast.service';

import { AuthData } from '../../models/auth-data.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,

    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinner
  ],

  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  // ============================================================
  // SERVICES
  // ============================================================

  private readonly fb = inject(FormBuilder);

  private readonly authService = inject(AuthService);

  private readonly storageService = inject(StorageService);

  private readonly currentUserService =
    inject(CurrentUserService);

  private readonly toast =
    inject(ToastService);

  private readonly router =
    inject(Router);


  // ============================================================
  // UI STATE
  // ============================================================

  readonly loading = signal(false);

  readonly showPassword = signal(false);


  // ============================================================
  // FORM
  // ============================================================

  readonly loginForm = this.fb.nonNullable.group({

    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],

    password: [
      '',
      [
        Validators.required,
        Validators.minLength(6)
      ]
    ]

  });


  // ============================================================
  // FORM GETTERS
  // ============================================================

  get email() {
    return this.loginForm.controls.email;
  }

  get password() {
    return this.loginForm.controls.password;
  }


  // ============================================================
  // PASSWORD VISIBILITY
  // ============================================================

  togglePassword(): void {

    this.showPassword.update(
      value => !value
    );

  }


  // ============================================================
  // LOGIN
  // ============================================================

  onSubmit(): void {

    // Prevent double submit
    if (this.loading()) {
      return;
    }


    // Validate form
    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();

      return;
    }


    const loginRequest = {

      email: this.email.value.trim(),

      password: this.password.value

    };


    console.log(
      '[Login] Starting login:',
      loginRequest.email
    );


    this.loading.set(true);


    // ==========================================================
    // LOGIN API
    // ==========================================================

    this.authService
      .login(loginRequest)
      .subscribe({

        // ======================================================
        // SUCCESS
        // ======================================================

        next: response => {

          console.log(
            '[Login] Full response:',
            response
          );


          // ----------------------------------------------------
          // Validate response
          // ----------------------------------------------------

          if (
            !response ||
            !response.success ||
            !response.data
          ) {

            console.error(
              '[Login] Invalid login response:',
              response
            );

            this.loading.set(false);

            this.toast.error(
              response?.message ??
              'Login failed. Invalid server response.'
            );

            return;
          }


          const data =
            response.data as AuthData;


          // ----------------------------------------------------
          // Validate access token
          // ----------------------------------------------------

          if (!data.accessToken) {

            console.error(
              '[Login] Access token missing:',
              data
            );

            this.loading.set(false);

            this.toast.error(
              'Login failed. Access token was not received.'
            );

            return;
          }


          console.log(
            '[Login] Access token received'
          );


          // ====================================================
          // SAVE ACCESS TOKEN FIRST
          // ====================================================

          this.storageService.saveAccessToken(
            data.accessToken
          );


          // ====================================================
          // SAVE REFRESH TOKEN
          // ====================================================

          if (data.refreshToken) {

            this.storageService.saveRefreshToken(
              data.refreshToken
            );

          }


          // ====================================================
          // SAVE USER INFORMATION
          // ====================================================

          if (data.email) {

            this.storageService.saveUserEmail(
              data.email
            );

          }


          if (data.role) {

            this.storageService.saveUserRole(
              data.role
            );

          }


          // ----------------------------------------------------
          // IMPORTANT:
          // Verify that token is actually stored.
          // ----------------------------------------------------

          const savedToken =
            this.storageService.getAccessToken();


          console.log(
            '[Login] Token saved:',
            !!savedToken
          );


          if (!savedToken) {

            this.loading.set(false);

            this.toast.error(
              'Login failed. Unable to save authentication token.'
            );

            return;
          }


          // ====================================================
          // ACCOUNT VERIFICATION
          // ====================================================

          if (
            data.emailVerified === false ||
            data.mobileVerified === false
          ) {

            sessionStorage.setItem(
              'verificationUser',
              JSON.stringify({

                email: data.email,

                mobileNumber:
                  data.mobileNumber,

                status:
                  data.status,

                emailVerified:
                  data.emailVerified,

                mobileVerified:
                  data.mobileVerified

              })
            );


            this.loading.set(false);


            this.toast.warning(
              'Please verify your account before continuing.'
            );


            this.router.navigateByUrl(
              '/verify-account',
              {
                replaceUrl: true
              }
            );


            return;
          }


          // ====================================================
          // LOGIN COMPLETED
          // ====================================================

          this.loading.set(false);


          this.toast.success(
            'Login successful'
          );


          // ====================================================
          // DO NOT BLOCK LOGIN WITH loadCurrentUser()
          // ====================================================
          //
          // Previously this request could return 401 and your
          // refresh interceptor could call logout(), sending
          // the user back to login.
          //
          // Navigate first because access token is already saved.
          // ====================================================

          this.router.navigateByUrl(
            '/dashboard',
            {
              replaceUrl: true
            }
          );


          // ----------------------------------------------------
          // Load current user AFTER navigation.
          // ----------------------------------------------------
          //
          // This should not control whether login succeeds.
          //
          try {

            this.currentUserService
              .loadCurrentUser();

          } catch (error) {

            console.warn(
              '[Login] Current user loading failed:',
              error
            );

          }

        },


        // ======================================================
        // ERROR
        // ======================================================

        error: error => {

          console.error(
            '[Login] Login API failed:',
            error
          );


          this.loading.set(false);


          // ----------------------------------------------------
          // ACCOUNT VERIFICATION
          // ----------------------------------------------------

          if (
            error?.error?.message ===
            'ACCOUNT_VERIFICATION_PENDING'
          ) {

            if (error?.error?.data) {

              sessionStorage.setItem(
                'verificationUser',
                JSON.stringify(
                  error.error.data
                )
              );

            }


            this.toast.warning(
              'Please verify your account to continue.'
            );


            this.router.navigateByUrl(
              '/verify-account',
              {
                replaceUrl: true
              }
            );


            return;
          }


          // ----------------------------------------------------
          // NORMAL LOGIN ERROR
          // ----------------------------------------------------

          const message =
            error?.error?.message ??
            error?.message ??
            'Login failed. Please check your email and password.';


          this.toast.error(message);

        }

      });

  }

}