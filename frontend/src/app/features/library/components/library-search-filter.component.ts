import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-library-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./library-search-filter.component.html',styleUrl:'./library-search-filter.component.scss'})
export class LibrarySearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
