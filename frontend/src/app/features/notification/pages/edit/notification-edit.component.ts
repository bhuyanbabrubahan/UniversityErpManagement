import { CommonModule } from "@angular/common";

import { Component, OnInit, inject, signal } from "@angular/core";

import { MatIconModule } from "@angular/material/icon";

import { ActivatedRoute, Router } from "@angular/router";

import { EMPTY, catchError, finalize } from "rxjs";

import { NotificationFormComponent } from "../../components/notification-form.component";

import { NotificationRequest } from "../../models/notification-request.model";

import { Notification } from "../../models/notification.model";

import { NotificationService } from "../../services/notification.service";

@Component({
  standalone: true,

  imports: [CommonModule, MatIconModule, NotificationFormComponent],

  templateUrl: "./notification-edit.component.html",

  styleUrl: "./notification-edit.component.scss",
})
export class NotificationEditComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly service = inject(NotificationService);

  readonly model = signal<Partial<Notification>>({});

  readonly loading = signal(true);

  readonly busy = signal(false);

  readonly error = signal("");

  readonly id = Number(this.route.snapshot.paramMap.get("id"));

  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {
    if (!Number.isInteger(this.id) || this.id <= 0) {
      this.loading.set(false);

      this.error.set("Invalid notification ID.");

      return;
    }

    this.loadNotification();
  }

  // =========================================================
  // LOAD
  // =========================================================

  private loadNotification(): void {
    this.loading.set(true);

    this.error.set("");

    this.service
      .get(this.id)
      .pipe(
        catchError((error) => {
          console.error("Load notification error:", error);

          this.error.set(
            error?.error?.message ??
              error?.error?.error ??
              "Notification could not be loaded.",
          );

          return EMPTY;
        }),

        finalize(() => {
          this.loading.set(false);
        }),
      )
      .subscribe({
        next: (notification: Notification) => {
          this.model.set(notification);
        },
      });
  }

  // =========================================================
  // SAVE
  // =========================================================

  save(request: NotificationRequest): void {
    if (this.busy()) {
      return;
    }

    this.busy.set(true);

    this.error.set("");

    this.service
      .update(this.id, request)
      .pipe(
        catchError((error) => {
          console.error("Update notification error:", error);

          this.error.set(
            error?.error?.message ??
              error?.error?.error ??
              "Unable to update the notification. Please try again.",
          );

          return EMPTY;
        }),

        finalize(() => {
          this.busy.set(false);
        }),
      )
      .subscribe({
        next: () => {
          this.router.navigate(["../../", this.id], {
            relativeTo: this.route,
          });
        },
      });
  }

  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {
    if (this.busy()) {
      return;
    }

    this.router.navigate(["../../", this.id], {
      relativeTo: this.route,
    });
  }
}
