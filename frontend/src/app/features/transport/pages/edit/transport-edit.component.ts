import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TransportService } from '../../services/transport.service';
import { Transport } from '../../models/transport.model';
import { TransportRequest } from '../../models/transport-request.model';
import { TransportFormComponent } from '../../components/transport-form.component';
@Component({standalone:true,imports:[TransportFormComponent],templateUrl:'./transport-edit.component.html',styleUrl:'./transport-edit.component.scss'})
export class TransportEditComponent { private readonly service=inject(TransportService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Transport>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Transport>){const request=value as TransportRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
