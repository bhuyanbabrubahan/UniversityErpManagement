import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject,
} from "@angular/core";

import { CommonModule } from "@angular/common";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";

import { AdmissionStatus } from "../../../../shared/enums/admission-staus.enum";
import { ToastService } from "../../../../shared/services/toast.service";
import { AdmissionSearchRequest } from "../../models/admission.model";
import { normalizeNullableText } from "../../shared/admission.utils";

@Component({
  selector: "app-admission-search-filter",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: "./admission-search-filter.component.html",
  styleUrl: "./admission-search-filter.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdmissionSearchFilterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly toast = inject(ToastService);

  @Output() searched = new EventEmitter<AdmissionSearchRequest>();
  @Output() cleared = new EventEmitter<void>();

  readonly statuses = Object.values(AdmissionStatus);

  readonly form = this.fb.nonNullable.group({
    admissionNumber: [""],
    studentId: [null as number | null],
    studentName: [""],
    departmentId: [null as number | null],
    courseId: [null as number | null],
    academicYear: [""],
    semesterId: [null as number | null],
    admissionStatus: [null as AdmissionStatus | null],
  });

  search(): void {
    const raw = this.form.getRawValue();

    if (
      raw.academicYear.trim() &&
      !/^\d{4}-\d{4}$/.test(raw.academicYear.trim())
    ) {
      this.toast.error("Academic year must be like 2026-2027.");
      return;
    }

    const request: AdmissionSearchRequest = {
      admissionNumber: normalizeNullableText(raw.admissionNumber),
      studentId: raw.studentId || null,
      studentName: normalizeNullableText(raw.studentName),
      departmentId: raw.departmentId || null,
      courseId: raw.courseId || null,
      academicYear: normalizeNullableText(raw.academicYear),
      semesterId: raw.semesterId || null,
      admissionStatus: raw.admissionStatus || null,
    };

    this.searched.emit(request);
  }

  clear(): void {
    this.form.reset({
      admissionNumber: "",
      studentId: null,
      studentName: "",
      departmentId: null,
      courseId: null,
      academicYear: "",
      semesterId: null,
      admissionStatus: null,
    });

    this.cleared.emit();
  }
}
