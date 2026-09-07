import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  MatDialog
} from '@angular/material/dialog';

import {
  MATERIAL_MODULES
} from '../../../../shared/material/material';

import {
  ToastService
} from '@app/shared/services/toast.service';

import {
  FacultyService
} from '../../services/faculty.service';

import {
  FacultyResponse
} from '../../model/faculty-response.model';

import {
  FacultySearchRequest
} from '../../model/faculty-search-request.model';

import {
  FacultyTableComponent
} from '../../components/faculty-table/faculty-table.component';

import {
  FacultySearchFilterComponent
} from '../../components/faculty-search-filter/faculty-search-filter.component';

import {
  DeleteFacultyDialogComponent
} from '../../components/delete-faculty-dialog/delete-faculty-dialog.component';


// =====================================================
// PAGINATION TYPE
// =====================================================

interface FacultyPageChange {
  pageIndex: number;
  pageSize: number;
}


// =====================================================
// SORT TYPE
// =====================================================

type SortDirection =
  | 'asc'
  | 'desc'
  | '';

interface FacultySort {
  active: string;
  direction: SortDirection;
}


@Component({
  selector: 'app-faculty-list',

  standalone: true,

  imports: [
    ...MATERIAL_MODULES,
    FacultyTableComponent,
    FacultySearchFilterComponent
  ],

  templateUrl: './faculty-list.component.html',

  styleUrl: './faculty-list.component.scss'
})
export class FacultyListComponent
  implements OnInit {


  // =====================================================
  // DEPENDENCIES
  // =====================================================

  private readonly facultyService =
    inject(FacultyService);

  private readonly router =
    inject(Router);

  private readonly dialog =
    inject(MatDialog);

  private readonly toast =
    inject(ToastService);


  // =====================================================
  // FACULTY DATA
  // =====================================================

  readonly faculties =
    signal<FacultyResponse[]>([]);


  // =====================================================
  // UI STATE
  // =====================================================

  readonly loading =
    signal(false);

  readonly deleting =
    signal(false);


  // =====================================================
  // PAGINATION
  // =====================================================

  readonly totalElements =
    signal(0);

  readonly pageNumber =
    signal(0);

  readonly pageSize =
    signal(5);


  // =====================================================
  // SORTING
  // =====================================================

  readonly sortBy =
    signal('id');

  readonly direction =
    signal<'asc' | 'desc'>('asc');


  // =====================================================
  // SEARCH
  // =====================================================

  readonly searchRequest =
    signal<FacultySearchRequest | null>(null);


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.loadCurrentData();

  }


  // =====================================================
  // LOAD CURRENT DATA
  // =====================================================

  private loadCurrentData(): void {

    if (this.searchRequest()) {

      this.loadSearchFaculties();

    } else {

      this.loadFaculties();

    }

  }


  // =====================================================
  // LOAD ALL FACULTIES
  // =====================================================

  loadFaculties(): void {

    this.loading.set(true);

    this.facultyService
      .getAllFaculties(
        this.pageNumber(),
        this.pageSize(),
        this.sortBy(),
        this.direction()
      )
      .subscribe({

        next: response => {

          const page =
            response?.data;

          this.faculties.set(
            page?.content ?? []
          );

          this.totalElements.set(
            page?.totalElements ?? 0
          );

          this.loading.set(false);

        },

        error: error => {

          this.loading.set(false);

          this.faculties.set([]);

          this.totalElements.set(0);

          this.showError(
            error,
            'Unable to load faculties.'
          );

        }

      });

  }


  // =====================================================
  // LOAD SEARCH FACULTIES
  // =====================================================

  loadSearchFaculties(): void {

    const request =
      this.searchRequest();


    if (!request) {

      this.loadFaculties();

      return;

    }


    this.loading.set(true);

    this.facultyService
      .searchFaculties(
        request,
        this.pageNumber(),
        this.pageSize(),
        this.sortBy(),
        this.direction()
      )
      .subscribe({

        next: response => {

          const page =
            response?.data;

          this.faculties.set(
            page?.content ?? []
          );

          this.totalElements.set(
            page?.totalElements ?? 0
          );

          this.loading.set(false);

        },

        error: error => {

          this.loading.set(false);

          this.faculties.set([]);

          this.totalElements.set(0);

          this.showError(
            error,
            'Unable to search faculties.'
          );

        }

      });

  }


  // =====================================================
  // SEARCH
  // =====================================================

  onSearch(
    request: FacultySearchRequest
  ): void {

    this.searchRequest.set(
      request
    );

    this.pageNumber.set(0);

    this.loadSearchFaculties();

  }


  // =====================================================
  // RESET SEARCH
  // =====================================================

  onReset(): void {

    this.searchRequest.set(null);

    this.pageNumber.set(0);

    this.pageSize.set(5);

    this.sortBy.set('id');

    this.direction.set('asc');

    this.loadFaculties();

  }


  // =====================================================
  // PAGE CHANGE
  // =====================================================

  onPageChange(
    event: FacultyPageChange
  ): void {

    if (!event) {
      return;
    }


    const newPageIndex =
      Number(event.pageIndex);

    const newPageSize =
      Number(event.pageSize);


    if (
      Number.isNaN(newPageIndex) ||
      Number.isNaN(newPageSize) ||
      newPageIndex < 0 ||
      newPageSize <= 0
    ) {

      return;

    }


    this.pageNumber.set(
      newPageIndex
    );

    this.pageSize.set(
      newPageSize
    );


    this.loadCurrentData();

  }


  // =====================================================
  // SORT CHANGE
  // =====================================================

  onSortChange(
    event: FacultySort
  ): void {

    if (!event) {
      return;
    }


    /*
     * If there is no active column,
     * return to default backend sorting.
     */

    if (!event.active) {

      this.sortBy.set('id');

      this.direction.set('asc');

      this.pageNumber.set(0);

      this.loadCurrentData();

      return;

    }


    this.sortBy.set(
      event.active
    );


    /*
     * Backend supports only asc / desc.
     *
     * The table can emit an empty direction
     * when sorting is cleared.
     *
     * In that case we use default ascending
     * sorting by the selected column.
     */

    this.direction.set(
      event.direction === 'desc'
        ? 'desc'
        : 'asc'
    );


    /*
     * Sorting always starts from
     * the first page.
     */

    this.pageNumber.set(0);


    this.loadCurrentData();

  }


  // =====================================================
  // ADD FACULTY
  // =====================================================

  addFaculty(): void {

    this.router.navigate([
      '/dashboard/faculty/add'
    ]);

  }


  // =====================================================
  // VIEW FACULTY
  // =====================================================

  viewFaculty(
    id: number
  ): void {

    if (
      !id ||
      id <= 0
    ) {

      return;

    }


    this.router.navigate([
      '/dashboard/faculty',
      id
    ]);

  }


  // =====================================================
  // EDIT FACULTY
  // =====================================================

  editFaculty(
    id: number
  ): void {

    if (
      !id ||
      id <= 0
    ) {

      return;

    }


    this.router.navigate([
      '/dashboard/faculty/edit',
      id
    ]);

  }


  // =====================================================
  // DELETE FACULTY
  // =====================================================

  deleteFaculty(
    id: number
  ): void {

    if (
      this.deleting() ||
      !id ||
      id <= 0
    ) {

      return;

    }


    // ===================================================
    // DELETE CONFIRMATION DIALOG
    // ===================================================

    const dialogRef =
      this.dialog.open(
        DeleteFacultyDialogComponent,
        {
          width: '400px',

          maxWidth:
            'calc(100vw - 32px)',

          disableClose: true
        }
      );


    dialogRef
      .afterClosed()
      .subscribe(result => {


        // =================================================
        // USER CANCELLED
        // =================================================

        if (result !== true) {

          return;

        }


        // =================================================
        // DELETE START
        // =================================================

        this.deleting.set(true);


        this.facultyService
          .deleteFaculty(id)
          .subscribe({

            // =============================================
            // SUCCESS
            // =============================================

            next: response => {

              this.deleting.set(false);


              this.toast.success(
                response?.message ??
                'Faculty deleted successfully.'
              );


              /*
               * If current page contains only one
               * record and it is not the first page,
               * move to previous page.
               */

              if (
                this.faculties().length === 1 &&
                this.pageNumber() > 0
              ) {

                this.pageNumber.update(
                  page => page - 1
                );

              }


              /*
               * Reload current page.
               */

              this.loadCurrentData();

            },


            // =============================================
            // ERROR
            // =============================================

            error: error => {

              this.deleting.set(false);

              this.showError(
                error,
                'Faculty delete failed.'
              );

            }

          });

      });

  }


  // =====================================================
  // ERROR HANDLER
  // =====================================================

  private showError(
    error: any,
    defaultMessage: string
  ): void {

    const message =
      error?.error?.message ??
      error?.message ??
      defaultMessage;


    this.toast.error(
      message
    );

  }

}