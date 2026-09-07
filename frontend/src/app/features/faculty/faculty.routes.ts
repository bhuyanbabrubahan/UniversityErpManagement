import { Routes } from '@angular/router';


export const FACULTY_ROUTES: Routes = [

    // ==========================================================
    // FACULTY LIST
    // /dashboard/faculties
    // ==========================================================

    {
        path: '',

        loadComponent: () =>
            import(
                './pages/faculty-list/faculty-list.component'
            ).then(
                m => m.FacultyListComponent
            )
    },


    // ==========================================================
    // ADD FACULTY
    // /dashboard/faculties/add
    // ==========================================================

    {
        path: 'add',

        loadComponent: () =>
            import(
                './pages/add-faculty/add-faculty.component'
            ).then(
                m => m.AddFacultyComponent
            )
    },


    // ==========================================================
    // EDIT FACULTY
    // /dashboard/faculties/edit/:id
    // ==========================================================

    {
        path: 'edit/:id',

        loadComponent: () =>
            import(
                './pages/edit-faculty/edit-faculty.component'
            ).then(
                m => m.EditFacultyComponent
            )
    },


    // ==========================================================
    // VIEW FACULTY
    // /dashboard/faculties/:id
    // ==========================================================

    {
        path: ':id',

        loadComponent: () =>
            import(
                './pages/view-faculty/view-faculty.component'
            ).then(
                m => m.ViewFacultyComponent
            )
    }

];