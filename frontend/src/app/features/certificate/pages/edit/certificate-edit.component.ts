import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CertificateService } from '../../services/certificate.service';
import { Certificate } from '../../models/certificate.model';
import { CertificateRequest } from '../../models/certificate-request.model';
import { CertificateFormComponent } from '../../components/certificate-form.component';
@Component({standalone:true,imports:[CertificateFormComponent],templateUrl:'./certificate-edit.component.html',styleUrl:'./certificate-edit.component.scss'})
export class CertificateEditComponent { private readonly service=inject(CertificateService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Certificate>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Certificate>){const request=value as CertificateRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
