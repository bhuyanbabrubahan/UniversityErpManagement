// =====================================================
// department.model.ts
// =====================================================


// =====================================================
// RECORD STATUS
// =====================================================

export type RecordStatus =
  | 'ACTIVE'
  | 'INACTIVE'
  | 'DELETED';


// =====================================================
// DEPARTMENT REQUEST
// Used for CREATE / UPDATE
// =====================================================

export interface DepartmentRequest {
  departmentName: string;
  departmentCode: string;
  description: string;

  /**
   * Optional for create/update.
   * Backend can apply its default status when omitted.
   */
  status?: RecordStatus;
}


// =====================================================
// DEPARTMENT RESPONSE
// Backend Department Response
// =====================================================

export interface DepartmentResponse {

  // ===================================================
  // Department Information
  // ===================================================

  id: number;

  departmentName: string;
  departmentCode: string;
  description: string;
  status: RecordStatus;


  // ===================================================
  // Audit - Created
  // ===================================================

  createdByUserId?: number;

  createdBy?: string;

  createdAt?: string;


  // ===================================================
  // Audit - Updated
  // ===================================================

  updatedByUserId?: number;

  updatedBy?: string;

  updatedAt?: string;
} 


// =====================================================
// DEPARTMENT SEARCH REQUEST
// Used for LIST FILTER / SEARCH
// =====================================================

export interface DepartmentSearchRequest {

  departmentName?: string;
  departmentCode?: string;
  status?: RecordStatus | null;
}


// =====================================================
// PAGE RESPONSE
// Generic Pagination Response
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
// API RESPONSE
// Common Backend Response Wrapper
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
// DEPARTMENT PAGE RESPONSE
// GET ALL / SEARCH
// =====================================================

export type DepartmentPageResponse =
  ApiResponse<PageResponse<DepartmentResponse>>;


// =====================================================
// DEPARTMENT SINGLE RESPONSE
// GET BY ID / CREATE / UPDATE
// =====================================================

export type DepartmentSingleResponse =
  ApiResponse<DepartmentResponse>;


// =====================================================
// DEPARTMENT DELETE RESPONSE
// DELETE
// =====================================================

export type DepartmentDeleteResponse =
  ApiResponse<void>;