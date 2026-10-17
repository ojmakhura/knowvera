import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const invoiceRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./invoices').then((module) => module.Invoices),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/invoice-edit').then((module) => module.InvoiceEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/invoice-edit').then((module) => module.InvoiceEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/invoice-details').then((module) => module.InvoiceDetails),
  },
];
