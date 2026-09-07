import { Routes } from '@angular/router';
export const MARKS_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/marks-list.component').then(m=>m.MarksListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/marks-add.component').then(m=>m.MarksAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/marks-edit.component').then(m=>m.MarksEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/marks-view.component').then(m=>m.MarksViewComponent)}
];
