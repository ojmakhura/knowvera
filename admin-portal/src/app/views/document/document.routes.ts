import { Routes } from '@angular/router';

export const documentRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./documents').then((module) => module.Documents),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/document-edit').then((module) => module.DocumentEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/document-edit').then((module) => module.DocumentEdit),
  },
  {
    path: 'details',
    loadComponent: () =>
      import('./details/document-details').then((module) => module.DocumentDetails),
  },
  {
    path: 'details/:id',
    loadComponent: () =>
      import('./details/document-details').then((module) => module.DocumentDetails),
  },
  {
    path: 'type',
    loadChildren: () =>
      import('./type/document-type.routes').then((module) => module.documentTypeRoutes),
  },
];
