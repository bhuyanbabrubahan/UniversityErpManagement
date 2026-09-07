import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CertificateService } from '../../services/certificate.service';
import { Certificate } from '../../models/certificate.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./certificate-view.component.html',styleUrl:'./certificate-view.component.scss'})
export class CertificateViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(CertificateService); readonly item=signal<Certificate|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
