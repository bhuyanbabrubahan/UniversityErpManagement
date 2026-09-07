import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

import { environment } from "../../../../environments/environment";

import { Hod } from "../models/hod.model";
import { HodRequest } from "../models/hod-request.model";
import { HodSearch } from "../models/hod-search.model";
import { HodPage } from "../models/hod-page.model";
import { ApiResponse } from "@app/shared/models/api-response.model";

@Injectable({
  providedIn: "root",
})
export class HodService {
  private readonly http = inject(HttpClient);

  private readonly url = `${environment.apiUrl}/erp/hod`;

  // =====================================================
  // LIST
  // =====================================================

  list(page = 0, size = 20): Observable<ApiResponse<HodPage>> {
    const params = new HttpParams().set("page", page).set("size", size);

    return this.http.get<ApiResponse<HodPage>>(this.url, { params });
  }

  // =====================================================
  // SEARCH
  // =====================================================

  search(
    request: HodSearch,
    page = 0,
    size = 20,
  ): Observable<ApiResponse<HodPage>> {
    const params = new HttpParams().set("page", page).set("size", size);

    return this.http.post<ApiResponse<HodPage>>(`${this.url}/search`, request, {
      params,
    });
  }

  // =====================================================
  // FIND BY ID
  // =====================================================

  get(id: number): Observable<ApiResponse<Hod>> {
    return this.http.get<ApiResponse<Hod>>(`${this.url}/${id}`);
  }

  // =====================================================
  // CREATE
  // =====================================================

  create(request: HodRequest): Observable<ApiResponse<Hod>> {
    return this.http.post<ApiResponse<Hod>>(this.url, request);
  }

  // =====================================================
  // UPDATE
  // =====================================================

  update(id: number, request: HodRequest): Observable<ApiResponse<Hod>> {
    return this.http.put<ApiResponse<Hod>>(`${this.url}/${id}`, request);
  }

  // =====================================================
  // DELETE
  // =====================================================

  delete(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.url}/${id}`);
  }
}
