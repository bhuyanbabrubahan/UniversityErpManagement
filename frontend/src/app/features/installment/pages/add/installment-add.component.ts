import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InstallmentService } from '../../services/installment.service';
import { Installment } from '../../models/installment.model';
import { InstallmentRequest } from '../../models/installment-request.model';
import { InstallmentFormComponent } from '../../components/installment-form.component';
@Component({standalone:true,imports:[InstallmentFormComponent],templateUrl:'./installment-add.component.html',styleUrl:'./installment-add.component.scss'})
export class InstallmentAddComponent { private readonly service=inject(InstallmentService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Installment>>({});   save(value:Partial<Installment>){const request=value as InstallmentRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
