import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient } from '@angular/common/http';
@Component({selector:'app-audit-page',standalone:true,imports:[CommonModule,FormsModule,MatButtonModule,MatCardModule,MatIconModule,MatProgressSpinnerModule],template:`
<section class="workspace-page"><header class="workspace-header"><div><span class="eyebrow">GOVERNANCE</span><h1>Audit Logs</h1><p>Who changed what, when and from which module.</p></div><button mat-flat-button (click)="load()"><mat-icon>refresh</mat-icon>Refresh</button></header>
<mat-card class="filter-card"><mat-card-content><div class="filter-grid"><input [(ngModel)]="query" placeholder="Search action, module or user..."><button mat-stroked-button (click)="load()"><mat-icon>search</mat-icon>Search</button></div></mat-card-content></mat-card>
<mat-card class="table-card"><mat-card-content>@if(loading){<div class="loading"><mat-spinner diameter="34"></mat-spinner></div>}@else{<div class="table-wrap"><table><thead><tr><th>ID</th><th>Action</th><th>Module</th><th>Description</th><th>User</th><th>Date</th></tr></thead><tbody>@for(row of rows;track row.id){<tr><td>{{row.id}}</td><td><span class="status-chip">{{row.action||'ACTIVITY'}}</span></td><td>{{row.module||'-'}}</td><td>{{row.description||'-'}}</td><td>{{row.username||row.userName||row.createdBy||'-'}}</td><td>{{row.createdAt||row.timestamp||'-'}}</td></tr>}@empty{<tr><td colspan="6" class="empty">No audit records found.</td></tr>}</tbody></table></div>}</mat-card-content></mat-card></section>`})
export class AuditComponent implements OnInit{private readonly http=inject(HttpClient);rows:any[]=[];loading=false;query='';ngOnInit(){this.load();}load(){this.loading=true;this.http.get<any>('/api/audit-logs?page=0&size=50&sort=createdAt,desc').subscribe({next:r=>{this.rows=r?.data?.content??r?.data??[];this.loading=false;},error:()=>this.loading=false});}}
