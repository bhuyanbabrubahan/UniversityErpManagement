import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { SubjectRequest, SubjectResponse } from '../../models/subject.model';

export interface SubjectSemesterOption {
  id: number;
  semesterName: string;
}

@Component({
  selector: 'app-subject-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './subject-form.component.html',
  styleUrl: './subject-form.component.scss'
})
export class SubjectFormComponent implements OnChanges {

  private readonly fb = inject(FormBuilder);

  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  subject: SubjectResponse | null = null;

  @Input()
  semesters: SubjectSemesterOption[] = [];

  @Input()
  loading = false;

  @Input()
  loadingSemesters = false;

  @Input()
  submitLabel = 'Save Subject';

  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  formSubmit =
    new EventEmitter<SubjectRequest>();

  @Output()
  cancel =
    new EventEmitter<void>();

  // =====================================================
  // FORM
  // =====================================================

  readonly form = this.fb.nonNullable.group({

    subjectName: this.fb.control(
      '',
      [
        Validators.required,
        Validators.maxLength(150)
      ]
    ),

    credits: this.fb.control<number | null>(
      null,
      [
        Validators.required,
        Validators.min(1),
        Validators.max(10)
      ]
    ),

    theoryMarks: this.fb.control<number | null>(
      null,
      [
        Validators.min(0),
        Validators.max(100)
      ]
    ),

    practicalMarks: this.fb.control<number | null>(
      null,
      [
        Validators.min(0),
        Validators.max(100)
      ]
    ),

    semesterId: this.fb.control<number | null>(
      null,
      [
        Validators.required
      ]
    )

  });

  // =====================================================
  // CHANGE DETECTION
  // =====================================================

  ngOnChanges(changes: SimpleChanges): void {

    if (
      changes['subject'] &&
      this.subject
    ) {

      this.patchForm(this.subject);

    }

  }

  // =====================================================
  // PATCH FORM
  // =====================================================

  private patchForm(
    subject: SubjectResponse
  ): void {

    this.form.patchValue({

      subjectName:
        subject.subjectName ?? '',

      credits:
        subject.credits ?? null,

      theoryMarks:
        subject.theoryMarks ?? null,

      practicalMarks:
        subject.practicalMarks ?? null,

      semesterId:
        subject.semesterId ?? null

    });

  }

  // =====================================================
  // SUBMIT
  // =====================================================

  onSubmit(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;
    }

    const value =
      this.form.getRawValue();

    const request: SubjectRequest = {

      subjectName: value.subjectName?.trim() || "",

      credits:
        value.credits!,

      theoryMarks:
        value.theoryMarks,

      practicalMarks:
        value.practicalMarks,

      semesterId:
        value.semesterId!

    };

    this.formSubmit.emit(request);
  }

  // =====================================================
  // CANCEL
  // =====================================================

  onCancel(): void {

    if (!this.loading) {

      this.cancel.emit();

    }

  }

  // =====================================================
  // FIELD HELPERS
  // =====================================================

  hasError(
    controlName: string,
    error: string
  ): boolean {

    const control =
      this.form.get(controlName);

    return !!(
      control &&
      control.touched &&
      control.hasError(error)
    );
  }

}