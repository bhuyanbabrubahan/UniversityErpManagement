import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Scholarship } from '../models/scholarship.model';
import { ScholarshipRequest } from '../models/scholarship-request.model';
import { ScholarshipSearch } from '../models/scholarship-search.model';

@Injectable({providedIn:'root'})
export class ScholarshipService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/scholarship`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:ScholarshipSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Scholarship>{ return this.http.get<Scholarship>(`${this.url}/${id}`); }
  create(request:ScholarshipRequest):Observable<Scholarship>{ return this.http.post<Scholarship>(this.url,request); }
  update(id:number,request:ScholarshipRequest):Observable<Scholarship>{ return this.http.put<Scholarship>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
