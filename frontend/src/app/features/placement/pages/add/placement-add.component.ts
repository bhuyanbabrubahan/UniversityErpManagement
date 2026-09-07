import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlacementService } from '../../services/placement.service';
import { Placement } from '../../models/placement.model';
import { PlacementRequest } from '../../models/placement-request.model';
import { PlacementFormComponent } from '../../components/placement-form.component';
@Component({standalone:true,imports:[PlacementFormComponent],templateUrl:'./placement-add.component.html',styleUrl:'./placement-add.component.scss'})
export class PlacementAddComponent { private readonly service=inject(PlacementService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Placement>>({});   save(value:Partial<Placement>){const request=value as PlacementRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
