import { Routes } from '@angular/router';
export const LIBRARY_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/library-list.component').then(m=>m.LibraryListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/library-add.component').then(m=>m.LibraryAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/library-edit.component').then(m=>m.LibraryEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/library-view.component').then(m=>m.LibraryViewComponent)}
];
