import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { MatIconModule } from "@angular/material/icon";
import { ActivatedRoute, Router } from "@angular/router";
import { EMPTY, catchError, finalize } from "rxjs";

import { NotificationFormComponent } from "../../components/notification-form.component";
import { NotificationRequest } from "../../models/notification-request.model";
import { NotificationService } from "../../services/notification.service";

@Component({
  standalone: true,

  imports: [CommonModule, MatIconModule, NotificationFormComponent],

  templateUrl: "./notification-add.component.html",

  styleUrl: "./notification-add.component.scss",
})
export class NotificationAddComponent {
  private readonly service = inject(NotificationService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  readonly busy = signal(false);

  readonly error = signal("");

  save(request: NotificationRequest): void {
    if (this.busy()) {
      return;
    }

    this.busy.set(true);
    this.error.set("");

    this.service
      .create(request)
      .pipe(
        catchError((error) => {
          console.error("Create notification error:", error);

          this.error.set(
            error?.error?.message ??
              error?.error?.error ??
              "Unable to create the notification. Please try again.",
          );

          return EMPTY;
        }),

        finalize(() => {
          this.busy.set(false);
        }),
      )
      .subscribe({
        next: () => {
          this.router.navigate(["../"], {
            relativeTo: this.route,
          });
        },
      });
  }

  cancel(): void {
    if (this.busy()) {
      return;
    }

    this.router.navigate(["../"], {
      relativeTo: this.route,
    });
  }
}
