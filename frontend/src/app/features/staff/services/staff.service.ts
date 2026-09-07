import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";
import { Staff } from "../models/staff.model";
import { StaffRequest } from "../models/staff-request.model";
import { StaffSearch } from "../models/staff-search.model";

@Injectable({ providedIn: "root" })
export class StaffService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/erp/staff`;
  list(page = 0, size = 20): Observable<any> {
    return this.http.get<any>(this.url, {
      params: new HttpParams().set("page", page).set("size", size),
    });
  }
  search(request: StaffSearch, page = 0, size = 20): Observable<any> {
    return this.http.post<any>(`${this.url}/search`, request, {
      params: new HttpParams().set("page", page).set("size", size),
    });
  }
  get(id: number): Observable<Staff> {
    return this.http.get<Staff>(`${this.url}/${id}`);
  }
  create(request: StaffRequest): Observable<Staff> {
    return this.http.post<Staff>(this.url, request);
  }
  update(id: number, request: StaffRequest): Observable<Staff> {
    return this.http.put<Staff>(`${this.url}/${id}`, request);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
