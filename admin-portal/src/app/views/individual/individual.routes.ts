import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const individualRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./individuals').then((module) => module.Individuals),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: 'edit/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: 'details',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/individual-details').then((module) => module.IndividualDetails),
  },
  {
    path: 'details/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/individual-details').then((module) => module.IndividualDetails),
  },
];
