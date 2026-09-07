import { Component, inject, signal } from "@angular/core";

import { ActivatedRoute, Router } from "@angular/router";

import { finalize } from "rxjs";

import { TimetableService } from "../../services/timetable.service";

import { Timetable } from "../../models/timetable.model";

import { TimetableRequest } from "../../models/timetable-request.model";

import { TimetableFormComponent } from "../../components/timetable-form.component";

import { ToastService } from "../../../../shared/services/toast.service";

@Component({
  standalone: true,

  imports: [TimetableFormComponent],

  templateUrl: "./timetable-edit.component.html",

  styleUrl: "./timetable-edit.component.scss",
})
export class TimetableEditComponent {
  private readonly service = inject(TimetableService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly toast = inject(ToastService);

  readonly model = signal<Partial<Timetable>>({});

  readonly loading = signal(false);

  readonly pageLoading = signal(true);

  private readonly id = this.readId();

  ngOnInit(): void {
    if (this.id === null) {
      return;
    }

    this.load();
  }

  private load(): void {
    this.pageLoading.set(true);

    this.service
      .get(this.id!)
      .pipe(finalize(() => this.pageLoading.set(false)))
      .subscribe({
        next: (response) => {
          this.model.set(response);
        },

        error: (error) => {
          console.error("Failed to load timetable:", error);

          this.toast.error(
            error?.error?.message || "Unable to load timetable.",
          );

          this.router.navigate(["../../"], {
            relativeTo: this.route,
          });
        },
      });
  }

  save(value: Partial<Timetable>): void {
    if (this.id === null) {
      return;
    }

    const request = this.toRequest(value);

    this.loading.set(true);

    this.service
      .update(this.id, request)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.toast.success("Timetable updated successfully.");

          this.router.navigate(["../../"], {
            relativeTo: this.route,
          });
        },

        error: (error) => {
          console.error("Failed to update timetable:", error);

          this.toast.error(
            error?.error?.message || "Unable to update timetable.",
          );
        },
      });
  }

  cancel(): void {
    this.router.navigate(["../../"], {
      relativeTo: this.route,
    });
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

  private toRequest(value: Partial<Timetable>): TimetableRequest {
    return {
      code: value.code?.trim() ?? "",
      academicYear: value.academicYear?.trim() ?? "",
      semester: value.semester?.trim() ?? "",
      course: value.course?.trim() ?? "",
      section: value.section?.trim() ?? "",
      subject: value.subject?.trim() ?? "",
      faculty: value.faculty?.trim() ?? "",
      room: value.room?.trim() ?? "",
      dayOfWeek: value.dayOfWeek ?? "",
      startTime: value.startTime ?? "",
      endTime: value.endTime ?? "",
      period: value.period?.trim() ?? "",
      status: value.status ?? "",
      description: value.description?.trim() ?? "",
    };
  }
}
