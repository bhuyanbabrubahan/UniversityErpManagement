import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HostelService } from '../../services/hostel.service';
import { Hostel } from '../../models/hostel.model';
@Component({standalone:true,imports:[CommonModule,RouterLink],templateUrl:'./hostel-view.component.html',styleUrl:'./hostel-view.component.scss'})
export class HostelViewComponent { private readonly route=inject(ActivatedRoute); private readonly service=inject(HostelService); readonly item=signal<Hostel|null>(null); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id'));this.service.get(id).subscribe(r=>this.item.set(r));} }
