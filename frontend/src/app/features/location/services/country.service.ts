import { Injectable, inject } from '@angular/core';

import {
  HttpClient
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';

import {
  environment
} from '../../../../environments/environment';

import {
  ApiResponse
} from '../../../shared/models/api-response.model';

import {
  Country
} from '../models/country.model';


@Injectable({
  providedIn: 'root'
})
export class CountryService {


  private http = inject(HttpClient);


  private apiUrl =
    `${environment.apiUrl}/countries`;



  getAllCountries(): Observable<ApiResponse<any>> {


    return this.http.get<ApiResponse<any>>(
      this.apiUrl
    );


  }


}