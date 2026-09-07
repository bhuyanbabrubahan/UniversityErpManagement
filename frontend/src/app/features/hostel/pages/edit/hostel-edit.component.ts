import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HostelService } from '../../services/hostel.service';
import { Hostel } from '../../models/hostel.model';
import { HostelRequest } from '../../models/hostel-request.model';
import { HostelFormComponent } from '../../components/hostel-form.component';
@Component({standalone:true,imports:[HostelFormComponent],templateUrl:'./hostel-edit.component.html',styleUrl:'./hostel-edit.component.scss'})
export class HostelEditComponent { private readonly service=inject(HostelService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Hostel>>({}); readonly id=Number(this.route.snapshot.paramMap.get('id')); ngOnInit(){const id=Number(this.route.snapshot.paramMap.get('id')); this.service.get(id).subscribe(r=>this.model.set(r));} save(value:Partial<Hostel>){const request=value as HostelRequest;const call=this.service.update(this.id,request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
