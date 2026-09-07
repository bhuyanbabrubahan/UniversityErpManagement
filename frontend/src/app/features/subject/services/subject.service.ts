import { Injectable, inject } from '@angular/core';
import {
  HttpClient,
  HttpParams
} from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  SubjectDeleteResponse,
  SubjectPageResponse,
  SubjectRequest,
  SubjectSearchRequest,
  SubjectSingleResponse
} from '../models/subject.model';

import { environment } from '@environments/environment';


@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly http =
    inject(HttpClient);


  // =====================================================
  // API URL
  // =====================================================

  private readonly apiUrl =
    `${environment.apiUrl}/subjects`;


  // =====================================================
  // CREATE SUBJECT
  // =====================================================

  createSubject(
    request: SubjectRequest
  ): Observable<SubjectSingleResponse> {

    return this.http.post<SubjectSingleResponse>(
      this.apiUrl,
      request
    );
  }


  // =====================================================
  // GET SUBJECT BY ID
  // =====================================================

  getSubjectById(
    id: number
  ): Observable<SubjectSingleResponse> {

    return this.http.get<SubjectSingleResponse>(
      `${this.apiUrl}/${id}`
    );
  }


  // =====================================================
  // GET ALL SUBJECTS
  // =====================================================

  getAllSubjects(
    page = 0,
    size = 10,
    sortBy = 'id',
    direction: 'asc' | 'desc' = 'asc'
  ): Observable<SubjectPageResponse> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sortBy', sortBy)
        .set('direction', direction);

    return this.http.get<SubjectPageResponse>(
      this.apiUrl,
      { params }
    );
  }


  // =====================================================
  // UPDATE SUBJECT
  // =====================================================

  updateSubject(
    id: number,
    request: SubjectRequest
  ): Observable<SubjectSingleResponse> {

    return this.http.put<SubjectSingleResponse>(
      `${this.apiUrl}/${id}`,
      request
    );
  }


  // =====================================================
  // DELETE SUBJECT
  // =====================================================

  deleteSubject(
    id: number
  ): Observable<SubjectDeleteResponse> {

    return this.http.delete<SubjectDeleteResponse>(
      `${this.apiUrl}/${id}`
    );
  }


  // =====================================================
  // SEARCH SUBJECTS
  // =====================================================
  //
  // Backend:
  // POST /api/subjects/search
  //
  // Search filters:
  // - subjectName
  // - subjectCode
  // - semesterNumber
  // - credits
  // - status
  //
  // IMPORTANT:
  // semesterId yahan use nahi karna.
  // Semester filter unique semester number ke basis par hai.
  // =====================================================

  searchSubjects(
    request: SubjectSearchRequest,
    page = 0,
    size = 10,
    sortBy = 'id',
    direction: 'asc' | 'desc' = 'asc'
  ): Observable<SubjectPageResponse> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sortBy', sortBy)
        .set('direction', direction);

    return this.http.post<SubjectPageResponse>(
      `${this.apiUrl}/search`,
      this.buildSearchRequest(request),
      { params }
    );
  }


  // =====================================================
  // GET ACTIVE SUBJECTS
  // =====================================================
  //
  // Initial subject list ke liye.
  //
  // Backend:
  // status null hone par ACTIVE records return karta hai.
  // =====================================================

  getActiveSubjects(
    page = 0,
    size = 10,
    sortBy = 'id',
    direction: 'asc' | 'desc' = 'asc'
  ): Observable<SubjectPageResponse> {

    const request: SubjectSearchRequest = {

      subjectName: null,

      subjectCode: null,

      semesterNumber: null,

      credits: null,

      status: null

    };

    return this.searchSubjects(
      request,
      page,
      size,
      sortBy,
      direction
    );
  }


  // =====================================================
  // BUILD / CLEAN SEARCH REQUEST
  // =====================================================
  //
  // Empty / undefined values ko null me convert karta hai.
  //
  // IMPORTANT:
  // semesterId nahi hai.
  // semesterNumber use hoga.
  // =====================================================

  buildSearchRequest(
    request: SubjectSearchRequest
  ): SubjectSearchRequest {

    return {

      subjectName:
        request.subjectName?.trim() || null,

      subjectCode:
        request.subjectCode?.trim() || null,

      semesterNumber:
        request.semesterNumber ?? null,

      credits:
        request.credits ?? null,

      status:
        request.status ?? null

    };
  }

}