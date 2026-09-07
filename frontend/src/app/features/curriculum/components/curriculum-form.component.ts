import { Component, EventEmitter, Input, Output, inject } from "@angular/core";

import { FormsModule, NgForm } from "@angular/forms";

import { Curriculum } from "../models/curriculum.model";
import { ToastService } from "@app/shared/services/toast.service";

// Apne project ke actual ToastService path ke according path change karo

@Component({
  selector: "app-curriculum-form",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./curriculum-form.component.html",
  styleUrl: "./curriculum-form.component.scss",
})
export class CurriculumFormComponent {
  private readonly toast = inject(ToastService);

  @Input()
  model: Partial<Curriculum> = {};

  @Input()
  submitLabel = "Save";

  @Input()
  loading = false;

  @Output()
  save = new EventEmitter<Partial<Curriculum>>();

  @Output()
  cancel = new EventEmitter<void>();

  submitted = false;

  // =====================================================
  // SUBMIT
  // =====================================================

  onSubmit(form: NgForm): void {
    this.submitted = true;

    if (this.loading) {
      return;
    }

    if (form.invalid) {
      form.control.markAllAsTouched();

      this.toast.error("Please fill all required fields.");

      return;
    }

    const value: Partial<Curriculum> = {
      code: this.clean(this.model.code),
      academicYear: this.clean(this.model.academicYear),
      course: this.clean(this.model.course),
      semester: this.clean(this.model.semester),
      subject: this.clean(this.model.subject),
      credits: this.clean(this.model.credits),
      contactHours: this.clean(this.model.contactHours),
      status: this.clean(this.model.status),
      description: this.clean(this.model.description),
    };

    this.save.emit(value);
  }

  // =====================================================
  // CANCEL
  // =====================================================

  onCancel(): void {
    if (this.loading) {
      return;
    }

    this.cancel.emit();
  }

  // =====================================================
  // CLEAN VALUE
  // =====================================================

  private clean(value: unknown): string {
    if (value === null || value === undefined) {
      return "";
    }

    return String(value).trim();
  }
}
