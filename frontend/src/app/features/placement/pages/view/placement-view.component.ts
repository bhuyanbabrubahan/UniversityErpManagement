import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PlacementService } from '../../services/placement.service';
import { Placement } from '../../models/placement.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./placement-view.component.html',styleUrl:'./placement-view.component.scss'})
export class PlacementViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(PlacementService); readonly item=signal<Placement|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
