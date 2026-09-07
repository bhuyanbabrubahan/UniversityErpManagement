import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Hostel } from '../models/hostel.model';
import { HostelRequest } from '../models/hostel-request.model';
import { HostelSearch } from '../models/hostel-search.model';

@Injectable({providedIn:'root'})
export class HostelService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/hostel`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:HostelSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Hostel>{ return this.http.get<Hostel>(`${this.url}/${id}`); }
  create(request:HostelRequest):Observable<Hostel>{ return this.http.post<Hostel>(this.url,request); }
  update(id:number,request:HostelRequest):Observable<Hostel>{ return this.http.put<Hostel>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
