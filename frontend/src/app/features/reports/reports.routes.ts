import { Routes } from '@angular/router';

export const REPORTS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/report-catalogue/report-catalogue.component').then(m => m.ReportCatalogueComponent)
  }
];
