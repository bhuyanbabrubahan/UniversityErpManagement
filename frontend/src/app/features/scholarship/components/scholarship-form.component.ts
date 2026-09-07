import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Scholarship } from '../models/scholarship.model';
@Component({selector:'app-scholarship-form',standalone:true,imports:[FormsModule],templateUrl:'./scholarship-form.component.html',styleUrl:'./scholarship-form.component.scss'})
export class ScholarshipFormComponent { @Input() model:Partial<Scholarship>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Scholarship>>(); @Output() cancel=new EventEmitter<void>(); }
