import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ResearchService } from '../../services/research.service';
import { Research } from '../../models/research.model';
import { ResearchSearchFilterComponent } from '../../components/research-search-filter.component';
import { ResearchTableComponent } from '../../components/research-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,ResearchSearchFilterComponent,ResearchTableComponent],templateUrl:'./research-list.component.html',styleUrl:'./research-list.component.scss'})
export class ResearchListComponent { private readonly service=inject(ResearchService); readonly rows=signal<Research[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
