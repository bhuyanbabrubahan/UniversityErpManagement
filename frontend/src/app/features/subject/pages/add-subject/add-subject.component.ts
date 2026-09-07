import {
  Component,
  DestroyRef,
  OnInit,
  inject
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  takeUntilDestroyed
} from '@angular/core/rxjs-interop';

import {
  SubjectFormComponent,
  SubjectSemesterOption
} from '../../components/subject-form/subject-form.component';

import {
  SubjectRequest
} from '../../models/subject.model';

import {
  SubjectService
} from '../../services/subject.service';

import {
  SemesterService
} from '@app/features/semester/services/semester.service';


@Component({
  selector: 'app-add-subject',
  standalone: true,

  imports: [
    SubjectFormComponent
  ],

  templateUrl: './add-subject.component.html',
  styleUrl: './add-subject.component.scss'
})
export class AddSubjectComponent
  implements OnInit {


  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly subjectService =
    inject(SubjectService);

  private readonly semesterService =
    inject(SemesterService);

  private readonly router =
    inject(Router);

  private readonly destroyRef =
    inject(DestroyRef);


  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;

  loadingSemesters = false;

  successMessage = '';

  errorMessage = '';


  // =====================================================
  // SEMESTERS
  // =====================================================

  semesters:
    SubjectSemesterOption[] = [];


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

    this.loadingSemesters = true;

    this.errorMessage = '';

    this.semesterService
      .getSemesterOptions()
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({

        next: response => {

          const semesterMap =
            new Map<number, SubjectSemesterOption>();

          for (const semester of response) {

            // Same semester number dobara aaye
            // to ignore kar do.
            if (
              !semesterMap.has(
                semester.semesterNumber
              )
            ) {

              semesterMap.set(
                semester.semesterNumber,
                {
                  id: semester.id,
                  semesterName:
                    semester.name
                }
              );

            }

          }

          this.semesters =
            Array.from(
              semesterMap.values()
            ).sort(
              (a, b) => {

                const aNumber =
                  response.find(
                    semester =>
                      semester.id === a.id
                  )?.semesterNumber ?? 0;

                const bNumber =
                  response.find(
                    semester =>
                      semester.id === b.id
                  )?.semesterNumber ?? 0;

                return aNumber - bNumber;

              }
            );

          this.loadingSemesters = false;

        },

        error: (error: any) => {

          this.loadingSemesters = false;

          this.semesters = [];

          this.errorMessage =
            error?.error?.message ||
            'Unable to load semesters.';

        }

      });

  }


  // =====================================================
  // CREATE
  // =====================================================

  create(
    request: SubjectRequest
  ): void {

    this.loading = true;

    this.errorMessage = '';

    this.subjectService
      .createSubject(request)
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({

        next: (response) => {

          this.loading = false;

          this.successMessage =
            response?.data?.subjectName
              ? `Subject "${response.data.subjectName}" created successfully.`
              : 'Subject created successfully.';

          setTimeout(() => {

            this.router.navigate([
              '/dashboard/subject'
            ]);

          }, 700);

        },

        error: (error: any) => {

          this.loading = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to create subject.';

        }

      });

  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    this.router.navigate([
      '/dashboard/subject'
    ]);

  }


  // =====================================================
  // CLOSE ERROR
  // =====================================================

  closeErrorMessage(): void {

    this.errorMessage = '';

  }

}