import { Routes } from '@angular/router';
import { authGuard } from '@app/@core/guards/auth.guard';

export const requestRoutes: Routes = [
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./client-requests').then((module) => module.ClientRequests),
  },
  // {
  //   path: 'edit',
  //   canActivate: [AuthenticationGuard],
  //   loadComponent: () =>
  //     import('./edit/client-request-edit').then(
  //       (module) => module.ClientRequestEdit
  //     ),
  // },
  // {
  //   path: 'edit/:id',
  //   canActivate: [AuthenticationGuard],
  //   loadComponent: () =>
  //     import('./edit/client-request-edit').then(
  //       (module) => module.ClientRequestEdit
  //     ),
  // },
  // {
  //   path: 'details',
  //   canActivate: [AuthenticationGuard],
  //   loadComponent: () =>
  //     import('./details/client-request-details').then(
  //       (module) => module.ClientRequestDetails
  //     ),
  // },
  // {
  //   path: 'details/:id',
  //   canActivate: [AuthenticationGuard],
  //   loadComponent: () =>
  //     import('./details/client-request-details').then(
  //       (module) => module.ClientRequestDetails
  //     ),
  // },
];
