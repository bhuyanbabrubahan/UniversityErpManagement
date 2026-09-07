import { Routes } from "@angular/router";

export const NOTIFICATION_ROUTES: Routes = [
  {
    path: "",
    loadComponent: () =>
      import("./pages/list/notification-list.component").then(
        (m) => m.NotificationListComponent,
      ),
  },
  {
    path: "add",
    loadComponent: () =>
      import("./pages/add/notification-add.component").then(
        (m) => m.NotificationAddComponent,
      ),
  },
  {
    path: "send",
    loadComponent: () =>
      import("./pages/send/notification-send.component").then(
        (m) => m.NotificationSendComponent,
      ),
  },
  {
    path: "edit/:id",
    loadComponent: () =>
      import("./pages/edit/notification-edit.component").then(
        (m) => m.NotificationEditComponent,
      ),
  },
  {
    path: ":id",
    loadComponent: () =>
      import("./pages/view/notification-view.component").then(
        (m) => m.NotificationViewComponent,
      ),
  },
];
