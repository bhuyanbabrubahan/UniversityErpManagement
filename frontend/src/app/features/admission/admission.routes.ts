import { Routes } from '@angular/router';

export const ADMISSION_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/admission-list/admission-list.component')
        .then(m => m.AdmissionListComponent)
  },
  {
    path: 'add',
    loadComponent: () =>
      import('./pages/add-admission/add-admission.component')
        .then(m => m.AddAdmissionComponent)
  },
  {
    path: 'view/:id',
    loadComponent: () =>
      import('./pages/view-admission/view-admission.component')
        .then(m => m.ViewAdmissionComponent)
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./pages/edit-admission/edit-admission.component')
        .then(m => m.EditAdmissionComponent)
  }
];