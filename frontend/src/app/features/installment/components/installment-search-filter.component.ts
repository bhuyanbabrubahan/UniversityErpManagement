import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({selector:'app-installment-search-filter',standalone:true,imports:[FormsModule],templateUrl:'./installment-search-filter.component.html',styleUrl:'./installment-search-filter.component.scss'})
export class InstallmentSearchFilterComponent { @Output() search=new EventEmitter<any>(); @Output() reset=new EventEmitter<void>(); keyword=''; status=''; submit(){this.search.emit({keyword:this.keyword,status:this.status});} clear(){this.keyword='';this.status='';this.reset.emit();} }
