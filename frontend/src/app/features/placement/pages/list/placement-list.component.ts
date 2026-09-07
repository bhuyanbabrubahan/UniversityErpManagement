import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PlacementService } from '../../services/placement.service';
import { Placement } from '../../models/placement.model';
import { PlacementSearchFilterComponent } from '../../components/placement-search-filter.component';
import { PlacementTableComponent } from '../../components/placement-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,PlacementSearchFilterComponent,PlacementTableComponent],templateUrl:'./placement-list.component.html',styleUrl:'./placement-list.component.scss'})
export class PlacementListComponent { private readonly service=inject(PlacementService); readonly rows=signal<Placement[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
