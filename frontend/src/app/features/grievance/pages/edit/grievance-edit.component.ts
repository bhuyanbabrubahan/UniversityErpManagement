import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GrievanceService } from '../../services/grievance.service';
import { Grievance } from '../../models/grievance.model';
import { GrievanceRequest } from '../../models/grievance-request.model';
import { GrievanceFormComponent } from '../../components/grievance-form.component';
@Component({standalone:true,imports:[GrievanceFormComponent],templateUrl:'./grievance-edit.component.html',styleUrl:'./grievance-edit.component.scss'})
export class GrievanceEditComponent { private readonly service=inject(GrievanceService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Grievance>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Grievance>){const request=value as GrievanceRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
