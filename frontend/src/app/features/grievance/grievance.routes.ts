import { Routes } from '@angular/router';
export const GRIEVANCE_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/grievance-list.component').then(m=>m.GrievanceListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/grievance-add.component').then(m=>m.GrievanceAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/grievance-edit.component').then(m=>m.GrievanceEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/grievance-view.component').then(m=>m.GrievanceViewComponent)}
];
