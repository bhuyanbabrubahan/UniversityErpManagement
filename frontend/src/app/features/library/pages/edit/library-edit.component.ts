import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LibraryService } from '../../services/library.service';
import { Library } from '../../models/library.model';
import { LibraryRequest } from '../../models/library-request.model';
import { LibraryFormComponent } from '../../components/library-form.component';
@Component({standalone:true,imports:[LibraryFormComponent],templateUrl:'./library-edit.component.html',styleUrl:'./library-edit.component.scss'})
export class LibraryEditComponent { private readonly service=inject(LibraryService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Library>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Library>){const request=value as LibraryRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
