import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HostelService } from '../../services/hostel.service';
import { Hostel } from '../../models/hostel.model';
import { HostelSearchFilterComponent } from '../../components/hostel-search-filter.component';
import { HostelTableComponent } from '../../components/hostel-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,HostelSearchFilterComponent,HostelTableComponent],templateUrl:'./hostel-list.component.html',styleUrl:'./hostel-list.component.scss'})
export class HostelListComponent { private readonly service=inject(HostelService); readonly rows=signal<Hostel[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
