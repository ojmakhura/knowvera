import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const recordsRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./records').then((module) => module.Records),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./edit/record-edit').then((module) => module.RecordEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./edit/record-edit').then((module) => module.RecordEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./details/record-details').then((module) => module.RecordDetails),
  },
];
