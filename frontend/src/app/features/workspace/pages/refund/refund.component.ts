import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { environment } from '../../../../../environments/environment';

interface Refund { id:number; refundNumber?:string; paymentId:number; studentName?:string; rollNumber?:string; refundAmount:number; refundReason:string; refundMode:string; refundStatus:string; requestedDate?:string; }
@Component({
  standalone:true,
  selector:'app-refund',
  imports:[CommonModule,FormsModule,MatButtonModule,MatFormFieldModule,MatIconModule,MatInputModule,MatSelectModule,MatSnackBarModule],
  templateUrl:'./refund.component.html',
  styleUrl:'./refund.component.scss'
})
export class RefundComponent {
  private readonly http=inject(HttpClient); private readonly snack=inject(MatSnackBar);
  readonly rows=signal<Refund[]>([]); readonly loading=signal(false); readonly total=signal(0);
  page=0; size=10; keyword=''; status='';
  readonly modes=['ONLINE','BANK_TRANSFER','CASH','CHEQUE'];
  ngOnInit(){this.load();}
  load(){this.loading.set(true); const params=new URLSearchParams({page:String(this.page),size:String(this.size),sortBy:'id',direction:'desc'}); const body:any={}; if(this.keyword.trim()){body.studentName=this.keyword.trim();body.refundNumber=this.keyword.trim();body.rollNumber=this.keyword.trim();} if(this.status)body.refundStatus=this.status; const req=Object.keys(body).length?this.http.post<any>(`${environment.apiUrl}/v1/fee-refunds/search?${params}`,body):this.http.get<any>(`${environment.apiUrl}/v1/fee-refunds?${params}`); req.subscribe({next:r=>{const d=r?.data; this.rows.set(d?.content??d??[]); this.total.set(d?.totalElements??this.rows().length); this.loading.set(false);},error:e=>{this.loading.set(false);this.fail(e);}});}
  approve(id:number){this.action(`${environment.apiUrl}/v1/fee-refunds/${id}/approve`);}
  complete(id:number){this.action(`${environment.apiUrl}/v1/fee-refunds/${id}/complete`);}
  reject(id:number){const reason=prompt('Rejection reason'); if(reason?.trim()) this.action(`${environment.apiUrl}/v1/fee-refunds/${id}/reject`,{reason});}
  cancel(id:number){const reason=prompt('Cancellation reason'); if(reason?.trim()) this.action(`${environment.apiUrl}/v1/fee-refunds/${id}/cancel`,{reason});}
  private action(url:string,body?:any){this.http.put(url,body??null).subscribe({next:()=>{this.snack.open('Refund updated successfully','OK',{duration:2200});this.load();},error:e=>this.fail(e)});}
  private fail(e:any){this.snack.open(e?.error?.message??'Unable to complete request','Close',{duration:3500});}
  next(){if((this.page+1)*this.size<this.total()){this.page++;this.load();}}
  prev(){if(this.page>0){this.page--;this.load();}}
}
