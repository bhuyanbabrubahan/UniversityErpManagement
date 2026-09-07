import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';
import { AttendanceSummaryComponent } from '../../components/attendance-summary/attendance-summary.component';
import { AttendanceService } from '../../services/attendance.service';
import { AttendanceRecord } from '../../models/attendance.model';

@Component({
  selector: 'app-attendance-report-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
  AttendanceSummaryComponent,
  ],
  templateUrl: './attendance-report-detail.component.html',
  styleUrl: './attendance-report-detail.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AttendanceReportDetailComponent {
  private readonly service = inject(AttendanceService);
  private readonly toast = inject(ToastService);
  records: AttendanceRecord[] = [];
  get total(): number { return this.records.length; }
  get present(): number { return this.records.filter(r => String(r.attendanceStatus) === 'PRESENT').length; }
  get absent(): number { return this.records.filter(r => String(r.attendanceStatus) === 'ABSENT').length; }
  get leave(): number { return this.records.filter(r => String(r.attendanceStatus) === 'LEAVE').length; }
  ngOnInit(): void {
    const mappingId = Number(new URLSearchParams(location.search).get('mappingId') ?? 0);
    this.service.search({ facultySubjectMappingId: mappingId || null }, 0, 500, 'attendanceDate', 'desc').subscribe({
      next: page => this.records = page.content ?? [],
      error: () => this.toast.error('Unable to load report detail.')
    });
  }

  private readonly router = inject(Router);
  readonly AttendanceStatus = AttendanceStatus;
}
