import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Placement } from '../models/placement.model';
@Component({selector:'app-placement-form',standalone:true,imports:[FormsModule],templateUrl:'./placement-form.component.html',styleUrl:'./placement-form.component.scss'})
export class PlacementFormComponent { @Input() model:Partial<Placement>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Placement>>(); @Output() cancel=new EventEmitter<void>(); }
