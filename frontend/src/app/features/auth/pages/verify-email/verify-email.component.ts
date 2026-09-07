import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../../../shared/services/toast.service';

@Component({
    selector: 'app-verify-email',
    standalone: true,
    imports: [
        CommonModule,
        MatCardModule,
        MatIconModule,
        MatProgressSpinnerModule
    ],
    templateUrl: './verify-email.component.html',
    styleUrl: './verify-email.component.scss'
})
export class VerifyEmailComponent implements OnInit {

    // ============================================================
    // DEPENDENCIES
    // ============================================================

    private readonly route =
        inject(ActivatedRoute);

    private readonly authService =
        inject(AuthService);

    private readonly router =
        inject(Router);

    private readonly toast =
        inject(ToastService);


    // ============================================================
    // UI STATE
    // ============================================================

    loading = signal(true);

    success = signal(false);

    message =
        signal('Verifying your email...');


    // ============================================================
    // INIT
    // ============================================================

    ngOnInit(): void {

        const token =
            this.route
                .snapshot
                .queryParamMap
                .get('token');


        // ========================================================
        // INVALID TOKEN
        // ========================================================

        if (!token) {

            this.loading.set(false);

            this.message.set(
                'Invalid verification link.'
            );

            this.toast.error(
                'Invalid verification link'
            );

            return;

        }


        // ========================================================
        // VERIFY EMAIL
        // ========================================================

        this.authService
            .verifyEmail(token)
            .subscribe({

                // ==================================================
                // SUCCESS
                // ==================================================

                next: () => {

                    this.loading.set(false);

                    this.success.set(true);

                    this.message.set(
                        'Your email has been verified successfully.'
                    );


                    this.toast.success(
                        'Email verified successfully'
                    );


                    // ==============================================
                    // UPDATE VERIFICATION SESSION
                    // ==============================================

                    let data: any = {};

                    const user =
                        sessionStorage.getItem(
                            'verificationUser'
                        );


                    if (user) {

                        data =
                            JSON.parse(user);

                    }


                    data.emailVerified = true;

                    data.status =
                        data.mobileVerified
                            ? 'ACTIVE'
                            : 'PENDING_VERIFICATION';


                    sessionStorage.setItem(
                        'verificationUser',
                        JSON.stringify(data)
                    );


                    // ==============================================
                    // REDIRECT
                    // ==============================================

                    setTimeout(() => {

                        this.router.navigateByUrl(
                            '/verify-account'
                        );

                    }, 1000);

                },


                // ==================================================
                // ERROR
                // ==================================================

                error: (err) => {

                    this.loading.set(false);

                    this.success.set(false);


                    const msg =
                        err?.error?.message ||
                        'Verification failed';


                    this.message.set(msg);


                    this.toast.error(
                        msg
                    );

                }

            });

    }

}