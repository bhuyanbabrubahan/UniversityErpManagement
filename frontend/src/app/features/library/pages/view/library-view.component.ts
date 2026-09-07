import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { LibraryService } from '../../services/library.service';
import { Library } from '../../models/library.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./library-view.component.html',styleUrl:'./library-view.component.scss'})
export class LibraryViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(LibraryService); readonly item=signal<Library|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
