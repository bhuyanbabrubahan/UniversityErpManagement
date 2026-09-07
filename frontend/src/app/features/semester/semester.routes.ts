import { Routes } from '@angular/router';

import { Role } from '@app/shared/enums/role.enum';


export const SEMESTER_ROUTES: Routes = [

  // =====================================================
  // SEMESTER LIST
  // /dashboard/semesters
  // =====================================================

  {
    path: '',

    loadComponent: () =>
      import(
        './pages/semester-list/semester-list.component'
      ).then(
        m => m.SemesterListComponent
      ),

    data: {
      roles: [
        Role.SUPER_ADMIN,
        Role.ADMIN,
        Role.FACULTY,
        Role.STUDENT
      ]
    }
  },


  // =====================================================
  // ADD SEMESTER
  // /dashboard/semesters/add
  // =====================================================

  {
    path: 'add',

    loadComponent: () =>
      import(
        './pages/add-semester/add-semester.component'
      ).then(
        m => m.AddSemesterComponent
      ),

    data: {
      roles: [
        Role.SUPER_ADMIN,
        Role.ADMIN
      ]
    }
  },


  // =====================================================
  // EDIT SEMESTER
  // /dashboard/semesters/edit/:id
  // =====================================================

  {
    path: 'edit/:id',

    loadComponent: () =>
      import(
        './pages/edit-semester/edit-semester.component'
      ).then(
        m => m.EditSemesterComponent
      ),

    data: {
      roles: [
        Role.SUPER_ADMIN,
        Role.ADMIN
      ]
    }
  },


  // =====================================================
  // VIEW SEMESTER
  // /dashboard/semesters/view/:id
  // =====================================================

  {
    path: 'view/:id',

    loadComponent: () =>
      import(
        './pages/view-semester/view-semester.component'
      ).then(
        m => m.ViewSemesterComponent
      ),

    data: {
      roles: [
        Role.SUPER_ADMIN,
        Role.ADMIN,
        Role.FACULTY,
        Role.STUDENT
      ]
    }
  }

];