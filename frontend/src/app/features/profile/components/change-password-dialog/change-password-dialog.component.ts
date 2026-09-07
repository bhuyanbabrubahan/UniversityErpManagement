import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  MatDialogModule,
  MatDialogRef
} from '@angular/material/dialog';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { ProfileService } from '../../services/profile.service';
import { AuthService } from '../../../auth/services/auth.service';

import { ChangePasswordRequest } from '../../models/change-password-request.model';

import { passwordMatchValidator } from '../../../../shared/validators/password-match.validator';
import { ToastService } from '../../../../shared/services/toast.service';

import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';


@Component({
  selector: 'app-change-password-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})
export class ChangePasswordDialogComponent {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly fb =
    inject(FormBuilder);

  private readonly profileService =
    inject(ProfileService);

  private readonly authService =
    inject(AuthService);

  private readonly dialogRef =
    inject(MatDialogRef<ChangePasswordDialogComponent>);

  private readonly toast =
    inject(ToastService);


  // ============================================================
  // UI STATE
  // ============================================================

  loading = false;

  hideCurrentPassword = true;

  hideNewPassword = true;

  hideConfirmPassword = true;


  // ============================================================
  // CHANGE PASSWORD FORM
  // ============================================================

  passwordForm = this.fb.group({

    currentPassword: [
      '',
      Validators.required
    ],

    newPassword: [
      '',
      [
        Validators.required,

        Validators.minLength(8),

        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/
        )
      ]
    ],

    confirmPassword: [
      '',
      Validators.required
    ]

  }, {
    validators: passwordMatchValidator()
  });


  // ============================================================
  // FORM CONTROLS
  // ============================================================

  get currentPassword() {

    return this.passwordForm.controls.currentPassword;

  }


  get newPassword() {

    return this.passwordForm.controls.newPassword;

  }


  get confirmPassword() {

    return this.passwordForm.controls.confirmPassword;

  }


  // ============================================================
  // CLOSE DIALOG
  // ============================================================

  close(): void {

    this.dialogRef.close();

  }


  // ============================================================
  // CHANGE PASSWORD
  // ============================================================

  submit(): void {

    if (this.passwordForm.invalid) {

      this.passwordForm.markAllAsTouched();

      return;

    }


    const request: ChangePasswordRequest = {

      currentPassword:
        this.passwordForm.value.currentPassword!,

      newPassword:
        this.passwordForm.value.newPassword!,

      confirmPassword:
        this.passwordForm.value.confirmPassword!

    };


    this.loading = true;


    this.profileService
      .changePassword(request)
      .subscribe({

        // ========================================================
        // SUCCESS
        // ========================================================

        next: (response) => {

          this.loading = false;


          this.toast.success(
            response?.message ??
            'Password changed successfully.'
          );


          // Close dialog
          this.dialogRef.close(true);


          // Logout after password change
          this.authService.logout();

        },


        // ========================================================
        // ERROR
        // ========================================================

        error: (error) => {

          this.loading = false;


          console.error(
            'Password Change Failed',
            error
          );


          this.toast.error(
            error?.error?.message ??
            'Unable to change password'
          );

        }

      });

  }

}