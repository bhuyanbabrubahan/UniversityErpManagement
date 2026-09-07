import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-research-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./research-search-filter.component.html',styleUrl:'./research-search-filter.component.scss'})
export class ResearchSearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
