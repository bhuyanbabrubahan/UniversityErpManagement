import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
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
  SemesterRequest,
  SemesterResponse
} from '../../models/semester.model';

import {
  CourseResponse
} from '@app/features/course/models/course.model';

import {
  CourseService
} from '@app/features/course/services/course.service';


@Component({
  selector: 'app-semester-form',
  standalone: true,

  imports: [
    ReactiveFormsModule
  ],

  templateUrl: './semester-form.component.html',
  styleUrl: './semester-form.component.scss'
})
export class SemesterFormComponent
  implements OnInit, OnChanges {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly fb =
    inject(FormBuilder);

  private readonly courseService =
    inject(CourseService);


  // =====================================================
  // INPUTS
  // =====================================================

  @Input()
  semester: SemesterResponse | null = null;

  @Input()
  courses: CourseResponse[] = [];

  @Input()
  loading = false;

  @Input()
  submitLabel = 'Save Semester';


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  formSubmit =
    new EventEmitter<SemesterRequest>();

  @Output()
  cancel =
    new EventEmitter<void>();


  // =====================================================
  // UI STATE
  // =====================================================

  loadingCourses = false;

  coursesError = '';


  // =====================================================
  // SEMESTER NUMBERS
  // =====================================================

  readonly semesterNumbers: number[] = [
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ];


  // =====================================================
  // FORM
  // =====================================================

  readonly form =
    this.fb.nonNullable.group({

      semesterName: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      semesterNumber: [
        0,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      courseId: [
        0,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      semesterStartDate: [
        '',
        Validators.required
      ],

      semesterEndDate: [
        '',
        Validators.required
      ],

      totalWorkingDays: [
        0
      ]

    });


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    /*
     * Agar parent ne courses nahi bheje,
     * to component khud courses load karega.
     */
    if (this.courses.length === 0) {
      this.loadCourses();
    }

  }


  // =====================================================
  // CHANGES
  // =====================================================

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    // ---------------------------------------------------
    // COURSES CHANGED
    // ---------------------------------------------------

    if (changes['courses']) {

      /*
       * Agar parent se courses aaye hain,
       * to unko use karo.
       */
      if (this.courses.length > 0) {

        this.courses =
          this.removeDuplicateCourses(
            this.courses
          );

        this.loadingCourses = false;
        this.coursesError = '';

      }

    }


    // ---------------------------------------------------
    // SEMESTER / EDIT MODE
    // ---------------------------------------------------

    if (
      changes['semester'] &&
      this.semester
    ) {

      this.form.patchValue({

        semesterName:
          this.semester.semesterName ?? '',

        semesterNumber:
          this.semester.semesterNumber ?? 0,

        courseId:
          this.semester.courseId ?? 0,

        semesterStartDate:
          this.formatDate(
            this.semester.semesterStartDate
          ),

        semesterEndDate:
          this.formatDate(
            this.semester.semesterEndDate
          ),

        totalWorkingDays:
          this.semester.totalWorkingDays ?? 0

      });

      this.onDateChange();

    }

  }


  // =====================================================
  // LOAD COURSES
  // =====================================================

  private loadCourses(): void {

    /*
     * Already loading hai to duplicate API call mat karo.
     */
    if (this.loadingCourses) {
      return;
    }

    this.loadingCourses = true;
    this.coursesError = '';

    this.courseService
      .getAllCourses(
        0,
        100,
        'courseName',
        'asc'
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Courses API response:',
            response
          );


          /*
           * IMPORTANT:
           *
           * CoursePageResponse:
           *
           * ApiResponse<
           *   PageResponse<CourseResponse>
           * >
           *
           * Isliye content:
           *
           * response.data.content
           */

          const apiCourses =
            response?.data?.content ?? [];


          /*
           * Duplicate course IDs remove.
           */
          this.courses =
            this.removeDuplicateCourses(
              apiCourses
            );


          console.log(
            'Courses loaded:',
            this.courses
          );


          this.loadingCourses = false;


          /*
           * Edit mode mein courseId already
           * form mein patched ho sakta hai.
           *
           * Isliye courses load hone ke baad
           * form value ko preserve karne ki zarurat nahi.
           */

        },


        error: (error: unknown) => {

          console.error(
            'Failed to load courses:',
            error
          );


          this.loadingCourses = false;

          this.courses = [];


          const httpError =
            error as {
              error?: {
                message?: string;
              };
              message?: string;
            };


          this.coursesError =
            httpError?.error?.message ||
            httpError?.message ||
            'Unable to load courses.';

        }

      });

  }


  // =====================================================
  // REMOVE DUPLICATE COURSES
  // =====================================================

  private removeDuplicateCourses(
    courses: CourseResponse[]
  ): CourseResponse[] {

    const uniqueCourses =
      new Map<number, CourseResponse>();


    for (const course of courses) {

      if (
        course?.id &&
        !uniqueCourses.has(course.id)
      ) {

        uniqueCourses.set(
          course.id,
          course
        );

      }

    }


    return Array.from(
      uniqueCourses.values()
    );

  }


  // =====================================================
  // INVALID FIELD
  // =====================================================

  isInvalid(
    controlName: string
  ): boolean {

    const control =
      this.form.get(controlName);

    return !!(
      control &&
      control.invalid &&
      (
        control.touched ||
        control.dirty
      )
    );

  }


  // =====================================================
  // DATE CHANGE
  // =====================================================

  onDateChange(): void {

    const start =
      this.form.controls
        .semesterStartDate.value;

    const end =
      this.form.controls
        .semesterEndDate.value;


    if (!start || !end) {

      this.form.controls
        .totalWorkingDays
        .setValue(0);

      return;
    }


    const startDate =
      this.parseDate(start);

    const endDate =
      this.parseDate(end);


    if (
      !startDate ||
      !endDate ||
      endDate < startDate
    ) {

      this.form.controls
        .totalWorkingDays
        .setValue(0);

      return;
    }


    let workingDays = 0;

    const current =
      new Date(startDate);


    while (
      current <= endDate
    ) {

      const day =
        current.getDay();


      /*
       * Sunday = 0
       *
       * Saturday working day count hoga.
       *
       * Agar Saturday holiday hai:
       *
       * if (day !== 0 && day !== 6)
       */

      if (day !== 0) {
        workingDays++;
      }


      current.setDate(
        current.getDate() + 1
      );

    }


    this.form.controls
      .totalWorkingDays
      .setValue(workingDays);

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


    const request: SemesterRequest = {

      semesterName:
        value.semesterName.trim(),

      semesterNumber:
        value.semesterNumber,

      courseId:
        value.courseId,

      semesterStartDate:
        value.semesterStartDate,

      semesterEndDate:
        value.semesterEndDate,

      totalWorkingDays:
        value.totalWorkingDays

    };


    this.formSubmit.emit(
      request
    );

  }


  // =====================================================
  // CANCEL
  // =====================================================

  onCancel(): void {

    this.cancel.emit();

  }


  // =====================================================
  // DATE FORMAT
  // =====================================================

  private formatDate(
    value: string | null | undefined
  ): string {

    if (!value) {
      return '';
    }


    /*
     * Example:
     *
     * 2026-08-01T00:00:00
     *
     * becomes:
     *
     * 2026-08-01
     */

    return value.substring(0, 10);

  }


  // =====================================================
  // PARSE DATE
  // =====================================================

  private parseDate(
    value: string
  ): Date | null {

    const parts =
      value.split('-');


    if (parts.length !== 3) {
      return null;
    }


    const year =
      Number(parts[0]);

    const month =
      Number(parts[1]);

    const day =
      Number(parts[2]);


    if (
      !year ||
      !month ||
      !day
    ) {
      return null;
    }


    return new Date(
      year,
      month - 1,
      day
    );

  }

}