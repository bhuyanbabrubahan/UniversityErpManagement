import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TransportService } from '../../services/transport.service';
import { Transport } from '../../models/transport.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./transport-view.component.html',styleUrl:'./transport-view.component.scss'})
export class TransportViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(TransportService); readonly item=signal<Transport|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
