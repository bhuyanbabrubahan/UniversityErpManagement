import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MarksService } from '../../services/marks.service';
import { Marks } from '../../models/marks.model';
import { MarksRequest } from '../../models/marks-request.model';
import { MarksFormComponent } from '../../components/marks-form.component';
@Component({standalone:true,imports:[MarksFormComponent],templateUrl:'./marks-edit.component.html',styleUrl:'./marks-edit.component.scss'})
export class MarksEditComponent { private readonly service=inject(MarksService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Marks>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Marks>){const request=value as MarksRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
