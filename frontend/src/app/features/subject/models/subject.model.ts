import { RecordStatus } from '@app/shared/enums/record-status.enum';


// =========================================================
// CREATE / UPDATE REQUEST
// =========================================================
//
// Backend:
// SubjectRequestDto
//
// IMPORTANT:
// Create / Update ke time exact Semester record select
// kiya jata hai.
//
// Isliye yahan semesterId use hoga.
//
// Search filter ke semesterNumber se iska relation nahi hai.
// =========================================================

export interface SubjectRequest {

  /**
   * Backend creation ke time subjectCode
   * auto-generate karta hai.
   *
   * Isliye frontend create form me normally
   * is field ko nahi bhejna.
   */
  subjectCode?: string | null;

  /**
   * Subject name
   */
  subjectName: string;

  /**
   * Subject credits
   */
  credits: number;

  /**
   * Theory marks
   */
  theoryMarks?: number | null;

  /**
   * Practical marks
   */
  practicalMarks?: number | null;

  /**
   * Exact Semester record ID.
   *
   * Example:
   * 1, 2, 3, 9, 10
   */
  semesterId: number;
}


// =========================================================
// SUBJECT RESPONSE
// =========================================================
//
// Backend:
// SubjectResponseDto
//
// Response me exact semester ID aur display information
// dono available rakhe gaye hain.
// =========================================================

export interface SubjectResponse {

  /**
   * Subject ID
   */
  id: number;

  /**
   * Generated subject code
   */
  subjectCode: string;

  /**
   * Subject name
   */
  subjectName: string;

  /**
   * Credits
   */
  credits: number;

  /**
   * Theory marks
   */
  theoryMarks?: number | null;

  /**
   * Practical marks
   */
  practicalMarks?: number | null;

  /**
   * Exact Semester record ID.
   *
   * Example:
   * 5
   */
  semesterId: number;

  /**
   * Semester display name.
   *
   * Example:
   * Semester 1
   */
  semesterName: string;

  /**
   * Semester number.
   *
   * Example:
   * 1
   * 2
   * 3
   *
   * Optional rakha gaya hai taaki agar backend
   * currently ye field return na kare to
   * frontend compilation issue na aaye.
   */
  semesterNumber?: number | null;

  /**
   * Record status
   */
  status: RecordStatus;


  // =======================================================
  // CREATION INFORMATION
  // =======================================================

  /**
   * Creator user ID
   */
  createdByUserId?: number | null;

  /**
   * Creator display name
   */
  createdBy?: string | null;

  /**
   * Creation timestamp
   */
  createdAt?: string | null;


  // =======================================================
  // UPDATE INFORMATION
  // =======================================================

  /**
   * Last updater user ID
   */
  updatedByUserId?: number | null;

  /**
   * Last updater display name
   */
  updatedBy?: string | null;

  /**
   * Last update timestamp
   */
  updatedAt?: string | null;
}


// =========================================================
// SEARCH REQUEST
// =========================================================
//
// Backend:
// SubjectSearchRequest
//
// Search filters:
//
// - subjectName
// - subjectCode
// - semesterNumber
// - credits
// - status
//
// IMPORTANT:
// Yahan semesterId intentionally nahi hai.
//
// Subject search ke liye semester number use hoga:
//
// Semester 1 -> 1
// Semester 2 -> 2
// Semester 3 -> 3
//
// Backend:
// SubjectSpecification.hasSemesterNumber(...)
// =========================================================

export interface SubjectSearchRequest {

  /**
   * Search by subject name.
   *
   * Example:
   * Java
   */
  subjectName?: string | null;

  /**
   * Search by subject code.
   *
   * Example:
   * SUB001
   */
  subjectCode?: string | null;

  /**
   * Search by semester number.
   *
   * Example:
   *
   * 1 = Semester 1
   * 2 = Semester 2
   * 3 = Semester 3
   * ...
   *
   * Backend:
   * SubjectSpecification.hasSemesterNumber(...)
   */
  semesterNumber?: number | null;

  /**
   * Search by credits.
   *
   * Example:
   * 4
   */
  credits?: number | null;

  /**
   * Search by record status.
   */
  status?: RecordStatus | null;
}


// =========================================================
// PAGINATION
// =========================================================
//
// Common paginated response.
// =========================================================

export interface PageResponse<T> {

  /**
   * Current page records
   */
  content: T[];

  /**
   * Current page number
   */
  pageNumber: number;

  /**
   * Current page size
   */
  pageSize: number;

  /**
   * Total matching records
   */
  totalElements: number;

  /**
   * Total number of pages
   */
  totalPages: number;

  /**
   * Whether current page is the last page
   */
  last: boolean;
}


// =========================================================
// COMMON API RESPONSE
// =========================================================
//
// Backend common API response wrapper.
// =========================================================

export interface ApiResponse<T> {

  /**
   * API operation success/failure
   */
  success: boolean;

  /**
   * Backend response message
   */
  message: string;

  /**
   * HTTP/application status
   */
  status: number;

  /**
   * Actual response data
   */
  data: T;

  /**
   * Response timestamp
   */
  timestamp: string;

  /**
   * Validation / field errors
   */
  errors?: Record<string, string>;
}


// =========================================================
// SUBJECT API TYPES
// =========================================================


// =========================================================
// GET ALL / SEARCH
// =========================================================
//
// Backend response:
//
// ApiResponse<PageResponse<SubjectResponse>>
// =========================================================

export type SubjectPageResponse =
  ApiResponse<PageResponse<SubjectResponse>>;


// =========================================================
// GET BY ID / CREATE / UPDATE
// =========================================================
//
// Backend response:
//
// ApiResponse<SubjectResponse>
// =========================================================

export type SubjectSingleResponse =
  ApiResponse<SubjectResponse>;


// =========================================================
// DELETE
// =========================================================
//
// Backend response:
//
// ApiResponse<void>
// =========================================================

export type SubjectDeleteResponse =
  ApiResponse<void>;