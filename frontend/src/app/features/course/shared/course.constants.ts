// =====================================================
// course.constants.ts
// =====================================================

import { RecordStatus } from '@app/shared/enums/record-status.enum';


// =====================================================
// STATUS OPTIONS
// =====================================================

export const COURSE_STATUS_OPTIONS: RecordStatus[] = [

  RecordStatus.ACTIVE,

  RecordStatus.INACTIVE

];


// =====================================================
// DEFAULT VALUES
// =====================================================

export const DEFAULT_COURSE_PAGE_SIZE = 10;

export const DEFAULT_COURSE_SORT_BY = 'id';

export const DEFAULT_COURSE_SORT_DIRECTION = 'asc';


// =====================================================
// VALIDATION
// =====================================================

export const COURSE_NAME_MAX_LENGTH = 100;

export const COURSE_CODE_MAX_LENGTH = 30;

export const COURSE_DURATION_MIN_MONTHS = 1;


// =====================================================
// MESSAGES
// =====================================================

export const COURSE_MESSAGES = {

  LOAD_ERROR:
    'Unable to load courses.',

  SEARCH_ERROR:
    'Unable to search courses.',

  CREATE_SUCCESS:
    'Course created successfully.',

  UPDATE_SUCCESS:
    'Course updated successfully.',

  DELETE_SUCCESS:
    'Course deleted successfully.',

  DELETE_ERROR:
    'Unable to delete course.',

  NOT_FOUND:
    'Course not found.'

} as const;