import { RecordStatus } from '../../../shared/enums/record-status.enum';

/* =====================================================
   SEMESTER MODEL
===================================================== */

export interface SemesterResponse {
  id: number;

  // ===================================================
  // Semester Details
  // ===================================================

  semesterName: string;
  semesterNumber: number;

  semesterStartDate: string;
  semesterEndDate: string;

  totalWorkingDays: number | null;

  // ===================================================
  // Course Details
  // ===================================================

  courseId: number;
  courseCode: string;
  courseName: string;

  // ===================================================
  // Status
  // ===================================================

  status: RecordStatus;

  // ===================================================
  // Creation Information
  // ===================================================

  createdByUserId: number | null;
  createdBy: string | null;
  createdAt: string | null;

  // ===================================================
  // Update Information
  // ===================================================

  updatedByUserId: number | null;
  updatedBy: string | null;
  updatedAt: string | null;
}

/* =====================================================
   SEMESTER REQUEST
===================================================== */

export interface SemesterRequest {
  semesterName: string;
  semesterNumber: number;

  semesterStartDate: string;
  semesterEndDate: string;

  /*
   * Backend says this is automatically calculated.
   * We keep it optional so frontend does not have to
   * send it manually.
   */
  totalWorkingDays?: number | null;

  courseId: number;
}

/* =====================================================
   SEMESTER SEARCH REQUEST
===================================================== */

export interface SemesterSearchRequest {
  // ===================================================
  // Semester
  // ===================================================

  semesterId?: number | null;
  semesterName?: string | null;
  semesterNumber?: number | null;

  // ===================================================
  // Course
  // ===================================================

  courseId?: number | null;
  courseCode?: string | null;
  courseName?: string | null;

  // ===================================================
  // Start Date Range
  // ===================================================

  startDateFrom?: string | null;
  startDateTo?: string | null;

  // ===================================================
  // End Date Range
  // ===================================================

  endDateFrom?: string | null;
  endDateTo?: string | null;

  // ===================================================
  // Working Days Range
  // ===================================================

  minWorkingDays?: number | null;
  maxWorkingDays?: number | null;

  // ===================================================
  // Status
  // ===================================================

  status?: RecordStatus | null;
}

/* =====================================================
   PAGE RESPONSE
===================================================== */

export interface SemesterPageResponse {
  content: SemesterResponse[];

  pageNumber: number;
  pageSize: number;

  totalElements: number;
  totalPages: number;

  last: boolean;
}

