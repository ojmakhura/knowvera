import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const documentTypeRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./document-type').then((module) => module.DocumentTypeComponent),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/document-type-edit').then((module) => module.DocumentTypeEdit),
  },
  {
    path: 'edit/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/document-type-edit').then((module) => module.DocumentTypeEdit),
  },
];
