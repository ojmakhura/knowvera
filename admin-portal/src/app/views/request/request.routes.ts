import { Routes } from '@angular/router';

export const requestRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./client-requests').then((module) => module.ClientRequests),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: 'details',
    loadComponent: () =>
      import('./details/client-request-details').then(
        (module) => module.ClientRequestDetails
      ),
  },
  {
    path: 'details/:id',
    loadComponent: () =>
      import('./details/client-request-details').then(
        (module) => module.ClientRequestDetails
      ),
  },
];
