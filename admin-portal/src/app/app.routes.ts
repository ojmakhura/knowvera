import { Routes } from '@angular/router';
import { AuthenticationGuard } from './auth/authentication.guard';

export const routes: Routes = [
  {
    path: 'documents',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/document/document.routes').then((module) => module.documentRoutes),
  },
  {
    path: 'document/type',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/document/type/document-type.routes').then((module) => module.documentTypeRoutes),
  },
  {
    path: 'organisation',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/organisation/organisation.routes').then(
        (module) => module.organisationRoutes,
      ),
  },
  {
    path: 'individual',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/individual/individual.routes').then((module) => module.individualRoutes),
  },
  {
    path: 'subscription',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/subscription/subscription.routes').then(
        (module) => module.subscriptionRoutes,
      ),
  },
  {
    path: 'sequence',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/sequence/sequence.routes').then((module) => module.sequenceRoutes),
  },
  {
    path: 'invoice',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/invoice/invoice.routes').then((module) => module.invoiceRoutes),
  },
  {
    path: 'records',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/records/records.routes').then((module) => module.recordsRoutes),
  },
  {
    path: 'kyc',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/records/records.routes').then((module) => module.recordsRoutes),
  },
  {
    path: 'client-request',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/request/request.routes').then((module) => module.requestRoutes),
  },
  {
    path: 'settings',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./views/settings/system-settings').then((module) => module.SystemSettings),
  },
];
