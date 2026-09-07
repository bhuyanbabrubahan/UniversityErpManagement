import { Component, inject, signal } from "@angular/core";

import { Router, ActivatedRoute } from "@angular/router";

import { finalize } from "rxjs";

import { TimetableService } from "../../services/timetable.service";

import { Timetable } from "../../models/timetable.model";

import { TimetableRequest } from "../../models/timetable-request.model";

import { TimetableFormComponent } from "../../components/timetable-form.component";

import { ToastService } from "../../../../shared/services/toast.service";
import { TIMETABLE_STATUS } from "../../shared/timetable.constants";

@Component({
  standalone: true,

  imports: [TimetableFormComponent],

  templateUrl: "./timetable-add.component.html",

  styleUrl: "./timetable-add.component.scss",
})
export class TimetableAddComponent {
  private readonly service = inject(TimetableService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly toast = inject(ToastService);

  readonly loading = signal(false);

  readonly model = signal<Partial<Timetable>>({
    status: TIMETABLE_STATUS.ACTIVE,
  });

  save(value: Partial<Timetable>): void {
    const request = this.toRequest(value);

    this.loading.set(true);

    this.service
      .create(request)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.toast.success("Timetable created successfully.");

          this.router.navigate(["../"], {
            relativeTo: this.route,
          });
        },

        error: (error) => {
          console.error("Failed to create timetable:", error);

          this.toast.error(
            error?.error?.message || "Unable to create timetable.",
          );
        },
      });
  }

  cancel(): void {
    this.router.navigate(["../"], {
      relativeTo: this.route,
    });
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
