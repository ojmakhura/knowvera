import { Routes } from '@angular/router';

export const documentTypeRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./document-type').then((module) => module.DocumentTypeComponent),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/document-type-edit').then((module) => module.DocumentTypeEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/document-type-edit').then((module) => module.DocumentTypeEdit),
  },
];
