import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { environment } from "../../../../../environments/environment";

interface ReportDefinition {
  code: string;
  name: string;
  category: string;
  format: string;
  pdfAvailable: boolean;
}
@Component({
  selector: "app-reports-workspace",
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, MatSnackBarModule],
  templateUrl: "./reports.component.html",
  styleUrl: "./reports.component.scss",
})
export class ReportsWorkspaceComponent {
  private readonly http = inject(HttpClient);
  private readonly snack = inject(MatSnackBar);
  readonly reports = signal<ReportDefinition[]>([]);
  readonly selected = signal<ReportDefinition | null>(null);
  readonly rows = signal<string[][]>([]);
  readonly loading = signal(false);
  readonly exporting = signal<string | null>(null);
  ngOnInit() {
    this.loadCatalogue();
  }
  loadCatalogue() {
    this.loading.set(true);
    this.http
      .get<ReportDefinition[]>(`${environment.apiUrl}/erp/reports`)
      .subscribe({
        next: (r) => {
          this.reports.set(r ?? []);
          this.loading.set(false);
        },
        error: (e) => {
          this.loading.set(false);
          this.fail(e);
        },
      });
  }
  open(r: ReportDefinition) {
    this.selected.set(r);
    this.loading.set(true);
    this.http
      .get<
        string[][]
      >(`${environment.apiUrl}/erp/reports/${encodeURIComponent(r.code)}/data`)
      .subscribe({
        next: (data) => {
          this.rows.set(data ?? []);
          this.loading.set(false);
        },
        error: (e) => {
          this.loading.set(false);
          this.fail(e);
        },
      });
  }
  export(format: "PDF" | "Excel", r?: ReportDefinition) {
    const report = r ?? this.selected();
    if (!report) {
      this.snack.open("Select a report first", "OK", { duration: 2200 });
      return;
    }
    this.exporting.set(report.code + format);
    const ext = format === "PDF" ? "pdf" : "xlsx";
    this.http
      .get(
        `${environment.apiUrl}/erp/reports/${encodeURIComponent(report.code)}/${format === "PDF" ? "pdf" : "excel"}`,
        { responseType: "blob" },
      )
      .subscribe({
        next: (b) => {
          const url = URL.createObjectURL(b);
          const a = document.createElement("a");
          a.href = url;
          a.download = `${report.code}.${ext}`;
          a.click();
          URL.revokeObjectURL(url);
          this.exporting.set(null);
        },
        error: (e) => {
          this.exporting.set(null);
          this.fail(e);
        },
      });
  }
  private fail(e: any) {
    this.snack.open(e?.error?.message ?? "Unable to load report", "Close", {
      duration: 3500,
    });
  }
}
