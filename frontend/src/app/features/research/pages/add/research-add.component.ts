import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ResearchService } from '../../services/research.service';
import { Research } from '../../models/research.model';
import { ResearchRequest } from '../../models/research-request.model';
import { ResearchFormComponent } from '../../components/research-form.component';
@Component({standalone:true,imports:[ResearchFormComponent],templateUrl:'./research-add.component.html',styleUrl:'./research-add.component.scss'})
export class ResearchAddComponent { private readonly service=inject(ResearchService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Research>>({});   save(value:Partial<Research>){const request=value as ResearchRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
