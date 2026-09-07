import { CommonModule } from '@angular/common';

import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
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
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-verify-account',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES,
    MatProgressBarModule
  ],

  templateUrl: './verify-account.component.html',

  styleUrl: './verify-account.component.scss'
})
export class VerifyAccountComponent
  implements AfterViewInit, OnDestroy {


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
  // OTP INPUT
  // ============================================================

  @ViewChild('otpInput')
  otpInput?: ElementRef<HTMLInputElement>;


  // ============================================================
  // UI STATE
  // ============================================================

  loading =
    signal(false);

  otpSent =
    signal(false);

  emailVerified =
    signal(false);

  mobileVerified =
    signal(false);

  verificationComplete =
    signal(false);

  status =
    signal('');


  // ============================================================
  // OTP TIMER
  // ============================================================

  timer =
    new OtpTimer();


  // ============================================================
  // FORM
  // ============================================================

  verifyForm =
    this.fb.group({

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      mobile: [
        '',
        [
          Validators.required,
          Validators.pattern('^[0-9]{10}$')
        ]
      ],

      otp: [
        '',
        [
          Validators.required,
          Validators.pattern('^[0-9]{6}$')
        ]
      ]

    });


  // ============================================================
  // CONSTRUCTOR
  // ============================================================

  constructor() {
    this.loadVerificationData();
    this.refreshVerificationStatus();
  }

  private refreshVerificationStatus(): void {
    const raw = sessionStorage.getItem('verificationUser');
    if (!raw) return;
    try {
      const stored = JSON.parse(raw);
      const email = stored.email || this.verifyForm.controls.email.value;
      if (!email) return;
      this.authService.getVerificationStatus(email).subscribe({
        next: (response: any) => {
          const data = response?.data ?? response;
          if (!data) return;
          const current = { ...stored, ...data };
          sessionStorage.setItem('verificationUser', JSON.stringify(current));
          this.verifyForm.patchValue({ email: current.email || email, mobile: current.mobileNumber || current.mobile || '' });
          this.emailVerified.set(current.emailVerified === true);
          this.mobileVerified.set(current.mobileVerified === true);
          this.status.set(current.status || 'PENDING_VERIFICATION');
          this.updateVerificationState();
        },
        error: () => { /* session state remains as a safe UI fallback */ }
      });
    } catch { /* invalid session data is handled by the existing flow */ }
  }


  // ============================================================
  // LOAD VERIFICATION DATA
  // ============================================================

  loadVerificationData(): void {

    const storedUser =
      sessionStorage.getItem(
        'verificationUser'
      );


    if (!storedUser) {

      return;

    }


    try {

      const data =
        JSON.parse(storedUser);


      // --------------------------------------------------------
      // Patch form
      // --------------------------------------------------------

      this.verifyForm.patchValue({

        email:
          data.email || '',

        mobile:
          data.mobileNumber || ''

      });


      // --------------------------------------------------------
      // Restore verification state
      // --------------------------------------------------------

      this.emailVerified.set(
        data.emailVerified === true
      );

      this.mobileVerified.set(
        data.mobileVerified === true
      );


      this.status.set(
        data.status ||
        'PENDING_VERIFICATION'
      );


      // --------------------------------------------------------
      // IMPORTANT
      // --------------------------------------------------------
      // Refresh ke baad bhi success state restore hogi.
      // --------------------------------------------------------

      this.updateVerificationState();

    } catch (error) {

      console.error(
        'Verification data parsing failed',
        error
      );

    }

  }


  // ============================================================
  // UPDATE VERIFICATION STATE
  // ============================================================

  private updateVerificationState(): void {

    const complete =
      this.emailVerified() &&
      this.mobileVerified();


    this.verificationComplete.set(
      complete
    );


    if (complete) {

      this.status.set(
        'ACTIVE'
      );

    }

  }


  // ============================================================
  // AFTER VIEW INIT
  // ============================================================

  ngAfterViewInit(): void {

    setTimeout(() => {

      if (
        this.otpSent() &&
        !this.verificationComplete()
      ) {

        this.otpInput
          ?.nativeElement
          .focus();

      }

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


    input.value =
      input.value.substring(
        0,
        6
      );


    this.verifyForm.patchValue({

      otp:
        input.value

    });

  }


  // ============================================================
  // RESEND EMAIL
  // ============================================================

  resendEmail(): void {

    const emailControl =
      this.verifyForm.controls.email;


    if (emailControl.invalid) {

      emailControl.markAsTouched();

      return;

    }


    const email =
      emailControl.value;


    if (!email) {

      return;

    }


    this.loading.set(true);


    this.authService
      .resendVerificationEmail(email)
      .subscribe({

        next: () => {

          this.toast.success(
            'Verification email sent successfully'
          );

          this.loading.set(false);

        },

        error: (error) => {

          this.toast.error(
            error?.error?.message ??
            'Email resend failed'
          );

          this.loading.set(false);

        }

      });

  }


  // ============================================================
  // SEND OTP
  // ============================================================

  sendOtp(): void {

    const mobileControl =
      this.verifyForm.controls.mobile;


    if (mobileControl.invalid) {

      mobileControl.markAsTouched();

      return;

    }


    const mobile =
      mobileControl.value;


    if (!mobile) {

      return;

    }


    this.loading.set(true);


    this.authService
      .sendMobileOtp(mobile)
      .subscribe({

        next: () => {

          // ================================================
          // OTP SENT
          // ================================================

          this.otpSent.set(true);


          // Clear old OTP
          this.verifyForm.controls.otp.reset();


          // ================================================
          // START TIMER IMMEDIATELY
          // ================================================

          this.timer.start();


          this.toast.success(
            'OTP sent successfully'
          );


          this.loading.set(false);


          // Focus OTP
          setTimeout(() => {

            this.otpInput
              ?.nativeElement
              .focus();

          }, 150);

        },

        error: (error) => {

          this.toast.error(
            error?.error?.message ??
            'OTP send failed'
          );

          this.loading.set(false);

        }

      });

  }


  // ============================================================
  // RESEND OTP
  // ============================================================

  resendOtp(): void {

    const mobile =
      this.verifyForm.controls.mobile.value;


    if (!mobile) {

      this.toast.error(
        'Mobile number is required'
      );

      return;

    }


    // Timer still running
    if (!this.timer.canResend()) {

      return;

    }


    this.loading.set(true);


    this.authService
      .sendMobileOtp(mobile)
      .subscribe({

        next: () => {

          // Clear previous OTP
          this.verifyForm.controls.otp.reset();


          // ================================================
          // START TIMER AGAIN
          // ================================================

          this.timer.start();


          this.otpSent.set(true);


          this.toast.success(
            'OTP sent again successfully'
          );


          this.loading.set(false);


          setTimeout(() => {

            this.otpInput
              ?.nativeElement
              .focus();

          }, 150);

        },

        error: (error) => {

          this.toast.error(
            error?.error?.message ??
            'OTP resend failed'
          );

          this.loading.set(false);

        }

      });

  }


  // ============================================================
  // VERIFY OTP
  // ============================================================

  verifyOtp(): void {

    const otpControl =
      this.verifyForm.controls.otp;


    if (otpControl.invalid) {

      otpControl.markAsTouched();

      return;

    }


    const mobile =
      this.verifyForm.controls.mobile.value;

    const otp =
      this.verifyForm.controls.otp.value;


    if (
      !mobile ||
      !otp
    ) {

      return;

    }


    this.loading.set(true);


    this.authService
      .verifyMobileOtp(
        mobile,
        otp
      )
      .subscribe({

        next: () => {

          const storedUser =
            sessionStorage.getItem(
              'verificationUser'
            );


          if (!storedUser) {

            this.loading.set(false);

            this.toast.error(
              'Verification session expired'
            );

            return;

          }


          try {

            const user =
              JSON.parse(
                storedUser
              );


            // ================================================
            // MOBILE VERIFIED
            // ================================================

            user.mobileVerified =
              true;


            // ================================================
            // BOTH VERIFICATION CHECK
            // ================================================

            const bothVerified =
              user.emailVerified === true &&
              user.mobileVerified === true;


            user.status =
              bothVerified
                ? 'ACTIVE'
                : 'PENDING_VERIFICATION';


            // ================================================
            // SAVE TO SESSION
            // ================================================

            // IMPORTANT:
            // Isko remove NAHI karna hai.
            // Refresh ke baad isi se state restore hogi.

            sessionStorage.setItem(
              'verificationUser',
              JSON.stringify(user)
            );


            // ================================================
            // UPDATE SIGNALS
            // ================================================

            this.emailVerified.set(
              user.emailVerified === true
            );


            this.mobileVerified.set(
              user.mobileVerified === true
            );


            this.status.set(
              user.status
            );


            // ================================================
            // UPDATE COMPLETE STATE
            // ================================================

            this.updateVerificationState();


            // ================================================
            // STOP TIMER
            // ================================================

            this.timer.stop();


            // ================================================
            // HIDE OTP
            // ================================================

            this.otpSent.set(false);


            this.verifyForm.controls.otp.reset();


            // ================================================
            // SUCCESS
            // ================================================

            if (
              this.emailVerified() &&
              this.mobileVerified()
            ) {

              this.toast.success(
                'Account verified successfully'
              );

            } else {

              this.toast.success(
                'Mobile verified successfully'
              );

            }


            this.loading.set(false);

          } catch (error) {

            console.error(
              'Verification update failed',
              error
            );

            this.loading.set(false);

            this.toast.error(
              'Unable to update verification status'
            );

          }

        },

        error: (error) => {

          console.error(
            'OTP verification failed',
            error
          );


          this.toast.error(
            error?.error?.message ??
            'Invalid OTP'
          );


          this.loading.set(false);

        }

      });

  }


  // ============================================================
  // GO TO LOGIN
  // ============================================================

  goToLogin(): void {

    if (
      !this.emailVerified() ||
      !this.mobileVerified()
    ) {

      return;

    }


    // ================================================
    // NOW REMOVE SESSION
    // ================================================

    sessionStorage.removeItem(
      'verificationUser'
    );


    this.router.navigate(['/login']);

  }


  // ============================================================
  // DESTROY
  // ============================================================

  ngOnDestroy(): void {

    this.timer.stop();

  }

}