import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const requestRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['client-requests:list'] },
    loadComponent: () =>
      import('./client-requests').then((module) => module.ClientRequests),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['client-requests:edit'] },
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['client-requests:edit'] },
    loadComponent: () =>
      import('./edit/client-request-edit').then(
        (module) => module.ClientRequestEdit
      ),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['client-requests:view'] },
    loadComponent: () =>
      import('./details/client-request-details').then(
        (module) => module.ClientRequestDetails
      ),
  },
];
