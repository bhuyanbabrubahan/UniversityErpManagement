import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Transport } from '../models/transport.model';
@Component({selector:'app-transport-form',standalone:true,imports:[FormsModule],templateUrl:'./transport-form.component.html',styleUrl:'./transport-form.component.scss'})
export class TransportFormComponent { @Input() model:Partial<Transport>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Transport>>(); @Output() cancel=new EventEmitter<void>(); }
