import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import {
  AdmissionRequest,
  AdmissionResponse,
  AdmissionSearchRequest,
  ApiResponse,
  PageResponse
} from '../models/admission.model';

import { API } from '../../../core/constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class AdmissionService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    API.ADMISSIONS;


  // =====================================================
  // GET ALL
  // =====================================================

  getAll(
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'desc'
  ): Observable<ApiResponse<PageResponse<AdmissionResponse>>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<
      ApiResponse<PageResponse<AdmissionResponse>>
    >(
      this.apiUrl,
      { params }
    );
  }


  // =====================================================
  // GET BY ID
  // =====================================================

  getById(
    id: number
  ): Observable<ApiResponse<AdmissionResponse>> {

    return this.http.get<
      ApiResponse<AdmissionResponse>
    >(
      `${this.apiUrl}/${id}`
    );
  }


  // =====================================================
  // CREATE
  // =====================================================

  create(
    request: AdmissionRequest
  ): Observable<ApiResponse<AdmissionResponse>> {

    return this.http.post<
      ApiResponse<AdmissionResponse>
    >(
      this.apiUrl,
      request
    );
  }


  // =====================================================
  // UPDATE
  // =====================================================

  update(
    id: number,
    request: AdmissionRequest
  ): Observable<ApiResponse<AdmissionResponse>> {

    return this.http.put<
      ApiResponse<AdmissionResponse>
    >(
      `${this.apiUrl}/${id}`,
      request
    );
  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(
    id: number
  ): Observable<ApiResponse<void>> {

    return this.http.delete<
      ApiResponse<void>
    >(
      `${this.apiUrl}/${id}`
    );
  }


  // =====================================================
  // SEARCH
  // =====================================================

  search(
    request: AdmissionSearchRequest,
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'desc'
  ): Observable<ApiResponse<PageResponse<AdmissionResponse>>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.post<
      ApiResponse<PageResponse<AdmissionResponse>>
    >(
      `${this.apiUrl}/search`,
      request,
      { params }
    );
  }

}