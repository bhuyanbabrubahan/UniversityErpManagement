import { Routes } from '@angular/router';
export const SCHOLARSHIP_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/scholarship-list.component').then(m=>m.ScholarshipListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/scholarship-add.component').then(m=>m.ScholarshipAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/scholarship-edit.component').then(m=>m.ScholarshipEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/scholarship-view.component').then(m=>m.ScholarshipViewComponent)}
];
