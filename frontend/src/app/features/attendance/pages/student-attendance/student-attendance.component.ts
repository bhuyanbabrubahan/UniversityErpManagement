import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { AttendanceSummaryComponent } from '../../components/attendance-summary/attendance-summary.component';
import { AttendanceService } from '../../services/attendance.service';
import { AttendanceRecord } from '../../models/attendance.model';
import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';

@Component({
  selector: 'app-student-attendance',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
  AttendanceSummaryComponent,
  ],
  templateUrl: './student-attendance.component.html',
  styleUrl: './student-attendance.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudentAttendanceComponent {
  private readonly service = inject(AttendanceService);
  private readonly toast = inject(ToastService);
  records: AttendanceRecord[] = [];
  loading = true;
  get total(): number { return this.records.length; }
  get present(): number { return this.records.filter(r => String(r.attendanceStatus) === 'PRESENT').length; }
  get absent(): number { return this.records.filter(r => String(r.attendanceStatus) === 'ABSENT').length; }
  get leave(): number { return this.records.filter(r => String(r.attendanceStatus) === 'LEAVE').length; }
  ngOnInit(): void {
    const key = 'student';
    const id = Number(new URLSearchParams(location.search).get('id') ?? 0);
    const request = key === 'student' ? { studentId: id || null } : { subjectId: id || null };
    this.service.search(request, 0, 500, 'attendanceDate', 'desc').subscribe({
      next: page => { this.records = page.content ?? []; this.loading = false; },
      error: () => { this.loading = false; this.toast.error('Unable to load attendance.'); }
    });
  }

  private readonly router = inject(Router);
  readonly AttendanceStatus = AttendanceStatus;
}
