import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HostelService } from '../../services/hostel.service';
import { Hostel } from '../../models/hostel.model';
import { HostelRequest } from '../../models/hostel-request.model';
import { HostelFormComponent } from '../../components/hostel-form.component';
@Component({standalone:true,imports:[HostelFormComponent],templateUrl:'./hostel-add.component.html',styleUrl:'./hostel-add.component.scss'})
export class HostelAddComponent { private readonly service=inject(HostelService); private readonly router=inject(Router); private readonly route=inject(ActivatedRoute); readonly model=signal<Partial<Hostel>>({});   save(value:Partial<Hostel>){const request=value as HostelRequest;const call=this.service.create(request);call.subscribe(()=>this.router.navigate(['../'],{relativeTo:this.route}));} cancel(){this.router.navigate(['../'],{relativeTo:this.route});} }
