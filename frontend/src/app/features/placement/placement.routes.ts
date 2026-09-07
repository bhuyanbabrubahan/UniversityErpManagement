import { Routes } from '@angular/router';
export const PLACEMENT_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/placement-list.component').then(m=>m.PlacementListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/placement-add.component').then(m=>m.PlacementAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/placement-edit.component').then(m=>m.PlacementEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/placement-view.component').then(m=>m.PlacementViewComponent)}
];
