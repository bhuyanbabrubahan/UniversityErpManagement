import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InstallmentService } from '../../services/installment.service';
import { Installment } from '../../models/installment.model';
import { InstallmentRequest } from '../../models/installment-request.model';
import { InstallmentFormComponent } from '../../components/installment-form.component';
@Component({standalone:true,imports:[InstallmentFormComponent],templateUrl:'./installment-edit.component.html',styleUrl:'./installment-edit.component.scss'})
export class InstallmentEditComponent { private readonly service=inject(InstallmentService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Installment>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Installment>){const request=value as InstallmentRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
