import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const organisationRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./organisations').then((module) => module.Organisations),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/organisation-edit').then((module) => module.OrganisationEdit),
  },
  {
    path: 'edit/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/organisation-edit').then((module) => module.OrganisationEdit),
  },
  {
    path: 'details',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/organisation-details').then((module) => module.OrganisationDetails),
  },
  {
    path: 'details/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/organisation-details').then((module) => module.OrganisationDetails),
  },
];
