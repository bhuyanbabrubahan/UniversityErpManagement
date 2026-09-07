import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../shared/models/api-response.model';
import { PageResponse } from '../../../shared/models/page-response.model';
import { StudentResponse } from '../models/student-response.model';
import { StudentRequest } from '../models/student-request.model';
import { StudentSearchRequest } from '../models/student-search-request.model';

@Injectable({
    providedIn: 'root'
})
export class StudentService {

    private readonly http = inject(HttpClient);

    private readonly apiUrl =
        `${environment.apiUrl}/students`;

    getAllStudents(
        page = 0,
        size = 10,
        sortBy = 'id',
        direction = 'asc'
    ): Observable<ApiResponse<PageResponse<StudentResponse>>> {

        const params = new HttpParams()
            .set('page', page)
            .set('size', size)
            .set('sortBy', sortBy)
            .set('direction', direction);

        return this.http.get<ApiResponse<PageResponse<StudentResponse>>>(
            this.apiUrl,
            { params }
        );
    }

    getStudentById(
        id: number
    ): Observable<ApiResponse<StudentResponse>> {

        return this.http.get<ApiResponse<StudentResponse>>(
            `${this.apiUrl}/${id}`
        );
    }

    createStudent(
        request: StudentRequest
    ): Observable<ApiResponse<StudentResponse>> {

        return this.http.post<ApiResponse<StudentResponse>>(
            this.apiUrl,
            request
        );
    }

    updateStudent(
        id: number,
        request: StudentRequest
    ): Observable<ApiResponse<StudentResponse>> {

        return this.http.put<ApiResponse<StudentResponse>>(
            `${this.apiUrl}/${id}`,
            request
        );
    }

    deleteStudent(
        id: number
    ): Observable<ApiResponse<void>> {

        return this.http.delete<ApiResponse<void>>(
            `${this.apiUrl}/${id}`
        );
    }

    searchStudents(
        request: StudentSearchRequest,
        page = 0,
        size = 10,
        sortBy = 'id',
        direction = 'asc'
    ): Observable<ApiResponse<PageResponse<StudentResponse>>> {

        const params = new HttpParams()
            .set('page', page)
            .set('size', size)
            .set('sortBy', sortBy)
            .set('direction', direction);

        return this.http.post<ApiResponse<PageResponse<StudentResponse>>>(
            `${this.apiUrl}/search`,
            request,
            { params }
        );
    }
}