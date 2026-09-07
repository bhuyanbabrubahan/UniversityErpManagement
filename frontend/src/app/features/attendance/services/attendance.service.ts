import { Injectable, inject } from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  Observable,
  map,
  from,
  concatMap,
  toArray
} from 'rxjs';

import {
  AttendanceRecord,
  AttendanceRequest,
  AttendanceUpdateRequest,
  AttendanceSearchRequest,
  AttendanceClassRequest,
  AttendanceClassResponse,
  BulkAttendanceRequest,
  AttendanceSession,
  AttendanceSessionRequest,
  FacultySubjectOption,
  PageResponse,
  StudentOption
} from '../models/attendance.model';

import {
  ATTENDANCE_API,
  ATTENDANCE_SESSION_API,
  ATTENDANCE_LOOKUP_API
} from '../shared/attendance.constants';


/* ============================================================
   API RESPONSE TYPES
============================================================ */

interface ApiEnvelope<T> {

  data?: T;

  result?: T;

  message?: string;

  success?: boolean;

  status?: number;
}


interface PaginatedData<T> {

  content?: T[];

  pageNumber?: number;

  pageSize?: number;

  totalElements?: number;

  totalPages?: number;

  last?: boolean;
}


/* ============================================================
   SERVICE
============================================================ */

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private readonly http =
    inject(HttpClient);


  /* ==========================================================
     ATTENDANCE SESSION
  ========================================================== */

  /**
   * Create/open attendance session.
   *
   * Backend:
   *
   * POST /api/attendance-sessions/open
   *
   * Backend identifies faculty from authenticated user.
   */
  openSession(
    request: AttendanceSessionRequest
  ): Observable<AttendanceSession> {

    return this.http
      .post<
        ApiEnvelope<AttendanceSession> |
        AttendanceSession
      >(
        `${ATTENDANCE_SESSION_API}/open`,
        request
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /**
   * Get attendance session by ID.
   *
   * Backend:
   *
   * GET /api/attendance-sessions/{id}
   */
  getSession(
    id: number
  ): Observable<AttendanceSession> {

    if (!id || id <= 0) {

      throw new Error(
        'Valid attendance session ID is required.'
      );
    }

    return this.http
      .get<
        ApiEnvelope<AttendanceSession> |
        AttendanceSession
      >(
        `${ATTENDANCE_SESSION_API}/${id}`
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /**
   * Get session by faculty-subject mapping and date.
   *
   * IMPORTANT:
   *
   * Backend controller accepts ONLY:
   *
   * GET
   * /api/attendance-sessions/mapping/{mappingId}/date
   * ?date=yyyy-MM-dd
   *
   * Do NOT send academicYear here.
   */
  getSessionByMappingAndDate(
    mappingId: number,
    date: string
  ): Observable<AttendanceSession> {

    if (!mappingId || mappingId <= 0) {

      throw new Error(
        'Valid faculty subject mapping ID is required.'
      );
    }

    if (!date?.trim()) {

      throw new Error(
        'Attendance date is required.'
      );
    }


    const params =
      new HttpParams()
        .set(
          'date',
          date.trim()
        );


    return this.http
      .get<
        ApiEnvelope<AttendanceSession> |
        AttendanceSession
      >(
        `${ATTENDANCE_SESSION_API}/mapping/${mappingId}/date`,
        {
          params
        }
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /**
   * Attendance session history.
   *
   * GET
   * /api/attendance-sessions/mapping/{mappingId}/history
   */
  getSessionHistory(
    mappingId: number
  ): Observable<AttendanceSession[]> {

    if (!mappingId || mappingId <= 0) {

      throw new Error(
        'Valid faculty subject mapping ID is required.'
      );
    }


    return this.http
      .get<
        ApiEnvelope<AttendanceSession[]> |
        AttendanceSession[]
      >(
        `${ATTENDANCE_SESSION_API}/mapping/${mappingId}/history`
      )
      .pipe(
        map(response =>
          this.unwrapList(
            response
          )
        )
      );
  }


  /**
   * Complete attendance session.
   *
   * POST
   * /api/attendance-sessions/{id}/complete
   */
  completeSession(
    id: number
  ): Observable<AttendanceSession> {

    if (!id || id <= 0) {

      throw new Error(
        'Valid attendance session ID is required.'
      );
    }


    return this.http
      .post<
        ApiEnvelope<AttendanceSession> |
        AttendanceSession
      >(
        `${ATTENDANCE_SESSION_API}/${id}/complete`,
        {}
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /**
   * Cancel attendance session.
   *
   * POST
   * /api/attendance-sessions/{id}/cancel
   */
  cancelSession(
    id: number
  ): Observable<AttendanceSession> {

    if (!id || id <= 0) {

      throw new Error(
        'Valid attendance session ID is required.'
      );
    }


    return this.http
      .post<
        ApiEnvelope<AttendanceSession> |
        AttendanceSession
      >(
        `${ATTENDANCE_SESSION_API}/${id}/cancel`,
        {}
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /* ==========================================================
     ATTENDANCE CRUD
  ========================================================== */

  create(
    request: AttendanceRequest
  ): Observable<AttendanceRecord> {

    return this.http
      .post<
        ApiEnvelope<AttendanceRecord> |
        AttendanceRecord
      >(
        ATTENDANCE_API,
        request
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  createMany(
    requests: AttendanceRequest[]
  ): Observable<AttendanceRecord[]> {

    return from(requests)
      .pipe(
        concatMap(request =>
          this.create(request)
        ),
        toArray()
      );
  }


  saveBulk(
    request: BulkAttendanceRequest
  ): Observable<AttendanceRecord[]> {

    return this.http
      .post<
        ApiEnvelope<AttendanceRecord[]> |
        AttendanceRecord[]
      >(
        `${ATTENDANCE_API}/bulk`,
        request
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  update(
    id: number,
    request:
      AttendanceUpdateRequest |
      AttendanceRequest
  ): Observable<AttendanceRecord> {

    return this.http
      .put<
        ApiEnvelope<AttendanceRecord> |
        AttendanceRecord
      >(
        `${ATTENDANCE_API}/${id}`,
        request
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  delete(
    id: number
  ): Observable<void> {

    return this.http
      .delete(
        `${ATTENDANCE_API}/${id}`
      )
      .pipe(
        map(() =>
          void 0
        )
      );
  }


  getById(
    id: number
  ): Observable<AttendanceRecord> {

    return this.http
      .get<
        ApiEnvelope<AttendanceRecord> |
        AttendanceRecord
      >(
        `${ATTENDANCE_API}/${id}`
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  getAll(
    page = 0,
    size = 20,
    sortBy = 'id',
    direction:
      'asc' |
      'desc' = 'desc'
  ): Observable<
    PageResponse<AttendanceRecord>
  > {

    const params =
      new HttpParams()
        .set(
          'page',
          page
        )
        .set(
          'size',
          size
        )
        .set(
          'sortBy',
          sortBy
        )
        .set(
          'direction',
          direction
        );


    return this.http
      .get<
        ApiEnvelope<
          PageResponse<AttendanceRecord>
        > |
        PageResponse<AttendanceRecord>
      >(
        ATTENDANCE_API,
        {
          params
        }
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  search(
    request: AttendanceSearchRequest,
    page = 0,
    size = 20,
    sortBy = 'attendanceDate',
    direction:
      'desc' |
      'asc' = 'desc'
  ): Observable<
    PageResponse<AttendanceRecord>
  > {

    const params =
      new HttpParams()
        .set(
          'page',
          page
        )
        .set(
          'size',
          size
        )
        .set(
          'sortBy',
          sortBy
        )
        .set(
          'direction',
          direction
        );


    return this.http
      .post<
        ApiEnvelope<
          PageResponse<AttendanceRecord>
        > |
        PageResponse<AttendanceRecord>
      >(
        `${ATTENDANCE_API}/search`,
        request,
        {
          params
        }
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /* ==========================================================
     FACULTY CLASS ATTENDANCE
  ========================================================== */

  getClassAttendance(
    request: AttendanceClassRequest
  ): Observable<AttendanceClassResponse> {

    return this.http
      .post<
        ApiEnvelope<AttendanceClassResponse> |
        AttendanceClassResponse
      >(
        `${ATTENDANCE_API}/class`,
        request
      )
      .pipe(
        map(response =>
          this.unwrap(response)
        )
      );
  }


  /* ==========================================================
     FACULTY SUBJECTS
  ========================================================== */

  /**
   * Returns subjects assigned to logged-in faculty.
   *
   * Backend determines faculty from authenticated user.
   *
   * academicYear is optional here intentionally.
   *
   * This allows both:
   *
   * getFacultySubjects()
   *
   * and:
   *
   * getFacultySubjects('2026-2027')
   *
   * to work.
   */
  getFacultySubjects(
    academicYear?: string
  ): Observable<FacultySubjectOption[]> {

    const resolvedAcademicYear =
      academicYear?.trim() ||
      this.getCurrentAcademicYear();


    if (!resolvedAcademicYear) {

      throw new Error(
        'Academic year is required to load faculty subjects.'
      );
    }


    const params =
      new HttpParams()
        .set(
          'academicYear',
          resolvedAcademicYear
        );


    return this.http
      .get<
        ApiEnvelope<
          PaginatedData<
            FacultySubjectOption
          > |
          FacultySubjectOption[]
        > |
        FacultySubjectOption[]
      >(
        ATTENDANCE_LOOKUP_API.facultySubjects,
        {
          params
        }
      )
      .pipe(
        map(response =>
          this.unwrapList(
            response
          )
        )
      );
  }


  /* ==========================================================
     CURRENT ACADEMIC YEAR
  ========================================================== */

  private getCurrentAcademicYear(): string {

    const today =
      new Date();

    const year =
      today.getFullYear();

    const month =
      today.getMonth() + 1;


    /*
     * Academic year starts in April.
     *
     * April 2026  -> 2026-2027
     * August 2026 -> 2026-2027
     * January 2027 -> 2026-2027
     * March 2027   -> 2026-2027
     */

    const startYear =
      month >= 4
        ? year
        : year - 1;


    const endYear =
      startYear + 1;


    return `${startYear}-${endYear}`;
  }


  /* ==========================================================
     STUDENTS
  ========================================================== */

  getStudents(): Observable<StudentOption[]> {

    return this.http
      .get<
        ApiEnvelope<
          PaginatedData<StudentOption> |
          StudentOption[]
        > |
        StudentOption[]
      >(
        ATTENDANCE_LOOKUP_API.students
      )
      .pipe(
        map(response =>
          this.unwrapList(
            response
          )
        )
      );
  }


  /* ==========================================================
     RESPONSE UNWRAP
  ========================================================== */

  /**
   * Supports both:
   *
   * 1. Raw response
   *
   * {
   *   id: 1
   * }
   *
   * 2. ApiResponse
   *
   * {
   *   data: {
   *     id: 1
   *   }
   * }
   *
   * 3. ApiResponse using result
   *
   * {
   *   result: {
   *     id: 1
   *   }
   * }
   */
  private unwrap<T>(
    response:
      ApiEnvelope<T> |
      T
  ): T {

    if (
      response &&
      typeof response === 'object'
    ) {

      const object =
        response as Record<
          string,
          unknown
        >;


      if (
        Object.prototype.hasOwnProperty.call(
          object,
          'data'
        ) ||
        Object.prototype.hasOwnProperty.call(
          object,
          'result'
        )
      ) {

        const envelope =
          response as ApiEnvelope<T>;


        return (
          envelope.data ??
          envelope.result
        ) as T;
      }
    }


    return response as T;
  }


  /* ==========================================================
     LIST RESPONSE UNWRAP
  ========================================================== */

  private unwrapList<T>(
    response:
      | ApiEnvelope<
          PaginatedData<T> |
          T[]
        >
      | T[]
  ): T[] {

    /* -----------------------------------------
       Raw array
    ----------------------------------------- */

    if (
      Array.isArray(response)
    ) {

      return response;
    }


    /* -----------------------------------------
       Envelope
    ----------------------------------------- */

    const envelope =
      response as ApiEnvelope<
        PaginatedData<T> |
        T[]
      >;


    const data =
      envelope.data ??
      envelope.result;


    /* -----------------------------------------
       data = []
    ----------------------------------------- */

    if (
      Array.isArray(data)
    ) {

      return data;
    }


    /* -----------------------------------------
       data = { content: [] }
    ----------------------------------------- */

    if (
      data &&
      typeof data === 'object' &&
      Array.isArray(
        (data as PaginatedData<T>)
          .content
      )
    ) {

      return (
        (data as PaginatedData<T>)
          .content ?? []
      );
    }


    return [];
  }

    exportReport(
    format: 'xlsx' | 'pdf',
    date?: string | null
  ): Observable<Blob> {
    let params = new HttpParams().set('format', format);
    if (date?.trim()) params = params.set('date', date.trim());

    return this.http.get('/api/attendance-reports/export', {
      params,
      responseType: 'blob'
    });
  }

/**
 * Get attendance records for an attendance session.
 *
 * GET
 * /api/attendance/session/{sessionId}
 */
getAttendanceBySession(
  sessionId: number
): Observable<AttendanceRecord[]> {

  if (!sessionId || sessionId <= 0) {

    throw new Error(
      'Valid attendance session ID is required.'
    );
  }

  return this.http
    .get<
      ApiEnvelope<AttendanceRecord[]> |
      AttendanceRecord[]
    >(
      `${ATTENDANCE_API}/session/${sessionId}`
    )
    .pipe(
      map(response =>
        this.unwrapList(response)
      )
    );
}


}
