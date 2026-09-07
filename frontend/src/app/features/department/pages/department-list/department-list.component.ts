import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import {
  DepartmentResponse,
  DepartmentSearchRequest,
  DepartmentPageResponse
} from '../../models/department.model';

import { DepartmentService } from '../../services/department.service';

import { DeleteDepartmentDialogComponent } from '../../components/delete-department-dialog/delete-department-dialog.component';

import { ToastService } from '@app/shared/services/toast.service';

import {
  RecordStatus
} from '@app/shared/enums/record-status.enum';


@Component({
  selector: 'app-department-list',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    DeleteDepartmentDialogComponent
  ],

  templateUrl: './department-list.component.html',

  styleUrl: './department-list.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DepartmentListComponent
  implements OnInit {


  // =====================================================
  // SERVICES
  // =====================================================

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

  departments: DepartmentResponse[] = [];


  // =====================================================
  // STATUS
  // =====================================================

  /**
   * Expose enum to template.
   *
   * Template mein directly:
   *
   * recordStatus.ACTIVE
   * recordStatus.INACTIVE
   *
   * use kar sakte hain.
   */
  readonly recordStatus = RecordStatus;


  /**
   * Status dropdown options.
   *
   * Status values manually string ke form mein
   * component/template mein nahi likhne padenge.
   */
  readonly statusOptions: RecordStatus[] = [
    RecordStatus.ACTIVE,
    RecordStatus.INACTIVE
  ];


  // =====================================================
  // PAGINATION
  // =====================================================

  page = 0;

  size = 10;

  totalElements = 0;

  totalPages = 0;

  last = true;


  // =====================================================
  // SORTING
  // =====================================================

  sortBy = 'id';

  direction: 'asc' | 'desc' = 'asc';


  // =====================================================
  // SEARCH
  // =====================================================

  search: DepartmentSearchRequest = {
    departmentName: '',
    departmentCode: '',
    status: null
  };

  searching = false;


  // =====================================================
  // UI STATE
  // =====================================================

  loading = false;

  deleting = false;


  // =====================================================
  // DELETE DIALOG STATE
  // =====================================================

  deleteDialogVisible = false;

  selectedDepartment:
    DepartmentResponse | null = null;


  // =====================================================
  // LIFECYCLE
  // =====================================================

  ngOnInit(): void {

    this.loadDepartments();

  }


  // =====================================================
  // LOAD DEPARTMENTS
  // =====================================================

  loadDepartments(): void {

    if (this.loading) {
      return;
    }

    this.loading = true;

    this.cdr.markForCheck();


    this.departmentService
      .getAllDepartments(
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .subscribe({

        next: (
          response: DepartmentPageResponse
        ) => {

          this.handleListResponse(
            response
          );

        },

        error: (error) => {

          this.handleError(
            error,
            'Unable to load departments.'
          );

        }

      });

  }


  // =====================================================
  // SEARCH DEPARTMENTS
  // =====================================================

  searchDepartments(): void {

    if (this.loading) {
      return;
    }

    this.page = 0;

    this.searching =
      this.hasSearchFilters();

    this.loading = true;

    const request =
      this.buildSearchRequest();

    this.cdr.markForCheck();


    this.departmentService
      .searchDepartments(
        request,
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .subscribe({

        next: (
          response: DepartmentPageResponse
        ) => {

          this.handleListResponse(
            response
          );

        },

        error: (error) => {

          this.handleError(
            error,
            'Unable to search departments.'
          );

        }

      });

  }


  // =====================================================
  // BUILD SEARCH REQUEST
  // =====================================================

  private buildSearchRequest():
    DepartmentSearchRequest {

    return {

      departmentName:
        this.search.departmentName
          ?.trim() ||
        undefined,

      departmentCode:
        this.search.departmentCode
          ?.trim()
          .toUpperCase() ||
        undefined,

      status:
        this.search.status ||
        undefined

    };

  }


  // =====================================================
  // CHECK SEARCH FILTER
  // =====================================================

  private hasSearchFilters(): boolean {

    return !!(
      this.search.departmentName?.trim() ||
      this.search.departmentCode?.trim() ||
      this.search.status
    );

  }


  // =====================================================
  // HANDLE LIST RESPONSE
  // =====================================================

  private handleListResponse(
    response: DepartmentPageResponse
  ): void {

    this.loading = false;


    if (!response?.success) {

      this.resetPageData();

      this.toast.error(
        response?.message ||
        'Unable to load departments.'
      );

      this.cdr.markForCheck();

      return;
    }


    const data = response.data;


    if (!data) {

      this.resetPageData();

      this.toast.error(
        'Department data was not returned by the server.'
      );

      this.cdr.markForCheck();

      return;
    }


    this.departments =
      data.content ?? [];


    this.totalElements =
      data.totalElements ?? 0;


    this.totalPages =
      data.totalPages ?? 0;


    this.last =
      data.last ??
      this.page >=
        Math.max(
          this.totalPages - 1,
          0
        );


    this.cdr.markForCheck();

  }


  // =====================================================
  // CLEAR SEARCH
  // =====================================================

  clearSearch(): void {

    this.search = {
      departmentName: '',
      departmentCode: '',
      status: null
    };

    this.searching = false;

    this.page = 0;

    this.loadDepartments();

  }


  // =====================================================
  // RETRY
  // =====================================================

  retry(): void {

    this.loadCurrentPage();

  }


  // =====================================================
  // PAGE SIZE
  // =====================================================

  onPageSizeChange(): void {

    this.page = 0;

    this.searching =
      this.hasSearchFilters();


    if (this.searching) {

      this.executeSearch();

    } else {

      this.loadDepartments();

    }

  }


  // =====================================================
  // NEXT PAGE
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


  // =====================================================
  // PREVIOUS PAGE
  // =====================================================

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


  // =====================================================
  // LOAD CURRENT PAGE
  // =====================================================

  private loadCurrentPage(): void {

    if (this.hasSearchFilters()) {

      this.searching = true;

      this.executeSearch();

    } else {

      this.searching = false;

      this.loadDepartments();

    }

  }


  // =====================================================
  // EXECUTE SEARCH
  // =====================================================

  private executeSearch(): void {

    if (this.loading) {
      return;
    }

    this.loading = true;

    const request =
      this.buildSearchRequest();

    this.cdr.markForCheck();


    this.departmentService
      .searchDepartments(
        request,
        this.page,
        this.size,
        this.sortBy,
        this.direction
      )
      .subscribe({

        next: (
          response: DepartmentPageResponse
        ) => {

          this.handleListResponse(
            response
          );

        },

        error: (error) => {

          this.handleError(
            error,
            'Unable to search departments.'
          );

        }

      });

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

    this.loadCurrentPage();

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
  // ADD DEPARTMENT
  // =====================================================

  addDepartment(): void {

    this.router.navigate([
      '/dashboard/department/add'
    ]);

  }


  // =====================================================
  // VIEW DEPARTMENT
  // =====================================================

  viewDepartment(
    id: number
  ): void {

    if (!id || id <= 0) {
      return;
    }

    this.router.navigate([
      '/dashboard/department',
      id,
      'view'
    ]);

  }


  // =====================================================
  // EDIT DEPARTMENT
  // =====================================================

  editDepartment(
    id: number
  ): void {

    if (!id || id <= 0) {
      return;
    }

    this.router.navigate([
      '/dashboard/department',
      id,
      'edit'
    ]);

  }


  // =====================================================
  // OPEN DELETE DIALOG
  // =====================================================

  deleteDepartment(
    department: DepartmentResponse
  ): void {

    if (
      this.deleting ||
      !department?.id
    ) {
      return;
    }


    this.selectedDepartment =
      department;


    this.deleteDialogVisible =
      true;


    this.cdr.markForCheck();

  }


  // =====================================================
  // CONFIRM DELETE
  // =====================================================

  confirmDeleteDepartment(): void {

    if (
      this.deleting ||
      !this.selectedDepartment?.id
    ) {
      return;
    }


    const departmentId =
      this.selectedDepartment.id;


    this.deleting = true;

    this.cdr.markForCheck();


    this.departmentService
      .deleteDepartment(
        departmentId
      )
      .subscribe({

        next: (response) => {

          this.deleting = false;


          if (!response?.success) {

            this.toast.error(
              response?.message ||
              'Unable to delete department.'
            );


            this.deleteDialogVisible =
              false;

            this.selectedDepartment =
              null;

            this.cdr.markForCheck();

            return;
          }


          this.toast.success(
            response.message ||
            'Department deleted successfully.'
          );


          this.deleteDialogVisible =
            false;

          this.selectedDepartment =
            null;


          /*
           * If deleted record was the last
           * record on current page,
           * move back one page.
           */
          if (
            this.departments.length === 1 &&
            this.page > 0
          ) {

            this.page--;

          }


          this.loadCurrentPage();

          this.cdr.markForCheck();

        },


        error: (error) => {

          this.deleting = false;

          this.deleteDialogVisible =
            false;

          this.selectedDepartment =
            null;


          this.toast.error(
            error?.error?.message ||
            error?.message ||
            'Unable to delete department.'
          );


          this.cdr.markForCheck();

        }

      });

  }


  // =====================================================
  // CANCEL DELETE
  // =====================================================

  cancelDeleteDepartment(): void {

    if (this.deleting) {
      return;
    }


    this.deleteDialogVisible =
      false;

    this.selectedDepartment =
      null;


    this.cdr.markForCheck();

  }


  // =====================================================
  // ERROR HANDLER
  // =====================================================

  private handleError(
    error: any,
    fallbackMessage: string
  ): void {

    this.loading = false;

    this.resetPageData();


    this.toast.error(
      error?.error?.message ||
      error?.message ||
      fallbackMessage
    );


    this.cdr.markForCheck();

  }


  // =====================================================
  // RESET PAGE DATA
  // =====================================================

  private resetPageData(): void {

    this.departments = [];

    this.totalElements = 0;

    this.totalPages = 0;

    this.last = true;

  }


  // =====================================================
  // TRACK BY
  // =====================================================

  trackByDepartmentId(
    index: number,
    department: DepartmentResponse
  ): number {

    return department.id;

  }

}