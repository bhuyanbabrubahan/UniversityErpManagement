import { RecordStatus } from '@app/shared/enums/record-status.enum';


// =====================================================
// DEFAULT PAGINATION
// =====================================================

export const SUBJECT_DEFAULT_PAGE = 0;

export const SUBJECT_DEFAULT_SIZE = 10;

export const SUBJECT_DEFAULT_SORT_BY = 'id';

export const SUBJECT_DEFAULT_DIRECTION = 'asc';


// =====================================================
// SUBJECT STATUS
// =====================================================

export const SUBJECT_STATUSES: RecordStatus[] = [

  RecordStatus.ACTIVE,

  RecordStatus.INACTIVE

];


// =====================================================
// CREDITS
// =====================================================

export const SUBJECT_CREDIT_OPTIONS: number[] = [

  1,
  2,
  3,
  4,
  5,
  6,
  7,
  8,
  9,
  10

];


// =====================================================
// MARKS
// =====================================================

export const SUBJECT_MAX_MARKS = 100;

export const SUBJECT_MIN_CREDITS = 1;

export const SUBJECT_MAX_CREDITS = 10;


// =====================================================
// MESSAGES
// =====================================================

export const SUBJECT_MESSAGES = {

  CREATE_SUCCESS:
    'Subject created successfully.',

  UPDATE_SUCCESS:
    'Subject updated successfully.',

  DELETE_SUCCESS:
    'Subject deleted successfully.',

  LOAD_ERROR:
    'Unable to load subjects.',

  SAVE_ERROR:
    'Unable to save subject.',

  DELETE_ERROR:
    'Unable to delete subject.',

  SEARCH_ERROR:
    'Unable to search subjects.'

} as const;