import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Certificate } from '../models/certificate.model';
@Component({selector:'app-certificate-form',standalone:true,imports:[FormsModule],templateUrl:'./certificate-form.component.html',styleUrl:'./certificate-form.component.scss'})
export class CertificateFormComponent { @Input() model:Partial<Certificate>={}; @Input() submitLabel='Save'; @Output() save=new EventEmitter<Partial<Certificate>>(); @Output() cancel=new EventEmitter<void>(); }
