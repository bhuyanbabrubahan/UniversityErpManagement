import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CertificateService } from '../../services/certificate.service';
import { Certificate } from '../../models/certificate.model';
import { CertificateSearchFilterComponent } from '../../components/certificate-search-filter.component';
import { CertificateTableComponent } from '../../components/certificate-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,CertificateSearchFilterComponent,CertificateTableComponent],templateUrl:'./certificate-list.component.html',styleUrl:'./certificate-list.component.scss'})
export class CertificateListComponent { private readonly service=inject(CertificateService); readonly rows=signal<Certificate[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
