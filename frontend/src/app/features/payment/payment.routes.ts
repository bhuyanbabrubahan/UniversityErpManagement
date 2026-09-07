import { Routes } from '@angular/router';

export const PAYMENT_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'list',
    pathMatch: 'full'
  },
  {
    path: 'list',
    loadComponent: () =>
      import('./pages/payment-list/payment-list.component')
        .then(m => m.PaymentListComponent)
  },
  {
    path: 'add',
    loadComponent: () =>
      import('./pages/add-payment/add-payment.component')
        .then(m => m.AddPaymentComponent)
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./pages/edit-payment/edit-payment.component')
        .then(m => m.EditPaymentComponent)
  },
  {
    path: 'view/:id',
    loadComponent: () =>
      import('./pages/view-payment/view-payment.component')
        .then(m => m.ViewPaymentComponent)
  }
];