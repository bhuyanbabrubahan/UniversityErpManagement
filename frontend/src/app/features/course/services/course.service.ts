import { Injectable, inject } from '@angular/core';
import {
  HttpClient,
  HttpParams
} from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  ApiResponse,
  CourseDeleteResponse,
  CoursePageResponse,
  CourseRequest,
  CourseResponse,
  CourseSearchRequest,
  CourseSingleResponse
} from '../models/course.model';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private readonly http = inject(HttpClient);

  private readonly API_URL = '/api/courses';

  createCourse(
    request: CourseRequest
  ): Observable<CourseSingleResponse> {

    return this.http.post<CourseSingleResponse>(
      this.API_URL,
      request
    );
  }

  getCourseById(
    id: number
  ): Observable<CourseSingleResponse> {

    return this.http.get<CourseSingleResponse>(
      `${this.API_URL}/${id}`
    );
  }

  updateCourse(
    id: number,
    request: CourseRequest
  ): Observable<CourseSingleResponse> {

    return this.http.put<CourseSingleResponse>(
      `${this.API_URL}/${id}`,
      request
    );
  }

  deleteCourse(
    id: number
  ): Observable<CourseDeleteResponse> {

    return this.http.delete<CourseDeleteResponse>(
      `${this.API_URL}/${id}`
    );
  }

  getAllCourses(
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'asc'
  ): Observable<CoursePageResponse> {

    const params = this.buildParams(
      page,
      size,
      sortBy,
      direction
    );

    return this.http.get<CoursePageResponse>(
      this.API_URL,
      { params }
    );
  }

  searchCourses(
    request: CourseSearchRequest,
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'asc'
  ): Observable<CoursePageResponse> {

    const params = this.buildParams(
      page,
      size,
      sortBy,
      direction
    );

    return this.http.post<CoursePageResponse>(
      `${this.API_URL}/search`,
      request,
      { params }
    );
  }

  private buildParams(
    page: number,
    size: number,
    sortBy: string,
    direction: string
  ): HttpParams {

    return new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);
  }
}