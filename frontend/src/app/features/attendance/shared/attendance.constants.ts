export const ATTENDANCE_API =
  '/api/attendances';

export const ATTENDANCE_SESSION_API =
  '/api/attendance-sessions';

export const ATTENDANCE_LOOKUP_API = {

  facultySubjects:
    '/api/faculty-subjects/my-subjects',

  students:
    '/api/students'

} as const;

export const ATTENDANCE_PAGE_SIZE = 20;

export const ATTENDANCE_EXCEL_HEADERS = [
  'studentId',
  'rollNumber',
  'studentName',
  'attendanceStatus',
  'attendanceSource',
  'remarks'
] as const;
