import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';
import { individualResolver } from '@app/resolvers/individual/individual.resolver';

export const individualRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['individuals:list'] },
    loadComponent: () =>
      import('./individuals').then((module) => module.Individuals),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['individuals:edit'] },
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['individuals:edit'] },
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['individuals:view'] },
    resolve: { ind: individualResolver },
    loadComponent: () =>
      import('./details/individual-details').then((module) => module.IndividualDetails),
  },
];
