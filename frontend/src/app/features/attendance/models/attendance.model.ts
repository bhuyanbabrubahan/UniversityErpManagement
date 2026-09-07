import { AttendanceStatus } from '../../../shared/enums/attendance-status.enum';
import { AttendanceSource } from '../../../shared/enums/attendance-source.enum';
import { RecordStatus } from '../../../shared/enums/record-status.enum';
import { AttendanceSessionStatus } from '../../../shared/enums/attendance-session-status.enum';

export interface AttendanceSession {
  id: number;
  facultySubjectMappingId: number;
  facultyId?: number;
  facultyName?: string;
  subjectId?: number;
  subjectCode?: string;
  subjectName?: string;
  semesterId?: number;
  semesterName?: string;
  attendanceDate: string;
  startedAt?: string | null;
  completedAt?: string | null;
  attendanceSource: AttendanceSource;
  sessionStatus: AttendanceSessionStatus;
  status: RecordStatus;
  totalStudents?: number;
  markedStudents?: number;
  presentCount?: number;
  absentCount?: number;
  halfDayCount?: number;
  leaveCount?: number;
}

export interface AttendanceRecord {
  id: number;
  attendanceSessionId: number;
  sessionStatus: AttendanceSessionStatus;
  studentId: number;
  rollNumber: string;
  studentName: string;
  facultySubjectMappingId: number;
  facultyId?: number;
  facultyName: string;
  subjectId?: number;
  subjectCode?: string;
  subjectName: string;
  attendanceDate: string;
  attendanceStatus: AttendanceStatus;
  remarks?: string | null;
  attendanceSource: AttendanceSource;
  markedAt?: string | null;
  status: RecordStatus;
  createdByUserId?: number;
  createdBy?: string;
  createdAt?: string;
  updatedByUserId?: number;
  updatedBy?: string;
  updatedAt?: string;
}

export interface AttendanceRequest {
  attendanceSessionId: number;
  studentId: number;
  attendanceStatus: AttendanceStatus;
  remarks?: string | null;
}

export interface AttendanceUpdateRequest {
  attendanceStatus: AttendanceStatus;
  remarks?: string | null;
}

export interface AttendanceMarkItem {
  studentId: number;
  attendanceStatus: AttendanceStatus;
  remarks?: string | null;
}

export interface BulkAttendanceRequest {
  attendanceSessionId: number;
  attendanceRecords: AttendanceMarkItem[];
}

export interface AttendanceSearchRequest {
  attendanceSessionId?: number | null;
  studentId?: number | null;
  facultyId?: number | null;
  subjectId?: number | null;
  facultySubjectMappingId?: number | null;
  attendanceDate?: string | null;
  attendanceStatus?: AttendanceStatus | null;
  sessionStatus?: AttendanceSessionStatus | null;
  status?: RecordStatus | null;
}

export interface AttendanceClassRequest {
  attendanceSessionId: number;
}

export interface AttendanceStudentRow {
  studentId: number;
  rollNumber: string;
  studentName: string;
  departmentName?: string;
  courseName?: string;
  semesterName?: string;
  section?: string;
  batch?: string;
  attendanceStatus: AttendanceStatus;
  attendanceSource?: AttendanceSource;
  remarks: string;
  attendanceId?: number;
}

export interface AttendanceClassResponse {
  attendanceSessionId: number;
  sessionStatus: AttendanceSessionStatus;
  attendanceSource: AttendanceSource;
  facultySubjectMappingId: number;
  facultyId?: number;
  facultyName?: string;
  subjectId?: number;
  subjectCode?: string;
  subjectName?: string;
  departmentId?: number;
  departmentName?: string;
  semesterId?: number;
  semesterName?: string;
  attendanceDate: string;
  attendanceOpenedAt?: string;
  students: AttendanceStudentRow[];
}

export interface AttendanceSessionRequest {
  facultySubjectMappingId: number;
  attendanceDate: string;
  attendanceSource: AttendanceSource;
}

export interface TimetableSlot {
  timetableId: number;
  dayOfWeek?: string;
  startTime?: string;
  endTime?: string;
  period?: string;
  room?: string;
  section?: string;
}

export interface FacultySubjectOption {
  id: number;
  facultyId?: number;
  facultyName?: string;
  subjectId?: number;
  subjectCode?: string;
  subjectName: string;
  semesterName?: string;
  className?: string;
  timetableSlots?: TimetableSlot[];
}

export interface StudentOption {
  id: number;
  rollNo?: string;
  rollNumber?: string;
  fullName: string;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
