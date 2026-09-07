import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ScholarshipService } from '../../services/scholarship.service';
import { Scholarship } from '../../models/scholarship.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./scholarship-view.component.html',styleUrl:'./scholarship-view.component.scss'})
export class ScholarshipViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(ScholarshipService); readonly item=signal<Scholarship|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
