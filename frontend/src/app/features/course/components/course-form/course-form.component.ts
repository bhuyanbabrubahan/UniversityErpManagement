import {
  CommonModule
} from '@angular/common';

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

import {
  CourseRequest,
  CourseResponse
} from '../../models/course.model';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';


export interface DepartmentOption {
  id: number;
  departmentName: string;
}


@Component({
  selector: 'app-course-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent
  implements OnChanges {

  private readonly fb = inject(FormBuilder);


  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  course: CourseResponse | null = null;

  @Input()
  departments: DepartmentOption[] = [];

  @Input()
  loading = false;

  @Input()
  submitLabel = 'Save Course';


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  formSubmit =
    new EventEmitter<CourseRequest>();

  @Output()
  cancel =
    new EventEmitter<void>();


  // =====================================================
  // FORM
  // =====================================================

  courseForm =
    this.fb.nonNullable.group({

      courseCode: [
        '',
        [
          Validators.required,
          Validators.maxLength(20)
        ]
      ],

      courseName: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      durationInMonths: [
        1,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      departmentId: [
        0,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      status: [
        RecordStatus.ACTIVE as RecordStatus,
        Validators.required
      ],

      description: [
        '',
        Validators.maxLength(500)
      ]

    });


  // =====================================================
  // ON CHANGES
  // =====================================================

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    if (
      changes['course'] &&
      this.course
    ) {

      this.courseForm.patchValue({

        courseCode:
          this.course.courseCode ?? '',

        courseName:
          this.course.courseName ?? '',

        durationInMonths:
          this.course.durationInMonths ?? 1,

        departmentId:
          this.course.departmentId ?? 0,

        status:
          this.course.status ??
          RecordStatus.ACTIVE,

        description:
          this.course.description ?? ''

      });
    }
  }


  // =====================================================
  // FORM CONTROLS
  // =====================================================

  get f() {
    return this.courseForm.controls;
  }


  // =====================================================
  // VALIDATION
  // =====================================================

  isInvalid(
    controlName:
      keyof typeof this.courseForm.controls
  ): boolean {

    const control =
      this.courseForm.controls[controlName];

    return control.invalid &&
      (
        control.touched ||
        control.dirty
      );
  }


  // =====================================================
  // SUBMIT
  // =====================================================

  submit(): void {

    if (this.loading) {
      return;
    }

    if (this.courseForm.invalid) {

      this.courseForm.markAllAsTouched();

      return;
    }

    const value =
      this.courseForm.getRawValue();


    const request: CourseRequest = {

      courseCode:
        value.courseCode
          .trim()
          .toUpperCase(),

      courseName:
        value.courseName
          .trim(),

      durationInMonths:
        Number(value.durationInMonths),

      departmentId:
        Number(value.departmentId),

      description:
        value.description
          .trim() || undefined,

      status:
        value.status

    };


    this.formSubmit.emit(request);
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

}