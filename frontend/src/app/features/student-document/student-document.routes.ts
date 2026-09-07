import { Routes } from '@angular/router';

export const STUDENT_DOCUMENT_ROUTES: Routes = [

  {
    path: '',
    loadComponent: () =>
      import(
        './pages/document-list/document-list.component'
      ).then(
        m => m.DocumentListComponent
      )
  },

  {
    path: 'add-document',
    loadComponent: () =>
      import(
        './pages/add-document/add-document.component'
      ).then(
        m => m.AddDocumentComponent
      )
  },

  {
    path: 'edit/:id',
    loadComponent: () =>
      import(
        './pages/edit-document/edit-document.component'
      ).then(
        m => m.EditDocumentComponent
      )
  },

  {
    path: 'view/:id',
    loadComponent: () =>
      import(
        './pages/view-document/view-document.component'
      ).then(
        m => m.ViewDocumentComponent
      )
  }

];