import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { AdmissionRequest, AdmissionResponse } from '../../models/admission.model';
import {  ADMISSION_ACADEMIC_YEAR_PATTERN } from '../../shared/admission.constants';
import { normalizeNullableText, toDateInputValue } from '../../shared/admission.utils';

@Component({
  selector: 'app-admission-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admission-form.component.html',
  styleUrl: './admission-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdmissionFormComponent implements OnChanges {
  @Input() admission: AdmissionResponse | null = null;
  @Input() submitting = false;
  @Input() mode: 'create' | 'edit' = 'create';

  @Output() submitted = new EventEmitter<AdmissionRequest>();
  @Output() cancelled = new EventEmitter<void>();

  private readonly fb = inject(FormBuilder);

  readonly form = this.fb.nonNullable.group({
    studentId: [0, [Validators.required, Validators.min(1)]],
    departmentId: [0, [Validators.required, Validators.min(1)]],
    courseId: [0, [Validators.required, Validators.min(1)]],
    academicYear: [
      '',
      [
        Validators.required,
        Validators.pattern(ADMISSION_ACADEMIC_YEAR_PATTERN)
      ]
    ],
    semesterId: [
      0,
      Validators.required
    ],

    admissionDate: ['', Validators.required],
    remarks: ['', Validators.maxLength(500)]
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['admission'] && this.admission) {
      this.form.patchValue({
        studentId: this.admission.studentId,
        departmentId: this.admission.departmentId,
        courseId: this.admission.courseId,
        academicYear: this.admission.academicYear,
        semesterId: this.admission.semesterId,
        admissionDate: toDateInputValue(this.admission.admissionDate),
        remarks: this.admission.remarks ?? ''
      });
    }
  }

  submit(): void {
    if (this.form.invalid || this.submitting) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    const request: AdmissionRequest = {
      studentId: raw.studentId,
      departmentId: raw.departmentId,
      courseId: raw.courseId,
      academicYear: raw.academicYear.trim(),
      semesterId: raw.semesterId,
      admissionDate: raw.admissionDate,
      remarks: normalizeNullableText(raw.remarks)
    };

    this.submitted.emit(request);
  }

  cancel(): void {
    if (!this.submitting) {
      this.cancelled.emit();
    }
  }

  controlInvalid(name: keyof typeof this.form.controls): boolean {
    const control = this.form.controls[name];
    return control.invalid && control.touched;
  }
}
