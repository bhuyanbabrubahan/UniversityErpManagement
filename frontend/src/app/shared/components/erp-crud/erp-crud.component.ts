import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({selector:'app-erp-crud',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./erp-crud.component.html',styleUrl:'./erp-crud.component.scss'})
export class ErpCrudComponent implements OnInit {
 @Input({required:true}) title='ERP Module'; @Input({required:true}) endpoint=''; @Input() fields:string[]=[];
 http=inject(HttpClient); rows:any[]=[]; model:any={}; editingId:number|null=null; loading=false;
 ngOnInit(){this.load();}
 load(){this.loading=true;this.http.get<any[]>(this.endpoint).subscribe({next:v=>this.rows=v,error:()=>this.rows=[],complete:()=>this.loading=false});}
 edit(row:any){this.editingId=row.id;this.model={...row};}
 reset(){this.editingId=null;this.model={};}
 save(){const req=this.editingId?this.http.put(`${this.endpoint}/${this.editingId}`,this.model):this.http.post(this.endpoint,this.model);req.subscribe({next:()=>{this.reset();this.load();}})}
 remove(id:number){if(confirm('Delete this record?'))this.http.delete(`${this.endpoint}/${id}`).subscribe(()=>this.load());}
}
