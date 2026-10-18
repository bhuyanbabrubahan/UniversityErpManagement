import { Component, inject, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../../../environments/environment";
@Component({
  selector: "app-audit-page",
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  styles: [
    `
      :host {
        display: block;
        min-width: 0;
      }
      .workspace-page {
        padding: clamp(14px, 2vw, 28px);
        display: grid;
        gap: 18px;
      }
      .workspace-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 14px;
        flex-wrap: wrap;
      }
      .workspace-header .eyebrow {
        font-size: 11px;
        font-weight: 800;
        letter-spacing: 0.12em;
        color: var(--primary-color, #4f46e5);
      }
      .workspace-header h1 {
        font-size: clamp(24px, 2.5vw, 32px);
        margin: 5px 0;
        font-weight: 800;
      }
      .workspace-header p {
        margin: 0;
        color: var(--muted-color, #64748b);
        font-size: 14px;
      }
      .filter-card,
      .table-card {
        border: 1px solid var(--border-color, #e2e8f0);
        border-radius: 14px;
        box-shadow: 0 6px 20px rgba(15, 23, 42, 0.04);
      }
      .filter-grid {
        display: flex;
        gap: 10px;
      }
      .filter-grid input {
        flex: 1;
        min-width: 0;
        padding: 12px 14px;
        border: 1px solid var(--border-color, #cbd5e1);
        border-radius: 9px;
        font: inherit;
        background: var(--surface, #fff);
        color: inherit;
      }
      .table-wrap {
        overflow: auto;
      }
      table {
        width: 100%;
        border-collapse: collapse;
        font-size: 13px;
      }
      th,
      td {
        padding: 12px 14px;
        border-bottom: 1px solid var(--border-color, #edf0f5);
        text-align: left;
        vertical-align: top;
      }
      th {
        background: var(--subtle-background, #f8fafc);
        font-size: 11px;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: var(--muted-color, #64748b);
        white-space: nowrap;
      }
      .status-chip {
        display: inline-block;
        padding: 4px 8px;
        border-radius: 999px;
        background: var(--subtle-background, #eef2ff);
        font-size: 11px;
        font-weight: 700;
      }
      .empty {
        text-align: center;
        color: var(--muted-color, #64748b);
        padding: 26px;
      }
      .loading {
        display: flex;
        justify-content: center;
        padding: 35px;
      }
      @media (max-width: 600px) {
        .workspace-page {
          padding: 12px;
        }
        .filter-grid {
          flex-direction: column;
        }
        .filter-grid button {
          align-self: flex-start;
        }
        th,
        td {
          padding: 10px;
        }
      }
    `,
  ],
  template: ` <section class="workspace-page">
    <header class="workspace-header">
      <div>
        <span class="eyebrow">GOVERNANCE</span>
        <h1>Audit Logs</h1>
        <p>Who changed what, when and from which module.</p>
      </div>
      <button mat-flat-button (click)="load()">
        <mat-icon>refresh</mat-icon>Refresh
      </button>
    </header>
    <mat-card class="filter-card"
      ><mat-card-content
        ><div class="filter-grid">
          <input
            [(ngModel)]="query"
            placeholder="Search action, module or user..."
          /><button mat-stroked-button (click)="load()">
            <mat-icon>search</mat-icon>Search
          </button>
        </div></mat-card-content
      ></mat-card
    >
    <mat-card class="table-card"
      ><mat-card-content>
        @if (loading) {
          <div class="loading"><mat-spinner diameter="34"></mat-spinner></div>
        } @else {
          <div class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Action</th>
                  <th>Module</th>
                  <th>Description</th>
                  <th>User</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                @for (row of filteredRows; track row.id) {
                  <tr>
                    <td>{{ row.id }}</td>
                    <td>
                      <span class="status-chip">{{
                        row.action || "ACTIVITY"
                      }}</span>
                    </td>
                    <td>{{ row.module || "-" }}</td>
                    <td>{{ row.description || "-" }}</td>
                    <td>
                      {{ row.username || row.userName || row.createdBy || "-" }}
                    </td>
                    <td>{{ row.createdAt || row.timestamp || "-" }}</td>
                  </tr>
                } @empty {
                  <tr>
                    <td colspan="6" class="empty">
                      {{ errorMessage || "No audit records found." }}
                    </td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        }
      </mat-card-content></mat-card
    >
  </section>`,
})
export class AuditComponent implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/audit-logs`;
  rows: any[] = [];
  loading = false;
  query = "";
  errorMessage = "";

  get filteredRows(): any[] {
    const q = this.query.trim().toLocaleLowerCase();
    if (!q) return this.rows;
    return this.rows.filter((row) =>
      [
        row.id,
        row.action,
        row.module,
        row.description,
        row.username,
        row.userName,
        row.createdBy,
        row.createdAt,
        row.timestamp,
      ].some((value) =>
        String(value ?? "")
          .toLocaleLowerCase()
          .includes(q),
      ),
    );
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.errorMessage = "";
    this.http
      .get<any>(this.url, {
        params: { page: "0", size: "100", sort: "createdAt,desc" },
      })
      .subscribe({
        next: (response) => {
          const data = response?.data ?? response?.result ?? response;
          this.rows = Array.isArray(data)
            ? data
            : (data?.content ?? data?.items ?? []);
          this.loading = false;
        },
        error: (error) => {
          this.rows = [];
          this.errorMessage =
            error?.error?.message ??
            "Audit records could not be loaded. Check your access and server logs.";
          this.loading = false;
        },
      });
  }
}
