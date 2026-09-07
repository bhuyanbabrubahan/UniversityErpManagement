import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { finalize } from 'rxjs';

import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';
import { AttendanceService } from '../../services/attendance.service';
import {
  AttendanceRecord,
  AttendanceUpdateRequest,
} from '../../models/attendance.model';

@Component({
  selector: 'app-attendance-edit',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
  ],
  templateUrl: './attendance-edit.component.html',
  styleUrl: './attendance-edit.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceEditComponent implements OnInit {
  private readonly service = inject(AttendanceService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly AttendanceStatus = AttendanceStatus;

  record: AttendanceRecord | null = null;

  loading = false;
  saving = false;

  ngOnInit(): void {
    const id = Number(
      this.route.snapshot.queryParamMap.get('id') ??
        this.route.snapshot.paramMap.get('id') ??
        0,
    );

    if (!id) {
      this.toast.warning('Attendance record id is required.');
      return;
    }

    this.load(id);
  }

  load(id: number): void {
    this.loading = true;

    this.service
      .getById(id)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (record) => {
          this.record = record;
          this.cdr.markForCheck();
        },
        error: (error) => {
          this.toast.error(
            error?.error?.message ?? 'Unable to load attendance record.',
          );
        },
      });
  }

  get editable(): boolean {
    return this.record?.sessionStatus === 'OPEN';
  }

  save(): void {
    if (!this.record) {
      this.toast.warning('Attendance record not found.');
      return;
    }

    if (!this.editable) {
      this.toast.warning(
        'This attendance session is not open for editing.',
      );
      return;
    }

    const request: AttendanceUpdateRequest = {
      attendanceStatus: this.record.attendanceStatus,
      remarks: this.record.remarks?.trim() || null,
    };

    this.saving = true;

    this.service
      .update(this.record.id, request)
      .pipe(
        finalize(() => {
          this.saving = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (updatedRecord) => {
          this.record = updatedRecord;

          this.toast.success(
            'Attendance updated successfully.',
          );

          this.cdr.markForCheck();
        },
        error: (error) => {
          this.toast.error(
            error?.error?.message ?? 'Unable to update attendance.',
          );
        },
      });
  }

  back(): void {
    this.router.navigate(
      ['/dashboard/attendance/view'],
      {
        queryParams: {
          id: this.record?.id,
        },
      },
    );
  }
}
