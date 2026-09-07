import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  MAT_DIALOG_DATA,
  MatDialogRef
} from '@angular/material/dialog';

import { MATERIAL_MODULES } from '../../../../shared/material/material';
import { ToastService } from '../../../../shared/services/toast.service';


@Component({
  selector: 'app-mobile-verification-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES
  ],
  templateUrl: './mobile-verification-dialog.component.html',
  styleUrl: './mobile-verification-dialog.component.scss'
})
export class MobileVerificationDialogComponent {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly fb =
    inject(FormBuilder);

  private readonly toast =
    inject(ToastService);


  constructor(

    @Inject(MAT_DIALOG_DATA)
    public data: {
      mobileNumber: string;
    },

    private dialogRef:
      MatDialogRef<MobileVerificationDialogComponent>

  ) {}


  // ============================================================
  // OTP FORM
  // ============================================================

  otpForm = this.fb.group({

    otp: [

      '',

      [
        Validators.required,

        Validators.pattern(
          /^\d{6}$/
        )
      ]

    ]

  });


  // ============================================================
  // CANCEL
  // ============================================================

  cancel(): void {

    this.dialogRef.close();

  }


  // ============================================================
  // VERIFY OTP
  // ============================================================

  verify(): void {

    if (this.otpForm.invalid) {

      this.otpForm.markAllAsTouched();

      this.toast.error(
        'Please enter a valid 6-digit OTP.'
      );

      return;

    }


    this.dialogRef.close({

      otp:
        this.otpForm.value.otp

    });

  }

}