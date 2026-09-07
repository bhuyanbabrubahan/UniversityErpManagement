import { CommonModule } from '@angular/common';

import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
  inject,
  signal
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { ToastService } from '../../../../shared/services/toast.service';
import { OtpTimer } from '../../../../shared/utils/otp-timer.util';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-verify-forgot-password-otp',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES
  ],

  templateUrl:
    './verify-forgot-password-otp.component.html',

  styleUrl:
    './verify-forgot-password-otp.component.scss'
})
export class VerifyForgotPasswordOtpComponent
  implements OnInit, OnDestroy, AfterViewInit {


  // ============================================================
  // OTP INPUT
  // ============================================================

  @ViewChild('otpInput')
  otpInput!: ElementRef<HTMLInputElement>;


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
  // OTP TIMER
  // ============================================================

  timer = new OtpTimer();


  // ============================================================
  // MOBILE NUMBER
  // ============================================================

  mobileNumber =
    sessionStorage.getItem(
      'forgotMobile'
    );


  // ============================================================
  // OTP FORM
  // ============================================================

  otpForm = this.fb.group({

    otp: [
      '',
      [
        Validators.required,

        Validators.pattern(
          '^[0-9]{6}$'
        )
      ]
    ]

  });


  // ============================================================
  // OTP CONTROL
  // ============================================================

  get otp() {
    return this.otpForm.controls.otp;
  }


  // ============================================================
  // INIT
  // ============================================================

  ngOnInit(): void {

    this.timer.start();

  }


  // ============================================================
  // AFTER VIEW INIT
  // ============================================================

  ngAfterViewInit(): void {

    setTimeout(() => {

      this.otpInput
        ?.nativeElement
        .focus();

    }, 300);

  }


  // ============================================================
  // OTP INPUT
  // ============================================================

  onOtpInput(event: Event): void {

    const input =
      event.target as HTMLInputElement;


    input.value =
      input.value.replace(
        /[^0-9]/g,
        ''
      );


    this.otpForm.patchValue({

      otp: input.value

    });

  }


  // ============================================================
  // VERIFY OTP
  // ============================================================

  verifyOtp(): void {

    if (this.otpForm.invalid) {

      this.otpForm.markAllAsTouched();

      return;
    }


    // Mobile number missing
    if (!this.mobileNumber) {

      this.toast.error(
        'Mobile number missing.'
      );

      return;
    }


    this.loading.set(true);


    this.authService
      .verifyForgotPasswordOtp({

        mobileNumber:
          this.mobileNumber,

        otp:
          this.otpForm.value.otp!

      })
      .subscribe({

        // ======================================================
        // SUCCESS
        // ======================================================

        next: (response) => {

          this.toast.success(
            response.message ??
            'OTP verified successfully.'
          );


          // Store reset token
          sessionStorage.setItem(
            'resetToken',
            response.data.token
          );


          this.loading.set(false);


          // Navigate to reset password
          setTimeout(() => {

            this.router.navigate([
              '/reset-password'
            ]);

          }, 800);

        },


        // ======================================================
        // ERROR
        // ======================================================

        error: (error) => {

          console.error(
            'OTP Verification Failed',
            error
          );


          this.toast.error(
            error.error?.message ??
            'Invalid OTP'
          );


          this.loading.set(false);

        }

      });

  }


  // ============================================================
  // RESEND OTP
  // ============================================================

  resendOtp(): void {

    if (!this.mobileNumber) {

      this.toast.error(
        'Mobile number missing.'
      );

      return;
    }


    this.loading.set(true);


    this.authService
      .forgotPassword({

        mobileNumber:
          this.mobileNumber

      })
      .subscribe({

        // ======================================================
        // SUCCESS
        // ======================================================

        next: () => {

          this.toast.success(
            'OTP sent again successfully.'
          );


          this.timer.start();


          this.loading.set(false);

        },


        // ======================================================
        // ERROR
        // ======================================================

        error: (error) => {

          this.toast.error(
            error.error?.message ??
            'OTP resend failed.'
          );


          this.loading.set(false);

        }

      });

  }


  // ============================================================
  // DESTROY
  // ============================================================

  ngOnDestroy(): void {

    this.timer.stop();

  }


  // ============================================================
  // BACK
  // ============================================================

  back(): void {

    this.router.navigate([
      '/forgot-password'
    ]);

  }

}