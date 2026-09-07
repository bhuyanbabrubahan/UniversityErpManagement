import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MarksService } from '../../services/marks.service';
import { Marks } from '../../models/marks.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./marks-view.component.html',styleUrl:'./marks-view.component.scss'})
export class MarksViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(MarksService); readonly item=signal<Marks|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
