import { AdmissionStatus } from '../../../shared/enums/admission-staus.enum';
import { RecordStatus } from '../../../shared/enums/record-status.enum';

export interface AdmissionRequest {
  studentId: number;
  departmentId: number;
  courseId: number;
  academicYear: string;
  semesterId: number;
  admissionDate: string;
  remarks: string | null;
}

export interface AdmissionResponse {
  id: number;
  admissionNumber: string;
  studentId: number;
  studentName: string;
  studentRollNumber: string;
  departmentId: number;
  departmentName: string;
  courseId: number;
  courseName: string;
  academicYear: string;
  semesterId: number;
  semesterName: string;
  admissionDate: string;
  admissionStatus: AdmissionStatus;
  status: RecordStatus;
  remarks: string | null;
  createdAt: string | null;
  updatedAt: string | null;
  createdByUserId: number | null;
  createdBy: string | null;
  updatedByUserId: number | null;
  updatedBy: string | null;

}

export interface AdmissionSearchRequest {
  admissionNumber: string | null;
  studentId: number | null;
  studentName: string | null;
  departmentId: number | null;
  courseId: number | null;
  academicYear: string | null;
  semesterId: number | null;
  admissionStatus: AdmissionStatus | null;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  success?: boolean;
  status?: number;
}
