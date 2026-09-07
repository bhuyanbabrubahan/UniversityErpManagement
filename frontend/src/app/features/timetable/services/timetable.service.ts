import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable, map } from "rxjs";

import { environment } from "../../../../environments/environment";

import { Timetable } from "../models/timetable.model";

import { TimetableRequest } from "../models/timetable-request.model";
import { TimetableSearch } from "../models/timetable-search.model";
import { ApiResponse } from "@app/shared/models/api-response.model";
import { TimetablePage } from "../models/timetable-page.model";

@Injectable({
  providedIn: "root",
})
export class TimetableService {
  private readonly http = inject(HttpClient);

  private readonly url = `${environment.apiUrl}/erp/timetable`;

  private buildParams(page: number, size: number): HttpParams {
    return new HttpParams().set("page", page).set("size", size);
  }

  list(page = 0, size = 20): Observable<TimetablePage> {
    return this.http
      .get<ApiResponse<TimetablePage>>(this.url, {
        params: this.buildParams(page, size),
      })
      .pipe(map((response) => response.data));
  }

  search(
    request: TimetableSearch,
    page = 0,
    size = 20,
  ): Observable<TimetablePage> {
    return this.http
      .post<ApiResponse<TimetablePage>>(`${this.url}/search`, request, {
        params: this.buildParams(page, size),
      })
      .pipe(map((response) => response.data));
  }

  get(id: number): Observable<Timetable> {
    return this.http
      .get<ApiResponse<Timetable>>(`${this.url}/${id}`)
      .pipe(map((response) => response.data));
  }

  create(request: TimetableRequest): Observable<Timetable> {
    return this.http
      .post<ApiResponse<Timetable>>(this.url, request)
      .pipe(map((response) => response.data));
  }

  update(id: number, request: TimetableRequest): Observable<Timetable> {
    return this.http
      .put<ApiResponse<Timetable>>(`${this.url}/${id}`, request)
      .pipe(map((response) => response.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.url}/${id}`)
      .pipe(map(() => undefined));
  }
}
