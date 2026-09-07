import { CommonModule } from "@angular/common";

import { Component, OnInit, inject, signal } from "@angular/core";

import { ActivatedRoute, RouterLink } from "@angular/router";

import { EMPTY, catchError, finalize } from "rxjs";

import { NotificationService } from "../../services/notification.service";

import { Notification } from "../../models/notification.model";

import { formatNotificationDate } from "../../shared/notification.utils";

@Component({
  standalone: true,

  imports: [CommonModule, RouterLink],

  templateUrl: "./notification-view.component.html",

  styleUrl: "./notification-view.component.scss",
})
export class NotificationViewComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);

  private readonly service = inject(NotificationService);

  readonly item = signal<Notification | null>(null);

  readonly loading = signal(true);

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
          this.item.set(notification);
        },
      });
  }

  // =========================================================
  // DATE
  // =========================================================

  formatDate(value?: string | null): string {
    return formatNotificationDate(value);
  }
}
