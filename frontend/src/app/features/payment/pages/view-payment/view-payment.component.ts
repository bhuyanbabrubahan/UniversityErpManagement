import {
  CommonModule
} from '@angular/common';

import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  PaymentService
} from '../../services/payment.service';

import {
  StudentFeePaymentResponse
} from '../../models/payment.model';

import {
  PaymentMode
} from '@app/shared/enums/payment-mode.enum';

import {
  PaymentStatus
} from '@app/shared/enums/payment-status.enum';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';

import {
  getPaymentModeLabel,
  getPaymentStatusLabel,
  getPaymentStatusClass,
  getPaymentModeClass,
  formatCurrency
} from '../../shared/payment.utils';


@Component({
  selector: 'app-view-payment',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './view-payment.component.html',
  styleUrl: './view-payment.component.scss'
})
export class ViewPaymentComponent
  implements OnInit {


  // =====================================================
  // INJECTIONS
  // =====================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly paymentService =
    inject(PaymentService);


  // =====================================================
  // DATA
  // =====================================================

  payment:
    StudentFeePaymentResponse | null = null;


  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;

  errorMessage = '';


  // =====================================================
  // ENUMS
  // =====================================================

  readonly PaymentMode =
    PaymentMode;

  readonly PaymentStatus =
    PaymentStatus;

  readonly RecordStatus =
    RecordStatus;


  // =====================================================
  // LIFECYCLE
  // =====================================================

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    const paymentId =
      Number(id);

    if (
      !id ||
      !Number.isInteger(paymentId) ||
      paymentId <= 0
    ) {

      this.router.navigate([
        '/dashboard/payment'
      ]);

      return;
    }

    this.loadPayment(paymentId);
  }


  // =====================================================
  // LOAD PAYMENT
  // =====================================================

  private loadPayment(
    id: number
  ): void {

    this.loading = true;

    this.errorMessage = '';

    this.paymentService
      .getById(id)
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response.data
          ) {

            this.payment =
              response.data;

          } else {

            this.payment = null;

            this.errorMessage =
              response?.message ||
              'Payment not found.';
          }

          this.loading = false;
        },

        error: error => {

          this.loading = false;

          this.payment = null;

          this.errorMessage =
            error?.error?.message ||
            error?.message ||
            'Unable to load payment details.';
        }

      });
  }


  // =====================================================
  // EDIT PAYMENT
  // =====================================================

  editPayment(): void {

    if (!this.payment?.id) {
      return;
    }

    this.router.navigate([
      '/dashboard/payment/edit',
      this.payment.id
    ]);
  }


  // =====================================================
  // BACK
  // =====================================================

  goBack(): void {

    this.router.navigate([
      '/dashboard/payment'
    ]);
  }


  // =====================================================
  // PAYMENT STATUS LABEL
  // =====================================================

  getPaymentStatusLabel(
    status:
      PaymentStatus |
      null |
      undefined
  ): string {

    return getPaymentStatusLabel(status);
  }


  // =====================================================
  // PAYMENT STATUS CLASS
  // =====================================================

  getPaymentStatusClass(
    status:
      PaymentStatus |
      null |
      undefined
  ): string {

    return getPaymentStatusClass(status);
  }


  // =====================================================
  // PAYMENT MODE LABEL
  // =====================================================

  getPaymentModeLabel(
    mode:
      PaymentMode |
      null |
      undefined
  ): string {

    return getPaymentModeLabel(mode);
  }


  // =====================================================
  // PAYMENT MODE CLASS
  // =====================================================

  getPaymentModeClass(
    mode:
      PaymentMode |
      null |
      undefined
  ): string {

    return getPaymentModeClass(mode);
  }


  // =====================================================
  // CURRENCY
  // =====================================================

  formatCurrency(
    amount:
      number |
      null |
      undefined
  ): string {

    return formatCurrency(amount);
  }


  // =====================================================
  // PAYMENT PROGRESS
  // =====================================================

  getPaymentProgress(): number {

    if (!this.payment) {
      return 0;
    }

    const payable =
      Number(
        this.payment.finalPayableAmount
      );

    const paid =
      Number(
        this.payment.paidAmount
      );

    if (
      !payable ||
      payable <= 0
    ) {

      return paid > 0
        ? 100
        : 0;
    }

    return Math.min(
      Math.max(
        (paid / payable) * 100,
        0
      ),
      100
    );
  }

}