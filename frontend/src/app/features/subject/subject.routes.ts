import { Routes } from '@angular/router';

export const SUBJECT_ROUTES: Routes = [

  // =====================================================
  // SUBJECT LIST
  // URL: /dashboard/subjects
  // =====================================================

  {
    path: '',
    loadComponent: () =>
      import(
        './pages/subject-list/subject-list.component'
      ).then(
        m => m.SubjectListComponent
      )
  },


  // =====================================================
  // ADD SUBJECT
  // URL: /dashboard/subjects/add
  // =====================================================

  {
    path: 'add',
    loadComponent: () =>
      import(
        './pages/add-subject/add-subject.component'
      ).then(
        m => m.AddSubjectComponent
      )
  },


  // =====================================================
  // VIEW SUBJECT
  // URL: /dashboard/subjects/view/:id
  // =====================================================

  {
    path: 'view/:id',
    loadComponent: () =>
      import(
        './pages/view-subject/view-subject.component'
      ).then(
        m => m.ViewSubjectComponent
      )
  },


  // =====================================================
  // EDIT SUBJECT
  // URL: /dashboard/subjects/edit/:id
  // =====================================================

  {
    path: 'edit/:id',
    loadComponent: () =>
      import(
        './pages/edit-subject/edit-subject.component'
      ).then(
        m => m.EditSubjectComponent
      )
  },


  // =====================================================
  // DELETE SUBJECT
  // URL: /dashboard/subjects/delete/:id
  // =====================================================

  {
    path: 'delete/:id',
    loadComponent: () =>
      import(
        './components/delete-subject-dialog/delete-subject-dialog.component'
      ).then(
        m => m.DeleteSubjectDialogComponent
      )
  }

];