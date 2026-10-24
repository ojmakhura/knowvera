import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const documentRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['documents:list'] },
    loadComponent: () =>
      import('./documents').then((module) => module.Documents),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['documents:edit'] },
    loadComponent: () =>
      import('./edit/document-edit').then((module) => module.DocumentEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['documents:edit'] },
    loadComponent: () =>
      import('./edit/document-edit').then((module) => module.DocumentEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['documents:view'] },
    loadComponent: () =>
      import('./details/document-details').then((module) => module.DocumentDetails),
  },
];
