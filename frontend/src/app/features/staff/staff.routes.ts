import { Routes } from "@angular/router";
export const STAFF_ROUTES: Routes = [
  {
    path: "",
    loadComponent: () =>
      import("./pages/list/staff-list.component").then(
        (m) => m.StaffListComponent,
      ),
  },
  {
    path: "add",
    loadComponent: () =>
      import("./pages/add/staff-add.component").then(
        (m) => m.StaffAddComponent,
      ),
  },
  {
    path: "edit/:id",
    loadComponent: () =>
      import("./pages/edit/staff-edit.component").then(
        (m) => m.StaffEditComponent,
      ),
  },
  {
    path: ":id",
    loadComponent: () =>
      import("./pages/view/staff-view.component").then(
        (m) => m.StaffViewComponent,
      ),
  },
];
