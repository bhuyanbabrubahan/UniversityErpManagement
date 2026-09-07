import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Transport } from '../models/transport.model';
import { TransportRequest } from '../models/transport-request.model';
import { TransportSearch } from '../models/transport-search.model';

@Injectable({providedIn:'root'})
export class TransportService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/transport`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:TransportSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Transport>{ return this.http.get<Transport>(`${this.url}/${id}`); }
  create(request:TransportRequest):Observable<Transport>{ return this.http.post<Transport>(this.url,request); }
  update(id:number,request:TransportRequest):Observable<Transport>{ return this.http.put<Transport>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
