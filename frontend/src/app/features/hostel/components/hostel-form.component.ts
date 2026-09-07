import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Hostel } from '../models/hostel.model';
@Component({selector:'app-hostel-form',standalone:true,imports:[FormsModule],templateUrl:'./hostel-form.component.html',styleUrl:'./hostel-form.component.scss'})
export class HostelFormComponent { @Input() model:Partial<Hostel>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Hostel>>(); @Output() cancel=new EventEmitter<void>(); }
