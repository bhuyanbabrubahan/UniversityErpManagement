import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ResearchService } from '../../services/research.service';
import { Research } from '../../models/research.model';
import { ResearchRequest } from '../../models/research-request.model';
import { ResearchFormComponent } from '../../components/research-form.component';
@Component({standalone:true,imports:[ResearchFormComponent],templateUrl:'./research-edit.component.html',styleUrl:'./research-edit.component.scss'})
export class ResearchEditComponent { private readonly service=inject(ResearchService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Research>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Research>){const request=value as ResearchRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
