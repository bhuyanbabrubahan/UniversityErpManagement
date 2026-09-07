import {
  Component,
  DestroyRef,
  OnInit,
  inject
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  takeUntilDestroyed
} from '@angular/core/rxjs-interop';

import {
  SubjectFormComponent
} from '../../components/subject-form/subject-form.component';

import {
  SubjectRequest,
  SubjectResponse
} from '../../models/subject.model';

import {
  SubjectService
} from '../../services/subject.service';

import {
  SemesterResponse
} from '../../../semester/models/semester.model';

import {
  SemesterService
} from '../../../semester/services/semester.service';


@Component({
  selector: 'app-edit-subject',
  standalone: true,

  imports: [
    SubjectFormComponent
  ],

  templateUrl: './edit-subject.component.html',
  styleUrl: './edit-subject.component.scss'
})
export class EditSubjectComponent implements OnInit {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly subjectService =
    inject(SubjectService);

  private readonly semesterService =
    inject(SemesterService);

  private readonly destroyRef =
    inject(DestroyRef);


  // =====================================================
  // SUBJECT
  // =====================================================

  subjectId!: number;

  subject: SubjectResponse | null = null;


  // =====================================================
  // SEMESTERS
  // =====================================================

  semesters: SemesterResponse[] = [];


  // =====================================================
  // UI STATE
  // =====================================================

  loadingData = true;

  loading = false;

  loadingSemesters = false;

  successMessage = '';

  errorMessage = '';


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    const id = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (!id || id <= 0) {

      this.errorMessage =
        'Invalid subject ID.';

      this.loadingData = false;

      return;
    }

    this.subjectId = id;

    this.loadSemesters();

    this.loadSubject();
  }


  // =====================================================
  // LOAD SEMESTERS
  // =====================================================

  private loadSemesters(): void {

    this.loadingSemesters = true;

    this.semesterService
      .getSemesters(
        0,
        100,
        'semesterNumber',
        'asc'
      )
      .pipe(
        takeUntilDestroyed(
          this.destroyRef
        )
      )
      .subscribe({

        next: (
          response
        ) => {

          this.semesters =
            response?.content ?? [];

          this.loadingSemesters = false;
        },

        error: (
          error: unknown
        ) => {

          this.loadingSemesters = false;

          this.semesters = [];

          const httpError =
            error as {
              error?: {
                message?: string;
              };
              message?: string;
            };

          this.errorMessage =
            httpError?.error?.message ||
            httpError?.message ||
            'Unable to load semesters.';
        }

      });
  }


  // =====================================================
  // LOAD SUBJECT
  // =====================================================

  private loadSubject(): void {

    this.loadingData = true;

    this.errorMessage = '';

    this.subjectService
      .getSubjectById(
        this.subjectId
      )
      .pipe(
        takeUntilDestroyed(
          this.destroyRef
        )
      )
      .subscribe({

        next: (
          response
        ) => {

          this.subject =
            response.data ?? null;

          this.loadingData = false;
        },

        error: (
          error: unknown
        ) => {

          this.loadingData = false;

          const httpError =
            error as {
              error?: {
                message?: string;
              };
              message?: string;
            };

          this.errorMessage =
            httpError?.error?.message ||
            httpError?.message ||
            'Unable to load subject.';
        }

      });
  }


  // =====================================================
  // UPDATE
  // =====================================================

  update(
    request: SubjectRequest
  ): void {

    if (!this.subjectId) {
      return;
    }

    this.loading = true;

    this.errorMessage = '';

    this.successMessage = '';

    this.subjectService
      .updateSubject(
        this.subjectId,
        request
      )
      .pipe(
        takeUntilDestroyed(
          this.destroyRef
        )
      )
      .subscribe({

        next: (
          response
        ) => {

          this.loading = false;

          this.subject =
            response.data ?? null;

          this.successMessage =
            'Subject updated successfully.';

          setTimeout(() => {

            this.router.navigate([
              '/dashboard/subject'
            ]);

          }, 700);
        },

        error: (
          error: unknown
        ) => {

          this.loading = false;

          const httpError =
            error as {
              error?: {
                message?: string;
              };
              message?: string;
            };

          this.errorMessage =
            httpError?.error?.message ||
            httpError?.message ||
            'Unable to update subject.';
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
  // CLOSE SUCCESS
  // =====================================================

  closeSuccessMessage(): void {

    this.successMessage = '';
  }


  // =====================================================
  // CLOSE ERROR
  // =====================================================

  closeErrorMessage(): void {

    this.errorMessage = '';
  }

}