// =====================================================
// 5. department.routes.ts
// =====================================================

import { Routes } from '@angular/router';

export const DEPARTMENT_ROUTES: Routes = [

  {
    path: '',
    loadComponent: () =>
      import('./pages/department-list/department-list.component')
        .then(m => m.DepartmentListComponent)
  },

  {
    path: 'add',
    loadComponent: () =>
      import('./pages/add-department/add-department.component')
        .then(m => m.AddDepartmentComponent)
  },

  {
    path: ':id/edit',
    loadComponent: () =>
      import('./pages/edit-department/edit-department.component')
        .then(m => m.EditDepartmentComponent)
  },

  {
    path: ':id/view',
    loadComponent: () =>
      import('./pages/view-department/view-department.component')
        .then(m => m.ViewDepartmentComponent)
  }

];