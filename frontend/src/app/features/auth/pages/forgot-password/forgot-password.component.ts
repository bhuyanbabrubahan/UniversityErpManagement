import { Component, inject, signal } from '@angular/core';

import { CommonModule } from '@angular/common';

// Forms
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

// Router
import { Router } from '@angular/router';

// Material
import { MATERIAL_MODULES } from '../../../../shared/material/material';

// Services
import { AuthService } from '../../services/auth.service';
import { ToastService } from '@app/shared/services/toast.service';



@Component({
  selector: 'app-forgot-password',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES
  ],

  templateUrl: './forgot-password.component.html',

  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly fb =
    inject(FormBuilder);

  private readonly authService =
    inject(AuthService);

  private readonly router =
    inject(Router);

  private readonly toast =
    inject(ToastService);


  // ============================================================
  // UI STATE
  // ============================================================

  loading = signal(false);


  // ============================================================
  // FORM
  // ============================================================

  forgotForm =
    this.fb.nonNullable.group({

      mobileNumber: [
        '',
        [
          Validators.required,

          Validators.pattern(
            /^[6-9][0-9]{9}$/
          )
        ]
      ]

    });


  // ============================================================
  // FORM CONTROL
  // ============================================================

  get mobileNumber() {
    return this.forgotForm.controls.mobileNumber;
  }


  // ============================================================
  // MOBILE INPUT
  // ============================================================

  onMobileInput(event: Event): void {

    const input =
      event.target as HTMLInputElement;


    // Keep digits only
    let value =
      input.value.replace(/\D/g, '');


    // Maximum 10 digits
    value =
      value.substring(0, 10);


    input.value = value;


    this.mobileNumber.setValue(
      value,
      {
        emitEvent: false
      }
    );


    this.mobileNumber.markAsDirty();


    this.mobileNumber.updateValueAndValidity({
      emitEvent: false
    });

  }


  // ============================================================
  // SEND OTP
  // ============================================================

  sendOtp(): void {

    // Prevent duplicate request
    if (this.loading()) {
      return;
    }


    // Validate form
    if (this.forgotForm.invalid) {

      this.forgotForm.markAllAsTouched();

      return;
    }


    const mobileNumber =
      this.mobileNumber.value.trim();


    // Extra defensive validation
    if (
      !/^[6-9][0-9]{9}$/.test(
        mobileNumber
      )
    ) {

      this.mobileNumber.setErrors({
        invalidMobile: true
      });


      this.mobileNumber.markAsTouched();

      return;
    }


    // Start loading
    this.loading.set(true);


    // API request
    this.authService
      .forgotPassword({
        mobileNumber
      })
      .subscribe({

        // ======================================================
        // SUCCESS
        // ======================================================

        next: (response) => {

          this.loading.set(false);


          // Toast success
          this.toast.success(
            response?.message ??
            'OTP sent successfully.'
          );


          // Store mobile for OTP verification
          sessionStorage.setItem(
            'forgotMobile',
            mobileNumber
          );


          // Navigate to OTP page
          setTimeout(() => {

            this.router.navigate([
              '/verify-forgot-password-otp'
            ]);

          }, 500);

        },


        // ======================================================
        // ERROR
        // ======================================================

        error: (error) => {

          console.error(
            'Forgot password failed:',
            error
          );


          this.loading.set(false);


          // Toast error
          this.toast.error(
            error?.error?.message ??
            'Unable to send OTP. Please try again.'
          );

        }

      });

  }


  // ============================================================
  // BACK TO LOGIN
  // ============================================================

  backToLogin(): void {

    if (this.loading()) {
      return;
    }


    this.router.navigate([
      '/login'
    ]);

  }

}