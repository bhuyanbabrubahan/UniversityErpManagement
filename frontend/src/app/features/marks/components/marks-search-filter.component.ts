import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-marks-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./marks-search-filter.component.html',styleUrl:'./marks-search-filter.component.scss'})
export class MarksSearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
