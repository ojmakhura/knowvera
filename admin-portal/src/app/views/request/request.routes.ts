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
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/client-request-details').then(
        (module) => module.ClientRequestDetails
      ),
  },
];
