import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const sequenceRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./sequences').then((module) => module.Sequences),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/sequence-edit').then((module) => module.SequenceEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/sequence-edit').then((module) => module.SequenceEdit),
  },
];
