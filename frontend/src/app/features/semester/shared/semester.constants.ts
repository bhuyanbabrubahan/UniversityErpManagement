import { RecordStatus } from '@app/shared/enums/record-status.enum';


/* =====================================================
   SEMESTER NUMBERS
===================================================== */

export const SEMESTER_NUMBERS = Array.from(
  { length: 12 },
  (_, index) => index + 1
);


/* =====================================================
   SEMESTER STATUS
===================================================== */

export const SEMESTER_STATUSES: RecordStatus[] = [
  RecordStatus.ACTIVE,
  RecordStatus.INACTIVE
];


/* =====================================================
   DEFAULT PAGINATION
===================================================== */

export const SEMESTER_PAGE_SIZE = 10;

export const SEMESTER_SORT_BY = 'semesterNumber';

export const SEMESTER_SORT_DIRECTION = 'asc';


/* =====================================================
   DATE FORMAT
===================================================== */

export const SEMESTER_DATE_FORMAT = 'yyyy-MM-dd';