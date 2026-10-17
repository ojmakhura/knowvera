import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';
import { individualResolver } from '@app/resolvers/individual/individual.resolver';

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
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    resolve: { ind: individualResolver },
    loadComponent: () =>
      import('./details/individual-details').then((module) => module.IndividualDetails),
  },
];
