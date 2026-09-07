import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { GrievanceService } from '../../services/grievance.service';
import { Grievance } from '../../models/grievance.model';
import { GrievanceSearchFilterComponent } from '../../components/grievance-search-filter.component';
import { GrievanceTableComponent } from '../../components/grievance-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,GrievanceSearchFilterComponent,GrievanceTableComponent],templateUrl:'./grievance-list.component.html',styleUrl:'./grievance-list.component.scss'})
export class GrievanceListComponent { private readonly service=inject(GrievanceService); readonly rows=signal<Grievance[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
