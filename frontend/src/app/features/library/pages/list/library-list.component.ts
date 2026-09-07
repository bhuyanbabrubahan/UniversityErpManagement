import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LibraryService } from '../../services/library.service';
import { Library } from '../../models/library.model';
import { LibrarySearchFilterComponent } from '../../components/library-search-filter.component';
import { LibraryTableComponent } from '../../components/library-table.component';
@Component({standalone:true,imports:[CommonModule,RouterLink,LibrarySearchFilterComponent,LibraryTableComponent],templateUrl:'./library-list.component.html',styleUrl:'./library-list.component.scss'})
export class LibraryListComponent { private readonly service=inject(LibraryService); readonly rows=signal<Library[]>([]); loading=signal(false); ngOnInit(){this.load();} load(search?:any){this.loading.set(true);const call=search?this.service.search(search):this.service.list();call.subscribe({next:r=>{this.rows.set(r.content??r);this.loading.set(false);},error:()=>this.loading.set(false)});} remove(id:number){if(confirm('Delete this record?'))this.service.delete(id).subscribe(()=>this.load());} }
