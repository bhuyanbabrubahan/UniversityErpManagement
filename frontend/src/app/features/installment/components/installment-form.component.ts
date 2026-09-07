import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Installment } from '../models/installment.model';
@Component({selector:'app-installment-form',standalone:true,imports:[FormsModule],templateUrl:'./installment-form.component.html',styleUrl:'./installment-form.component.scss'})
export class InstallmentFormComponent { @Input() model:Partial<Installment>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Installment>>(); @Output() cancel=new EventEmitter<void>(); }
