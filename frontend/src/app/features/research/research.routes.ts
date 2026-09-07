import { Routes } from "@angular/router";

export const RESEARCH_ROUTES: Routes = [
  {
    path: "",
    loadComponent: () =>
      import("./pages/list/research-list.component").then(
        (m) => m.ResearchListComponent,
      ),
  },
  {
    path: "add",
    loadComponent: () =>
      import("./pages/add/research-add.component").then(
        (m) => m.ResearchAddComponent,
      ),
  },
  {
    path: "edit/:id",
    loadComponent: () =>
      import("./pages/edit/research-edit.component").then(
        (m) => m.ResearchEditComponent,
      ),
  },
  {
    path: ":id",
    loadComponent: () =>
      import("./pages/view/research-view.component").then(
        (m) => m.ResearchViewComponent,
      ),
  },
];
