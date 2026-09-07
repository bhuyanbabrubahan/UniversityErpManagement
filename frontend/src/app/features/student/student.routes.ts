import { Routes } from '@angular/router';

export const STUDENT_ROUTES: Routes = [

  {
    path: '',
    loadComponent: () =>
      import('./pages/student-list/student-list.component')
        .then(m => m.StudentListComponent)
  },


  {
    path: 'add',
    loadComponent: () =>
      import('./pages/add-student/add-student.component')
        .then(m => m.AddStudentComponent)
  },


  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./pages/edit-student/edit-student.component')
        .then(m => m.EditStudentComponent)
  },


  {
    path: ':id',
    loadComponent: () =>
      import('./pages/view-student/view-student.component')
        .then(m => m.ViewStudentComponent)
  }

];