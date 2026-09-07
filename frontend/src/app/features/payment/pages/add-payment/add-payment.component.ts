import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { PaymentFormComponent } from '../../components/payment-form/payment-form.component';

import {
  StudentFeePaymentRequest
} from '../../models/payment.model';

import { PaymentService } from '../../services/payment.service';

import { ToastService } from '@app/shared/services/toast.service';


@Component({
  selector: 'app-add-payment',
  standalone: true,

  imports: [
    CommonModule,
    ...MATERIAL_MODULES,
    PaymentFormComponent
  ],

  templateUrl: './add-payment.component.html',
  styleUrl: './add-payment.component.scss'
})
export class AddPaymentComponent {

  // ==========================================================
  // SERVICES
  // ==========================================================

  private readonly router =
    inject(Router);

  private readonly paymentService =
    inject(PaymentService);

  private readonly toastService =
    inject(ToastService);


  // ==========================================================
  // SUBMIT PAYMENT
  // ==========================================================

  onSubmitted(
    request: StudentFeePaymentRequest
  ): void {

    this.paymentService
      .createPayment(request)
      .subscribe({

        // ====================================================
        // SUCCESS
        // ====================================================

        next: response => {

          this.toastService.success(
            response?.message ??
            'Payment created successfully.'
          );

          this.router.navigate([
            '/dashboard/payment/list'
          ]);

        },


        // ====================================================
        // ERROR
        // ====================================================

        error: error => {

          console.error(
            'Payment Create Failed:',
            error
          );

          this.toastService.error(
            error?.error?.message ??
            'Unable to create payment.'
          );

        }

      });

  }


  // ==========================================================
  // CANCEL
  // ==========================================================

  onCancel(): void {

    this.router.navigate([
      '/dashboard/payment/list'
    ]);

  }

}