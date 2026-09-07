import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ScholarshipService } from '../../services/scholarship.service';
import { Scholarship } from '../../models/scholarship.model';
import { ScholarshipSearchFilterComponent } from '../../components/scholarship-search-filter.component';
import { ScholarshipTableComponent } from '../../components/scholarship-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,ScholarshipSearchFilterComponent,ScholarshipTableComponent],templateUrl:'./scholarship-list.component.html',styleUrl:'./scholarship-list.component.scss'})
export class ScholarshipListComponent { private readonly service=inject(ScholarshipService); readonly rows=signal<Scholarship[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
