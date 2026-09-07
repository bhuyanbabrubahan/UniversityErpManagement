import { Routes } from '@angular/router';
export const CURRICULUM_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/curriculum-list.component').then(m=>m.CurriculumListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/curriculum-add.component').then(m=>m.CurriculumAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/curriculum-edit.component').then(m=>m.CurriculumEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/curriculum-view.component').then(m=>m.CurriculumViewComponent)}
];
