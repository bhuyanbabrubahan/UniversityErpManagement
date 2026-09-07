import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ToastService } from '@app/shared/services/toast.service';
import { AttendanceStatus } from '@app/shared/enums/attendance-status.enum';

@Component({
  selector: 'app-attendance-success',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
  ],
  templateUrl: './attendance-success.component.html',
  styleUrl: './attendance-success.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AttendanceSuccessComponent {
  goToHistory(): void { this.router.navigate(['/dashboard/attendance/history']); }
  markAnother(): void { this.router.navigate(['/dashboard/attendance/mark']); }

  private readonly router = inject(Router);
  readonly AttendanceStatus = AttendanceStatus;
}
