import { Routes } from '@angular/router';
export const TRANSPORT_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/transport-list.component').then(m=>m.TransportListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/transport-add.component').then(m=>m.TransportAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/transport-edit.component').then(m=>m.TransportEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/transport-view.component').then(m=>m.TransportViewComponent)}
];
