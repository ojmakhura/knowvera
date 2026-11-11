import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const recordsRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['kyc-records:list'] },
    loadComponent: () => import('./records').then((module) => module.Records),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['kyc-records:edit'] },
    loadComponent: () => import('./edit/record-edit').then((module) => module.RecordEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['kyc-records:edit'] },
    loadComponent: () => import('./edit/record-edit').then((module) => module.RecordEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['kyc-records:view'] },
    loadComponent: () => import('./details/record-details').then((module) => module.RecordDetails),
  },
];
