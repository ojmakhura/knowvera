import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const recordsRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./records').then((module) => module.Records),
  },
  {
    path: 'details/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./details/record-details').then((module) => module.RecordDetails),
  },
  {
    path: 'details',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./details/record-details').then((module) => module.RecordDetails),
  },
  {
    path: 'edit/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./edit/record-edit').then((module) => module.RecordEdit),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () => import('./edit/record-edit').then((module) => module.RecordEdit),
  },
];
