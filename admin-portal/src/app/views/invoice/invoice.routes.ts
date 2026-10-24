import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const invoiceRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['invoices:list'] },
    loadComponent: () =>
      import('./invoices').then((module) => module.Invoices),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['invoices:edit'] },
    loadComponent: () =>
      import('./edit/invoice-edit').then((module) => module.InvoiceEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['invoices:edit'] },
    loadComponent: () =>
      import('./edit/invoice-edit').then((module) => module.InvoiceEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['invoices:view'] },
    loadComponent: () =>
      import('./details/invoice-details').then((module) => module.InvoiceDetails),
  },
];
