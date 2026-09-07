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
  AttendanceStatus
} from '@app/shared/enums/attendance-status.enum';

import {
  AttendanceSource
} from '@app/shared/enums/attendance-source.enum';

import {
  AttendanceService
} from '../../services/attendance.service';

import {
  AttendanceStudentRow,
  AttendanceSession
} from '../../models/attendance.model';


@Component({
  selector: 'app-mark-attendance',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],

  templateUrl:
    './mark-attendance.component.html',

  styleUrl:
    './mark-attendance.component.scss',

  changeDetection:
    ChangeDetectionStrategy.OnPush
})
export class MarkAttendanceComponent
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

  students:
    AttendanceStudentRow[] = [];

  session:
    AttendanceSession | null = null;

  sessionId = 0;

  mappingId = 0;

  attendanceDate =
    new Date()
      .toISOString()
      .slice(0, 10);

  loading = false;


  // =========================================================
  // ENUMS
  // =========================================================

  readonly AttendanceStatus =
    AttendanceStatus;

  readonly AttendanceSource =
    AttendanceSource;


  // =========================================================
  // TODAY
  // =========================================================

  readonly today =
    new Date()
      .toISOString()
      .slice(0, 10);


  // =========================================================
  // SUMMARY
  // =========================================================

  get present(): number {

    return this.students.filter(
      student =>
        this.statusValue(
          student.attendanceStatus
        ) === AttendanceStatus.PRESENT
    ).length;
  }


  get absent(): number {

    return this.students.filter(
      student =>
        this.statusValue(
          student.attendanceStatus
        ) === AttendanceStatus.ABSENT
    ).length;
  }


  get leave(): number {

    return this.students.filter(
      student =>
        this.statusValue(
          student.attendanceStatus
        ) === AttendanceStatus.LEAVE
    ).length;
  }


  get halfDay(): number {

    return this.students.filter(
      student =>
        this.statusValue(
          student.attendanceStatus
        ) === AttendanceStatus.HALF_DAY
    ).length;
  }


  get isEditable(): boolean {

    return (
      this.session?.sessionStatus === 'OPEN'
    );
  }


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    const params =
      new URLSearchParams(
        window.location.search
      );


    this.sessionId =
      Number(
        params.get('sessionId') ?? 0
      );


    // -------------------------------------------------------
    // sessionId is REQUIRED on this page.
    // -------------------------------------------------------

    if (!this.sessionId) {

      this.toast.error(
        'Attendance session ID is missing.'
      );

      this.back();

      return;
    }


    // -------------------------------------------------------
    // Load session.
    //
    // Backend checks that session belongs to
    // authenticated faculty.
    // -------------------------------------------------------

    this.loadSession();
  }


  // =========================================================
  // LOAD SESSION
  // =========================================================

  private loadSession(): void {

    this.loading = true;


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

        next: session => {

          if (!session?.id) {

            this.toast.error(
              'Invalid attendance session.'
            );

            this.back();

            return;
          }


          this.session =
            session;


          this.mappingId =
            session.facultySubjectMappingId ??
            0;


          this.attendanceDate =
            session.attendanceDate ??
            this.today;


          // -------------------------------------------------
          // Future date protection
          // -------------------------------------------------

          if (
            this.attendanceDate >
            this.today
          ) {

            this.toast.warning(
              'Future dates are not allowed for attendance.'
            );

            this.back();

            return;
          }


          // -------------------------------------------------
          // Load students
          // -------------------------------------------------

          this.loadStudents();
        },


        error: error => {

          this.session = null;

          this.students = [];


          this.toast.error(
            error?.error?.message ??
            'Unable to load attendance session.'
          );


          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // LOAD STUDENTS
  // =========================================================

  loadStudents(): void {

    if (!this.sessionId) {

      return;
    }


    this.loading = true;


    this.service
      .getClassAttendance({

        attendanceSessionId:
          this.sessionId

      })
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        next: response => {

          this.students =
            (response?.students ?? [])
              .map(student => ({

                studentId:
                  student.studentId,

                rollNumber:
                  student.rollNumber ?? '',

                studentName:
                  student.studentName ??
                  'Unknown Student',

                departmentName:
                  student.departmentName,

                courseName:
                  student.courseName,

                semesterName:
                  student.semesterName,

                section:
                  student.section,

                batch:
                  student.batch,

                attendanceStatus:
                  student.attendanceStatus ??
                  AttendanceStatus.PRESENT,

                attendanceSource:
                  response.attendanceSource,

                remarks:
                  student.remarks ?? '',

                attendanceId:
                  student.attendanceId

              }));


          // -------------------------------------------------
          // Synchronize session state
          // -------------------------------------------------

          if (this.session) {

            this.session = {

              ...this.session,

              sessionStatus:
                response.sessionStatus ??
                this.session.sessionStatus,

              attendanceSource:
                response.attendanceSource ??
                this.session.attendanceSource,

              attendanceDate:
                response.attendanceDate ??
                this.session.attendanceDate

            };
          }


          this.cdr.markForCheck();
        },


        error: error => {

          this.students = [];


          this.toast.error(
            error?.error?.message ??
            'Unable to load students.'
          );


          this.cdr.markForCheck();
        }

      });
  }


  // =========================================================
  // STATUS NORMALIZER
  // =========================================================

  statusValue(
    status:
      AttendanceStatus |
      string |
      null |
      undefined
  ): string {

    return String(
      status ?? ''
    )
      .trim()
      .toUpperCase()
      .replace(
        /[\s-]+/g,
        '_'
      );
  }


  // =========================================================
  // MARK ALL
  // =========================================================

  setAll(
    status: AttendanceStatus
  ): void {

    if (!this.isEditable) {
      return;
    }


    this.students =
      this.students.map(
        student => ({

          ...student,

          attendanceStatus:
            status

        })
      );


    this.cdr.markForCheck();
  }


  // =========================================================
  // TOGGLE PRESENT / ABSENT
  // =========================================================

  toggle(
    row: AttendanceStudentRow
  ): void {

    if (!this.isEditable) {
      return;
    }


    const current =
      this.statusValue(
        row.attendanceStatus
      );


    row.attendanceStatus =
      current ===
      AttendanceStatus.PRESENT

        ? AttendanceStatus.ABSENT

        : AttendanceStatus.PRESENT;


    this.cdr.markForCheck();
  }


  // =========================================================
  // SET STATUS
  // =========================================================

  setStatus(
    row: AttendanceStudentRow,
    status: AttendanceStatus
  ): void {

    if (!this.isEditable) {
      return;
    }


    row.attendanceStatus =
      status;


    this.cdr.markForCheck();
  }


  // =========================================================
  // SAVE
  // =========================================================

  submit(): void {

    if (
      !this.sessionId ||
      !this.students.length ||
      !this.isEditable
    ) {

      this.toast.warning(
        'Attendance session is not open for modification.'
      );

      return;
    }


    this.loading = true;


    this.service
      .saveBulk({

        attendanceSessionId:
          this.sessionId,

        attendanceRecords:
          this.students.map(
            student => ({

              studentId:
                student.studentId,

              attendanceStatus:
                student.attendanceStatus,

              remarks:
                student.remarks?.trim() ||
                null

            })
          )

      })
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        next: () => {

          this.toast.success(
            'Attendance saved successfully.'
          );


          this.loadStudents();
        },


        error: error => {

          this.toast.error(
            error?.error?.message ??
            'Attendance could not be saved.'
          );
        }

      });
  }


  // =========================================================
  // COMPLETE SESSION
  // =========================================================

  completeSession(): void {

    if (
      !this.sessionId ||
      !this.isEditable
    ) {

      return;
    }


    this.loading = true;


    this.service
      .completeSession(
        this.sessionId
      )
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        next: session => {

          this.session =
            session;


          this.toast.success(
            'Attendance session completed.'
          );


          this.loadStudents();
        },


        error: error => {

          this.toast.error(
            error?.error?.message ??
            'Unable to complete session.'
          );
        }

      });
  }


  // =========================================================
  // CANCEL SESSION
  // =========================================================

  cancelSession(): void {

    if (
      !this.sessionId ||
      !this.isEditable
    ) {

      return;
    }


    this.loading = true;


    this.service
      .cancelSession(
        this.sessionId
      )
      .pipe(

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();
        })

      )
      .subscribe({

        next: session => {

          this.session =
            session;


          this.toast.success(
            'Attendance session cancelled.'
          );


          this.loadStudents();
        },


        error: error => {

          this.toast.error(
            error?.error?.message ??
            'Unable to cancel session.'
          );
        }

      });
  }


  // =========================================================
  // BACK
  // =========================================================

  back(): void {

    this.router.navigate([
      '/dashboard/attendance/classes'
    ]);
  }

}
