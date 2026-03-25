import { Routes } from '@angular/router';

export const organisationRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./organisations').then((module) => module.Organisations),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/organisation-edit').then((module) => module.OrganisationEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/organisation-edit').then((module) => module.OrganisationEdit),
  },
  {
    path: 'details',
    loadComponent: () =>
      import('./details/organisation-details').then((module) => module.OrganisationDetails),
  },
  {
    path: 'details/:id',
    loadComponent: () =>
      import('./details/organisation-details').then((module) => module.OrganisationDetails),
  },
];
