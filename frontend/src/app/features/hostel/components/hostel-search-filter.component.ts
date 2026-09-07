import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-hostel-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./hostel-search-filter.component.html',styleUrl:'./hostel-search-filter.component.scss'})
export class HostelSearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
