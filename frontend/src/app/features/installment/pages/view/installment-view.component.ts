import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { InstallmentService } from '../../services/installment.service';
import { Installment } from '../../models/installment.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./installment-view.component.html',styleUrl:'./installment-view.component.scss'})
export class InstallmentViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(InstallmentService); readonly item=signal<Installment|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
