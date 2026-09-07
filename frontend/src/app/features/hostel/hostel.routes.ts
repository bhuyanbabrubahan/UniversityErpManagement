import { Routes } from '@angular/router';
export const HOSTEL_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/hostel-list.component').then(m=>m.HostelListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/hostel-add.component').then(m=>m.HostelAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/hostel-edit.component').then(m=>m.HostelEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/hostel-view.component').then(m=>m.HostelViewComponent)}
];
