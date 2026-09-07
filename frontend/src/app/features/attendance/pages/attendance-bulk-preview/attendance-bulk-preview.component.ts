import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { finalize, switchMap } from 'rxjs';

import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';
import { ToastService } from '@app/shared/services/toast.service';

import { AttendanceService } from '../../services/attendance.service';
import { AttendanceExcelService } from '../../services/attendance-excel.service';
import { AttendanceStudentRow } from '../../models/attendance.model';

// Adjust this path according to your project structure.
import { AttendanceSummaryComponent } from '../../components/attendance-summary/attendance-summary.component';

@Component({
  selector: 'app-attendance-bulk-preview',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    AttendanceSummaryComponent,
  ],
  templateUrl: './attendance-bulk-preview.component.html',
  styleUrl: './attendance-bulk-preview.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceBulkPreviewComponent {
  private readonly service = inject(AttendanceService);
  private readonly excel = inject(AttendanceExcelService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly AttendanceStatus = AttendanceStatus;

  private readonly state = history.state ?? {};

  rows: AttendanceStudentRow[] = this.state.rows ?? [];

  sessionId = Number(this.state.sessionId ?? 0);

  mappingId = Number(this.state.mappingId ?? 0);

  attendanceDate = this.state.attendanceDate ?? '';

  facultyName = this.state.facultyName ?? '';

  subjectName = this.state.subjectName ?? '';

  className = this.state.className ?? '';

  sectionName = this.state.sectionName ?? '';

  academicYear = this.state.academicYear ?? '';

  loading = false;

  get present(): number {
    return this.countByStatus(AttendanceStatus.PRESENT);
  }

  get absent(): number {
    return this.countByStatus(AttendanceStatus.ABSENT);
  }

  get leave(): number {
    return this.countByStatus(AttendanceStatus.LEAVE);
  }

  get halfDay(): number {
    return this.countByStatus(AttendanceStatus.HALF_DAY);
  }

  private countByStatus(status: AttendanceStatus): number {
    return this.rows.filter(
      (row) => this.statusValue(row.attendanceStatus) === status,
    ).length;
  }

  statusValue(status: unknown): string {
    return String(status ?? '')
      .trim()
      .toUpperCase()
      .replace(/[\s-]+/g, '_');
  }

  statusLabel(status: unknown): string {
    return (
      this.statusValue(status)
        .replaceAll('_', ' ')
        .toLowerCase()
        .replace(/\b\w/g, (character) => character.toUpperCase()) || '—'
    );
  }

  sourceLabel(source: unknown): string {
    return (
      String(source ?? '')
        .replaceAll('_', ' ')
        .toLowerCase()
        .replace(/\b\w/g, (character) => character.toUpperCase()) || '—'
    );
  }

  studentName(row: AttendanceStudentRow): string {
    return row.studentName || 'Unknown Student';
  }

  studentInitial(row: AttendanceStudentRow): string {
    return this.studentName(row).charAt(0).toUpperCase();
  }

  submit(): void {
    if (!this.sessionId || !this.mappingId || !this.attendanceDate || !this.rows.length) {
      this.toast.warning('Attendance session, class, date and Excel rows are required.');
      return;
    }

    const allowed = new Set<string>([
      AttendanceStatus.PRESENT,
      AttendanceStatus.ABSENT,
      AttendanceStatus.HALF_DAY,
      AttendanceStatus.LEAVE
    ]);
    const ids = this.rows.map(row => Number(row.studentId));
    const duplicateIds = ids.filter((id, index) => ids.indexOf(id) !== index);
    const invalidStatus = this.rows.some(row => !allowed.has(this.statusValue(row.attendanceStatus)));

    if (ids.some(id => !Number.isInteger(id) || id <= 0) || duplicateIds.length || invalidStatus) {
      this.toast.error('Excel contains invalid student IDs, duplicate students, or unsupported attendance status.');
      return;
    }

    this.loading = true;

    const request = {
      attendanceSessionId: this.sessionId,
      attendanceRecords: this.rows.map((row) => ({
        studentId: row.studentId,
        attendanceStatus: row.attendanceStatus,
        remarks: row.remarks?.trim() || null,
      })),
    };

    this.service
      .getClassAttendance({ attendanceSessionId: this.sessionId })
      .pipe(
        switchMap(classData => {
          const eligible = new Set((classData?.students ?? []).map(student => Number(student.studentId)));
          const missing = (classData?.students ?? []).filter(student => !ids.includes(Number(student.studentId)));
          const unknown = ids.filter(id => !eligible.has(id));

          if (unknown.length || missing.length) {
            throw new Error(
              `Excel must match the selected class. ${unknown.length} unknown and ${missing.length} missing student record(s).`
            );
          }

          return this.service.saveBulk(request);
        }),
        finalize(() => {
          this.loading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: () => {
          this.toast.success(
            'Bulk attendance saved successfully.',
          );

          this.router.navigate(
            ['/dashboard/attendance/mark'],
            {
              queryParams: {
                sessionId: this.sessionId,
              },
            },
          );
        },
        error: (error) => {
          this.toast.error(
            error?.error?.message ??
              error?.message ??
              'Bulk attendance could not be saved.',
          );
        },
      });
  }

  download(): void {
    this.excel.downloadTemplate();
  }

  cancel(): void {
    this.back();
  }

  back(): void {
    this.router.navigate(
      ['/dashboard/attendance/bulk-upload'],
      {
        queryParams: {
          sessionId: this.sessionId,
        },
      },
    );
  }
}
