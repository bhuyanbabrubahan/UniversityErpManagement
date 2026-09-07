import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";

import { Observable, map } from "rxjs";

import { environment } from "../../../../environments/environment";

import { Curriculum } from "../models/curriculum.model";
import { CurriculumRequest } from "../models/curriculum-request.model";
import { CurriculumSearch } from "../models/curriculum-search.model";

export interface CurriculumPage {
  content: Curriculum[];

  totalElements: number;
  totalPages: number;

  size: number;
  number: number;

  first: boolean;
  last: boolean;
  empty: boolean;

  numberOfElements?: number;
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  status: number;
  data: T;
  timestamp?: string;
  errors?: unknown;
}

@Injectable({
  providedIn: "root",
})
export class CurriculumService {
  private readonly http = inject(HttpClient);

  private readonly url = `${environment.apiUrl}/erp/curriculum`;

  // =====================================================
  // LIST
  // =====================================================

  list(page = 0, size = 20): Observable<CurriculumPage> {
    const params = new HttpParams().set("page", page).set("size", size);

    return this.http
      .get<ApiResponse<CurriculumPage>>(this.url, { params })
      .pipe(map((response) => response.data));
  }

  // =====================================================
  // SEARCH
  // =====================================================

  search(
    request: CurriculumSearch,
    page = 0,
    size = 20,
  ): Observable<CurriculumPage> {
    const params = new HttpParams().set("page", page).set("size", size);

    return this.http
      .post<
        ApiResponse<CurriculumPage>
      >(`${this.url}/search`, request, { params })
      .pipe(map((response) => response.data));
  }

  // =====================================================
  // GET BY ID
  // =====================================================

  get(id: number): Observable<Curriculum> {
    return this.http
      .get<ApiResponse<Curriculum>>(`${this.url}/${id}`)
      .pipe(map((response) => response.data));
  }

  // =====================================================
  // CREATE
  // =====================================================

  create(request: CurriculumRequest): Observable<Curriculum> {
    return this.http
      .post<ApiResponse<Curriculum>>(this.url, request)
      .pipe(map((response) => response.data));
  }

  // =====================================================
  // UPDATE
  // =====================================================

  update(id: number, request: CurriculumRequest): Observable<Curriculum> {
    return this.http
      .put<ApiResponse<Curriculum>>(`${this.url}/${id}`, request)
      .pipe(map((response) => response.data));
  }

  // =====================================================
  // DELETE
  // =====================================================

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<null>>(`${this.url}/${id}`)
      .pipe(map(() => undefined));
  }
}
