import {
  Injectable
} from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';

import {
  PaymentPageResponse,
  PaymentSingleResponse,
  PaymentDeleteResponse,
  StudentFeePaymentRequest,
  StudentFeePaymentSearchRequest
} from '../models/payment.model';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';


@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  // =====================================================
  // API URL
  // =====================================================

  private readonly baseUrl =
    '/api/v1/student-fee-payments';


  // =====================================================
  // CONSTRUCTOR
  // =====================================================

  constructor(
    private readonly http: HttpClient
  ) {}


  // =====================================================
  // CREATE PAYMENT
  // =====================================================

  createPayment(
    request: StudentFeePaymentRequest
  ): Observable<PaymentSingleResponse> {

    return this.http.post<PaymentSingleResponse>(
      this.baseUrl,
      request
    );
  }


  // =====================================================
  // GET PAYMENT BY ID
  // =====================================================

  getPaymentById(
    id: number
  ): Observable<PaymentSingleResponse> {

    return this.http.get<PaymentSingleResponse>(
      `${this.baseUrl}/${id}`
    );
  }


  // =====================================================
  // ALIAS - GET BY ID
  // =====================================================

  getById(
    id: number
  ): Observable<PaymentSingleResponse> {

    return this.getPaymentById(id);
  }


  // =====================================================
  // UPDATE PAYMENT
  // =====================================================

  updatePayment(
    id: number,
    request: StudentFeePaymentRequest
  ): Observable<PaymentSingleResponse> {

    return this.http.put<PaymentSingleResponse>(
      `${this.baseUrl}/${id}`,
      request
    );
  }


  // =====================================================
  // DELETE PAYMENT
  // =====================================================

  deletePayment(
    id: number
  ): Observable<PaymentDeleteResponse> {

    return this.http.delete<PaymentDeleteResponse>(
      `${this.baseUrl}/${id}`
    );
  }


  // =====================================================
  // GET ALL PAYMENTS
  // =====================================================

  getAllPayments(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: 'asc' | 'desc' = 'desc'
  ): Observable<PaymentPageResponse> {

    const params =
      this.buildPaginationParams(
        page,
        size,
        sortBy,
        direction
      );

    return this.http.get<PaymentPageResponse>(
      this.baseUrl,
      { params }
    );
  }


  // =====================================================
  // ALIAS - GET ALL
  // =====================================================

  getAll(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: 'asc' | 'desc' = 'desc'
  ): Observable<PaymentPageResponse> {

    return this.getAllPayments(
      page,
      size,
      sortBy,
      direction
    );
  }


  // =====================================================
  // SEARCH PAYMENTS
  // =====================================================

  searchPayments(
    request: StudentFeePaymentSearchRequest,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: 'asc' | 'desc' = 'desc'
  ): Observable<PaymentPageResponse> {

    const params =
      this.buildPaginationParams(
        page,
        size,
        sortBy,
        direction
      );

    return this.http.post<PaymentPageResponse>(
      `${this.baseUrl}/search`,
      this.cleanSearchRequest(request),
      { params }
    );
  }


  // =====================================================
  // ALIAS - SEARCH
  // =====================================================

  search(
    request: StudentFeePaymentSearchRequest,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: 'asc' | 'desc' = 'desc'
  ): Observable<PaymentPageResponse> {

    return this.searchPayments(
      request,
      page,
      size,
      sortBy,
      direction
    );
  }


  // =====================================================
  // PAGINATION PARAMS
  // =====================================================

  private buildPaginationParams(
    page: number,
    size: number,
    sortBy: string,
    direction: 'asc' | 'desc'
  ): HttpParams {

    return new HttpParams()
      .set(
        'page',
        page.toString()
      )
      .set(
        'size',
        size.toString()
      )
      .set(
        'sortBy',
        sortBy
      )
      .set(
        'direction',
        direction
      );
  }


  // =====================================================
  // CLEAN SEARCH REQUEST
  // =====================================================

  private cleanSearchRequest(
    request: StudentFeePaymentSearchRequest
  ): StudentFeePaymentSearchRequest {

    const cleaned:
      StudentFeePaymentSearchRequest = {};


    if (
      request.studentId != null
    ) {
      cleaned.studentId =
        request.studentId;
    }


    if (
      request.admissionId != null
    ) {
      cleaned.admissionId =
        request.admissionId;
    }


    if (
      request.academicYear != null &&
      request.academicYear.trim() !== ''
    ) {
      cleaned.academicYear =
        request.academicYear.trim();
    }


    if (
      request.paymentStatus != null
    ) {
      cleaned.paymentStatus =
        request.paymentStatus;
    }


    if (
      request.paymentMode != null
    ) {
      cleaned.paymentMode =
        request.paymentMode;
    }


    if (
      request.minPaidAmount != null
    ) {
      cleaned.minPaidAmount =
        request.minPaidAmount;
    }


    if (
      request.maxPaidAmount != null
    ) {
      cleaned.maxPaidAmount =
        request.maxPaidAmount;
    }


    if (
      request.status != null
    ) {
      cleaned.status =
        request.status;
    }


    return cleaned;
  }


  // =====================================================
  // GET ACTIVE PAYMENTS
  // =====================================================

  getActivePayments(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: 'asc' | 'desc' = 'desc'
  ): Observable<PaymentPageResponse> {

    return this.searchPayments(
      {
        status: RecordStatus.ACTIVE
      },
      page,
      size,
      sortBy,
      direction
    );
  }

}