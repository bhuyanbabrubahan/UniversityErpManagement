import { Component, OnInit, inject, signal } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

import { CurriculumService } from "../../services/curriculum.service";
import { Curriculum } from "../../models/curriculum.model";
import { CurriculumRequest } from "../../models/curriculum-request.model";
import { CurriculumFormComponent } from "../../components/curriculum-form.component";
import { ToastService } from "../../../../shared/services/toast.service";

@Component({
  selector: "app-curriculum-edit",
  standalone: true,
  imports: [CurriculumFormComponent],
  templateUrl: "./curriculum-edit.component.html",
  styleUrl: "./curriculum-edit.component.scss",
})
export class CurriculumEditComponent implements OnInit {
  private readonly service = inject(CurriculumService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);

  readonly model = signal<Partial<Curriculum>>({});
  readonly loading = signal(false);
  readonly saving = signal(false);

  private id: number | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get("id");

    if (!idParam || !/^\d+$/.test(idParam)) {
      this.toast.error("Invalid curriculum ID.");
      this.goBack();
      return;
    }

    const id = Number(idParam);

    if (!Number.isSafeInteger(id) || id <= 0) {
      this.toast.error("Invalid curriculum ID.");
      this.goBack();
      return;
    }

    this.id = id;

    this.loadCurriculum();
  }

  private loadCurriculum(): void {
    if (this.id === null) {
      return;
    }

    this.loading.set(true);

    this.service.get(this.id).subscribe({
      next: (response) => {
        this.model.set({
          ...response,
          academicYear: response.academicYear?.trim() || "2026-2027",
        });

        this.loading.set(false);
      },

      error: (error) => {
        this.loading.set(false);

        console.error("Failed to load curriculum:", error);

        this.toast.error(
          error?.error?.message || "Unable to load curriculum details.",
        );

        this.goBack();
      },
    });
  }

  save(value: Partial<Curriculum>): void {
    if (this.id === null) {
      this.toast.error("Invalid curriculum ID.");
      return;
    }

    const request: CurriculumRequest = {
      code: value.code?.trim() ?? "",
      academicYear: value.academicYear?.trim() || "2026-2027",
      course: value.course?.trim() ?? "",
      semester: value.semester?.trim() ?? "",
      subject: value.subject?.trim() ?? "",
      credits: value.credits?.toString().trim() ?? "",
      contactHours: value.contactHours?.toString().trim() ?? "",
      status: value.status?.trim() || "ACTIVE",
      description: value.description?.trim() ?? "",
    };

    this.saving.set(true);

    this.service.update(this.id, request).subscribe({
      next: () => {
        this.saving.set(false);

        this.toast.success("Curriculum updated successfully.");

        this.goBack();
      },

      error: (error) => {
        this.saving.set(false);

        console.error("Failed to update curriculum:", error);

        this.toast.error(
          error?.error?.message || "Failed to update curriculum.",
        );
      },
    });
  }

  cancel(): void {
    this.goBack();
  }

  private goBack(): void {
    this.router.navigate(["/dashboard/curriculum"]);
  }
}
