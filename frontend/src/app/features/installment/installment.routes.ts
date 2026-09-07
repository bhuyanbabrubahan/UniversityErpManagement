import { Routes } from '@angular/router';
export const INSTALLMENT_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/installment-list.component').then(m=>m.InstallmentListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/installment-add.component').then(m=>m.InstallmentAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/installment-edit.component').then(m=>m.InstallmentEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/installment-view.component').then(m=>m.InstallmentViewComponent)}
];
