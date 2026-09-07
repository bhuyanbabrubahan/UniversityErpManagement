import { CommonModule } from "@angular/common";
import { Component, OnInit, inject, signal } from "@angular/core";
import { RouterLink } from "@angular/router";
import { EMPTY, catchError, finalize } from "rxjs";

import { Notification } from "../../models/notification.model";
import {
  NotificationPage,
  NotificationSearch,
} from "../../models/notification-search.model";
import { NotificationSearchFilterComponent } from "../../components/notification-search-filter.component";
import { NotificationTableComponent } from "../../components/notification-table.component";
import { NotificationService } from "../../services/notification.service";
import { NOTIFICATION_PAGE_SIZES } from "../../shared/notification.constants";

@Component({
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    NotificationSearchFilterComponent,
    NotificationTableComponent,
  ],
  templateUrl: "./notification-list.component.html",
  styleUrl: "./notification-list.component.scss",
})
export class NotificationListComponent implements OnInit {
  private readonly service = inject(NotificationService);

  readonly rows = signal<Notification[]>([]);
  readonly loading = signal(false);
  readonly error = signal("");

  readonly page = signal(0);
  readonly size = signal(20);

  readonly totalElements = signal(0);
  readonly totalPages = signal(0);

  readonly search = signal<NotificationSearch>({});

  readonly pageSizes = NOTIFICATION_PAGE_SIZES;
  readonly Math = Math;

  ngOnInit(): void {
    this.load();
  }

  load(search?: NotificationSearch): void {
    if (search !== undefined) {
      this.search.set(search);
      this.page.set(0);
    }

    this.loading.set(true);
    this.error.set("");

    const currentSearch = this.search();

    const request =
      Object.keys(currentSearch).length > 0
        ? this.service.search(currentSearch, this.page(), this.size())
        : this.service.list(this.page(), this.size());

    request
      .pipe(
        catchError((error) => {
          console.error("Notification list API error:", error);

          const message =
            error?.error?.message ??
            error?.error?.error ??
            error?.message ??
            "Unable to connect to notification server.";

          this.error.set(message);

          this.rows.set([]);
          this.totalElements.set(0);
          this.totalPages.set(0);

          return EMPTY;
        }),

        finalize(() => {
          this.loading.set(false);
        }),
      )
      .subscribe({
        next: (response: NotificationPage<Notification>) => {
          console.log("Notification list response:", response);

          this.rows.set(response?.content ?? []);
          this.totalElements.set(response?.totalElements ?? 0);
          this.totalPages.set(response?.totalPages ?? 0);
        },
      });
  }

  reset(): void {
    this.page.set(0);
    this.search.set({});
    this.load();
  }

  goToPage(next: number): void {
    if (this.loading() || next < 0 || next >= this.totalPages()) {
      return;
    }

    this.page.set(next);
    this.load();
  }

  changeSize(value: string): void {
    const nextSize = Number(value);

    if (!Number.isFinite(nextSize) || nextSize <= 0) {
      return;
    }

    this.size.set(nextSize);
    this.page.set(0);
    this.load();
  }

  remove(id: number): void {
    if (this.loading() || id === undefined || id === null) {
      return;
    }

    const confirmed = window.confirm("Delete this notification?");

    if (!confirmed) {
      return;
    }

    this.loading.set(true);
    this.error.set("");

    this.service
      .delete(id)
      .pipe(
        catchError((error) => {
          console.error("Delete notification error:", error);

          this.error.set(
            error?.error?.message ??
              error?.error?.error ??
              error?.message ??
              "Unable to delete notification.",
          );

          return EMPTY;
        }),

        finalize(() => {
          this.loading.set(false);
        }),
      )
      .subscribe({
        next: () => {
          if (this.rows().length === 1 && this.page() > 0) {
            this.page.set(this.page() - 1);
          }

          this.load();
        },
      });
  }
}
