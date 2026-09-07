import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CertificateService } from '../../services/certificate.service';
import { Certificate } from '../../models/certificate.model';
import { CertificateRequest } from '../../models/certificate-request.model';
import { CertificateFormComponent } from '../../components/certificate-form.component';
@Component({standalone:true,imports:[CertificateFormComponent],templateUrl:'./certificate-add.component.html',styleUrl:'./certificate-add.component.scss'})
export class CertificateAddComponent { private readonly service=inject(CertificateService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Certificate>>({});   save(value:Partial<Certificate>){const request=value as CertificateRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
