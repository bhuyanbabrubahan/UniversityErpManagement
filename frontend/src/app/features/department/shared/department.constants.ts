// =====================================================
// 3. department.constants.ts
// =====================================================

export const DEPARTMENT_API = {
  BASE: '/api/departments',
  SEARCH: '/api/departments/search'
} as const;

export const DEPARTMENT_DEFAULTS = {
  PAGE: 0,
  SIZE: 10,
  SORT_BY: 'id',
  DIRECTION: 'asc'
} as const;

export const DEPARTMENT_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  DELETED: 'DELETED'
} as const;

export const DEPARTMENT_STATUS_OPTIONS = [
  {
    label: 'Active',
    value: 'ACTIVE'
  },
  {
    label: 'Inactive',
    value: 'INACTIVE'
  }
] as const;