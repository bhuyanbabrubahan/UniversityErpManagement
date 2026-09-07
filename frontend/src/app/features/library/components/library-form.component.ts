import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Library } from '../models/library.model';
@Component({selector:'app-library-form',standalone:true,imports:[FormsModule],templateUrl:'./library-form.component.html',styleUrl:'./library-form.component.scss'})
export class LibraryFormComponent { @Input() model:Partial<Library>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Library>>(); @Output() cancel=new EventEmitter<void>(); }
