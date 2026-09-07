import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LibraryService } from '../../services/library.service';
import { Library } from '../../models/library.model';
import { LibraryRequest } from '../../models/library-request.model';
import { LibraryFormComponent } from '../../components/library-form.component';
@Component({standalone:true,imports:[LibraryFormComponent],templateUrl:'./library-add.component.html',styleUrl:'./library-add.component.scss'})
export class LibraryAddComponent { private readonly service=inject(LibraryService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Library>>({});   save(value:Partial<Library>){const request=value as LibraryRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
