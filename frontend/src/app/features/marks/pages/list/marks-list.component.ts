import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MarksService } from '../../services/marks.service';
import { Marks } from '../../models/marks.model';
import { MarksSearchFilterComponent } from '../../components/marks-search-filter.component';
import { MarksTableComponent } from '../../components/marks-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,MarksSearchFilterComponent,MarksTableComponent],templateUrl:'./marks-list.component.html',styleUrl:'./marks-list.component.scss'})
export class MarksListComponent { private readonly service=inject(MarksService); readonly rows=signal<Marks[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
