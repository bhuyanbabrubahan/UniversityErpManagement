import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Certificate } from '../models/certificate.model';
import { CertificateRequest } from '../models/certificate-request.model';
import { CertificateSearch } from '../models/certificate-search.model';

@Injectable({providedIn:'root'})
export class CertificateService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/certificate`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:CertificateSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Certificate>{ return this.http.get<Certificate>(`${this.url}/${id}`); }
  create(request:CertificateRequest):Observable<Certificate>{ return this.http.post<Certificate>(this.url,request); }
  update(id:number,request:CertificateRequest):Observable<Certificate>{ return this.http.put<Certificate>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
