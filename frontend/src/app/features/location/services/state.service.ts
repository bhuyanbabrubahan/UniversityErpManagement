import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import { ApiResponse } from '../../../shared/models/api-response.model';


@Injectable({
    providedIn: 'root'
})
export class StateService {


    private http = inject(HttpClient);


    private apiUrl =
        `${environment.apiUrl}/states`;



    getStatesByCountry(countryId: number) {


        return this.http.get<ApiResponse<any>>(
            `${this.apiUrl}/country/${countryId}`
        );


    }


}