export const ADMISSION_SORT_FIELDS = [
  'id',
  'admissionNumber',
  'admissionDate',
  'academicYear'
] as const;

export const ADMISSION_DEFAULT_PAGE_SIZE = 10;

export const ADMISSION_ACADEMIC_YEAR_PATTERN = /^\d{4}-\d{4}$/;
