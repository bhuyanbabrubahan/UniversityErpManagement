import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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
  AttendanceSession,
  FacultySubjectOption
} from '../../models/attendance.model';


@Component({
  selector: 'app-attendance-classes',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],

  templateUrl:
    './attendance-classes.component.html',

  styleUrl:
    './attendance-classes.component.scss',

  changeDetection:
    ChangeDetectionStrategy.OnPush
})
export class AttendanceClassesComponent
  implements OnInit {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly service =
    inject(AttendanceService);

  private readonly toast =
    inject(ToastService);

  private readonly router =
    inject(Router);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // STATE
  // =========================================================

  classes:
    FacultySubjectOption[] = [];

  sessions:
    Record<number, AttendanceSession | null> = {};

  dates:
    Record<number, string> = {};

  loading = false;

  openingMappingId = 0;

  errorMessage = '';


  // =========================================================
  // TODAY
  // =========================================================

  readonly today =
    this.formatDate(new Date());


  // =========================================================
  // ATTENDANCE SOURCE
  // =========================================================

  readonly AttendanceSource =
    AttendanceSource;


  // =========================================================
  // ACADEMIC YEAR
  // =========================================================

  academicYear = '';


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.academicYear =
      this.getCurrentAcademicYear();

    this.loadClasses();
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
  // LOAD FACULTY CLASSES
  // =========================================================

  loadClasses(): void {

    if (this.loading) {
      return;
    }


    // -------------------------------------------------------
    // Academic year validation
    // -------------------------------------------------------

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

    this.classes = [];

    this.sessions = {};

    this.dates = {};


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

          this.classes =
            (list ?? [])
              .filter(
                item =>
                  !!item &&
                  Number(item.id) > 0
              );


          // -------------------------------------------------
          // Initialize every mapping
          // -------------------------------------------------

          for (
            const mapping
            of this.classes
          ) {

            const mappingId =
              Number(mapping.id);

            this.dates[mappingId] =
              this.today;

            this.sessions[mappingId] =
              null;
          }


          // -------------------------------------------------
          // No assignments
          // -------------------------------------------------

          if (
            this.classes.length === 0
          ) {

            this.errorMessage =
              `No subjects have been assigned to you for academic year ${this.academicYear}.`;

            this.cdr.markForCheck();

            return;
          }


          // -------------------------------------------------
          // Load today's session history
          // -------------------------------------------------

          for (
            const mapping
            of this.classes
          ) {

            this.loadSession(
              Number(mapping.id)
            );
          }


          this.cdr.markForCheck();
        },


        // ===================================================
        // ERROR
        // ===================================================

        error: error => {

          this.classes = [];

          this.sessions = {};

          this.dates = {};

          this.errorMessage =
            error?.error?.message ??
            'Unable to load classes for the logged-in faculty.';

          this.toast.error(
            this.errorMessage
          );

          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // DATE CHANGE
  // =========================================================

  onDateChange(
    mappingId: number
  ): void {

    if (!mappingId) {
      return;
    }


    const selectedDate =
      this.dates[mappingId];


    if (!selectedDate) {
      return;
    }


    // -------------------------------------------------------
    // Future date protection
    // -------------------------------------------------------

    if (
      selectedDate > this.today
    ) {

      this.dates[mappingId] =
        this.today;

      this.sessions[mappingId] =
        null;

      this.toast.warning(
        'Future dates are not allowed for attendance.'
      );

      this.cdr.markForCheck();

      return;
    }


    // -------------------------------------------------------
    // Clear previous date's session
    // -------------------------------------------------------

    this.sessions[mappingId] =
      null;

    this.cdr.markForCheck();


    // -------------------------------------------------------
    // Load selected date from history
    // -------------------------------------------------------

    this.loadSession(
      mappingId
    );
  }


  // =========================================================
  // LOAD SESSION FOR MAPPING + DATE
  // =========================================================
  //
  // IMPORTANT:
  //
  // DO NOT call:
  //
  // getSessionByMappingAndDate()
  //
  // because a missing session is an expected condition.
  //
  // Instead:
  //
  // getSessionHistory(mappingId)
  //
  // Then find the requested attendanceDate locally.
  //
  // =========================================================

  loadSession(
    mappingId: number
  ): void {

    if (!mappingId) {
      return;
    }


    const requestedDate =
      this.dates[mappingId];


    if (!requestedDate) {
      return;
    }


    if (
      requestedDate > this.today
    ) {

      return;
    }


    this.service
      .getSessionHistory(
        mappingId
      )
      .subscribe({

        // ===================================================
        // HISTORY SUCCESS
        // ===================================================

        next: sessions => {

          /*
           * Make sure the user hasn't changed the date
           * while the HTTP request was running.
           */

          if (
            this.dates[mappingId] !==
            requestedDate
          ) {

            return;
          }


          const session =
            this.findSessionByDate(
              sessions,
              requestedDate
            );


          /*
           * session can be:
           *
           * OPEN
           * COMPLETED
           * CANCELLED
           * null
           *
           * null is completely normal.
           */

          this.sessions[mappingId] =
            session;


          this.cdr.markForCheck();
        },


        // ===================================================
        // HISTORY ERROR
        // ===================================================

        error: error => {

          /*
           * IMPORTANT:
           *
           * This is NOT the old
           *
           * "Attendance session not found..."
           *
           * request.
           *
           * We are only calling history here.
           */

          this.sessions[mappingId] =
            null;


          this.toast.error(
            error?.error?.message ??
            'Unable to load attendance session history.'
          );


          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // FIND SESSION BY DATE
  // =========================================================

  private findSessionByDate(
    sessions: AttendanceSession[] | null | undefined,
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
  //
  // Handles:
  //
  // 2026-08-21
  // 2026-08-21T00:00:00
  // 2026-08-21T00:00:00.000Z
  //
  // without changing timezone/date unexpectedly.
  //
  // =========================================================

  private normalizeAttendanceDate(
    value: string | null | undefined
  ): string {

    if (!value) {
      return '';
    }


    return String(value)
      .slice(0, 10);
  }


  // =========================================================
  // TAKE ATTENDANCE
  // =========================================================

  takeAttendance(
    mapping: FacultySubjectOption
  ): void {

    if (!mapping) {
      return;
    }


    const mappingId =
      Number(mapping.id);


    if (!mappingId) {

      this.toast.error(
        'Invalid class selected.'
      );

      return;
    }


    // -------------------------------------------------------
    // Verify assignment
    // -------------------------------------------------------

    const assigned =
      this.classes.some(
        item =>
          Number(item.id) === mappingId
      );


    if (!assigned) {

      this.toast.error(
        'You are not assigned to this class.'
      );

      return;
    }


    // -------------------------------------------------------
    // Selected date
    // -------------------------------------------------------

    const date =
      this.dates[mappingId] ||
      this.today;


    // -------------------------------------------------------
    // Future date protection
    // -------------------------------------------------------

    if (
      date > this.today
    ) {

      this.toast.warning(
        'Future dates are not allowed for attendance.'
      );

      return;
    }


    // -------------------------------------------------------
    // Prevent double click
    // -------------------------------------------------------

    if (
      this.openingMappingId !== 0
    ) {

      return;
    }


    this.openingMappingId =
      mappingId;

    this.cdr.markForCheck();


    // =======================================================
    // FIND SESSION FROM HISTORY
    // =======================================================

    this.service
      .getSessionHistory(
        mappingId
      )
      .subscribe({

        // ===================================================
        // HISTORY LOADED
        // ===================================================

        next: sessions => {

          /*
           * User may have changed the date while the
           * request was running.
           */

          if (
            this.dates[mappingId] !== date
          ) {

            this.openingMappingId =
              0;

            this.cdr.markForCheck();

            return;
          }


          const session =
            this.findSessionByDate(
              sessions,
              date
            );


          // =================================================
          // EXISTING SESSION
          // =================================================

          if (session) {

            this.sessions[mappingId] =
              session;

            this.openingMappingId =
              0;

            this.cdr.markForCheck();


            // ------------------------------------------------
            // OPEN SESSION
            // ------------------------------------------------

            if (
              this.isSessionOpenStatus(
                session
              )
            ) {

              this.openMarkAttendance(
                session.id
              );

              return;
            }


            // ------------------------------------------------
            // COMPLETED SESSION
            //
            // NO WARNING.
            //
            // Session remains in state so template can show:
            //
            // ✓ COMPLETED
            //
            // ------------------------------------------------

            if (
              this.isSessionCompletedStatus(session)
            ) {

              this.openCompletedSession(
                session.id
              );

              return;
            }



            // ------------------------------------------------
            // CANCELLED SESSION
            // ------------------------------------------------

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


            // ------------------------------------------------
            // UNKNOWN STATUS
            // ------------------------------------------------

            this.toast.warning(
              `Attendance session is ${session.sessionStatus ??
              'not available'
              }.`
            );

            return;
          }


          // =================================================
          // NO SESSION
          // =================================================
          //
          // This is normal.
          //
          // No error.
          // No "session not found" toast.
          //
          // Create new session.
          // =================================================

          this.createSession(
            mappingId,
            date
          );
        },


        // ===================================================
        // HISTORY ERROR
        // ===================================================

        error: error => {

          this.openingMappingId =
            0;


          this.toast.error(
            error?.error?.message ??
            'Unable to check attendance session history.'
          );


          this.cdr.markForCheck();
        }

      });
  }

  // =========================================================
  // OPEN COMPLETED SESSION
  // =========================================================

  private openCompletedSession(
  sessionId: number
): void {

  if (!sessionId) {

    this.toast.error(
      'Attendance session ID is missing.'
    );

    return;
  }

  this.router.navigate(
    [
      '/dashboard/attendance/view'
    ],
    {
      queryParams: {
        sessionId
      }
    }
  )
  .then(success => {

    if (!success) {

      this.toast.error(
        'Unable to open attendance session.'
      );

      this.cdr.markForCheck();
    }

  })
  .catch(() => {

    this.toast.error(
      'Unable to open attendance session.'
    );

    this.cdr.markForCheck();
  });
}


  // =========================================================
  // CREATE NEW ATTENDANCE SESSION
  // =========================================================

  private createSession(
    mappingId: number,
    date: string
  ): void {

    const source =
      date === this.today
        ? AttendanceSource.ONLINE
        : AttendanceSource.MANUAL_BACKDATE;


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

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        // ===================================================
        // CREATED
        // ===================================================

        next: session => {

          this.sessions[mappingId] =
            session;

          this.openingMappingId =
            0;

          this.cdr.markForCheck();


          // -------------------------------------------------
          // Validate returned session
          // -------------------------------------------------

          if (
            !session?.id
          ) {

            this.toast.error(
              'Attendance session was created but session ID was not returned.'
            );

            return;
          }


          // -------------------------------------------------
          // Open attendance marking page
          // -------------------------------------------------

          this.openMarkAttendance(
            session.id
          );
        },


        // ===================================================
        // CREATE ERROR
        // ===================================================

        error: error => {

          this.openingMappingId =
            0;


          this.toast.error(
            error?.error?.message ??
            'Unable to create attendance session.'
          );


          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // OPEN MARK ATTENDANCE
  // =========================================================

  private openMarkAttendance(
    sessionId: number
  ): void {

    if (!sessionId) {

      this.openingMappingId =
        0;

      this.toast.error(
        'Attendance session ID is missing.'
      );

      this.cdr.markForCheck();

      return;
    }


    this.router
      .navigate(
        [
          '/dashboard/attendance/mark'
        ],
        {
          queryParams: {
            sessionId
          }
        }
      )
      .then(
        success => {

          if (!success) {

            this.openingMappingId =
              0;

            this.toast.error(
              'Unable to open attendance marking page.'
            );

            this.cdr.markForCheck();
          }

        }
      )
      .catch(
        () => {

          this.openingMappingId =
            0;

          this.toast.error(
            'Unable to open attendance marking page.'
          );

          this.cdr.markForCheck();
        }
      );
  }


  // =========================================================
  // SESSION STATUS HELPERS
  // =========================================================

  isSessionOpen(
    mappingId: number
  ): boolean {

    return (
      this.sessions[mappingId]
        ?.sessionStatus === 'OPEN'
    );
  }


  isSessionCompleted(
    mappingId: number
  ): boolean {

    return (
      this.sessions[mappingId]
        ?.sessionStatus === 'COMPLETED'
    );
  }


  isSessionCancelled(
    mappingId: number
  ): boolean {

    return (
      this.sessions[mappingId]
        ?.sessionStatus === 'CANCELLED'
    );
  }


  // =========================================================
  // SESSION STATUS CHECKS
  // =========================================================

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
// VIEW COMPLETED ATTENDANCE
// =========================================================

viewAttendance(
  mappingId: number
): void {

  if (!mappingId) {

    this.toast.error(
      'Invalid class selected.'
    );

    return;
  }


  const session =
    this.sessions[mappingId];


  // -------------------------------------------------------
  // Session validation
  // -------------------------------------------------------

  if (!session?.id) {

    this.toast.error(
      'Attendance session could not be found.'
    );

    return;
  }


  // -------------------------------------------------------
  // Make sure session is completed
  // -------------------------------------------------------

  if (
    session.sessionStatus !== 'COMPLETED'
  ) {

    this.toast.warning(
      'This attendance session is not completed.'
    );

    return;
  }


  // -------------------------------------------------------
  // Navigate using SESSION ID
  // -------------------------------------------------------

  this.router.navigate(
    [
      '/dashboard/attendance/view'
    ],
    {
      queryParams: {
        sessionId: session.id
      }
    }
  )
  .then(success => {

    if (!success) {

      this.toast.error(
        'Unable to open attendance.'
      );

      this.cdr.markForCheck();
    }

  })
  .catch(() => {

    this.toast.error(
      'Unable to open attendance.'
    );

    this.cdr.markForCheck();
  });
}


  // =========================================================
  // BACK
  // =========================================================

  goBack(): void {

    this.router.navigate([
      '/dashboard/attendance'
    ]);
  }

}
