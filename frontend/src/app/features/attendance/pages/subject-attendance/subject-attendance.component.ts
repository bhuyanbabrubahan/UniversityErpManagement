import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  inject
} from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import { AttendanceSummaryComponent } from '../../components/attendance-summary/attendance-summary.component';
import { AttendanceService } from '../../services/attendance.service';
import { AttendanceRecord } from '../../models/attendance.model';

import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';

@Component({
  selector: 'app-subject-attendance',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
],
  templateUrl: './subject-attendance.component.html',
  styleUrl: './subject-attendance.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SubjectAttendanceComponent {

  private readonly service = inject(AttendanceService);
  private readonly toast = inject(ToastService);
  private readonly route = inject(ActivatedRoute);

  records: AttendanceRecord[] = [];
  loading = true;

  readonly AttendanceStatus = AttendanceStatus;

  ngOnInit(): void {
    const id = Number(
      this.route.snapshot.queryParamMap.get('id') ?? 0
    );

    if (!id) {
      this.loading = false;
      this.toast.warning('Subject ID is required.');
      return;
    }

    this.loadAttendance(id);
  }

  private loadAttendance(subjectId: number): void {
    this.loading = true;

    this.service.search(
      {
        subjectId
      },
      0,
      500,
      'attendanceDate',
      'desc'
    ).subscribe({
      next: page => {
        this.records = page.content ?? [];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.toast.error('Unable to load subject attendance.');
      }
    });
  }
}