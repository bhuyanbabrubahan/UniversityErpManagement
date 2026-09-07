import {
  Component,
  EventEmitter,
  OnInit,
  Output,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule
} from '@angular/forms';

import {
  SubjectSearchRequest
} from '../../models/subject.model';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';

import {
  RECORD_STATUSES
} from '@app/features/payment/shared/payment.constants';

import {
  SemesterService
} from '@app/features/semester/services/semester.service';


// =========================================================
// SEMESTER SEARCH OPTION
// =========================================================
//
// IMPORTANT:
//
// Database mein Semester 1 multiple records ho sakta hai
// because different courses ke liye alag semester records hain.
//
// Filter mein hume unique semester number chahiye:
//
// Semester 1
// Semester 2
// Semester 3
// ...
// Semester 8
//
// Isliye yahan ID nahi, semesterNumber use hoga.
// =========================================================

export interface SubjectSearchSemesterOption {

  semesterNumber: number;

  name: string;

}


@Component({

  selector:
    'app-subject-search-filter',

  standalone: true,

  imports: [
    ReactiveFormsModule
  ],

  templateUrl:
    './subject-search-filter.component.html',

  styleUrl:
    './subject-search-filter.component.scss'

})
export class SubjectSearchFilterComponent
  implements OnInit {


  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly fb =
    inject(FormBuilder);

  private readonly semesterService =
    inject(SemesterService);


  // =====================================================
  // OUTPUTS
  // =====================================================

  @Output()
  search =
    new EventEmitter<SubjectSearchRequest>();

  @Output()
  reset =
    new EventEmitter<void>();


  // =====================================================
  // SEMESTER OPTIONS
  // =====================================================

  semesters:
    SubjectSearchSemesterOption[] = [];


  // =====================================================
  // SEMESTER LOADING
  // =====================================================

  semestersLoading =
    false;


  // =====================================================
  // OPTIONS
  // =====================================================

  readonly recordStatuses =
    RECORD_STATUSES;


  // =====================================================
  // FORM
  // =====================================================

  readonly form =
    this.fb.nonNullable.group({

      subjectName:
        this.fb.control(''),

      subjectCode:
        this.fb.control(''),

      /**
       * IMPORTANT:
       *
       * Search ke liye semesterId nahi.
       *
       * Unique semester number use hoga.
       *
       * Example:
       * 1 = Semester 1
       * 2 = Semester 2
       * ...
       * 8 = Semester 8
       */
      semesterNumber:
        this.fb.control<number | null>(null),

      credits:
        this.fb.control<number | null>(null),

      status:
        this.fb.control<RecordStatus | null>(null)

    });


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.loadSemesters();

  }


  // =====================================================
  // LOAD SEMESTERS
  // =====================================================

  private loadSemesters(): void {

    this.semestersLoading = true;


    this.semesterService
      .getSemesters(
        0,
        100,
        'semesterNumber',
        'asc'
      )
      .subscribe({

        next: response => {

          const semesterMap =
            new Map<number, SubjectSearchSemesterOption>();


          // =================================================
          // UNIQUE SEMESTER NUMBER
          // =================================================
          //
          // Example backend data:
          //
          // Semester 1 - Course A
          // Semester 2 - Course A
          // Semester 1 - Course B
          // Semester 2 - Course B
          //
          // Map ke through:
          //
          // Semester 1
          // Semester 2
          //
          // sirf ek baar rahega.
          // =================================================

          for (const semester of response.content) {

            if (
              semester.semesterNumber === null ||
              semester.semesterNumber === undefined
            ) {
              continue;
            }


            if (
              !semesterMap.has(
                semester.semesterNumber
              )
            ) {

              semesterMap.set(
                semester.semesterNumber,
                {
                  semesterNumber:
                    semester.semesterNumber,

                  name:
                    semester.semesterName?.trim() ||
                    `Semester ${semester.semesterNumber}`
                }
              );

            }

          }


          this.semesters =
            Array.from(
              semesterMap.values()
            ).sort(
              (a, b) =>
                a.semesterNumber -
                b.semesterNumber
            );


          this.semestersLoading = false;

        },


        error: error => {

          console.error(
            'Failed to load semesters for subject filter:',
            error
          );

          this.semesters = [];

          this.semestersLoading = false;

        }

      });

  }


  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(): void {

    const value =
      this.form.getRawValue();


    const request:
      SubjectSearchRequest = {

      subjectName:
        value.subjectName?.trim() ||
        null,

      subjectCode:
        value.subjectCode?.trim() ||
        null,

      /**
       * IMPORTANT:
       *
       * semesterId ke instead
       * semesterNumber send hoga.
       */
      semesterNumber:
        value.semesterNumber,

      credits:
        value.credits,

      status:
        value.status

    };


    this.search.emit(
      request
    );

  }


  // =====================================================
  // RESET
  // =====================================================

  onReset(): void {

    this.form.reset({

      subjectName:
        '',

      subjectCode:
        '',

      semesterNumber:
        null,

      credits:
        null,

      status:
        null

    });


    this.reset.emit();

  }

}