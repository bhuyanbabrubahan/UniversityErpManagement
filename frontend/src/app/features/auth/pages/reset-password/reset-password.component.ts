import { CommonModule } from '@angular/common';

import {
    Component,
    computed,
    inject,
    OnInit,
    signal
} from '@angular/core';

import {
    FormBuilder,
    ReactiveFormsModule,
    Validators
} from '@angular/forms';

import { Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { AuthService } from '../../services/auth.service';

import { passwordMatchValidator } from '../../../../shared/validators/password-match.validator';

import { ToastService } from '../../../../shared/services/toast.service';


@Component({
    selector: 'app-reset-password',
    standalone: true,

    imports: [
        CommonModule,
        ReactiveFormsModule,
        ...MATERIAL_MODULES
    ],

    templateUrl: './reset-password.component.html',
    styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent implements OnInit {


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

    showNewPassword = signal(false);

    showConfirmPassword = signal(false);


    // ============================================================
    // RESET TOKEN
    // ============================================================

    resetToken =
        sessionStorage.getItem('resetToken');


    // ============================================================
    // FORM
    // ============================================================

    resetForm = this.fb.group(
        {
            newPassword: [
                '',
                [
                    Validators.required,
                    Validators.minLength(8)
                ]
            ],

            confirmPassword: [
                '',
                [
                    Validators.required
                ]
            ]
        },
        {
            validators: passwordMatchValidator()
        }
    );


    // ============================================================
    // PASSWORD STRENGTH
    // ============================================================

    passwordStrength = computed(() => {

        const password =
            this.newPassword.value ?? '';

        if (!password) {
            return '';
        }

        let score = 0;

        if (password.length >= 8) score++;

        if (/[A-Z]/.test(password)) score++;

        if (/[a-z]/.test(password)) score++;

        if (/[0-9]/.test(password)) score++;

        if (/[@$!%*?&#]/.test(password)) score++;


        if (score <= 2) {
            return 'Weak';
        }

        if (score <= 4) {
            return 'Medium';
        }

        return 'Strong';

    });


    passwordStrengthPercent = computed(() => {

        const password =
            this.newPassword.value ?? '';

        if (!password) {
            return 0;
        }

        let score = 0;

        if (password.length >= 8) score++;

        if (/[A-Z]/.test(password)) score++;

        if (/[a-z]/.test(password)) score++;

        if (/[0-9]/.test(password)) score++;

        if (/[@$!%*?&#]/.test(password)) score++;

        return score * 20;

    });


    // ============================================================
    // INIT
    // ============================================================

    ngOnInit(): void {

        if (!this.resetToken) {

            this.toast.error(
                'Reset session expired. Please request again.'
            );

            this.router.navigate(
                ['/forgot-password'],
                {
                    replaceUrl: true
                }
            );

        }

    }


    // ============================================================
    // FORM CONTROLS
    // ============================================================

    get newPassword() {
        return this.resetForm.controls.newPassword;
    }


    get confirmPassword() {
        return this.resetForm.controls.confirmPassword;
    }


    // ============================================================
    // RESET PASSWORD
    // ============================================================

    resetPassword(): void {

        // --------------------------------------------------------
        // FORM VALIDATION
        // --------------------------------------------------------

        if (this.resetForm.invalid) {

            this.resetForm.markAllAsTouched();

            return;

        }


        // --------------------------------------------------------
        // TOKEN VALIDATION
        // --------------------------------------------------------

        if (!this.resetToken) {

            this.toast.error(
                'Reset token missing.'
            );

            return;

        }


        // --------------------------------------------------------
        // START LOADING
        // --------------------------------------------------------

        this.loading.set(true);


        // --------------------------------------------------------
        // API REQUEST
        // --------------------------------------------------------

        this.authService
            .resetPassword({

                token:
                    this.resetToken,

                newPassword:
                    this.resetForm.value.newPassword!,

                confirmPassword:
                    this.resetForm.value.confirmPassword!

            })
            .subscribe({

                // ==================================================
                // SUCCESS
                // ==================================================

                next: (response) => {

                    this.toast.success(
                        response?.message ??
                        'Password reset successfully.'
                    );


                    // Clear reset session
                    this.clearSession();


                    this.loading.set(false);


                    // Navigate to login
                    setTimeout(() => {

                        this.router.navigate(
                            ['/login'],
                            {
                                replaceUrl: true
                            }
                        );

                    }, 1500);

                },


                // ==================================================
                // ERROR
                // ==================================================

                error: (error) => {

                    const message =
                        error?.error?.message ??
                        'Password reset failed.';


                    this.toast.error(
                        message
                    );


                    // ------------------------------------------------
                    // INVALID / EXPIRED TOKEN
                    // ------------------------------------------------

                    const lowerMessage =
                        message.toLowerCase();

                    if (
                        lowerMessage.includes('expired') ||
                        lowerMessage.includes(
                            'invalid reset token'
                        )
                    ) {

                        this.clearSession();


                        this.router.navigate(
                            ['/forgot-password'],
                            {
                                replaceUrl: true
                            }
                        );

                    }


                    this.loading.set(false);

                }

            });

    }


    // ============================================================
    // CLEAR SESSION
    // ============================================================

    clearSession(): void {

        sessionStorage.removeItem(
            'resetToken'
        );

        sessionStorage.removeItem(
            'forgotMobile'
        );

        sessionStorage.removeItem(
            'verificationUser'
        );

    }


    // ============================================================
    // PASSWORD VISIBILITY
    // ============================================================

    toggleNewPassword(): void {

        this.showNewPassword.update(
            value => !value
        );

    }


    toggleConfirmPassword(): void {

        this.showConfirmPassword.update(
            value => !value
        );

    }


    // ============================================================
    // BACK TO LOGIN
    // ============================================================

    backToLogin(): void {

        this.router.navigate(
            ['/login'],
            {
                replaceUrl: true
            }
        );

    }


    // ============================================================
    // STRENGTH CLASS
    // ============================================================

    getStrengthClass(): string {

        switch (
            this.passwordStrength()
        ) {

            case 'Weak':
                return 'weak';

            case 'Medium':
                return 'medium';

            case 'Strong':
                return 'strong';

            default:
                return '';

        }

    }

}