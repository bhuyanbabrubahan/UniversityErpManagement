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
  SubjectPageResponse,
  SubjectResponse,
  SubjectSearchRequest
} from '../../models/subject.model';

import {
  SubjectService
} from '../../services/subject.service';

import {
  SubjectSearchFilterComponent
} from '../../components/subject-search-filter/subject-search-filter.component';

import {
  SubjectTableComponent
} from '../../components/subject-table/subject-table.component';


@Component({
  selector: 'app-subject-list',
  standalone: true,

  imports: [
    SubjectSearchFilterComponent,
    SubjectTableComponent
  ],

  templateUrl: './subject-list.component.html',
  styleUrl: './subject-list.component.scss'
})
export class SubjectListComponent implements OnInit {

  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly subjectService =
    inject(SubjectService);

  private readonly router =
    inject(Router);

  private readonly destroyRef =
    inject(DestroyRef);


  // =====================================================
  // SUBJECT DATA
  // =====================================================

  subjects: SubjectResponse[] = [];


  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;

  errorMessage = '';

  successMessage = '';


  // =====================================================
  // PAGINATION
  // =====================================================

  page = 0;

  size = 10;

  totalElements = 0;

  totalPages = 0;

  last = true;


  // =====================================================
  // SORT
  // =====================================================

  sortBy = 'id';

  direction: 'asc' | 'desc' = 'desc';


  // =====================================================
  // SEARCH
  // =====================================================

  currentSearch: SubjectSearchRequest = {};


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.loadSubjects();

  }


  // =====================================================
  // LOAD ALL SUBJECTS
  // =====================================================

  loadSubjects(): void {

    this.loading = true;

    this.errorMessage = '';

    this.subjectService
      .getAllSubjects(
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .pipe(
        takeUntilDestroyed(
          this.destroyRef
        )
      )
      .subscribe({

        next: (
          response: SubjectPageResponse
        ) => {

          this.applyPageResponse(
            response
          );

          this.loading = false;
        },

        error: (error: unknown) => {

          this.loading = false;

          this.errorMessage =
            this.getErrorMessage(
              error,
              'Unable to load subjects.'
            );

        }

      });

  }


  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(
    request: SubjectSearchRequest
  ): void {

    this.currentSearch = {
      ...request
    };

    this.page = 0;

    this.searchSubjects();

  }


  searchSubjects(): void {

    this.loading = true;

    this.errorMessage = '';

    const request =
      this.subjectService
        .buildSearchRequest(
          this.currentSearch
        );


    this.subjectService
      .searchSubjects(
        request,
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .pipe(
        takeUntilDestroyed(
          this.destroyRef
        )
      )
      .subscribe({

        next: (
          response: SubjectPageResponse
        ) => {

          this.applyPageResponse(
            response
          );

          this.loading = false;

        },

        error: (error: unknown) => {

          this.loading = false;

          this.errorMessage =
            this.getErrorMessage(
              error,
              'Unable to search subjects.'
            );

        }

      });

  }


  // =====================================================
  // RESET SEARCH
  // =====================================================

  onReset(): void {

    this.currentSearch = {};

    this.page = 0;

    this.loadSubjects();

  }


  // =====================================================
  // PAGE CHANGE
  // =====================================================

  onPageChange(
    page: number
  ): void {

    if (page < 0) {
      return;
    }

    if (
      this.totalPages > 0 &&
      page >= this.totalPages
    ) {
      return;
    }

    this.page = page;

    this.loadCurrentData();

  }


  // =====================================================
  // PAGE SIZE CHANGE
  // =====================================================

  onPageSizeChange(
    size: number
  ): void {

    if (size <= 0) {
      return;
    }

    this.size = size;

    this.page = 0;

    this.loadCurrentData();

  }


  // =====================================================
  // SORT CHANGE
  // =====================================================

  onSortChange(
    event: {
      sortBy: string;
      direction: 'asc' | 'desc';
    }
  ): void {

    this.sortBy =
      event.sortBy;

    this.direction =
      event.direction;

    this.page = 0;

    this.loadCurrentData();

  }


  // =====================================================
  // LOAD CURRENT DATA
  // =====================================================

  private loadCurrentData(): void {

    const hasSearch =
      Object.keys(
        this.currentSearch
      ).length > 0;


    if (hasSearch) {

      this.searchSubjects();

    } else {

      this.loadSubjects();

    }

  }


  // =====================================================
  // ADD
  // =====================================================

  onAdd(): void {

    this.router.navigateByUrl(
      '/dashboard/subject/add'
    );

  }


  // =====================================================
  // VIEW
  // =====================================================

  onView(
    subject: SubjectResponse
  ): void {

    if (!subject?.id) {
      return;
    }

    this.router.navigateByUrl(
      `/dashboard/subject/view/${subject.id}`
    );

  }


  // =====================================================
  // EDIT
  // =====================================================

  onEdit(
    subject: SubjectResponse
  ): void {

    if (!subject?.id) {
      return;
    }

    this.router.navigateByUrl(
      `/dashboard/subject/edit/${subject.id}`
    );

  }


  // =====================================================
  // DELETE
  // =====================================================

  onDelete(
    subject: SubjectResponse
  ): void {

    if (!subject?.id) {
      return;
    }

    this.router.navigateByUrl(
      `/dashboard/subject/delete/${subject.id}`
    );

  }


  // =====================================================
  // APPLY PAGE RESPONSE
  // =====================================================

  private applyPageResponse(
    response: SubjectPageResponse
  ): void {

    const pageResponse =
      response?.data;


    if (!pageResponse) {

      this.subjects = [];

      this.page = 0;

      this.size = 10;

      this.totalElements = 0;

      this.totalPages = 0;

      this.last = true;

      return;
    }


    this.subjects =
      pageResponse.content ?? [];


    this.page =
      pageResponse.pageNumber ?? 0;


    this.size =
      pageResponse.pageSize ?? 10;


    this.totalElements =
      pageResponse.totalElements ?? 0;


    this.totalPages =
      pageResponse.totalPages ?? 0;


    this.last =
      pageResponse.last ?? true;

  }


  // =====================================================
  // ERROR MESSAGE
  // =====================================================

  private getErrorMessage(
    error: unknown,
    fallback: string
  ): string {

    if (
      typeof error === 'object' &&
      error !== null
    ) {

      const httpError =
        error as {
          error?: {
            message?: string;
          };
          message?: string;
        };


      return (
        httpError.error?.message ||
        httpError.message ||
        fallback
      );

    }

    return fallback;

  }


  // =====================================================
  // CLOSE ERROR
  // =====================================================

  closeErrorMessage(): void {

    this.errorMessage = '';

  }


  // =====================================================
  // CLOSE SUCCESS
  // =====================================================

  closeSuccessMessage(): void {

    this.successMessage = '';

  }

}