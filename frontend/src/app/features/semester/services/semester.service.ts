import {
  Injectable,
  inject
} from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  Observable,
  map
} from 'rxjs';

import {
  SemesterResponse,
  SemesterPageResponse,
  SemesterRequest,
  SemesterSearchRequest
} from '../models/semester.model';

import {
  RecordStatus
} from '../../../shared/enums/record-status.enum';

import {
  ApiResponse
} from '../../../shared/models/api-response.model';


// =====================================================
// SEMESTER OPTION
// =====================================================
//
// Common option model.
//
// id:
// Exact semester database record ID.
//
// name:
// Display name.
//
// semesterNumber:
// Actual semester number.
//
// IMPORTANT:
// This model is used for both:
// - Subject Create / Update
// - Subject Search
// =====================================================

export interface SemesterOption {

  id: number;

  name: string;

  semesterNumber: number;

}


// =====================================================
// SERVICE
// =====================================================

@Injectable({
  providedIn: 'root'
})
export class SemesterService {


  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly http =
    inject(HttpClient);


  // =====================================================
  // API URL
  // =====================================================

  private readonly baseUrl =
    '/api/semesters';


  // =====================================================
  // CREATE
  // POST /api/semesters
  // =====================================================

  createSemester(
    request: SemesterRequest
  ): Observable<SemesterResponse> {

    return this.http
      .post<ApiResponse<SemesterResponse>>(
        this.baseUrl,
        request
      )
      .pipe(
        map(response => response.data)
      );

  }


  // =====================================================
  // GET BY ID
  // GET /api/semesters/{id}
  // =====================================================

  getSemesterById(
    id: number
  ): Observable<SemesterResponse> {

    return this.http
      .get<ApiResponse<SemesterResponse>>(
        `${this.baseUrl}/${id}`
      )
      .pipe(
        map(response => response.data)
      );

  }


  // =====================================================
  // GET ALL
  // GET /api/semesters
  // =====================================================

  getSemesters(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'semesterNumber',
    direction: 'asc' | 'desc' = 'asc'
  ): Observable<SemesterPageResponse> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sortBy', sortBy)
        .set('direction', direction);

    return this.http
      .get<ApiResponse<SemesterPageResponse>>(
        this.baseUrl,
        { params }
      )
      .pipe(
        map(response => response.data)
      );

  }


  // =====================================================
  // UPDATE
  // PUT /api/semesters/{id}
  // =====================================================

  updateSemester(
    id: number,
    request: SemesterRequest
  ): Observable<SemesterResponse> {

    return this.http
      .put<ApiResponse<SemesterResponse>>(
        `${this.baseUrl}/${id}`,
        request
      )
      .pipe(
        map(response => response.data)
      );

  }


  // =====================================================
  // DELETE
  // DELETE /api/semesters/{id}
  // =====================================================

  deleteSemester(
    id: number
  ): Observable<void> {

    return this.http
      .delete<ApiResponse<void>>(
        `${this.baseUrl}/${id}`
      )
      .pipe(
        map(() => undefined)
      );

  }


  // =====================================================
  // SEARCH
  // POST /api/semesters/search
  // =====================================================

  searchSemesters(
    request: SemesterSearchRequest,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'semesterNumber',
    direction: 'asc' | 'desc' = 'asc'
  ): Observable<SemesterPageResponse> {

    const params =
      new HttpParams()
        .set('page', page)
        .set('size', size)
        .set('sortBy', sortBy)
        .set('direction', direction);

    return this.http
      .post<ApiResponse<SemesterPageResponse>>(
        `${this.baseUrl}/search`,
        this.cleanSearchRequest(request),
        { params }
      )
      .pipe(
        map(response => response.data)
      );

  }


  // =====================================================
  // ACTIVE SEMESTERS
  // =====================================================
  //
  // Returns only ACTIVE semester records.
  //
  // Default:
  // page = 0
  // size = 100
  // sort = semesterNumber ASC
  // =====================================================

  getActiveSemesters(
    page: number = 0,
    size: number = 100,
    sortBy: string = 'semesterNumber',
    direction: 'asc' | 'desc' = 'asc'
  ): Observable<SemesterPageResponse> {

    return this.searchSemesters(
      {
        status: RecordStatus.ACTIVE
      },
      page,
      size,
      sortBy,
      direction
    );

  }


  // =====================================================
  // SEMESTER OPTIONS
  // =====================================================
  //
  // USED BY:
  // - Subject Create
  // - Subject Update
  // - Other forms where exact semesterId is required
  //
  // IMPORTANT:
  // Duplicate semester numbers are NOT removed.
  //
  // Example:
  //
  // ID 1 -> Semester 1
  // ID 5 -> Semester 1
  // ID 9 -> Semester 2
  //
  // Result:
  //
  // [
  //   { id: 1, name: 'Semester 1', semesterNumber: 1 },
  //   { id: 5, name: 'Semester 1', semesterNumber: 1 },
  //   { id: 9, name: 'Semester 2', semesterNumber: 2 }
  // ]
  //
  // This is intentional because CREATE / UPDATE needs
  // the exact semester database ID.
  // =====================================================

  getSemesterOptions():
    Observable<SemesterOption[]> {

    return this.getActiveSemesters(
      0,
      100,
      'semesterNumber',
      'asc'
    ).pipe(

      map(response => {

        return response.content.map(
          semester => ({

            id:
              semester.id,

            name:
              semester.semesterName,

            semesterNumber:
              semester.semesterNumber

          })
        );

      })

    );

  }


  // =====================================================
  // UNIQUE SEMESTER NUMBER OPTIONS
  // =====================================================
  //
  // USED BY:
  // Subject Search Filter
  //
  // IMPORTANT:
  // Only ONE option is returned for each semesterNumber.
  //
  // Example:
  //
  // Backend:
  //
  // ID 1 -> Semester 1
  // ID 5 -> Semester 1
  // ID 9 -> Semester 2
  // ID 10 -> Semester 2
  // ID 12 -> Semester 3
  //
  // Frontend:
  //
  // Semester 1
  // Semester 2
  // Semester 3
  //
  // Search backend will receive:
  //
  // semesterNumber: 1
  //
  // NOT semesterId.
  // =====================================================

  getUniqueSemesterNumberOptions():
    Observable<SemesterOption[]> {

    return this.getActiveSemesters(
      0,
      100,
      'semesterNumber',
      'asc'
    ).pipe(

      map(response => {

        const unique =
          new Map<number, SemesterOption>();


        for (
          const semester of response.content
        ) {

          const semesterNumber =
            semester.semesterNumber;


          if (
            !unique.has(
              semesterNumber
            )
          ) {

            unique.set(
              semesterNumber,
              {
                id:
                  semester.id,

                name:
                  semester.semesterName,

                semesterNumber:
                  semesterNumber
              }
            );

          }

        }


        return Array.from(
          unique.values()
        );

      })

    );

  }


  // =====================================================
  // CLEAN SEARCH REQUEST
  // =====================================================
  //
  // Removes null / undefined / empty values before
  // sending search request to backend.
  // =====================================================

  private cleanSearchRequest(
    request: SemesterSearchRequest
  ): SemesterSearchRequest {

    const cleaned:
      SemesterSearchRequest = {};


    // ===================================================
    // SEMESTER ID
    // ===================================================

    if (
      request.semesterId !== null &&
      request.semesterId !== undefined
    ) {

      cleaned.semesterId =
        request.semesterId;

    }


    // ===================================================
    // SEMESTER NAME
    // ===================================================

    if (
      request.semesterName !== null &&
      request.semesterName !== undefined &&
      request.semesterName.trim() !== ''
    ) {

      cleaned.semesterName =
        request.semesterName.trim();

    }


    // ===================================================
    // SEMESTER NUMBER
    // ===================================================

    if (
      request.semesterNumber !== null &&
      request.semesterNumber !== undefined
    ) {

      cleaned.semesterNumber =
        request.semesterNumber;

    }


    // ===================================================
    // COURSE ID
    // ===================================================

    if (
      request.courseId !== null &&
      request.courseId !== undefined
    ) {

      cleaned.courseId =
        request.courseId;

    }


    // ===================================================
    // COURSE CODE
    // ===================================================

    if (
      request.courseCode !== null &&
      request.courseCode !== undefined &&
      request.courseCode.trim() !== ''
    ) {

      cleaned.courseCode =
        request.courseCode.trim();

    }


    // ===================================================
    // COURSE NAME
    // ===================================================

    if (
      request.courseName !== null &&
      request.courseName !== undefined &&
      request.courseName.trim() !== ''
    ) {

      cleaned.courseName =
        request.courseName.trim();

    }


    // ===================================================
    // START DATE FROM
    // ===================================================

    if (
      request.startDateFrom !== null &&
      request.startDateFrom !== undefined &&
      request.startDateFrom !== ''
    ) {

      cleaned.startDateFrom =
        request.startDateFrom;

    }


    // ===================================================
    // START DATE TO
    // ===================================================

    if (
      request.startDateTo !== null &&
      request.startDateTo !== undefined &&
      request.startDateTo !== ''
    ) {

      cleaned.startDateTo =
        request.startDateTo;

    }


    // ===================================================
    // END DATE FROM
    // ===================================================

    if (
      request.endDateFrom !== null &&
      request.endDateFrom !== undefined &&
      request.endDateFrom !== ''
    ) {

      cleaned.endDateFrom =
        request.endDateFrom;

    }


    // ===================================================
    // END DATE TO
    // ===================================================

    if (
      request.endDateTo !== null &&
      request.endDateTo !== undefined &&
      request.endDateTo !== ''
    ) {

      cleaned.endDateTo =
        request.endDateTo;

    }


    // ===================================================
    // MIN WORKING DAYS
    // ===================================================

    if (
      request.minWorkingDays !== null &&
      request.minWorkingDays !== undefined
    ) {

      cleaned.minWorkingDays =
        request.minWorkingDays;

    }


    // ===================================================
    // MAX WORKING DAYS
    // ===================================================

    if (
      request.maxWorkingDays !== null &&
      request.maxWorkingDays !== undefined
    ) {

      cleaned.maxWorkingDays =
        request.maxWorkingDays;

    }


    // ===================================================
    // STATUS
    // ===================================================

    if (
      request.status !== null &&
      request.status !== undefined
    ) {

      cleaned.status =
        request.status;

    }


    return cleaned;

  }

}