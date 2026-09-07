import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Marks } from '../models/marks.model';
@Component({selector:'app-marks-form',standalone:true,imports:[FormsModule],templateUrl:'./marks-form.component.html',styleUrl:'./marks-form.component.scss'})
export class MarksFormComponent { @Input() model:Partial<Marks>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Marks>>(); @Output() cancel=new EventEmitter<void>(); }
