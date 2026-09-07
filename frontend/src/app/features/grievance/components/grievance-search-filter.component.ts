import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-grievance-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./grievance-search-filter.component.html',styleUrl:'./grievance-search-filter.component.scss'})
export class GrievanceSearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
