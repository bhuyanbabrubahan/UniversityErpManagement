import { Injectable, inject } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

import { ApiResponse } from '../../../shared/models/api-response.model';


@Injectable({
    providedIn: 'root'
})
export class TehsilService {


    private http = inject(HttpClient);


    private apiUrl =
        `${environment.apiUrl}/tehsils`;



    getTehsilsByDistrict(districtId: number) {


        return this.http.get<ApiResponse<any>>(
            `${this.apiUrl}/district/${districtId}`
        );


    }


}