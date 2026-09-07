import { Injectable } from "@angular/core";

import { AttendanceStudentRow } from "../models/attendance.model";

import { AttendanceSource } from "../../../shared/enums/attendance-source.enum";

import { AttendanceStatus } from "../../../shared/enums/attendance-status.enum";

import { ATTENDANCE_EXCEL_HEADERS } from "../shared/attendance.constants";

/**
 * SheetJS is intentionally isolated here.
 *
 * Excel parsing/export remains outside attendance pages.
 */
@Injectable({
  providedIn: "root",
})
export class AttendanceExcelService {
  /* ============================================================
     READ EXCEL
  ============================================================ */

  async read(file: File): Promise<AttendanceStudentRow[]> {
    const XLSX = await import("xlsx");

    const buffer = await file.arrayBuffer();

    const workbook = XLSX.read(buffer, {
      type: "array",
    });

    if (!workbook.SheetNames.length) {
      return [];
    }

    const sheet = workbook.Sheets[workbook.SheetNames[0]];

    const rows = XLSX.utils.sheet_to_json<Record<string, unknown>>(sheet, {
      defval: "",
    });

    return (
      rows

        .map((row) => {
          const rawStatus = String(
            row["attendanceStatus"] ?? row["status"] ?? row["Attendance"] ?? "",
          )
            .trim()
            .toUpperCase();

          let attendanceStatus: AttendanceStatus;

          switch (rawStatus) {
            case AttendanceStatus.PRESENT:
              attendanceStatus = AttendanceStatus.PRESENT;

              break;

            case AttendanceStatus.ABSENT:
              attendanceStatus = AttendanceStatus.ABSENT;

              break;

            case AttendanceStatus.HALF_DAY:
              attendanceStatus = AttendanceStatus.HALF_DAY;

              break;

            case AttendanceStatus.LEAVE:
              attendanceStatus = AttendanceStatus.LEAVE;

              break;

            default:
              /*
               * Invalid attendance status.
               *
               * This row will be removed by the
               * type-safe filter below.
               */
              return null;
          }

          const attendanceRow: AttendanceStudentRow = {
            studentId: Number(
              row["studentId"] ?? row["Student ID"] ?? row["studentID"],
            ),

            rollNumber: String(
              row["rollNumber"] ?? row["rollNo"] ?? row["Roll Number"] ?? "",
            ).trim(),

            studentName: String(
              row["studentName"] ?? row["Student Name"] ?? "",
            ).trim(),

            attendanceStatus,

            /*
             * Excel attendance always comes from import.
             */
            attendanceSource: AttendanceSource.EXCEL_IMPORT,

            remarks: String(row["remarks"] ?? row["Remarks"] ?? "").trim(),
          };

          return attendanceRow;
        })

        /*
         * Type guard.
         *
         * Removes null rows and tells TypeScript
         * that the remaining rows are AttendanceStudentRow.
         */
        .filter(
          (row): row is AttendanceStudentRow =>
            row !== null && Number.isFinite(row.studentId) && row.studentId > 0,
        )
    );
  }

  /* ============================================================
     DOWNLOAD TEMPLATE
  ============================================================ */

  downloadTemplate(): void {
    this.downloadRows(
      "attendance-template.xlsx",

      [
        {
          studentId: "",
          rollNumber: "",
          studentName: "",
          attendanceStatus: AttendanceStatus.PRESENT,
          remarks: "",
        },
      ],
    );
  }

  /* ============================================================
     DOWNLOAD ATTENDANCE
  ============================================================ */

  downloadAttendance(filename: string, rows: AttendanceStudentRow[]): void {
    this.downloadRows(
      filename,

      rows.map((row) => ({
        studentId: row.studentId,

        rollNumber: row.rollNumber,

        studentName: row.studentName,

        attendanceStatus: String(row.attendanceStatus),

        attendanceSource: String(row.attendanceSource ?? ""),

        remarks: row.remarks,
      })),
    );
  }

  /* ============================================================
     DOWNLOAD
  ============================================================ */

  private downloadRows(
    filename: string,
    rows: Record<string, unknown>[],
  ): void {
    void import("xlsx").then((XLSX) => {
      const worksheet = XLSX.utils.json_to_sheet(rows, {
        header: [...ATTENDANCE_EXCEL_HEADERS],
      });

      const workbook = XLSX.utils.book_new();

      XLSX.utils.book_append_sheet(workbook, worksheet, "Attendance");

      XLSX.writeFile(workbook, filename);
    });
  }
}
