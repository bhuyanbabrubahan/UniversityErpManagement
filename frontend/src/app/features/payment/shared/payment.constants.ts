// =====================================================
// PAYMENT CONSTANTS
// =====================================================

import { PaymentMode } from "@app/shared/enums/payment-mode.enum";
import { PaymentStatus } from "@app/shared/enums/payment-status.enum";
import { RecordStatus } from "@app/shared/enums/record-status.enum";



// =====================================================
// API
// =====================================================

export const PAYMENT_API =
  '/api/v1/student-fee-payments';


// =====================================================
// PAGINATION
// =====================================================

export const PAYMENT_PAGE_SIZE = 10;

export const PAYMENT_SORT_BY = 'id';

export const PAYMENT_SORT_DIRECTION =
  'desc' as const;


// =====================================================
// PAYMENT STATUS OPTIONS
// =====================================================

export const PAYMENT_STATUSES: PaymentStatus[] = [
  PaymentStatus.PAID,
  PaymentStatus.PARTIAL,
  PaymentStatus.PENDING,
  PaymentStatus.CANCELLED
];


// =====================================================
// PAYMENT MODE OPTIONS
// =====================================================

export const PAYMENT_MODES: PaymentMode[] = [
  PaymentMode.CASH,
  PaymentMode.ONLINE,
  PaymentMode.CARD,
  PaymentMode.UPI,
  PaymentMode.CHEQUE
];


// =====================================================
// RECORD STATUS OPTIONS
// =====================================================

export const RECORD_STATUSES: RecordStatus[] = [
  RecordStatus.ACTIVE,
  RecordStatus.INACTIVE,
  RecordStatus.DELETED
];


// =====================================================
// MESSAGES
// =====================================================

export const PAYMENT_MESSAGES = {

  CREATE_SUCCESS:
    'Fee payment created successfully.',

  UPDATE_SUCCESS:
    'Fee payment updated successfully.',

  DELETE_SUCCESS:
    'Fee payment deleted successfully.',

  LOAD_ERROR:
    'Unable to load fee payments.',

  SAVE_ERROR:
    'Unable to save fee payment.',

  DELETE_ERROR:
    'Unable to delete fee payment.',

  DETAIL_ERROR:
    'Unable to load payment details.'

} as const;


// =====================================================
// PAYMENT STATUS LABELS
// =====================================================

export const PAYMENT_STATUS_LABELS:
  Record<PaymentStatus, string> = {

  [PaymentStatus.PAID]:
    'Paid',

  [PaymentStatus.PARTIAL]:
    'Partial',

  [PaymentStatus.PENDING]:
    'Pending',

  [PaymentStatus.CANCELLED]:
    'Cancelled'

};


// =====================================================
// PAYMENT MODE LABELS
// =====================================================

export const PAYMENT_MODE_LABELS:
  Record<PaymentMode, string> = {

  [PaymentMode.CASH]:
    'Cash',

  [PaymentMode.ONLINE]:
    'Online',

  [PaymentMode.CARD]:
    'Card',

  [PaymentMode.UPI]:
    'UPI',

  [PaymentMode.CHEQUE]:
    'Cheque'

};


// =====================================================
// RECORD STATUS LABELS
// =====================================================

export const RECORD_STATUS_LABELS:
  Record<RecordStatus, string> = {

  [RecordStatus.ACTIVE]:
    'Active',

  [RecordStatus.INACTIVE]:
    'Inactive',

  [RecordStatus.DELETED]:
    'Deleted'

};