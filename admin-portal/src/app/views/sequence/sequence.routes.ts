import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const sequenceRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['sequences:list'] },
    loadComponent: () =>
      import('./sequences').then((module) => module.Sequences),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['sequences:edit'] },
    loadComponent: () =>
      import('./edit/sequence-edit').then((module) => module.SequenceEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['sequences:edit'] },
    loadComponent: () =>
      import('./edit/sequence-edit').then((module) => module.SequenceEdit),
  },
];
