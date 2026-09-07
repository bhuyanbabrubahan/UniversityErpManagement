import {
  Injectable,
  inject
} from '@angular/core';

import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';

import {
  StudentDocument,
  StudentDocumentApiResponse,
  StudentDocumentPage,
  StudentDocumentSearchRequest
} from '../models/student-document.model';
import { DocumentType } from '@app/shared/enums/document-type.enum';

@Injectable({
  providedIn: 'root'
})
export class StudentDocumentService {

  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly http =
    inject(HttpClient);
  readonly documentTypes = Object.values(DocumentType);

  // ============================================================
  // API URL
  // ============================================================

  private readonly apiUrl =
    '/api/student-documents';


  // ============================================================
  // UPLOAD DOCUMENT
  // ============================================================

  uploadDocument(
    studentId: number,
    documentType: string,
    file: File
  ): Observable<
    StudentDocumentApiResponse<StudentDocument>
  > {

    const formData =
      new FormData();


    formData.append(
      'studentId',
      String(studentId)
    );


    formData.append(
      'documentType',
      documentType
    );


    formData.append(
      'file',
      file,
      file.name
    );


    return this.http.post<
      StudentDocumentApiResponse<StudentDocument>
    >(
      `${this.apiUrl}/upload`,
      formData
    );

  }


  // ============================================================
  // GET DOCUMENT BY ID
  // ============================================================

  getDocumentById(
    id: number
  ): Observable<
    StudentDocumentApiResponse<StudentDocument>
  > {

    return this.http.get<
      StudentDocumentApiResponse<StudentDocument>
    >(
      `${this.apiUrl}/${id}`
    );

  }


  // ============================================================
  // DOWNLOAD DOCUMENT
  // ============================================================

  downloadDocument(
    id: number
  ): Observable<Blob> {

    return this.http.get(
      `${this.apiUrl}/download/${id}`,
      {
        responseType: 'blob'
      }
    );

  }


  // ============================================================
  // UPDATE DOCUMENT
  // ============================================================

  updateDocument(
    id: number,
    file: File
  ): Observable<
    StudentDocumentApiResponse<StudentDocument>
  > {

    const formData =
      new FormData();


    formData.append(
      'file',
      file,
      file.name
    );


    return this.http.put<
      StudentDocumentApiResponse<StudentDocument>
    >(
      `${this.apiUrl}/${id}`,
      formData
    );

  }


  // ============================================================
  // DELETE DOCUMENT
  // ============================================================

  deleteDocument(
    id: number
  ): Observable<
    StudentDocumentApiResponse<void>
  > {

    return this.http.delete<
      StudentDocumentApiResponse<void>
    >(
      `${this.apiUrl}/${id}`
    );

  }


  // ============================================================
  // GET ALL DOCUMENTS
  // ============================================================

  getAllDocuments(
    page = 0,
    size = 10,
    sortBy = 'uploadedAt',
    direction = 'DESC'
  ): Observable<
    StudentDocumentApiResponse<StudentDocumentPage>
  > {

    const params =
      this.buildPaginationParams(
        page,
        size,
        sortBy,
        direction
      );


    return this.http.get<
      StudentDocumentApiResponse<StudentDocumentPage>
    >(
      this.apiUrl,
      {
        params
      }
    );

  }


  // ============================================================
  // SEARCH DOCUMENTS
  // ============================================================

  searchDocuments(
    request: StudentDocumentSearchRequest,
    page = 0,
    size = 10,
    sortBy = 'uploadedAt',
    direction = 'DESC'
  ): Observable<
    StudentDocumentApiResponse<StudentDocumentPage>
  > {

    const params =
      this.buildPaginationParams(
        page,
        size,
        sortBy,
        direction
      );


    return this.http.post<
      StudentDocumentApiResponse<StudentDocumentPage>
    >(
      `${this.apiUrl}/search`,
      request,
      {
        params
      }
    );

  }


  // ============================================================
  // PAGINATION PARAMS
  // ============================================================

  private buildPaginationParams(
    page: number,
    size: number,
    sortBy: string,
    direction: string
  ): HttpParams {

    return new HttpParams()

      .set(
        'page',
        String(page)
      )

      .set(
        'size',
        String(size)
      )

      .set(
        'sortBy',
        sortBy
      )

      .set(
        'direction',
        direction
      );

  }

}