import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { environment } from '../../../../../environments/environment';

interface Account { id:number; email:string; mobileNumber:string; role:string; status:string; emailVerified:boolean; mobileVerified:boolean; }
@Component({selector:'app-account-management',standalone:true,imports:[CommonModule,FormsModule,MatButtonModule,MatIconModule,MatTableModule,MatPaginatorModule,MatFormFieldModule,MatInputModule,MatSelectModule,MatDialogModule],templateUrl:'./account-management.component.html',styleUrl:'./account-management.component.scss'})
export class AccountManagementComponent {
 private http=inject(HttpClient); private dialog=inject(MatDialog); readonly rows=signal<Account[]>([]); readonly total=signal(0); readonly loading=signal(false); page=0; size=20; keyword=''; role=''; status='';
 displayed=['email','mobileNumber','role','status','verification','actions']; roles=['SUPER_ADMIN','ADMIN','VC','REGISTRAR','PRINCIPAL','DEAN','HOD','FACULTY','ACCOUNTANT','ADMISSION_OFFICER','EXAM_CONTROLLER','LIBRARIAN','HOSTEL_WARDEN','TRANSPORT_MANAGER','PLACEMENT_OFFICER','HR','STAFF','STUDENT','PARENT','ALUMNI']; statuses=['ACTIVE','BLOCKED','INACTIVE','PENDING_VERIFICATION'];
 ngOnInit(){this.load();}
 load(){this.loading.set(true); const params:any={page:this.page,size:this.size}; if(this.keyword.trim())params.keyword=this.keyword.trim(); if(this.role)params.role=this.role; if(this.status)params.status=this.status; this.http.get<any>(`${environment.apiUrl}/v1/account-management`,{params}).subscribe({next:r=>{this.rows.set(r.content||[]);this.total.set(r.totalElements||0);this.loading.set(false)},error:()=>this.loading.set(false)});}
 pageChange(e:PageEvent){this.page=e.pageIndex;this.size=e.pageSize;this.load();}
 reset(){this.keyword='';this.role='';this.status='';this.page=0;this.load();}
 toggle(row:Account){const block=row.status!=='BLOCKED'; const reason=window.prompt(block?'Reason for blocking this account:':'Reason for unblocking this account:'); if(!reason?.trim())return; this.http.put<Account>(`${environment.apiUrl}/v1/account-management/${row.id}/${block?'block':'unblock'}`,{reason:reason.trim()}).subscribe({next:()=>this.load()});}
}
