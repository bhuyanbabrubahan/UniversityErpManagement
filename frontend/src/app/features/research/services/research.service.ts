import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Research } from '../models/research.model';
import { ResearchRequest } from '../models/research-request.model';
import { ResearchSearch } from '../models/research-search.model';

@Injectable({providedIn:'root'})
export class ResearchService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/research`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:ResearchSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Research>{ return this.http.get<Research>(`${this.url}/${id}`); }
  create(request:ResearchRequest):Observable<Research>{ return this.http.post<Research>(this.url,request); }
  update(id:number,request:ResearchRequest):Observable<Research>{ return this.http.put<Research>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
