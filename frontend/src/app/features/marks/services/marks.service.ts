import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Marks } from '../models/marks.model';
import { MarksRequest } from '../models/marks-request.model';
import { MarksSearch } from '../models/marks-search.model';

@Injectable({providedIn:'root'})
export class MarksService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/marks`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:MarksSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Marks>{ return this.http.get<Marks>(`${this.url}/${id}`); }
  create(request:MarksRequest):Observable<Marks>{ return this.http.post<Marks>(this.url,request); }
  update(id:number,request:MarksRequest):Observable<Marks>{ return this.http.put<Marks>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
