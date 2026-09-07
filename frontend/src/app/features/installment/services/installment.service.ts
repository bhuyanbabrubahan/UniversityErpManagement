import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Installment } from '../models/installment.model';
import { InstallmentRequest } from '../models/installment-request.model';
import { InstallmentSearch } from '../models/installment-search.model';

@Injectable({providedIn:'root'})
export class InstallmentService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/installment`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:InstallmentSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Installment>{ return this.http.get<Installment>(`${this.url}/${id}`); }
  create(request:InstallmentRequest):Observable<Installment>{ return this.http.post<Installment>(this.url,request); }
  update(id:number,request:InstallmentRequest):Observable<Installment>{ return this.http.put<Installment>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
