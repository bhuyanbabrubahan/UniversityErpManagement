import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GrievanceService } from '../../services/grievance.service';
import { Grievance } from '../../models/grievance.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./grievance-view.component.html',styleUrl:'./grievance-view.component.scss'})
export class GrievanceViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(GrievanceService); readonly item=signal<Grievance|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
