import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Grievance } from '../models/grievance.model';
import { GrievanceRequest } from '../models/grievance-request.model';
import { GrievanceSearch } from '../models/grievance-search.model';

@Injectable({providedIn:'root'})
export class GrievanceService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/grievance`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:GrievanceSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Grievance>{ return this.http.get<Grievance>(`${this.url}/${id}`); }
  create(request:GrievanceRequest):Observable<Grievance>{ return this.http.post<Grievance>(this.url,request); }
  update(id:number,request:GrievanceRequest):Observable<Grievance>{ return this.http.put<Grievance>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
