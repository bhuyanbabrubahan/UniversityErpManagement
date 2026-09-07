// =====================================================
// course.routes.ts
// =====================================================

import { Routes } from '@angular/router';

export const COURSE_ROUTES: Routes = [

  // ===================================================
  // COURSE LIST
  // URL: /dashboard/course
  // ===================================================

  {
    path: '',
    loadComponent: () =>
      import('./pages/course-list/course-list.component')
        .then(m => m.CourseListComponent)
  },


  // ===================================================
  // ADD COURSE
  // URL: /dashboard/course/add
  // ===================================================

  {
    path: 'add',
    loadComponent: () =>
      import('./pages/add-course/add-course.component')
        .then(m => m.AddCourseComponent)
  },


  // ===================================================
  // EDIT COURSE
  // URL: /dashboard/course/edit/10
  // ===================================================

  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./pages/edit-course/edit-course.component')
        .then(m => m.EditCourseComponent)
  },


  // ===================================================
  // VIEW COURSE
  // URL: /dashboard/course/10/view
  // ===================================================

  {
    path: 'view/:id',
    loadComponent: () =>
      import('./pages/view-course/view-course.component')
        .then(m => m.ViewCourseComponent)
  }

];