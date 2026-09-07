import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <section class="page">

      <header class="page-header">

        <div>
          <span class="eyebrow">
            ANALYTICS
          </span>

          <h1>
            Reports
          </h1>

          <p>
            View institutional reports and operational analytics.
          </p>
        </div>

        <button
          mat-flat-button
          color="primary">
          <mat-icon>download</mat-icon>
          Export Report
        </button>

      </header>

      <div class="report-grid">

        <mat-card class="report-card">

          <mat-card-content>

            <div class="report-icon">
              <mat-icon>school</mat-icon>
            </div>

            <h2>
              Student Reports
            </h2>

            <p>
              Student enrollment and academic reports.
            </p>

          </mat-card-content>

        </mat-card>

        <mat-card class="report-card">

          <mat-card-content>

            <div class="report-icon">
              <mat-icon>groups</mat-icon>
            </div>

            <h2>
              Faculty Reports
            </h2>

            <p>
              Faculty and teaching activity reports.
            </p>

          </mat-card-content>

        </mat-card>

        <mat-card class="report-card">

          <mat-card-content>

            <div class="report-icon">
              <mat-icon>payments</mat-icon>
            </div>

            <h2>
              Financial Reports
            </h2>

            <p>
              Fee collection and payment reports.
            </p>

          </mat-card-content>

        </mat-card>

        <mat-card class="report-card">

          <mat-card-content>

            <div class="report-icon">
              <mat-icon>bar_chart</mat-icon>
            </div>

            <h2>
              Academic Analytics
            </h2>

            <p>
              Academic performance and result analytics.
            </p>

          </mat-card-content>

        </mat-card>

      </div>

    </section>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
    }

    .page {
      width: 100%;
      max-width: 1600px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      gap: 20px;
      margin-bottom: 24px;
    }

    .eyebrow {
      display: block;
      margin-bottom: 5px;
      color: #94a3b8;
      font-size: 11px;
      font-weight: 700;
      letter-spacing: .1em;
    }

    h1 {
      margin: 0;
      font-size: 30px;
      font-weight: 700;
    }

    .page-header p {
      margin: 8px 0 0;
      color: #64748b;
      font-size: 13px;
    }

    .report-grid {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 18px;
    }

    .report-card {
      border-radius: 16px;
    }

    .report-card mat-card-content {
      min-height: 180px;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }

    .report-icon {
      width: 44px;
      height: 44px;
      display: grid;
      place-items: center;
      margin-bottom: 16px;
      border-radius: 12px;
      background: #eff6ff;
      color: #2563eb;
    }

    .report-icon mat-icon {
      font-size: 22px;
    }

    .report-card h2 {
      margin: 0 0 6px;
      font-size: 17px;
      font-weight: 700;
    }

    .report-card p {
      margin: 0;
      color: #64748b;
      font-size: 13px;
      line-height: 1.5;
    }

    @media (max-width: 700px) {
      .page-header {
        flex-direction: column;
      }

      .report-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class ReportsComponent {}