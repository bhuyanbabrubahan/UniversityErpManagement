import {
  CourseRequest,
  CourseResponse,
  CourseSearchRequest
} from '../models/course.model';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';


// =====================================================
// CREATE EMPTY COURSE SEARCH
// =====================================================

export function createEmptyCourseSearch(): CourseSearchRequest {

  return {

    courseName: '',

    courseCode: '',

    departmentId: undefined,

    status: undefined,

    minDuration: undefined,

    maxDuration: undefined

  };
}


// =====================================================
// NORMALIZE COURSE SEARCH
// =====================================================

export function normalizeCourseSearch(
  search: CourseSearchRequest
): CourseSearchRequest {

  return {

    courseName:
      search.courseName?.trim() || undefined,

    courseCode:
      search.courseCode
        ?.trim()
        .toUpperCase() || undefined,

    departmentId:
      search.departmentId != null
        ? Number(search.departmentId)
        : undefined,

    status:
      search.status ?? undefined,

    minDuration:
      search.minDuration != null
        ? Number(search.minDuration)
        : undefined,

    maxDuration:
      search.maxDuration != null
        ? Number(search.maxDuration)
        : undefined

  };
}


// =====================================================
// CREATE COURSE REQUEST
// =====================================================

export function createCourseRequest(
  value: Partial<CourseRequest>
): CourseRequest {

  return {

    courseCode:
      value.courseCode
        ?.trim()
        .toUpperCase() || '',

    courseName:
      value.courseName
        ?.trim() || '',

    durationInMonths:
      Number(value.durationInMonths),

    description:
      value.description
        ?.trim() || undefined,

    departmentId:
      Number(value.departmentId),

    status:
      value.status ?? RecordStatus.ACTIVE

  };
}


// =====================================================
// COURSE TO REQUEST
// =====================================================

export function courseToRequest(
  course: CourseResponse
): CourseRequest {

  return {

    courseCode:
      course.courseCode,

    courseName:
      course.courseName,

    durationInMonths:
      course.durationInMonths,

    description:
      course.description || undefined,

    departmentId:
      course.departmentId,

    status:
      course.status

  };
}


// =====================================================
// STATUS LABEL
// =====================================================

export function getStatusLabel(
  status: RecordStatus
): string {

  switch (status) {

    case RecordStatus.ACTIVE:
      return 'Active';

    case RecordStatus.INACTIVE:
      return 'Inactive';

    case RecordStatus.DELETED:
      return 'Deleted';

    default:
      return status;

  }
}


// =====================================================
// DURATION LABEL
// =====================================================

export function getDurationLabel(
  months: number
): string {

  if (months === 1) {
    return '1 Month';
  }

  if (months < 12) {
    return `${months} Months`;
  }

  const years =
    Math.floor(months / 12);

  const remainingMonths =
    months % 12;

  if (remainingMonths === 0) {

    return years === 1
      ? '1 Year'
      : `${years} Years`;

  }

  return `${years}Y ${remainingMonths}M`;
}