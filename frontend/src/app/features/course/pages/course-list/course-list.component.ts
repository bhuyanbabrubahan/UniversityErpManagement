import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  Router
} from '@angular/router';

import {
  CourseResponse,
  CourseSearchRequest,
  PageResponse
} from '../../models/course.model';

import {
  DepartmentOption
} from '../../components/course-form/course-form.component';

import {
  CourseService
} from '../../services/course.service';



import {
  createEmptyCourseSearch,
  normalizeCourseSearch,
  getDurationLabel
} from '../../shared/course.utils';

import {
  DeleteCourseDialogComponent
} from '../../components/delete-course-dialog/delete-course-dialog.component';

import {
  ToastService
} from '@app/shared/services/toast.service';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';
import { DepartmentService } from '@app/features/department/services/department.service';


@Component({
  selector: 'app-course-list',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    DeleteCourseDialogComponent
  ],

  templateUrl: './course-list.component.html',

  styleUrl: './course-list.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CourseListComponent
  implements OnInit {


  // =====================================================
  // SERVICES
  // =====================================================

  private readonly service =
    inject(CourseService);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly router =
    inject(Router);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly toast =
    inject(ToastService);


  // =====================================================
  // DATA
  // =====================================================

  courses: CourseResponse[] = [];

  departments: DepartmentOption[] = [];


  // =====================================================
  // STATUS
  // =====================================================

  /**
   * RecordStatus enum template mein use karne ke liye
   */
  readonly recordStatus = RecordStatus;


  /**
   * Status dropdown ke options
   *
   * Yahan ACTIVE / INACTIVE / DELETED
   * manually string ke form mein nahi hain.
   */
  readonly statusOptions: RecordStatus[] = [
    RecordStatus.ACTIVE,
    RecordStatus.INACTIVE,
    RecordStatus.DELETED
  ];


  // =====================================================
  // SEARCH
  // =====================================================

  search: CourseSearchRequest =
    createEmptyCourseSearch();

  searching = false;


  // =====================================================
  // PAGINATION
  // =====================================================

  page = 0;

  size = 10;

  totalElements = 0;

  totalPages = 0;

  last = false;


  // =====================================================
  // SORTING
  // =====================================================

  sortBy = 'id';

  direction: 'asc' | 'desc' = 'asc';


  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;

  departmentLoading = false;


  // =====================================================
  // DELETE DIALOG
  // =====================================================

  selectedCourse: CourseResponse | null = null;

  deleting = false;


  // =====================================================
  // LIFECYCLE
  // =====================================================

  ngOnInit(): void {

    /**
     * Courses aur departments dono alag API
     * se load honge.
     */
    this.loadDepartments();

    this.loadCourses();
  }


  // =====================================================
  // LOAD DEPARTMENTS
  // =====================================================

  private loadDepartments(): void {

    if (this.departmentLoading) {
      return;
    }

    this.departmentLoading = true;

    this.cdr.markForCheck();


    this.departmentService
      .getAllDepartments(
        0,
        1000,
        'departmentName',
        'asc'
      )
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response?.data
          ) {

            this.departments =
              (response.data.content ?? [])
                .filter(
                  department =>
                    department?.id &&
                    department.id > 0
                )
                .map(
                  department => ({
                    id: department.id,
                    departmentName:
                      department.departmentName
                  })
                );

          } else {

            this.departments = [];

            this.toast.error(
              response?.message ||
              'Unable to load departments.'
            );
          }

          this.departmentLoading = false;

          this.cdr.markForCheck();
        },


        error: error => {

          this.departmentLoading = false;

          this.departments = [];

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load departments.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =====================================================
  // LOAD COURSES
  // =====================================================

  loadCourses(): void {

    if (this.loading) {
      return;
    }

    this.loading = true;

    this.cdr.markForCheck();


    this.service
      .getAllCourses(
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response?.data
          ) {

            this.setPageData(
              response.data
            );

          } else {

            this.resetPageData();

            this.toast.error(
              response?.message ||
              'Unable to load courses.'
            );
          }

          this.loading = false;

          this.cdr.markForCheck();
        },


        error: error => {

          this.loading = false;

          this.resetPageData();

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to load courses.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =====================================================
  // SEARCH COURSES
  // =====================================================

  searchCourses(): void {

    this.page = 0;

    this.searching =
      this.hasSearchFilters();

    this.executeSearch();
  }


  // =====================================================
  // HAS SEARCH FILTERS
  // =====================================================

  private hasSearchFilters(): boolean {

    const request =
      normalizeCourseSearch(
        this.search
      );

    return Object.values(request)
      .some(value =>
        value !== undefined &&
        value !== null &&
        value !== ''
      );
  }


  // =====================================================
  // EXECUTE SEARCH
  // =====================================================

  private executeSearch(): void {

    if (this.loading) {
      return;
    }

    this.loading = true;


    const normalized =
      normalizeCourseSearch(
        this.search
      );


    const request: CourseSearchRequest = {

      ...normalized,


      // =================================================
      // UI = YEARS
      // API = MONTHS
      // =================================================

      minDuration:
        this.search.minDuration != null
          ? Number(
              this.search.minDuration
            ) * 12
          : undefined,

      maxDuration:
        this.search.maxDuration != null
          ? Number(
              this.search.maxDuration
            ) * 12
          : undefined

    };


    console.log(
      'COURSE SEARCH REQUEST:',
      request
    );


    this.cdr.markForCheck();


    this.service
      .searchCourses(
        request,
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .subscribe({

        next: response => {

          if (
            response?.success &&
            response?.data
          ) {

            this.setPageData(
              response.data
            );

          } else {

            this.resetPageData();

            this.toast.error(
              response?.message ||
              'No courses found.'
            );
          }

          this.loading = false;

          this.cdr.markForCheck();
        },


        error: error => {

          this.loading = false;

          this.resetPageData();

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to search courses.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =====================================================
  // CLEAR SEARCH
  // =====================================================

  clearSearch(): void {

    this.search =
      createEmptyCourseSearch();

    this.searching = false;

    this.page = 0;

    this.loadCourses();
  }


  // =====================================================
  // SORT
  // =====================================================

  sort(field: string): void {

    if (this.loading) {
      return;
    }


    if (this.sortBy === field) {

      this.direction =
        this.direction === 'asc'
          ? 'desc'
          : 'asc';

    } else {

      this.sortBy = field;

      this.direction = 'asc';
    }


    this.page = 0;


    if (this.searching) {

      this.executeSearch();

    } else {

      this.loadCourses();
    }
  }


  // =====================================================
  // SORT ICON
  // =====================================================

  getSortIcon(
    field: string
  ): string {

    if (this.sortBy !== field) {
      return '↕';
    }

    return this.direction === 'asc'
      ? '↑'
      : '↓';
  }


  // =====================================================
  // PAGINATION
  // =====================================================

  nextPage(): void {

    if (
      this.loading ||
      this.last ||
      this.page >=
        this.totalPages - 1
    ) {
      return;
    }


    this.page++;

    this.loadCurrentPage();
  }


  previousPage(): void {

    if (
      this.loading ||
      this.page <= 0
    ) {
      return;
    }


    this.page--;

    this.loadCurrentPage();
  }


  onPageSizeChange(): void {

    this.page = 0;

    this.loadCurrentPage();
  }


  private loadCurrentPage(): void {

    if (this.hasSearchFilters()) {

      this.searching = true;

      this.executeSearch();

    } else {

      this.searching = false;

      this.loadCourses();
    }
  }


  // =====================================================
  // ADD
  // =====================================================

  addCourse(): void {

    this.router.navigate([
      '/dashboard/course/add'
    ]);
  }


  // =====================================================
  // VIEW
  // =====================================================

  viewCourse(id: number): void {

    if (!id || id <= 0) {
      return;
    }

    this.router.navigate([
      '/dashboard/course/view',
      id
    ]);
  }


  // =====================================================
  // EDIT
  // =====================================================

  editCourse(id: number): void {

    if (!id || id <= 0) {
      return;
    }

    this.router.navigate([
      '/dashboard/course/edit',
      id
    ]);
  }


  // =====================================================
  // DELETE DIALOG
  // =====================================================

  deleteCourse(id: number): void {

    if (this.deleting) {
      return;
    }


    const course =
      this.courses.find(
        item =>
          item.id === id
      );


    if (!course) {
      return;
    }


    this.selectedCourse = course;

    this.cdr.markForCheck();
  }


  // =====================================================
  // CONFIRM DELETE
  // =====================================================

  confirmDeleteCourse(
    id: number
  ): void {

    if (
      this.deleting ||
      !id
    ) {
      return;
    }


    this.deleting = true;

    this.cdr.markForCheck();


    this.service
      .deleteCourse(id)
      .subscribe({

        next: response => {

          if (!response?.success) {

            this.deleting = false;

            this.toast.error(
              response?.message ||
              'Unable to delete course.'
            );

            this.cdr.markForCheck();

            return;
          }


          this.deleting = false;

          this.selectedCourse = null;


          this.toast.success(
            response.message ||
            'Course deleted successfully.'
          );


          /**
           * Agar current page par sirf ek
           * course tha aur previous page available hai,
           * to previous page par move karo.
           */
          if (
            this.courses.length === 1 &&
            this.page > 0
          ) {

            this.page--;
          }


          /**
           * Search active hai to search result reload.
           * Otherwise normal course list reload.
           */
          if (this.searching) {

            this.executeSearch();

          } else {

            this.loadCourses();
          }

        },


        error: error => {

          this.deleting = false;

          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to delete course.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =====================================================
  // CANCEL DELETE
  // =====================================================

  cancelDeleteCourse(): void {

    if (this.deleting) {
      return;
    }

    this.selectedCourse = null;

    this.cdr.markForCheck();
  }


  // =====================================================
  // DURATION
  // =====================================================

  durationLabel(
    months: number
  ): string {

    return getDurationLabel(
      months
    );
  }


  // =====================================================
  // PAGE DATA
  // =====================================================

  private setPageData(
    data: PageResponse<CourseResponse>
  ): void {

    this.courses =
      data.content ?? [];


    this.totalElements =
      data.totalElements ?? 0;


    this.totalPages =
      data.totalPages ?? 0;


    this.last =
      data.last ?? true;
  }


  // =====================================================
  // RESET PAGE DATA
  // =====================================================

  private resetPageData(): void {

    this.courses = [];

    this.totalElements = 0;

    this.totalPages = 0;

    this.last = true;
  }


  // =====================================================
  // TRACK
  // =====================================================

  trackByCourseId(
    index: number,
    course: CourseResponse
  ): number {

    return course.id;
  }

}