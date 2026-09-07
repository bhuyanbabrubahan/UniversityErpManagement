import { CommonModule } from "@angular/common";
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";

import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";

@Component({
  selector: "app-attendance-session-view",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
  ],
  templateUrl: "./attendance-view.component.html",
  styleUrl: "./attendance-view.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceViewComponent implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly snack = inject(MatSnackBar);
  private readonly cdr = inject(ChangeDetectorRef);

  sessionId = 0;

  loading = true;
  resolvingId = false;

  session: any = null;
  records: any[] = [];

  errorMessage = "";

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      const sessionIdParam = params.get("sessionId");
      const attendanceIdParam = params.get("id");

      /*
       * NEW / CORRECT FLOW
       *
       * /attendance/view?sessionId=4
       *
       * Directly load session 4.
       */
      if (sessionIdParam) {
        const sessionId = Number(sessionIdParam);

        if (Number.isInteger(sessionId) && sessionId > 0) {
          this.sessionId = sessionId;
          this.loadSession();
          return;
        }

        this.showError("Invalid attendance session id.");
        return;
      }

      /*
       * BACKWARD COMPATIBILITY
       *
       * Old URL:
       * /attendance/view?id=7
       *
       * Here 7 is Attendance ID, not Session ID.
       *
       * So first:
       * /api/attendances/7
       *
       * response:
       * attendanceSessionId = 4
       *
       * then:
       * /api/attendance-sessions/4/details
       */
      if (attendanceIdParam) {
        const attendanceId = Number(attendanceIdParam);

        if (Number.isInteger(attendanceId) && attendanceId > 0) {
          this.resolveAttendanceRecord(attendanceId);
          return;
        }

        this.showError("Invalid attendance id.");
        return;
      }

      this.showError("Attendance session id is required.");
    });
  }

  /**
   * Resolve Attendance ID -> Attendance Session ID
   *
   * Example:
   *
   * attendance id = 7
   * attendanceSessionId = 4
   */
  private resolveAttendanceRecord(attendanceId: number): void {
    this.loading = true;
    this.resolvingId = true;

    this.errorMessage = "";
    this.session = null;
    this.records = [];

    this.cdr.markForCheck();

    this.http.get<any>(`/api/attendances/${attendanceId}`).subscribe({
      next: (response) => {
        const attendance = response?.data ?? response;

        const resolvedSessionId = Number(attendance?.attendanceSessionId);

        if (!Number.isInteger(resolvedSessionId) || resolvedSessionId <= 0) {
          this.resolvingId = false;
          this.loading = false;

          this.errorMessage = `Attendance record #${attendanceId} does not contain a valid attendance session id.`;

          this.snack.open(this.errorMessage, "Close", {
            duration: 4500,
          });

          this.cdr.markForCheck();
          return;
        }

        this.sessionId = resolvedSessionId;

        this.resolvingId = false;

        /*
         * Now ONLY session details request.
         *
         * /api/attendance-sessions/4/details
         */
        this.loadSession();
      },

      error: (error) => {
        this.resolvingId = false;
        this.handleHttpError(error, attendanceId, "attendance");
      },
    });
  }

  /**
   * Load complete session details.
   */
  loadSession(): void {
    if (!Number.isInteger(this.sessionId) || this.sessionId <= 0) {
      this.showError("Attendance session id is required.");
      return;
    }

    this.loading = true;
    this.errorMessage = "";

    this.session = null;
    this.records = [];

    this.cdr.markForCheck();

    this.http
      .get<any>(`/api/attendance-sessions/${this.sessionId}/details`)
      .subscribe({
        next: (response) => {
          const data = response?.data ?? response;

          this.session = data?.session ?? null;

          this.records = Array.isArray(data?.attendance) ? data.attendance : [];

          this.loading = false;

          if (!this.session) {
            this.errorMessage = `Attendance session not found with id : ${this.sessionId}`;

            this.snack.open(this.errorMessage, "Close", {
              duration: 4000,
            });
          }

          this.cdr.markForCheck();
        },

        error: (error) => {
          this.handleHttpError(error, this.sessionId, "session");
        },
      });
  }

  /**
   * HTTP error handling.
   */
  private handleHttpError(
    error: any,
    id: number,
    type: "attendance" | "session",
  ): void {
    this.loading = false;

    this.session = null;
    this.records = [];

    if (error?.status === 404) {
      if (type === "attendance") {
        this.errorMessage =
          error?.error?.message ??
          `Attendance record not found with id : ${id}`;
      } else {
        this.errorMessage =
          error?.error?.message ??
          `Attendance session not found with id : ${id}`;
      }
    } else if (error?.status === 403) {
      this.errorMessage =
        error?.error?.message ??
        "You are not authorized to view this attendance session.";
    } else if (error?.status === 401) {
      this.errorMessage = "Your login session has expired. Please login again.";
    } else {
      this.errorMessage =
        error?.error?.message ??
        error?.error?.error ??
        error?.message ??
        "Unable to load attendance session.";
    }

    this.snack.open(this.errorMessage, "Close", {
      duration: 4500,
    });

    this.cdr.markForCheck();
  }

  private showError(message: string): void {
    this.loading = false;
    this.resolvingId = false;

    this.session = null;
    this.records = [];

    this.errorMessage = message;

    this.snack.open(message, "Close", {
      duration: 3500,
    });

    this.cdr.markForCheck();
  }

  /**
   * Export current attendance session.
   */
  download(format: "pdf" | "xlsx"): void {
    if (!this.sessionId || !this.session) {
      this.snack.open("Attendance session is not loaded.", "Close", {
        duration: 3000,
      });
      return;
    }

    const url = `/api/attendance-reports/session/${this.sessionId}/export?format=${format}`;

    window.open(url, "_blank", "noopener,noreferrer");
  }

  back(): void {
    this.router.navigate(["/dashboard/attendance/sessions"]);
  }

  retry(): void {
    if (this.sessionId > 0 && !this.resolvingId) {
      this.loadSession();
    }
  }

  get total(): number {
    return this.records.length;
  }

  get present(): number {
    return this.records.filter(
      (record) => record?.attendanceStatus === "PRESENT",
    ).length;
  }

  get absent(): number {
    return this.records.filter(
      (record) => record?.attendanceStatus === "ABSENT",
    ).length;
  }

  get leave(): number {
    return this.records.filter((record) => record?.attendanceStatus === "LEAVE")
      .length;
  }

  get halfDay(): number {
    return this.records.filter(
      (record) => record?.attendanceStatus === "HALF_DAY",
    ).length;
  }

  get attendancePercentage(): number {
    if (!this.total) {
      return 0;
    }

    return Math.round((this.present / this.total) * 100);
  }

  trackByRecordId(index: number, record: any): number | string {
    return record?.id ?? index;
  }
}
