import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-transport-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./transport-search-filter.component.html',styleUrl:'./transport-search-filter.component.scss'})
export class TransportSearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
