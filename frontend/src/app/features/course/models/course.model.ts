import { RecordStatus } from '@app/shared/enums/record-status.enum';


// =====================================================
// COURSE REQUEST
// =====================================================

export interface CourseRequest {

  courseCode: string;

  courseName: string;

  durationInMonths: number;

  description?: string;

  departmentId: number;

  status?: RecordStatus;
}


// =====================================================
// COURSE RESPONSE
// =====================================================

export interface CourseResponse {

  id: number;

  courseCode: string;

  courseName: string;

  durationInMonths: number;

  description?: string;

  status: RecordStatus;

  departmentId: number;

  departmentName: string;


  // ===================================================
  // AUDIT FIELDS
  // ===================================================

  createdAt?: string;

  updatedAt?: string;

  createdByUserId?: number;

  createdBy?: string;

  createdDate?: string;

  updatedByUserId?: number;

  updatedBy?: string;

  updatedDate?: string;
}


// =====================================================
// COURSE SEARCH REQUEST
// =====================================================

export interface CourseSearchRequest {

  courseName?: string;

  courseCode?: string;

  departmentId?: number;

  status?: RecordStatus;

  minDuration?: number;

  maxDuration?: number;
}


// =====================================================
// PAGE RESPONSE
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
// GENERIC API RESPONSE
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
// COURSE API RESPONSE TYPES
// =====================================================

export type CoursePageResponse =
  ApiResponse<
    PageResponse<CourseResponse>
  >;


export type CourseSingleResponse =
  ApiResponse<CourseResponse>;


export type CourseDeleteResponse =
  ApiResponse<void>;