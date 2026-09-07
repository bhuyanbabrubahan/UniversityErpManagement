import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  Router,
  RouterModule
} from '@angular/router';

import { FormsModule } from '@angular/forms';

import { finalize } from 'rxjs';

import { ToastService } from '@app/shared/services/toast.service';

import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';

import { RecordStatus } from '@app/shared/enums/record-status.enum';

import { AttendanceService } from '../../services/attendance.service';

import { AttendanceRecord } from '../../models/attendance.model';


@Component({

  selector: 'app-attendance-history',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],

  templateUrl:
    './attendance-history.component.html',

  styleUrl:
    './attendance-history.component.scss',

  changeDetection:
    ChangeDetectionStrategy.OnPush

})
export class AttendanceHistoryComponent
  implements OnInit {


  /* ============================================================
     DEPENDENCIES
  ============================================================ */

  private readonly service =
    inject(AttendanceService);

  private readonly toast =
    inject(ToastService);

  private readonly router =
    inject(Router);

  private readonly cdr =
    inject(ChangeDetectorRef);


  /* ============================================================
     ENUMS
  ============================================================ */

  readonly AttendanceStatus =
    AttendanceStatus;

  readonly RecordStatus =
    RecordStatus;


  /* ============================================================
     DATA
  ============================================================ */

  records: AttendanceRecord[] = [];

  loading = false;


  /* ============================================================
     FILTERS
  ============================================================ */

  studentId:
    number | null = null;

  facultyId:
    number | null = null;

  subjectId:
    number | null = null;

  mappingId:
    number | null = null;

  attendanceDate = '';

  attendanceStatus:
    AttendanceStatus | null = null;


  /* ============================================================
     PAGINATION
  ============================================================ */

  page = 0;

  size = 5;

  readonly pageSizes = [
    5,
    10,
    15,
    20
  ];

  totalElements = 0;

  totalPages = 0;


  /* ============================================================
     SORT
  ============================================================ */

  sortDirection:
    'asc' | 'desc' = 'desc';


  /* ============================================================
     INIT
  ============================================================ */

  ngOnInit(): void {

    this.search();

  }


  /* ============================================================
     SEARCH
  ============================================================ */

  search(): void {

    if (this.loading) {
      return;
    }


    this.loading = true;

    this.cdr.markForCheck();


    this.service
      .search(

        {

          studentId:
            this.studentId,

          facultyId:
            this.facultyId,

          subjectId:
            this.subjectId,

          facultySubjectMappingId:
            this.mappingId,

          attendanceDate:
            this.attendanceDate ||
            null,

          attendanceStatus:
            this.attendanceStatus,

          status:
            this.RecordStatus.ACTIVE

        },

        this.page,

        this.size,

        'attendanceDate',

        this.sortDirection

      )

      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          this.records =
            response?.content ?? [];

          this.totalElements =
            response?.totalElements ?? 0;

          this.totalPages =
            response?.totalPages ?? 0;


          /*
           * If filtering/deleting changed the number
           * of pages and current page no longer exists,
           * safely move to the last available page.
           */
          if (
            this.totalPages > 0 &&
            this.page >= this.totalPages
          ) {

            this.page =
              this.totalPages - 1;


            /*
             * Wait until current observable finishes
             * before starting the next request.
             */
            setTimeout(() => {

              this.search();

            });

            return;

          }


          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            '[AttendanceHistory] Search error:',
            error
          );


          this.records = [];

          this.totalElements = 0;

          this.totalPages = 0;


          this.toast.error(

            error?.error?.message ??
            error?.message ??
            'Unable to search attendance records.'

          );


          this.cdr.markForCheck();

        }

      });

  }


  /* ============================================================
     SEARCH FROM FIRST PAGE
  ============================================================ */

  searchFromFirstPage(): void {

    this.page = 0;

    this.search();

  }


  /* ============================================================
     RESET
  ============================================================ */

  reset(): void {

    this.studentId = null;

    this.facultyId = null;

    this.subjectId = null;

    this.mappingId = null;

    this.attendanceDate = '';

    this.attendanceStatus = null;

    this.page = 0;

    this.sortDirection = 'desc';

    this.search();

  }


  /* ============================================================
     ROWS PER PAGE
  ============================================================ */

  changePageSize(): void {

    this.page = 0;

    this.search();

  }


  /* ============================================================
     SORT
  ============================================================ */

  toggleSort(): void {

    if (this.loading) {
      return;
    }


    this.sortDirection =
      this.sortDirection === 'desc'
        ? 'asc'
        : 'desc';


    this.page = 0;

    this.search();

  }


  /* ============================================================
     SORT LABEL
  ============================================================ */

  get sortLabel(): string {

    return this.sortDirection === 'desc'
      ? 'Latest first'
      : 'Oldest first';

  }


  /* ============================================================
     PREVIOUS PAGE
  ============================================================ */

  previousPage(): void {

    if (
      this.loading ||
      this.page <= 0
    ) {
      return;
    }


    this.page--;

    this.search();

  }


  /* ============================================================
     NEXT PAGE
  ============================================================ */

  nextPage(): void {

    if (
      this.loading ||
      this.page + 1 >= this.totalPages
    ) {
      return;
    }


    this.page++;

    this.search();

  }


  /* ============================================================
     BACK
  ============================================================ */

  back(): void {

    if (this.loading) {
      return;
    }


    this.router.navigate([
      '/dashboard/attendance'
    ]);

  }


  /* ============================================================
     DELETE
  ============================================================ */

  remove(id: number): void {

    if (this.loading) {
      return;
    }


    const confirmed =
      window.confirm(
        'Are you sure you want to delete this attendance record?'
      );


    if (!confirmed) {
      return;
    }


    this.loading = true;

    this.cdr.markForCheck();


    this.service
      .delete(id)

      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: () => {

          this.toast.success(
            'Attendance deleted successfully.'
          );


          /*
           * If current page contains only one record,
           * move to previous page after deletion.
           */
          if (
            this.records.length === 1 &&
            this.page > 0
          ) {

            this.page--;

          }


          /*
           * Important:
           * Do not call search() immediately because
           * loading is still true during this subscription.
           */
          setTimeout(() => {

            this.search();

          });

        },


        error: error => {

          console.error(
            '[AttendanceHistory] Delete error:',
            error
          );


          this.toast.error(

            error?.error?.message ??
            error?.message ??
            'Unable to delete attendance.'

          );


          this.cdr.markForCheck();

        }

      });

  }


  /* ============================================================
     STATUS VALUE
  ============================================================ */

  statusValue(

    status:
      AttendanceStatus |
      string |
      null |
      undefined

  ): string {

    return String(status ?? '')
      .trim()
      .toUpperCase()
      .replace(/[\s-]+/g, '_');

  }


  /* ============================================================
     STATUS LABEL
  ============================================================ */

  statusLabel(

    status:
      AttendanceStatus |
      string |
      null |
      undefined

  ): string {

    const value =
      this.statusValue(status);


    switch (value) {

      case 'PRESENT':
        return 'Present';

      case 'ABSENT':
        return 'Absent';

      case 'LEAVE':
        return 'Leave';

      case 'HALF_DAY':
        return 'Half Day';

      default:
        return value || '—';

    }

  }


  /* ============================================================
     SOURCE LABEL
  ============================================================ */

  sourceLabel(

    source:
      string |
      null |
      undefined

  ): string {

    const value =
      String(source ?? '')
        .trim()
        .toUpperCase();


    switch (value) {

      case 'ONLINE':
        return 'Online';

      case 'EXCEL':
        return 'Excel';

      case 'EXCEL_IMPORT':
        return 'Excel Import';

      case 'BULK_UPLOAD':
        return 'Bulk Upload';

      case 'MANUAL':
        return 'Manual';

      default:
        return source || '—';

    }

  }


  /* ============================================================
     STUDENT INITIAL
  ============================================================ */

  studentInitial(
    name:
      string |
      null |
      undefined
  ): string {

    const value =
      String(name ?? '').trim();


    if (!value) {
      return '?';
    }


    return value
      .charAt(0)
      .toUpperCase();

  }

}
