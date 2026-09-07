import { Routes } from '@angular/router';
export const CERTIFICATE_ROUTES: Routes = [
  {path:'',loadComponent:()=>import('./pages/list/certificate-list.component').then(m=>m.CertificateListComponent)},
  {path:'add',loadComponent:()=>import('./pages/add/certificate-add.component').then(m=>m.CertificateAddComponent)},
  {path:'edit/:id',loadComponent:()=>import('./pages/edit/certificate-edit.component').then(m=>m.CertificateEditComponent)},
  {path:':id',loadComponent:()=>import('./pages/view/certificate-view.component').then(m=>m.CertificateViewComponent)}
];
