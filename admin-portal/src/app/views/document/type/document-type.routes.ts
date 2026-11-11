import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const documentTypeRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['document-types:list'] },
    loadComponent: () =>
      import('./document-type').then((module) => module.DocumentTypeComponent),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['document-types:edit'] },
    loadComponent: () =>
      import('./edit/document-type-edit').then((module) => module.DocumentTypeEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['document-types:edit'] },
    loadComponent: () =>
      import('./edit/document-type-edit').then((module) => module.DocumentTypeEdit),
  },
];
