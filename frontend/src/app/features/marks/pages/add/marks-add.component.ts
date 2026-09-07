import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MarksService } from '../../services/marks.service';
import { Marks } from '../../models/marks.model';
import { MarksRequest } from '../../models/marks-request.model';
import { MarksFormComponent } from '../../components/marks-form.component';
@Component({standalone:true,imports:[MarksFormComponent],templateUrl:'./marks-add.component.html',styleUrl:'./marks-add.component.scss'})
export class MarksAddComponent { private readonly service=inject(MarksService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Marks>>({});   save(value:Partial<Marks>){const request=value as MarksRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
