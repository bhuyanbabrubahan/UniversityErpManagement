import {
  CommonModule
} from '@angular/common';

import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  FormsModule
} from '@angular/forms';

import {
  Router,
  RouterModule
} from '@angular/router';

import {
  finalize
} from 'rxjs';

import {
  ToastService
} from '@app/shared/services/toast.service';

import {
  AttendanceSource
} from '@app/shared/enums/attendance-source.enum';

import {
  AttendanceService
} from '../../services/attendance.service';

import {
  AttendanceExcelService
} from '../../services/attendance-excel.service';

import {
  AttendanceSession,
  AttendanceStudentRow,
  FacultySubjectOption
} from '../../models/attendance.model';


@Component({
  selector: 'app-attendance-bulk-upload',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],

  templateUrl:
    './attendance-bulk-upload.component.html',

  styleUrl:
    './attendance-bulk-upload.component.scss',

  changeDetection:
    ChangeDetectionStrategy.OnPush
})
export class AttendanceBulkUploadComponent
  implements OnInit {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly service =
    inject(AttendanceService);

  private readonly excel =
    inject(AttendanceExcelService);

  private readonly toast =
    inject(ToastService);

  private readonly router =
    inject(Router);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // TODAY
  // =========================================================

  readonly today =
    this.formatDate(new Date());


  // =========================================================
  // ACADEMIC YEAR
  // =========================================================

  academicYear = '';


  // =========================================================
  // MAPPINGS
  // =========================================================

  mappings:
    FacultySubjectOption[] = [];

  selectedMappingId = 0;


  // =========================================================
  // SESSION
  // =========================================================

  attendanceDate =
    this.today;

  sessionId = 0;

  sessionStatus = '';

  session:
    AttendanceSession | null = null;


  // =========================================================
  // EXCEL
  // =========================================================

  rows:
    AttendanceStudentRow[] = [];

  selectedFile = '';


  // =========================================================
  // STATE
  // =========================================================

  loading = false;

  errorMessage = '';


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.academicYear =
      this.getCurrentAcademicYear();

    this.readQueryParams();

    /*
     * If sessionId exists, this page was opened
     * from an existing attendance session.
     *
     * Otherwise load assigned faculty subjects.
     */

    if (this.sessionId) {

      this.loadSession();

      return;
    }

    this.loadMappings();
  }


  // =========================================================
  // QUERY PARAMS
  // =========================================================

  private readQueryParams(): void {

    const params =
      new URLSearchParams(
        window.location.search
      );


    this.sessionId =
      Number(
        params.get('sessionId') ?? 0
      );


    this.selectedMappingId =
      Number(
        params.get('mappingId') ?? 0
      );


    const queryDate =
      params.get('date');


    if (queryDate) {

      this.attendanceDate =
        queryDate;
    }


    /*
     * Never allow a future date coming from query params.
     */

    if (
      this.attendanceDate >
      this.today
    ) {

      this.attendanceDate =
        this.today;
    }
  }


  // =========================================================
  // CURRENT ACADEMIC YEAR
  // =========================================================

  private getCurrentAcademicYear(): string {

    const date =
      new Date();

    const year =
      date.getFullYear();

    const month =
      date.getMonth() + 1;


    /*
     * Academic year starts in April.
     *
     * April 2026  -> 2026-2027
     * August 2026 -> 2026-2027
     * January 2027 -> 2026-2027
     */

    const startYear =
      month >= 4
        ? year
        : year - 1;


    return `${startYear}-${startYear + 1}`;
  }


  // =========================================================
  // DATE FORMAT
  // =========================================================

  private formatDate(
    date: Date
  ): string {

    const year =
      date.getFullYear();

    const month =
      String(
        date.getMonth() + 1
      ).padStart(2, '0');

    const day =
      String(
        date.getDate()
      ).padStart(2, '0');


    return `${year}-${month}-${day}`;
  }


  // =========================================================
  // LOAD FACULTY SUBJECTS
  // =========================================================

  loadMappings(): void {

    if (this.loading) {
      return;
    }


    if (
      !this.academicYear?.trim()
    ) {

      this.errorMessage =
        'Academic year could not be determined.';

      this.toast.error(
        this.errorMessage
      );

      this.cdr.markForCheck();

      return;
    }


    this.loading = true;

    this.errorMessage = '';


    this.service
      .getFacultySubjects(
        this.academicYear
      )
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        // ===================================================
        // SUCCESS
        // ===================================================

        next: list => {

          this.mappings =
            (list ?? [])
              .filter(
                item =>
                  !!item &&
                  Number(item.id) > 0
              );


          /*
           * If mappingId came from query params,
           * verify that it actually belongs to the
           * logged-in faculty.
           */

          if (
            this.selectedMappingId &&
            !this.mappings.some(
              item =>
                Number(item.id) ===
                this.selectedMappingId
            )
          ) {

            this.selectedMappingId =
              0;
          }


          if (
            this.mappings.length === 0
          ) {

            this.errorMessage =
              `No subjects have been assigned to you for academic year ${this.academicYear}.`;

            this.cdr.markForCheck();

            return;
          }


          this.cdr.markForCheck();
        },


        // ===================================================
        // ERROR
        // ===================================================

        error: error => {

          this.mappings = [];

          this.errorMessage =
            error?.error?.message ??
            'Unable to load assigned subjects.';

          this.toast.error(
            this.errorMessage
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // LOAD EXISTING SESSION
  // =========================================================

  loadSession(): void {

    if (!this.sessionId) {
      return;
    }


    this.loading = true;

    this.errorMessage = '';


    this.service
      .getSession(
        this.sessionId
      )
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        // ===================================================
        // SUCCESS
        // ===================================================

        next: session => {

          if (!session?.id) {

            this.toast.error(
              'Invalid attendance session returned.'
            );

            return;
          }


          this.session =
            session;

          this.sessionId =
            Number(session.id);

          this.selectedMappingId =
            Number(
              session.facultySubjectMappingId
            );

          this.attendanceDate =
            this.normalizeAttendanceDate(
              session.attendanceDate
            ) || this.today;

          this.sessionStatus =
            session.sessionStatus ?? '';


          /*
           * Future session protection.
           */

          if (
            this.attendanceDate >
            this.today
          ) {

            this.attendanceDate =
              this.today;
          }


          this.cdr.markForCheck();
        },


        // ===================================================
        // ERROR
        // ===================================================

        error: error => {

          this.session = null;

          this.sessionId = 0;

          this.sessionStatus = '';

          this.toast.error(
            error?.error?.message ??
            'Unable to load attendance session.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // OPEN / FIND SESSION
  // =========================================================
  //
  // IMPORTANT:
  //
  // Do NOT call:
  //
  // getSessionByMappingAndDate()
  //
  // Missing session is expected.
  //
  // Instead:
  //
  // 1. Load history
  // 2. Find requested date
  // 3. If found -> use it
  // 4. If missing -> create new session
  //
  // =========================================================

  openSession(): void {

    if (this.loading) {
      return;
    }


    if (
      !this.selectedMappingId
    ) {

      this.toast.warning(
        'Select an assigned subject.'
      );

      return;
    }


    if (
      !this.attendanceDate
    ) {

      this.toast.warning(
        'Select attendance date.'
      );

      return;
    }


    if (
      this.attendanceDate >
      this.today
    ) {

      this.attendanceDate =
        this.today;

      this.toast.warning(
        'Future dates are not allowed for attendance.'
      );

      this.cdr.markForCheck();

      return;
    }


    /*
     * Verify the selected mapping belongs
     * to the loaded assignments.
     */

    const assigned =
      this.mappings.some(
        mapping =>
          Number(mapping.id) ===
          Number(this.selectedMappingId)
      );


    if (!assigned) {

      this.toast.error(
        'You are not assigned to this class.'
      );

      return;
    }


    this.loading = true;

    this.errorMessage = '';


    this.service
      .getSessionHistory(
        this.selectedMappingId
      )
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        // ===================================================
        // HISTORY SUCCESS
        // ===================================================

        next: sessions => {

          const session =
            this.findSessionByDate(
              sessions,
              this.attendanceDate
            );


          /*
           * Existing session.
           */

          if (session) {

            this.setSession(
              session
            );


            /*
             * Only OPEN sessions can be used
             * for bulk upload.
             */

            if (
              this.isSessionOpenStatus(
                session
              )
            ) {

              this.toast.success(
                'Existing attendance session loaded.'
              );

              return;
            }


            if (
              this.isSessionCompletedStatus(
                session
              )
            ) {

              this.toast.warning(
                'Attendance session for this date is already completed.'
              );

              return;
            }


            if (
              this.isSessionCancelledStatus(
                session
              )
            ) {

              this.toast.warning(
                'Attendance session for this date is cancelled.'
              );

              return;
            }


            this.toast.warning(
              `Attendance session is ${
                session.sessionStatus ??
                'not available'
              }.`
            );

            return;
          }


          /*
           * No session for this date.
           *
           * This is completely normal.
           *
           * Create a new session.
           */

          this.createSession(
            this.selectedMappingId,
            this.attendanceDate
          );
        },


        // ===================================================
        // HISTORY ERROR
        // ===================================================

        error: error => {

          this.toast.error(
            error?.error?.message ??
            'Unable to check attendance session history.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // CREATE SESSION
  // =========================================================

  private createSession(
    mappingId: number,
    date: string
  ): void {

    const source =
      date === this.today
        ? AttendanceSource.ONLINE
        : AttendanceSource.MANUAL_BACKDATE;


    this.loading = true;


    this.service
      .openSession({

        facultySubjectMappingId:
          mappingId,

        attendanceDate:
          date,

        attendanceSource:
          source

      })
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        // ===================================================
        // CREATED
        // ===================================================

        next: session => {

          if (!session?.id) {

            this.toast.error(
              'Attendance session was created but session ID was not returned.'
            );

            return;
          }


          this.setSession(
            session
          );


          this.toast.success(
            'Attendance session opened successfully.'
          );


          this.cdr.markForCheck();
        },


        // ===================================================
        // ERROR
        // ===================================================

        error: error => {

          this.toast.error(
            error?.error?.message ??
            'Unable to create attendance session.'
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // SET SESSION
  // =========================================================

  private setSession(
    session: AttendanceSession
  ): void {

    this.session =
      session;

    this.sessionId =
      Number(session.id);

    this.selectedMappingId =
      Number(
        session.facultySubjectMappingId
      );

    this.attendanceDate =
      this.normalizeAttendanceDate(
        session.attendanceDate
      ) || this.today;

    this.sessionStatus =
      session.sessionStatus ?? '';


    /*
     * Clear any previously loaded Excel data
     * whenever a new session is selected.
     */

    this.rows = [];

    this.selectedFile = '';
  }


  // =========================================================
  // FIND SESSION BY DATE
  // =========================================================

  private findSessionByDate(
    sessions:
      AttendanceSession[] |
      null |
      undefined,

    date: string
  ): AttendanceSession | null {

    if (
      !Array.isArray(sessions) ||
      sessions.length === 0
    ) {

      return null;
    }


    return (
      sessions.find(
        session =>
          this.normalizeAttendanceDate(
            session?.attendanceDate
          ) === date
      ) ?? null
    );
  }


  // =========================================================
  // NORMALIZE ATTENDANCE DATE
  // =========================================================

  private normalizeAttendanceDate(
    value:
      string |
      null |
      undefined
  ): string {

    if (!value) {
      return '';
    }


    return String(value)
      .slice(0, 10);
  }


  downloadTemplate(): void {
    this.excel.downloadTemplate();
    this.toast.success('Attendance Excel template downloaded.');
  }

  // =========================================================
  // FILE SELECT
  // =========================================================

  onFile(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    const file =
      input.files?.item(0);


    if (!file) {
      return;
    }


    // -------------------------------------------------------
    // File extension
    // -------------------------------------------------------

    const extension =
      file.name
        .split('.')
        .pop()
        ?.toLowerCase();


    if (
      extension !== 'xlsx' &&
      extension !== 'xls'
    ) {

      this.toast.error(
        'Please select a valid Excel file (.xlsx or .xls).'
      );

      input.value = '';

      return;
    }


    // -------------------------------------------------------
    // Session validation
    // -------------------------------------------------------

    if (!this.sessionId) {

      this.toast.warning(
        'Open an attendance session before uploading Excel.'
      );

      input.value = '';

      return;
    }


    if (
      !this.isSessionOpen(
        this.sessionStatus
      )
    ) {

      this.toast.warning(
        'Only an OPEN attendance session can be uploaded.'
      );

      input.value = '';

      return;
    }


    // -------------------------------------------------------
    // Read Excel
    // -------------------------------------------------------

    this.loading = true;

    this.selectedFile =
      file.name;

    this.rows = [];


    this.excel
      .read(file)
      .then(
        rows => {

          this.rows =
            rows ?? [];

          this.loading = false;


          if (
            this.rows.length === 0
          ) {

            this.selectedFile = '';

            this.toast.warning(
              'No attendance records found in the file.'
            );

          } else {

            this.toast.success(
              `${this.rows.length} rows loaded successfully.`
            );
          }


          this.cdr.markForCheck();
        }
      )
      .catch(
        () => {

          this.loading = false;

          this.rows = [];

          this.selectedFile = '';

          this.toast.error(
            'Invalid Excel file.'
          );

          this.cdr.markForCheck();
        }
      );
  }


  // =========================================================
  // REMOVE FILE
  // =========================================================

  removeFile(
    input?: HTMLInputElement
  ): void {

    this.rows = [];

    this.selectedFile = '';


    if (input) {
      input.value = '';
    }


    this.cdr.markForCheck();
  }


  // =========================================================
  // PREVIEW
  // =========================================================

  preview(): void {

    if (!this.sessionId) {

      this.toast.warning(
        'Open an attendance session first.'
      );

      return;
    }


    if (
      !this.isSessionOpen(
        this.sessionStatus
      )
    ) {

      this.toast.warning(
        'Only an OPEN attendance session can be uploaded.'
      );

      return;
    }


    if (
      !this.rows.length
    ) {

      this.toast.warning(
        'Select a valid Excel file first.'
      );

      return;
    }


    this.router.navigate(
      [
        '/dashboard/attendance/bulk-preview'
      ],
      {
        state: {

          rows:
            this.rows,

          sessionId:
            this.sessionId,

          mappingId:
            this.selectedMappingId,

          attendanceDate:
            this.attendanceDate

        }
      }
    );
  }


  // =========================================================
  // BACK
  // =========================================================

  back(): void {

    this.router.navigate([
      '/dashboard/attendance/classes'
    ]);
  }


  // =========================================================
  // STATUS HELPERS
  // =========================================================

  isSessionOpen(
    status:
      string |
      null |
      undefined
  ): boolean {

    return status === 'OPEN';
  }


  isSessionCompleted(
    status:
      string |
      null |
      undefined
  ): boolean {

    return status === 'COMPLETED';
  }


  isSessionCancelled(
    status:
      string |
      null |
      undefined
  ): boolean {

    return status === 'CANCELLED';
  }


  private isSessionOpenStatus(
    session:
      AttendanceSession |
      null |
      undefined
  ): boolean {

    return (
      session?.sessionStatus === 'OPEN'
    );
  }


  private isSessionCompletedStatus(
    session:
      AttendanceSession |
      null |
      undefined
  ): boolean {

    return (
      session?.sessionStatus === 'COMPLETED'
    );
  }


  private isSessionCancelledStatus(
    session:
      AttendanceSession |
      null |
      undefined
  ): boolean {

    return (
      session?.sessionStatus === 'CANCELLED'
    );
  }


  // =========================================================
  // SELECTED MAPPING
  // =========================================================

  get selectedMapping():
    FacultySubjectOption | null {

    return (
      this.mappings.find(
        mapping =>
          Number(mapping.id) ===
          Number(this.selectedMappingId)
      ) ?? null
    );
  }
}
