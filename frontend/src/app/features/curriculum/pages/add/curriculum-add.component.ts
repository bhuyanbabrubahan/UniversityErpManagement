import { Component, inject, signal } from "@angular/core";

import { ActivatedRoute, Router } from "@angular/router";

import { CurriculumService } from "../../services/curriculum.service";

import { Curriculum } from "../../models/curriculum.model";

import { CurriculumRequest } from "../../models/curriculum-request.model";

import { CurriculumFormComponent } from "../../components/curriculum-form.component";

import { ToastService } from "../../../../shared/services/toast.service";

@Component({
  selector: "app-curriculum-add",
  standalone: true,
  imports: [CurriculumFormComponent],
  templateUrl: "./curriculum-add.component.html",
  styleUrl: "./curriculum-add.component.scss",
})
export class CurriculumAddComponent {
  private readonly service = inject(CurriculumService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);

  readonly saving = signal(false);

  readonly model = signal<Partial<Curriculum>>({
    academicYear: "2026-2027",
    status: "ACTIVE",
  });

  save(value: Partial<Curriculum>): void {
    const request: CurriculumRequest = {
      code: value.code?.trim() ?? "",
      academicYear: value.academicYear?.trim() || "2026-2027",
      course: value.course?.trim() ?? "",
      semester: value.semester?.trim() ?? "",
      subject: value.subject?.trim() ?? "",
      credits: value.credits?.toString() ?? "",
      contactHours: value.contactHours?.toString() ?? "",
      status: value.status || "ACTIVE",
      description: value.description?.trim() ?? "",
    };

    this.saving.set(true);

    this.service.create(request).subscribe({
      next: () => {
        this.saving.set(false);

        this.toast.success("Curriculum created successfully.");

        this.goBack();
      },

      error: (error) => {
        this.saving.set(false);

        console.error("Failed to create curriculum:", error);

        this.toast.error(
          error?.error?.message || "Failed to create curriculum.",
        );
      },
    });
  }

  cancel(): void {
    this.goBack();
  }

  private goBack(): void {
    this.router.navigate(["../"], {
      relativeTo: this.route,
    });
  }
}
