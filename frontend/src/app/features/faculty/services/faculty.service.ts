import { Injectable, inject } from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import { Observable } from 'rxjs';

import { FacultyRequest } from '../model/faculty-request.model';
import { FacultyResponse } from '../model/faculty-response.model';
import { FacultySearchRequest } from '../model/faculty-search-request.model';


// ==========================================================
// API RESPONSE
// ==========================================================

export interface ApiResponse<T> {

  data: T;

  message: string;

  status?: number;

  success?: boolean;

}


// ==========================================================
// PAGE RESPONSE
// ==========================================================

export interface PageResponse<T> {

  content: T[];

  pageNumber: number;

  pageSize: number;

  totalElements: number;

  totalPages: number;

  last: boolean;

}


// ==========================================================
// FACULTY SERVICE
// ==========================================================

@Injectable({
  providedIn: 'root'
})
export class FacultyService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = '/api/faculties';


  // ========================================================
  // CREATE
  // ========================================================

  createFaculty(
    request: FacultyRequest
  ): Observable<ApiResponse<FacultyResponse>> {

    return this.http.post<ApiResponse<FacultyResponse>>(
      this.apiUrl,
      request
    );

  }


  // ========================================================
  // GET BY ID
  // ========================================================

  getFacultyById(
    id: number
  ): Observable<ApiResponse<FacultyResponse>> {

    return this.http.get<ApiResponse<FacultyResponse>>(
      `${this.apiUrl}/${id}`
    );

  }


  // ========================================================
  // GET ALL
  // ========================================================

  getAllFaculties(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: string = 'asc'
  ): Observable<ApiResponse<PageResponse<FacultyResponse>>> {

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<
      ApiResponse<PageResponse<FacultyResponse>>
    >(
      this.apiUrl,
      { params }
    );

  }


  // ========================================================
  // UPDATE
  // ========================================================

  updateFaculty(
    id: number,
    request: FacultyRequest
  ): Observable<ApiResponse<FacultyResponse>> {

    return this.http.put<ApiResponse<FacultyResponse>>(
      `${this.apiUrl}/${id}`,
      request
    );

  }


  // ========================================================
  // DELETE
  // ========================================================

  deleteFaculty(
    id: number
  ): Observable<ApiResponse<void>> {

    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}/${id}`
    );

  }


  // ========================================================
  // SEARCH
  // ========================================================

  searchFaculties(
    request: FacultySearchRequest,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    direction: string = 'asc'
  ): Observable<ApiResponse<PageResponse<FacultyResponse>>> {

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.post<
      ApiResponse<PageResponse<FacultyResponse>>
    >(
      `${this.apiUrl}/search`,
      request,
      { params }
    );

  }

}
