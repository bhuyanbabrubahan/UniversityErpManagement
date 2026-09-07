import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Placement } from '../models/placement.model';
import { PlacementRequest } from '../models/placement-request.model';
import { PlacementSearch } from '../models/placement-search.model';

@Injectable({providedIn:'root'})
export class PlacementService {
  private readonly http=inject(HttpClient);
  private readonly url=`${environment.apiUrl}/erp/placement`;
  list(page=0,size=20):Observable<any>{ return this.http.get<any>(this.url,{params:new HttpParams().set('page',page).set('size',size)}); }
  search(request:PlacementSearch,page=0,size=20):Observable<any>{ return this.http.post<any>(`${this.url}/search`,request,{params:new HttpParams().set('page',page).set('size',size)}); }
  get(id:number):Observable<Placement>{ return this.http.get<Placement>(`${this.url}/${id}`); }
  create(request:PlacementRequest):Observable<Placement>{ return this.http.post<Placement>(this.url,request); }
  update(id:number,request:PlacementRequest):Observable<Placement>{ return this.http.put<Placement>(`${this.url}/${id}`,request); }
  delete(id:number):Observable<void>{ return this.http.delete<void>(`${this.url}/${id}`); }
}
