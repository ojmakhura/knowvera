import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const documentRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./documents').then((module) => module.Documents),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/document-edit').then((module) => module.DocumentEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/document-edit').then((module) => module.DocumentEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/document-details').then((module) => module.DocumentDetails),
  },
];
