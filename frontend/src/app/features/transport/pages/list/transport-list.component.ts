import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TransportService } from '../../services/transport.service';
import { Transport } from '../../models/transport.model';
import { TransportSearchFilterComponent } from '../../components/transport-search-filter.component';
import { TransportTableComponent } from '../../components/transport-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,TransportSearchFilterComponent,TransportTableComponent],templateUrl:'./transport-list.component.html',styleUrl:'./transport-list.component.scss'})
export class TransportListComponent { private readonly service=inject(TransportService); readonly rows=signal<Transport[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
