import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ReportDefinition } from '../models/report-definition.model';
@Injectable({providedIn:'root'})
export class ReportsService {
 private readonly http=inject(HttpClient); private readonly url=`${environment.apiUrl}/erp/reports`;
 catalogue():Observable<ReportDefinition[]>{return this.http.get<ReportDefinition[]>(this.url);}
 pdfUrl(code:string){return `${this.url}/${encodeURIComponent(code)}/pdf`;}
 excelUrl(code:string){return `${this.url}/${encodeURIComponent(code)}/excel`;}
}
