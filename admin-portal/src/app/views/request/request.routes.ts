import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const requestRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./client-requests').then((module) => module.ClientRequests),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: 'edit/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: 'details',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/client-request-details').then(
        (module) => module.ClientRequestDetails
      ),
  },
  {
    path: 'details/:id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/client-request-details').then(
        (module) => module.ClientRequestDetails
      ),
  },
];
