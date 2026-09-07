import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  SemesterSearchFilterComponent
} from '../../components/semester-search-filter/semester-search-filter.component';

import {
  SemesterTableComponent
} from '../../components/semester-table/semester-table.component';

import {
  DeleteSemesterDialogComponent
} from '../../components/delete-semester-dialog/delete-semester-dialog.component';

import {
  SemesterSearchRequest,
  SemesterResponse
} from '../../models/semester.model';

import {
  SemesterService
} from '../../services/semester.service';

import {
  ToastService
} from '@app/shared/services/toast.service';


@Component({
  selector: 'app-semester-list',
  standalone: true,

  imports: [
    SemesterSearchFilterComponent,
    SemesterTableComponent,
    DeleteSemesterDialogComponent
  ],

  templateUrl: './semester-list.component.html',
  styleUrl: './semester-list.component.scss'
})
export class SemesterListComponent
  implements OnInit {


  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly semesterService =
    inject(SemesterService);

  private readonly router =
    inject(Router);

  private readonly toastService =
    inject(ToastService);


  // =====================================================
  // DATA
  // =====================================================

  semesters: SemesterResponse[] = [];


  // =====================================================
  // SELECTED SEMESTER
  // =====================================================

  /**
   * Currently selected semester for delete confirmation.
   */
  selectedSemester:
    SemesterResponse | null = null;


  /**
   * Delete API loading state.
   */
  deleteLoading = false;


  // =====================================================
  // LOADING
  // =====================================================

  loading = false;


  // =====================================================
  // PAGINATION
  // =====================================================

  page = 0;

  size = 10;

  totalElements = 0;

  totalPages = 0;

  last = true;


  // =====================================================
  // SEARCH
  // =====================================================

  currentSearch:
    SemesterSearchRequest = {};


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.loadSemesters();

  }


  // =====================================================
  // LOAD SEMESTERS
  // =====================================================

  loadSemesters(): void {

    this.loading = true;

    this.semesterService
      .searchSemesters(
        this.currentSearch,
        this.page,
        this.size,
        'semesterNumber',
        'asc'
      )
      .subscribe({

        next: (response) => {

          this.semesters =
            response?.content ?? [];


          this.page =
            response?.pageNumber ?? 0;


          this.size =
            response?.pageSize ?? this.size;


          this.totalElements =
            response?.totalElements ?? 0;


          this.totalPages =
            response?.totalPages ?? 0;


          this.last =
            response?.last ?? true;


          this.loading = false;

        },


        error: (error: any) => {

          this.loading = false;

          this.semesters = [];

          this.totalElements = 0;

          this.totalPages = 0;

          this.last = true;


          this.toastService.error(
            error?.error?.message ||
            'Unable to load semesters.'
          );

        }

      });

  }


  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(
    request: SemesterSearchRequest
  ): void {

    this.currentSearch = {
      ...request
    };

    this.page = 0;

    this.loadSemesters();

  }


  // =====================================================
  // RESET
  // =====================================================

  onReset(): void {

    this.currentSearch = {};

    this.page = 0;

    this.loadSemesters();

  }


  // =====================================================
  // PAGE CHANGE
  // =====================================================

  onPageChange(
    page: number
  ): void {

    this.page = page;

    this.loadSemesters();

  }


  // =====================================================
  // ADD SEMESTER
  // =====================================================

  addSemester(): void {

    this.router.navigate([
      '/dashboard/semester/add'
    ]);

  }


  // =====================================================
  // VIEW SEMESTER
  // =====================================================

  viewSemester(
    id: number
  ): void {

    this.router.navigate([
      '/dashboard/semester/view',
      id
    ]);

  }


  // =====================================================
  // EDIT SEMESTER
  // =====================================================

  editSemester(
    id: number
  ): void {

    this.router.navigate([
      '/dashboard/semester/edit',
      id
    ]);

  }


  // =====================================================
  // OPEN DELETE DIALOG
  // =====================================================

  deleteSemester(
    id: number
  ): void {

    if (!id || id <= 0) {

      this.toastService.error(
        'Invalid semester id.'
      );

      return;
    }


    const semester =
      this.semesters.find(
        item => item.id === id
      );


    if (!semester) {

      this.toastService.error(
        'Semester record not found.'
      );

      return;
    }


    /*
     * IMPORTANT:
     *
     * Yahan API call nahi karni.
     *
     * Pehle selected semester set hoga.
     * Uske baad dialog open hoga.
     */

    this.selectedSemester =
      semester;

  }


  // =====================================================
  // CONFIRM DELETE
  // =====================================================

  onDeleteConfirm(
    id: number
  ): void {

    if (
      !id ||
      id <= 0 ||
      this.deleteLoading
    ) {

      return;
    }


    this.deleteLoading = true;


    this.semesterService
      .deleteSemester(id)
      .subscribe({

        next: () => {

          this.deleteLoading = false;


          /*
           * Dialog close
           */

          this.selectedSemester =
            null;


          /*
           * Success toast
           */

          this.toastService.success(
            'Semester deleted successfully.'
          );


          /*
           * Agar current page par sirf
           * ek record tha aur page 0 se bada hai,
           * to previous page par chale jao.
           */

          if (
            this.semesters.length === 1 &&
            this.page > 0
          ) {

            this.page--;

          }


          /*
           * List reload
           */

          this.loadSemesters();

        },


        error: (error: any) => {

          this.deleteLoading = false;


          this.toastService.error(
            error?.error?.message ||
            'Unable to delete semester.'
          );

        }

      });

  }


  // =====================================================
  // CANCEL DELETE
  // =====================================================

  onDeleteCancel(): void {

    if (this.deleteLoading) {

      return;
    }


    this.selectedSemester =
      null;

  }

}