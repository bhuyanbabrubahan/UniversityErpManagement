import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ResearchService } from '../../services/research.service';
import { Research } from '../../models/research.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./research-view.component.html',styleUrl:'./research-view.component.scss'})
export class ResearchViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(ResearchService); readonly item=signal<Research|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
