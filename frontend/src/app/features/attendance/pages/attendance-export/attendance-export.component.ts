import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router, RouterModule } from "@angular/router";
import { finalize } from "rxjs";

import { AttendanceService } from "../../services/attendance.service";
import { ToastService } from "@app/shared/services/toast.service";

@Component({
  selector: "app-attendance-export",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./attendance-export.component.html",
  styleUrl: "./attendance-export.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceExportComponent {
  private readonly attendanceService = inject(AttendanceService);

  private readonly toast = inject(ToastService);

  private readonly router = inject(Router);

  date = "";
  loading = false;

  /**

Navigate back to attendance dashboard.
*/
  back(): void {
    if (this.loading) {
      return;
    }
    this.router.navigate(["/dashboard/attendance"]);
  }

  /**

Export attendance report.
*/
  export(format: "xlsx" | "pdf"): void {
    if (this.loading) {
      return;
    }

    this.loading = true;

    this.attendanceService
      .exportReport(format, this.date || null)
      .pipe(
        finalize(() => {
          this.loading = false;
        }),
      )
      .subscribe({
        next: (blob: Blob) => {
          this.downloadFile(blob, format);

          this.toast.success(
            `Attendance ${format.toUpperCase()} downloaded successfully.`,
          );
        },

        error: async (error) => {
          let message = "";

          if (error?.error instanceof Blob) {
            try {
              message = await error.error.text();
            } catch {
              message = "";
            }
          }

          this.toast.error(
            message || "Unable to export attendance. Please try again.",
          );
        },
      });
  }

  /**

Create browser download.
*/
  private downloadFile(blob: Blob, format: "xlsx" | "pdf"): void {
    const url = URL.createObjectURL(blob);

    const anchor = document.createElement("a");

    anchor.href = url;

    anchor.download = `attendance-${this.date || "all"}.${format}`;

    anchor.style.display = "none";

    document.body.appendChild(anchor);

    anchor.click();

    document.body.removeChild(anchor);

    /*
     * Give the browser enough time to start the download
     * before releasing the object URL.
     */
    setTimeout(() => {
      URL.revokeObjectURL(url);
    }, 1000);
  }
}
