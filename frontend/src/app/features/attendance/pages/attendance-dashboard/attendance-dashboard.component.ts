import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  OnInit,
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { finalize } from 'rxjs';

import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';

import { AttendanceService } from '../../services/attendance.service';

import { AttendanceRecord } from '../../models/attendance.model';

import { todayIso } from '../../shared/attendance.utils';

@Component({
  selector: 'app-attendance-dashboard',

  standalone: true,

  imports: [
    CommonModule,
    RouterModule,
  ],

  templateUrl: './attendance-dashboard.component.html',

  styleUrl: './attendance-dashboard.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceDashboardComponent
  implements OnInit
{
  // ============================================================
  // DEPENDENCIES
  // ============================================================

  private readonly service =
    inject(AttendanceService);

  private readonly toast =
    inject(ToastService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  // ============================================================
  // CONSTANTS
  // ============================================================

  readonly AttendanceStatus =
    AttendanceStatus;

  // ============================================================
  // STATE
  // ============================================================

  today =
    todayIso();

  loadingStudents = true;

  loadingAttendance = true;

  totalStudents = 0;

  totalRecordsToday = 0;

  present = 0;

  absent = 0;

  leave = 0;

  halfDay = 0;

  todayRecords: AttendanceRecord[] = [];

  // ============================================================
  // INIT
  // ============================================================

  ngOnInit(): void {
    this.loadStudents();

    this.loadTodayAttendance();
  }

  // ============================================================
  // STUDENTS
  // ============================================================

  private loadStudents(): void {
    this.loadingStudents = true;

    this.cdr.markForCheck();

    this.service
      .getStudents()
      .pipe(
        finalize(() => {
          this.loadingStudents = false;

          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (students) => {

          this.totalStudents =
            Array.isArray(students)
              ? students.length
              : 0;

          this.cdr.markForCheck();
        },

        error: (error) => {

          this.totalStudents = 0;

          this.cdr.markForCheck();
        },
      });
  }

  // ============================================================
  // TODAY ATTENDANCE
  // ============================================================

  private loadTodayAttendance(): void {
    this.loadingAttendance = true;

    this.cdr.markForCheck();

    this.service
      .search(
        {
          attendanceDate: this.today,

          status: undefined,
        },

        0,

        // Dashboard ko sirf recent 10 records chahiye.
        10000,

        'attendanceDate',

        'desc',
      )
      .pipe(
        finalize(() => {
          this.loadingAttendance = false;

          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (response) => {

          this.todayRecords =
            response?.content ?? [];

          this.calculateSummary();

          this.cdr.markForCheck();
        },

        error: (error) => {

          this.todayRecords = [];

          this.calculateSummary();

          this.toast.error(
            error?.error?.message ??
              'Unable to load today attendance.',
          );

          this.cdr.markForCheck();
        },
      });
  }

  // ============================================================
  // SUMMARY
  // ============================================================

  private calculateSummary(): void {
    this.totalRecordsToday =
      this.todayRecords.length;

    this.present =
      this.todayRecords.filter(
        (record) =>
          this.statusValue(
            record.attendanceStatus,
          ) === 'PRESENT',
      ).length;

    this.absent =
      this.todayRecords.filter(
        (record) =>
          this.statusValue(
            record.attendanceStatus,
          ) === 'ABSENT',
      ).length;

    this.leave =
      this.todayRecords.filter(
        (record) =>
          this.statusValue(
            record.attendanceStatus,
          ) === 'LEAVE',
      ).length;

    this.halfDay =
      this.todayRecords.filter(
        (record) =>
          this.statusValue(
            record.attendanceStatus,
          ) === 'HALF_DAY',
      ).length;
  }

  // ============================================================
  // STATUS
  // ============================================================

  statusValue(
    status:
      | AttendanceStatus
      | string
      | null
      | undefined,
  ): string {
    return String(status ?? '')
      .trim()
      .toUpperCase()
      .replace(/[\s-]+/g, '_');
  }

  // ============================================================
  // DASHBOARD LOADING
  // ============================================================

  get dashboardLoading(): boolean {
    return (
      this.loadingStudents ||
      this.loadingAttendance
    );
  }

  // ============================================================
  // REFRESH
  // ============================================================

  refresh(): void {
    this.loadStudents();

    this.loadTodayAttendance();
  }
}
