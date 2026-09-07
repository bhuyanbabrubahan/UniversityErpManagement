import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApiResponse } from '../../../shared/models/api-response.model';
import { DashboardResponse } from '../../dashboard/models/dashboard-response.model';
import { API } from '../../../core/constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private http = inject(HttpClient);


  getDashboard(): Observable<ApiResponse<DashboardResponse>> {

    return this.http.get<ApiResponse<DashboardResponse>>(
      API.DASHBOARD
    );

  }


}