import { Routes } from '@angular/router';

export const individualRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./individuals').then((module) => module.Individuals),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/individual-edit').then((module) => module.IndividualEdit),
  },
  {
    path: 'details',
    loadComponent: () =>
      import('./details/individual-details').then((module) => module.IndividualDetails),
  },
  {
    path: 'details/:id',
    loadComponent: () =>
      import('./details/individual-details').then((module) => module.IndividualDetails),
  },
];
