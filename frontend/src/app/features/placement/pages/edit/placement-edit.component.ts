import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlacementService } from '../../services/placement.service';
import { Placement } from '../../models/placement.model';
import { PlacementRequest } from '../../models/placement-request.model';
import { PlacementFormComponent } from '../../components/placement-form.component';
@Component({standalone:true,imports:[PlacementFormComponent],templateUrl:'./placement-edit.component.html',styleUrl:'./placement-edit.component.scss'})
export class PlacementEditComponent { private readonly service=inject(PlacementService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Placement>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Placement>){const request=value as PlacementRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
