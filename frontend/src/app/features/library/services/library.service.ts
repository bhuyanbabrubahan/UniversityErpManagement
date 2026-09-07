import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Library } from '../models/library.model';
import { LibraryRequest } from '../models/library-request.model';
import { LibrarySearch } from '../models/library-search.model';

@Injectable({providedIn:'root'})
export class LibraryService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/library`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:LibrarySearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Library>{ return this.http.get<Library>(`${this.url}/${id}`); }
  create(request:LibraryRequest):Observable<Library>{ return this.http.post<Library>(this.url,request); }
  update(id:number,request:LibraryRequest):Observable<Library>{ return this.http.put<Library>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
