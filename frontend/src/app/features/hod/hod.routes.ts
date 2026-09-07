import { Routes } from '@angular/router';
export const HOD_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/hod-list.component').then(m=>m.HodListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/hod-add.component').then(m=>m.HodAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/hod-edit.component').then(m=>m.HodEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/hod-view.component').then(m=>m.HodViewComponent)}
];
