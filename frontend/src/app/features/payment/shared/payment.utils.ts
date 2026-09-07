// =====================================================
// PAYMENT UTILS
// =====================================================

import {
  StudentFeePaymentResponse,
  StudentFeePaymentSearchRequest
} from '../models/payment.model';


import { PaymentMode } from "@app/shared/enums/payment-mode.enum";
import { PaymentStatus } from "@app/shared/enums/payment-status.enum";


import {
  PAYMENT_MODE_LABELS,
  PAYMENT_STATUS_LABELS
} from './payment.constants';


// =====================================================
// PAYMENT STATUS LABEL
// =====================================================

export function getPaymentStatusLabel(
  status: PaymentStatus | null | undefined
): string {

  if (!status) {
    return '-';
  }

  return PAYMENT_STATUS_LABELS[status] ?? status;
}


// =====================================================
// PAYMENT MODE LABEL
// =====================================================

export function getPaymentModeLabel(
  mode: PaymentMode | null | undefined
): string {

  if (!mode) {
    return '-';
  }

  return PAYMENT_MODE_LABELS[mode] ?? mode;
}


// =====================================================
// PAYMENT STATUS CSS CLASS
// =====================================================

export function getPaymentStatusClass(
  status: PaymentStatus | null | undefined
): string {

  switch (status) {

    case PaymentStatus.PAID:
      return 'status-paid';

    case PaymentStatus.PARTIAL:
      return 'status-partial';

    case PaymentStatus.PENDING:
      return 'status-pending';

    case PaymentStatus.CANCELLED:
      return 'status-cancelled';

    default:
      return '';
  }
}


// =====================================================
// PAYMENT MODE CSS CLASS
// =====================================================

export function getPaymentModeClass(
  mode: PaymentMode | null | undefined
): string {

  switch (mode) {

    case PaymentMode.CASH:
      return 'mode-cash';

    case PaymentMode.ONLINE:
      return 'mode-online';

    case PaymentMode.CARD:
      return 'mode-card';

    case PaymentMode.UPI:
      return 'mode-upi';

    case PaymentMode.CHEQUE:
      return 'mode-cheque';

    default:
      return '';
  }
}


// =====================================================
// CURRENCY FORMAT
// =====================================================

export function formatCurrency(
  amount: number | null | undefined
): string {

  if (
    amount === null ||
    amount === undefined ||
    Number.isNaN(amount)
  ) {
    return '0.00';
  }

  return amount.toFixed(2);
}


// =====================================================
// NORMALIZE SEARCH REQUEST
// =====================================================

export function normalizePaymentSearchRequest(
  request: StudentFeePaymentSearchRequest
): StudentFeePaymentSearchRequest {

  const normalized:
    StudentFeePaymentSearchRequest = {};


  // ---------------------------------------------------
  // STUDENT ID
  // ---------------------------------------------------

  if (
    request.studentId !== null &&
    request.studentId !== undefined
  ) {

    normalized.studentId =
      request.studentId;
  }


  // ---------------------------------------------------
  // ADMISSION ID
  // ---------------------------------------------------

  if (
    request.admissionId !== null &&
    request.admissionId !== undefined
  ) {

    normalized.admissionId =
      request.admissionId;
  }


  // ---------------------------------------------------
  // ACADEMIC YEAR
  // ---------------------------------------------------

  if (request.academicYear?.trim()) {

    normalized.academicYear =
      request.academicYear.trim();
  }


  // ---------------------------------------------------
  // PAYMENT STATUS
  // ---------------------------------------------------

  if (request.paymentStatus) {

    normalized.paymentStatus =
      request.paymentStatus;
  }


  // ---------------------------------------------------
  // PAYMENT MODE
  // ---------------------------------------------------

  if (request.paymentMode) {

    normalized.paymentMode =
      request.paymentMode;
  }


  // ---------------------------------------------------
  // MIN PAID AMOUNT
  // ---------------------------------------------------

  if (
    request.minPaidAmount !== null &&
    request.minPaidAmount !== undefined
  ) {

    normalized.minPaidAmount =
      Number(request.minPaidAmount);
  }


  // ---------------------------------------------------
  // MAX PAID AMOUNT
  // ---------------------------------------------------

  if (
    request.maxPaidAmount !== null &&
    request.maxPaidAmount !== undefined
  ) {

    normalized.maxPaidAmount =
      Number(request.maxPaidAmount);
  }


  // ---------------------------------------------------
  // RECORD STATUS
  // ---------------------------------------------------

  if (request.status) {

    normalized.status =
      request.status;
  }


  return normalized;
}


// =====================================================
// CHECK SEARCH FILTERS
// =====================================================

export function hasPaymentSearchFilters(
  request: StudentFeePaymentSearchRequest
): boolean {

  return Object.values(request).some(
    value =>
      value !== null &&
      value !== undefined &&
      value !== ''
  );
}


// =====================================================
// PAYMENT PROGRESS
// =====================================================

export function calculatePaymentProgress(
  payment: StudentFeePaymentResponse
): number {

  const payableAmount =
    Number(payment.finalPayableAmount);

  const paidAmount =
    Number(payment.paidAmount);


  if (
    !payableAmount ||
    payableAmount <= 0
  ) {

    return paidAmount > 0
      ? 100
      : 0;
  }


  const progress =
    (paidAmount / payableAmount) * 100;


  return Math.min(
    Math.max(progress, 0),
    100
  );
}