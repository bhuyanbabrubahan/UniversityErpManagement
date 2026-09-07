import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScholarshipService } from '../../services/scholarship.service';
import { Scholarship } from '../../models/scholarship.model';
import { ScholarshipRequest } from '../../models/scholarship-request.model';
import { ScholarshipFormComponent } from '../../components/scholarship-form.component';
@Component({standalone:true,imports:[ScholarshipFormComponent],templateUrl:'./scholarship-add.component.html',styleUrl:'./scholarship-add.component.scss'})
export class ScholarshipAddComponent { private readonly service=inject(ScholarshipService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Scholarship>>({});   save(value:Partial<Scholarship>){const request=value as ScholarshipRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
