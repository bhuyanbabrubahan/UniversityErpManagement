import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GrievanceService } from '../../services/grievance.service';
import { Grievance } from '../../models/grievance.model';
import { GrievanceRequest } from '../../models/grievance-request.model';
import { GrievanceFormComponent } from '../../components/grievance-form.component';
@Component({standalone:true,imports:[GrievanceFormComponent],templateUrl:'./grievance-add.component.html',styleUrl:'./grievance-add.component.scss'})
export class GrievanceAddComponent { private readonly service=inject(GrievanceService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Grievance>>({});   save(value:Partial<Grievance>){const request=value as GrievanceRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
