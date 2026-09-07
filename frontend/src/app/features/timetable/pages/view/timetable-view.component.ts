import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";

import { TimetableService } from "../../services/timetable.service";
import { Timetable } from "../../models/timetable.model";
import { ToastService } from "../../../../shared/services/toast.service";

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./timetable-view.component.html",
  styleUrl: "./timetable-view.component.scss",
})
export class TimetableViewComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly service = inject(TimetableService);
  private readonly toast = inject(ToastService);

  readonly item = signal<Timetable | null>(null);
  readonly loading = signal(true);

  readonly id = this.readId();

  ngOnInit(): void {
    if (this.id === null) {
      this.loading.set(false);
      return;
    }

    this.service.get(this.id).subscribe({
      next: (response) => {
        this.item.set(response);
        this.loading.set(false);
      },

      error: (error) => {
        console.error("Failed to load timetable:", error);

        this.loading.set(false);

        this.toast.error(error?.error?.message || "Unable to load timetable.");

        this.router.navigate(["../../"], {
          relativeTo: this.route,
        });
      },
    });
  }

  back(): void {
    this.router.navigate(["../"], {
      relativeTo: this.route,
    });
  }

  formatDate(value?: string): string {
    if (!value) {
      return "—";
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return value;
    }

    return new Intl.DateTimeFormat("en-IN", {
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }).format(date);
  }

  private readId(): number | null {
    const rawId = this.route.snapshot.paramMap.get("id");

    if (!rawId || !/^\d+$/.test(rawId)) {
      this.toast.error("Invalid timetable ID.");
      return null;
    }

    const id = Number(rawId);

    if (!Number.isSafeInteger(id) || id <= 0) {
      this.toast.error("Invalid timetable ID.");
      return null;
    }

    return id;
  }
}
