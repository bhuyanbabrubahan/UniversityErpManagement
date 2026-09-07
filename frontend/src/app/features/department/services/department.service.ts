// =====================================================
// department.service.ts
// =====================================================

import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  ApiResponse,
  DepartmentPageResponse,
  DepartmentDeleteResponse,
  DepartmentRequest,
  DepartmentResponse,
  DepartmentSearchRequest,
  DepartmentSingleResponse,
  PageResponse
} from '../models/department.model';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private readonly http = inject(HttpClient);

  private readonly API_URL = '/api/departments';

  // ===================================================
  // CREATE
  // ===================================================

  createDepartment(
    request: DepartmentRequest
  ): Observable<DepartmentSingleResponse> {

    return this.http.post<DepartmentSingleResponse>(
      this.API_URL,
      request
    );
  }

  // ===================================================
  // GET BY ID
  // ===================================================

  getDepartmentById(
    id: number
  ): Observable<DepartmentSingleResponse> {

    return this.http.get<DepartmentSingleResponse>(
      `${this.API_URL}/${id}`
    );
  }

  // Alias - useful for existing components
  getById(
    id: number
  ): Observable<DepartmentSingleResponse> {

    return this.getDepartmentById(id);
  }

  // ===================================================
  // UPDATE
  // ===================================================

  updateDepartment(
    id: number,
    request: DepartmentRequest
  ): Observable<DepartmentSingleResponse> {

    return this.http.put<DepartmentSingleResponse>(
      `${this.API_URL}/${id}`,
      request
    );
  }

  // ===================================================
  // DELETE
  // ===================================================

  deleteDepartment(
    id: number
  ): Observable<DepartmentDeleteResponse> {

    return this.http.delete<DepartmentDeleteResponse>(
      `${this.API_URL}/${id}`
    );
  }

  // ===================================================
  // GET ALL
  // ===================================================

  getAllDepartments(
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'asc'
  ): Observable<DepartmentPageResponse> {

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<DepartmentPageResponse>(
      this.API_URL,
      { params }
    );
  }

  // ===================================================
  // SEARCH
  // ===================================================

  searchDepartments(
    request: DepartmentSearchRequest,
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'asc'
  ): Observable<DepartmentPageResponse> {

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.post<DepartmentPageResponse>(
      `${this.API_URL}/search`,
      request,
      { params }
    );
  }
}