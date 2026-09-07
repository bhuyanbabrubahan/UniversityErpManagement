import {
Component,
EventEmitter,
Output,
inject
} from '@angular/core';

import {
FormBuilder,
ReactiveFormsModule,
Validators
} from '@angular/forms';

import {
StudentFeePaymentSearchRequest
} from '../../models/payment.model';

import {
PAYMENT_MODES,
PAYMENT_STATUSES,
RECORD_STATUSES
} from '../../shared/payment.constants';

import { PaymentStatus } from '@app/shared/enums/payment-status.enum';
import { PaymentMode } from '@app/shared/enums/payment-mode.enum';
import { RecordStatus } from '@app/shared/enums/record-status.enum';

@Component({
selector: 'app-payment-search-filter',
standalone: true,
imports: [
ReactiveFormsModule
],
templateUrl: './payment-search-filter.component.html',
styleUrl: './payment-search-filter.component.scss'
})
export class PaymentSearchFilterComponent {

private readonly fb = inject(FormBuilder);

// =====================================================
// OUTPUTS
// =====================================================

@Output()
readonly search =
new EventEmitter<StudentFeePaymentSearchRequest>();

@Output()
readonly reset =
new EventEmitter<void>();

// =====================================================
// OPTIONS
// =====================================================

readonly paymentStatuses =
PAYMENT_STATUSES;

readonly paymentModes =
PAYMENT_MODES;

readonly recordStatuses =
RECORD_STATUSES;

// =====================================================
// FORM
// =====================================================

readonly form = this.fb.group({


studentId: this.fb.control<number | null>(
  null,
  [
    Validators.min(1)
  ]
),

admissionId: this.fb.control<number | null>(
  null,
  [
    Validators.min(1)
  ]
),

academicYear: this.fb.control<string>(
  ''
),

paymentStatus: this.fb.control<PaymentStatus | null>(
  null
),

paymentMode: this.fb.control<PaymentMode | null>(
  null
),

minPaidAmount: this.fb.control<number | null>(
  null,
  [
    Validators.min(0)
  ]
),

maxPaidAmount: this.fb.control<number | null>(
  null,
  [
    Validators.min(0)
  ]
),

status: this.fb.control<RecordStatus | null>(
  null
)


});

// =====================================================
// SEARCH
// =====================================================

onSearch(): void {


if (this.form.invalid) {

  this.form.markAllAsTouched();

  return;
}


const value =
  this.form.getRawValue();


// ===================================================
// RANGE VALIDATION
// ===================================================

if (
  value.minPaidAmount !== null &&
  value.maxPaidAmount !== null &&
  value.minPaidAmount > value.maxPaidAmount
) {

  this.form.controls.maxPaidAmount.setErrors({
    range: true
  });

  this.form.controls.maxPaidAmount.markAsTouched();

  return;
}


// ===================================================
// BUILD CLEAN REQUEST
// Do not send unnecessary null / empty values
// ===================================================

const request: StudentFeePaymentSearchRequest = {};


// Student
if (
  value.studentId !== null &&
  value.studentId !== undefined
) {
  request.studentId = value.studentId;
}


// Admission
if (
  value.admissionId !== null &&
  value.admissionId !== undefined
) {
  request.admissionId = value.admissionId;
}


// Academic Year
const academicYear =
  value.academicYear?.trim();

if (academicYear) {

  request.academicYear =
    academicYear;
}


// Payment Status
if (value.paymentStatus !== null) {

  request.paymentStatus =
    value.paymentStatus;
}


// Payment Mode
if (value.paymentMode !== null) {

  request.paymentMode =
    value.paymentMode;
}


// Minimum Paid Amount
if (
  value.minPaidAmount !== null &&
  value.minPaidAmount !== undefined
) {

  request.minPaidAmount =
    value.minPaidAmount;
}


// Maximum Paid Amount
if (
  value.maxPaidAmount !== null &&
  value.maxPaidAmount !== undefined
) {

  request.maxPaidAmount =
    value.maxPaidAmount;
}


// Record Status
if (value.status !== null) {

  request.status =
    value.status;
}


// ===================================================
// EMIT
// ===================================================

this.search.emit(request);


}

// =====================================================
// RESET
// =====================================================

onReset(): void {

this.form.reset({

  studentId: null,

  admissionId: null,

  academicYear: '',

  paymentStatus: null,

  paymentMode: null,

  minPaidAmount: null,

  maxPaidAmount: null,

  status: null

});

this.reset.emit();

}

}
