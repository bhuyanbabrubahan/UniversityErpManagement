import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import {
  StudentFeePaymentRequest,
  StudentFeePaymentResponse
} from '../../models/payment.model';

import { PaymentMode } from '@app/shared/enums/payment-mode.enum';
import { PaymentStatus } from '@app/shared/enums/payment-status.enum';
import { PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-payment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './payment-form.component.html',
  styleUrl: './payment-form.component.scss'
})
export class PaymentFormComponent implements OnChanges {

  private readonly fb = inject(FormBuilder);
  private readonly paymentService = inject(PaymentService);

  @Input() payment: StudentFeePaymentResponse | null = null;
  @Input() loading = false;

  @Output() submitted = new EventEmitter<StudentFeePaymentRequest>();
  @Output() cancelled = new EventEmitter<void>();

  readonly paymentModes = Object.values(PaymentMode);
  readonly paymentStatuses = Object.values(PaymentStatus);

  readonly form = this.fb.nonNullable.group({
    studentId: [0, [Validators.required, Validators.min(1)]],
    scholarshipId: [null as number | null],
    admissionId: [0, [Validators.required, Validators.min(1)]],
    academicYear: ['', [Validators.required, Validators.maxLength(20)]],
    paidAmount: [0, [Validators.required, Validators.min(0)]],
    paymentDate: [''],
    paymentMode: [null as PaymentMode | null],
    transactionReference: ['', [Validators.maxLength(100)]],
    remarks: ['', [Validators.maxLength(500)]]
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['payment']) {
      this.patchForm();
    }
  }

  get isEditMode(): boolean {
    return !!this.payment;
  }

  get f() {
    return this.form.controls;
  }

  submit(): void {
    if (this.loading) {
      return;
    }

    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    const value = this.form.getRawValue();

    const request: StudentFeePaymentRequest = {
      studentId: value.studentId,
      scholarshipId: value.scholarshipId || null,
      admissionId: value.admissionId,
      academicYear: value.academicYear.trim(),
      paidAmount: Number(value.paidAmount),
      paymentDate: value.paymentDate || null,
      paymentMode: value.paymentMode,
      transactionReference: value.transactionReference?.trim() || null,
      remarks: value.remarks?.trim() || null
    };

    this.submitted.emit(request);
  }

  cancel(): void {
    this.cancelled.emit();
  }

  reset(): void {
    this.payment = null;
    this.form.reset({
      studentId: 0,
      scholarshipId: null,
      admissionId: 0,
      academicYear: '',
      paidAmount: 0,
      paymentDate: '',
      paymentMode: null,
      transactionReference: '',
      remarks: ''
    });
  }

  private patchForm(): void {
    if (!this.payment) {
      this.reset();
      return;
    }

    this.form.patchValue({
      studentId: this.payment.studentId,
      scholarshipId: this.payment.scholarshipId ?? null,
      admissionId: this.payment.admissionId,
      academicYear: this.payment.academicYear,
      paidAmount: this.payment.paidAmount,
      paymentDate: this.payment.paymentDate ?? '',
      paymentMode: this.payment.paymentMode ?? null,
      transactionReference: this.payment.transactionReference ?? '',
      remarks: this.payment.remarks ?? ''
    });
  }
}

