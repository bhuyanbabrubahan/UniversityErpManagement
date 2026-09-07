import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { InstallmentService } from '../../services/installment.service';
import { Installment } from '../../models/installment.model';
import { InstallmentSearchFilterComponent } from '../../components/installment-search-filter.component';
import { InstallmentTableComponent } from '../../components/installment-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,InstallmentSearchFilterComponent,InstallmentTableComponent],templateUrl:'./installment-list.component.html',styleUrl:'./installment-list.component.scss'})
export class InstallmentListComponent { private readonly service=inject(InstallmentService); readonly rows=signal<Installment[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
