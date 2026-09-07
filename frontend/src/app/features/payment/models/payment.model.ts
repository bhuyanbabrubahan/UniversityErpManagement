import { PaymentMode } from '@app/shared/enums/payment-mode.enum';
import { PaymentStatus } from '@app/shared/enums/payment-status.enum';
import { RecordStatus } from '@app/shared/enums/record-status.enum';


// =====================================================
// PAYMENT REQUEST
// =====================================================

export interface StudentFeePaymentRequest {

  studentId: number;

  scholarshipId?: number | null;

  admissionId: number;

  academicYear: string;

  paidAmount: number;

  paymentDate?: string | null;

  paymentMode?: PaymentMode | null;

  transactionReference?: string | null;

  remarks?: string | null;
}


// =====================================================
// PAYMENT RESPONSE
// =====================================================

export interface StudentFeePaymentResponse {

  id: number;


  // ===================================================
  // SCHOLARSHIP
  // ===================================================

  scholarshipId?: number | null;

  scholarshipAmount: number;

  finalPayableAmount: number;


  // ===================================================
  // STUDENT
  // ===================================================

  studentId: number;

  studentName: string;

  admissionId: number;

  academicYear: string;


  // ===================================================
  // FEE
  // ===================================================

  totalFee: number;

  paidAmount: number;

  dueAmount: number;


  // ===================================================
  // PAYMENT
  // ===================================================

  paymentStatus: PaymentStatus;

  paymentDate?: string | null;

  paymentMode?: PaymentMode | null;

  transactionReference?: string | null;

  remarks?: string | null;


  // ===================================================
  // RECORD STATUS
  // ===================================================

  status: RecordStatus;


  // ===================================================
  // AUDIT - CREATED
  // ===================================================

  createdByUserId?: number | null;

  createdBy?: string | null;

  createdAt?: string | null;


  // ===================================================
  // AUDIT - UPDATED
  // ===================================================

  updatedByUserId?: number | null;

  updatedBy?: string | null;

  updatedAt?: string | null;


  // ===================================================
  // AUDIT - DELETED
  // ===================================================

  deletedByUserId?: number | null;

  deletedBy?: string | null;

  deletedDate?: string | null;
}


// =====================================================
// PAYMENT SEARCH
// =====================================================

export interface StudentFeePaymentSearchRequest {

  studentId?: number | null;

  admissionId?: number | null;

  academicYear?: string | null;

  paymentStatus?: PaymentStatus | null;

  paymentMode?: PaymentMode | null;

  minPaidAmount?: number | null;

  maxPaidAmount?: number | null;

  status?: RecordStatus | null;
}


// =====================================================
// PAGINATION
// =====================================================

export interface PageResponse<T> {

  content: T[];

  pageNumber: number;

  pageSize: number;

  totalElements: number;

  totalPages: number;

  last: boolean;
}


// =====================================================
// API RESPONSE
// =====================================================

export interface ApiResponse<T> {

  success: boolean;

  message: string;

  status: number;

  data: T;

  timestamp: string;

  errors?: Record<string, string>;
}


// =====================================================
// RESPONSE TYPES
// =====================================================

export type PaymentPageResponse =
  ApiResponse<
    PageResponse<StudentFeePaymentResponse>
  >;


export type PaymentSingleResponse =
  ApiResponse<StudentFeePaymentResponse>;


export type PaymentDeleteResponse =
  ApiResponse<void>;

export { PaymentMode, PaymentStatus };
