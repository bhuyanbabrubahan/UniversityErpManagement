import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { PaymentFormComponent } from '../../components/payment-form/payment-form.component';

import {
  StudentFeePaymentRequest,
  StudentFeePaymentResponse
} from '../../models/payment.model';

import { PaymentService } from '../../services/payment.service';

import { ToastService } from '@app/shared/services/toast.service';

@Component({
  selector: 'app-edit-payment',
  standalone: true,

  imports: [
    PaymentFormComponent
  ],

  templateUrl: './edit-payment.component.html',
  styleUrl: './edit-payment.component.scss'
})
export class EditPaymentComponent implements OnInit {

  // =====================================================
  // SERVICES
  // =====================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly paymentService =
    inject(PaymentService);

  private readonly toastService =
    inject(ToastService);


  // =====================================================
  // STATE
  // =====================================================

  payment: StudentFeePaymentResponse | null = null;

  loading = false;

  submitting = false;


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {
    this.loadPayment();
  }


  // =====================================================
  // LOAD PAYMENT
  // =====================================================

  loadPayment(): void {

    const id = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (
      !Number.isInteger(id) ||
      id <= 0
    ) {

      this.toastService.error(
        'Invalid payment ID.'
      );

      this.router.navigate([
        '/dashboard/payment/list'
      ]);

      return;
    }

    this.loading = true;

    this.payment = null;

    this.paymentService
      .getPaymentById(id)
      .subscribe({

        next: response => {

          this.loading = false;

          if (
            response?.success &&
            response.data
          ) {

            this.payment = response.data;

          } else {

            this.toastService.error(
              response?.message ||
              'Payment not found.'
            );

          }

        },

        error: error => {

          this.loading = false;

          this.toastService.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load payment details.'
          );

        }

      });
  }


  // =====================================================
  // UPDATE PAYMENT
  // =====================================================

  updatePayment(
    request: StudentFeePaymentRequest
  ): void {

    if (
      this.submitting ||
      !this.payment?.id
    ) {
      return;
    }

    this.submitting = true;

    this.paymentService
      .updatePayment(
        this.payment.id,
        request
      )
      .subscribe({

        next: response => {

          this.submitting = false;

          if (
            response?.success &&
            response.data
          ) {

            this.payment = response.data;

            this.toastService.success(
              response.message ||
              'Fee payment updated successfully.'
            );

            this.router.navigate([
              '/dashboard/payment/list'
            ]);

          } else {

            this.toastService.error(
              response?.message ||
              'Unable to update fee payment.'
            );

          }

        },

        error: error => {

          this.submitting = false;

          this.toastService.error(
            error?.error?.message ||
            error?.message ||
            'Unable to update fee payment.'
          );

        }

      });
  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    if (this.submitting) {
      return;
    }

    this.router.navigate([
      '/dashboard/payment/list'
    ]);
  }

}