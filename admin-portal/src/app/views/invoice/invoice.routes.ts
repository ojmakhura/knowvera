import { Routes } from '@angular/router';

export const invoiceRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./invoices').then((module) => module.Invoices),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/invoice-edit').then((module) => module.InvoiceEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/invoice-edit').then((module) => module.InvoiceEdit),
  },
  {
    path: 'details',
    loadComponent: () =>
      import('./details/invoice-details').then((module) => module.InvoiceDetails),
  },
  {
    path: 'details/:id',
    loadComponent: () =>
      import('./details/invoice-details').then((module) => module.InvoiceDetails),
  },
];
