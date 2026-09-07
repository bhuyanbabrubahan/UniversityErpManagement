import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScholarshipService } from '../../services/scholarship.service';
import { Scholarship } from '../../models/scholarship.model';
import { ScholarshipRequest } from '../../models/scholarship-request.model';
import { ScholarshipFormComponent } from '../../components/scholarship-form.component';
@Component({standalone:true,imports:[ScholarshipFormComponent],templateUrl:'./scholarship-edit.component.html',styleUrl:'./scholarship-edit.component.scss'})
export class ScholarshipEditComponent { private readonly service=inject(ScholarshipService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Scholarship>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Scholarship>){const request=value as ScholarshipRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
