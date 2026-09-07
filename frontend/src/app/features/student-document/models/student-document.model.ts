import { RecordStatus } from "@app/shared/enums/record-status.enum";
import { DocumentType } from "@app/shared/enums/document-type.enum";
import { DocumentStatus } from "@app/shared/enums/document-status.enum";


export interface StudentDocument {
  id: number;
  studentId: number;
  studentName?: string;
  studentCode?: string;
  documentType: DocumentType;
  originalFileName: string;
  storedFileName: string;
  filePath?: string;
  contentType?: string;
  fileSize?: number;
  uploadedAt?: string;
  status?: RecordStatus;
  documentStatus?: DocumentStatus;
  createdAt?: string;
  updatedAt?: string;
  createdByUserId?: number;
  createdBy?: string;
  updatedByUserId?: number;
  updatedBy?: string;
}

export interface StudentDocumentSearchRequest {
  id?: number | null;
  studentId?: number | null;
  documentType?: DocumentType | null;

  originalFileName?: string | null;
  storedFileName?: string | null;
  contentType?: string | null;

  minFileSize?: number | null;
  maxFileSize?: number | null;

  uploadedFrom?: string | null;
  uploadedTo?: string | null;
}

export interface StudentDocumentPage {
  content: StudentDocument[];

  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface StudentDocumentFormValue {
  studentId: number;
  documentType: DocumentType;
  file: File | null;
}

export interface StudentDocumentUploadRequest {
  studentId: number;
  documentType: DocumentType;
  file: File;
}

export interface StudentDocumentApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export { DocumentType, RecordStatus };
