import { CommonModule } from "@angular/common";
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
} from "@angular/core";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { ToastService } from "@app/shared/services/toast.service";
import { AttendanceStatus } from "@app/shared/enums/attendance-status.enum";

import { AttendanceService } from "../../services/attendance.service";
import { AttendanceRecord } from "../../models/attendance.model";

@Component({
  selector: "app-attendance-report",
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: "./attendance-report.component.html",
  styleUrl: "./attendance-report.component.scss",
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AttendanceReportComponent {
  private readonly service = inject(AttendanceService);
  private readonly toast = inject(ToastService);
  private readonly cdr = inject(ChangeDetectorRef);

  records: AttendanceRecord[] = [];

  date = "";

  loading = false;
  hasGenerated = false;

  // ============================================================
  // PAGINATION
  // ============================================================

  pageIndex = 0;

  pageSize = 5;

  readonly pageSizeOptions = [5, 10, 15, 20];

  totalElements = 0;

  totalPages = 0;

  // ============================================================
  // SUMMARY COUNTS
  // ============================================================

  total = 0;
  present = 0;
  absent = 0;
  leave = 0;
  halfDay = 0;

  readonly AttendanceStatus = AttendanceStatus;

  // ============================================================
  // GENERATE REPORT
  // ============================================================

  run(): void {
    this.pageIndex = 0;

    this.loadReport();
  }

  // ============================================================
  // LOAD REPORT
  // ============================================================

  private loadReport(): void {
    this.loading = true;

    this.hasGenerated = false;

    this.cdr.markForCheck();

    this.service
      .search(
        {
          attendanceDate: this.date || null,
        },
        this.pageIndex,
        this.pageSize,
        "attendanceDate",
        "desc",
      )
      .subscribe({
        next: (page) => {
          this.records = page?.content ?? [];

          this.totalElements = page?.totalElements ?? 0;

          this.totalPages = page?.totalPages ?? 0;

          this.calculateSummary();

          this.loading = false;

          this.hasGenerated = true;

          this.cdr.markForCheck();
        },

        error: (error) => {
          console.error("[AttendanceReport] Load error:", error);

          this.records = [];

          this.resetSummary();

          this.totalElements = 0;

          this.totalPages = 0;

          this.loading = false;

          this.hasGenerated = true;

          this.toast.error(
            error?.error?.message ?? "Unable to generate attendance report.",
          );

          this.cdr.markForCheck();
        },
      });
  }

  // ============================================================
  // CHANGE PAGE SIZE
  // ============================================================

  changePageSize(): void {
    this.pageIndex = 0;

    this.loadReport();
  }

  // ============================================================
  // PREVIOUS PAGE
  // ============================================================

  previousPage(): void {
    if (this.loading || this.pageIndex <= 0) {
      return;
    }

    this.pageIndex--;

    this.loadReport();
  }

  // ============================================================
  // NEXT PAGE
  // ============================================================

  nextPage(): void {
    if (this.loading || this.pageIndex >= this.totalPages - 1) {
      return;
    }

    this.pageIndex++;

    this.loadReport();
  }

  // ============================================================
  // CURRENT PAGE
  // ============================================================

  get currentPage(): number {
    return this.totalPages > 0 ? this.pageIndex + 1 : 0;
  }

  // ============================================================
  // CALCULATE SUMMARY
  // ============================================================

  private calculateSummary(): void {
    this.total = this.totalElements;

    this.present = this.records.filter(
      (record) => this.statusValue(record.attendanceStatus) === "PRESENT",
    ).length;

    this.absent = this.records.filter(
      (record) => this.statusValue(record.attendanceStatus) === "ABSENT",
    ).length;

    this.leave = this.records.filter(
      (record) => this.statusValue(record.attendanceStatus) === "LEAVE",
    ).length;

    this.halfDay = this.records.filter(
      (record) => this.statusValue(record.attendanceStatus) === "HALF_DAY",
    ).length;
  }

  // ============================================================
  // NORMALIZE STATUS
  // ============================================================

  statusValue(status: AttendanceStatus | string | null | undefined): string {
    return String(status ?? "")
      .trim()
      .toUpperCase()
      .replace(/[\s-]+/g, "_");
  }

  // ============================================================
  // RESET
  // ============================================================

  private resetSummary(): void {
    this.total = 0;
    this.present = 0;
    this.absent = 0;
    this.leave = 0;
    this.halfDay = 0;
  }

  // ============================================================
  // CLEAR FILTER
  // ============================================================

  clear(): void {
    this.date = "";

    this.records = [];

    this.resetSummary();

    this.totalElements = 0;

    this.totalPages = 0;

    this.pageIndex = 0;

    this.hasGenerated = false;

    this.cdr.markForCheck();
  }
}
