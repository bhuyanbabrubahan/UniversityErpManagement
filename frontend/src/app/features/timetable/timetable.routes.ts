import { Routes } from '@angular/router';
export const TIMETABLE_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/timetable-list.component').then(m=>m.TimetableListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/timetable-add.component').then(m=>m.TimetableAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/timetable-edit.component').then(m=>m.TimetableEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/timetable-view.component').then(m=>m.TimetableViewComponent)}
];
