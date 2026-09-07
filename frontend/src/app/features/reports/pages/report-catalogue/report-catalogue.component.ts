import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ReportsService } from '../../services/reports.service';
import { ReportDefinition } from '../../models/report-definition.model';

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './report-catalogue.component.html',
  styleUrl: './report-catalogue.component.scss'
})
export class ReportCatalogueComponent {
  private readonly service = inject(ReportsService);
  private readonly sanitizer = inject(DomSanitizer);
  readonly reports = signal<ReportDefinition[]>([]);
  readonly selectedPdf = signal<SafeResourceUrl | null>(null);
  readonly loading = signal(false);

  ngOnInit(): void {
    this.loading.set(true);
    this.service.catalogue().subscribe({
      next: reports => { this.reports.set(reports); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  openExcel(report: ReportDefinition): void { window.open(this.service.excelUrl(report.code), '_blank', 'noopener'); }

  openPdf(report: ReportDefinition): void {
    this.selectedPdf.set(this.sanitizer.bypassSecurityTrustResourceUrl(this.service.pdfUrl(report.code)));
  }
}
