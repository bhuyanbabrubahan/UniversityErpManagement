import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';

@Component({
  selector: 'app-attendance-preview',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
],
  templateUrl: './attendance-preview.component.html',
  styleUrl: './attendance-preview.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AttendancePreviewComponent {
  readonly note = 'Preview is available before final submission from the Mark Attendance screen.';
  private readonly router = inject(Router);
  readonly AttendanceStatus = AttendanceStatus;
}
