import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Grievance } from '../models/grievance.model';
@Component({selector:'app-grievance-form',standalone:true,imports:[FormsModule],templateUrl:'./grievance-form.component.html',styleUrl:'./grievance-form.component.scss'})
export class GrievanceFormComponent { @Input() model:Partial<Grievance>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Grievance>>(); @Output() cancel=new EventEmitter<void>(); }
