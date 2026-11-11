import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const organisationRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['organisations:list'] },
    loadComponent: () =>
      import('./organisations').then((module) => module.Organisations),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['organisations:edit'] },
    loadComponent: () =>
      import('./edit/organisation-edit').then((module) => module.OrganisationEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['organisations:edit'] },
    loadComponent: () =>
      import('./edit/organisation-edit').then((module) => module.OrganisationEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['organisations:view'] },
    loadComponent: () =>
      import('./details/organisation-details').then((module) => module.OrganisationDetails),
  },
];
