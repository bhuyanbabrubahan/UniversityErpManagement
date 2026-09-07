import { CommonModule } from "@angular/common";
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
} from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router, RouterModule } from "@angular/router";

import { ToastService } from "@app/shared/services/toast.service";
import { AttendanceService } from "../../services/attendance.service";
import {
  AttendanceSession,
  FacultySubjectOption,
} from "../../models/attendance.model";

@Component({
  selector: "app-attendance-session-history",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./attendance-session-history.component.html",
  styleUrl: "./attendance-session-history.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceSessionHistoryComponent implements OnInit {
  private readonly service = inject(AttendanceService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  mappings: FacultySubjectOption[] = [];
  sessions: AttendanceSession[] = [];

  mappingId = 0;
  loading = false;
  subjectsLoading = false;

  ngOnInit(): void {
    this.loadMappings();
  }

  private loadMappings(): void {
    this.subjectsLoading = true;

    this.service.getFacultySubjects().subscribe({
      next: (mappings) => {
        this.mappings = Array.isArray(mappings) ? mappings : [];
        this.subjectsLoading = false;

        /*
         * Do not automatically call load().
         * User must select a subject first.
         */
        this.cdr.markForCheck();
      },

      error: (error) => {
        this.mappings = [];
        this.subjectsLoading = false;

        this.toast.error(
          error?.error?.message ?? "Unable to load assigned subjects.",
        );

        this.cdr.markForCheck();
      },
    });
  }

  load(): void {
    const selectedMappingId = Number(this.mappingId);

    if (!Number.isInteger(selectedMappingId) || selectedMappingId <= 0) {
      this.sessions = [];
      this.mappingId = 0;
      this.cdr.markForCheck();
      return;
    }

    this.mappingId = selectedMappingId;
    this.loading = true;
    this.sessions = [];

    this.service.getSessionHistory(selectedMappingId).subscribe({
      next: (sessions) => {
        this.sessions = Array.isArray(sessions) ? sessions : [];

        this.loading = false;
        this.cdr.markForCheck();
      },

      error: (error) => {
        this.sessions = [];
        this.loading = false;

        this.toast.error(
          error?.error?.message ?? "Unable to load session history.",
        );

        this.cdr.markForCheck();
      },
    });
  }

  /*
   * IMPORTANT:
   *
   * s.id = Attendance SESSION ID
   *
   * Therefore View page must receive:
   * ?sessionId=<session id>
   *
   * Do NOT send:
   * ?id=<attendance record id>
   */
  open(session: AttendanceSession): void {
    const sessionId = Number(session?.id);

    if (!Number.isInteger(sessionId) || sessionId <= 0) {
      this.toast.error("Attendance session id is missing.");
      return;
    }

    this.router.navigate(["/dashboard/attendance/view"], {
      queryParams: {
        sessionId,
      },
    });
  }

  trackBySessionId(index: number, session: AttendanceSession): number | string {
    return session?.id ?? index;
  }
}
