// =====================================================
// department.utils.ts
// =====================================================

import {
  DepartmentRequest,
  DepartmentResponse,
  DepartmentSearchRequest,
  RecordStatus
} from '../models/department.model';


// =====================================================
// NORMALIZE DEPARTMENT REQUEST
// =====================================================

export function normalizeDepartmentRequest(
  request: DepartmentRequest
): DepartmentRequest {

  return {
    departmentName: request.departmentName?.trim() ?? '',
    departmentCode: request.departmentCode?.trim().toUpperCase() ?? '',
    ...(request.status ? { status: request.status } : {})
  };
}


// =====================================================
// CREATE DEPARTMENT REQUEST
// =====================================================

export function createDepartmentRequest(
  departmentName: string,
  departmentCode: string,
  status?: RecordStatus
): DepartmentRequest {

  return normalizeDepartmentRequest({
    departmentName,
    departmentCode,
    status
  });
}


// =====================================================
// UPDATE DEPARTMENT REQUEST
// =====================================================

export function updateDepartmentRequest(
  departmentName: string,
  departmentCode: string,
  status?: RecordStatus
): DepartmentRequest {

  return normalizeDepartmentRequest({
    departmentName,
    departmentCode,
    status
  });
}


// =====================================================
// NORMALIZE SEARCH REQUEST
// =====================================================

export function normalizeDepartmentSearchRequest(
  request: DepartmentSearchRequest
): DepartmentSearchRequest {

  const normalized: DepartmentSearchRequest = {};

  const departmentName = request.departmentName?.trim();

  const departmentCode = request.departmentCode?.trim();

  if (departmentName) {
    normalized.departmentName = departmentName;
  }

  if (departmentCode) {
    normalized.departmentCode = departmentCode.toUpperCase();
  }

  if (request.status) {
    normalized.status = request.status;
  }

  return normalized;
}


// =====================================================
// CHECK ACTIVE DEPARTMENT
// =====================================================

export function isActiveDepartment(
  department: DepartmentResponse | null | undefined
): boolean {

  return department?.status === 'ACTIVE';
}


// =====================================================
// CHECK INACTIVE DEPARTMENT
// =====================================================

export function isInactiveDepartment(
  department: DepartmentResponse | null | undefined
): boolean {

  return department?.status === 'INACTIVE';
}


// =====================================================
// CHECK DELETED DEPARTMENT
// =====================================================

export function isDeletedDepartment(
  department: DepartmentResponse | null | undefined
): boolean {

  return department?.status === 'DELETED';
}


// =====================================================
// GET STATUS LABEL
// =====================================================

export function getDepartmentStatusLabel(
  status: RecordStatus
): string {

  switch (status) {

    case 'ACTIVE':
      return 'Active';

    case 'INACTIVE':
      return 'Inactive';

    case 'DELETED':
      return 'Deleted';

    default:
      return status;
  }
}


// =====================================================
// GET STATUS CSS CLASS
// =====================================================

export function getDepartmentStatusClass(
  status: RecordStatus
): string {

  switch (status) {

    case 'ACTIVE':
      return 'status-active';

    case 'INACTIVE':
      return 'status-inactive';

    case 'DELETED':
      return 'status-deleted';

    default:
      return '';
  }
}


// =====================================================
// FORMAT DEPARTMENT CODE
// =====================================================

export function formatDepartmentCode(
  departmentCode: string | null | undefined
): string {

  return departmentCode?.trim().toUpperCase() ?? '';
}


// =====================================================
// FORMAT DEPARTMENT NAME
// =====================================================

export function formatDepartmentName(
  departmentName: string | null | undefined
): string {

  return departmentName?.trim() ?? '';
}


// =====================================================
// GET DISPLAY NAME
// =====================================================

export function getDepartmentDisplayName(
  department: DepartmentResponse | null | undefined
): string {

  if (!department) {
    return '';
  }

  return department.departmentName?.trim() || '-';
}


// =====================================================
// GET DISPLAY CODE
// =====================================================

export function getDepartmentDisplayCode(
  department: DepartmentResponse | null | undefined
): string {

  if (!department) {
    return '';
  }

  return department.departmentCode?.trim().toUpperCase() || '-';
}


// =====================================================
// VALIDATE DEPARTMENT ID
// =====================================================

export function isValidDepartmentId(
  id: number | null | undefined
): boolean {

  return id !== null &&
         id !== undefined &&
         Number.isInteger(id) &&
         id > 0;
}

