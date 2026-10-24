import { Routes } from '@angular/router';
import { AuthenticationGuard } from './auth/authentication.guard';
import { SystemSettings } from './views/settings/system-settings';

export const routes: Routes = [
  {
    path: 'documents',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/document/document.routes').then((module) => module.documentRoutes),
  },
  {
    path: 'document-types',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/document/type/document-type.routes').then(
        (module) => module.documentTypeRoutes,
      ),
  },
  {
    path: 'organisations',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/organisation/organisation.routes').then(
        (module) => module.organisationRoutes,
      ),
  },
  {
    path: 'individuals',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/individual/individual.routes').then((module) => module.individualRoutes),
  },
  {
    path: 'subscriptions',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/subscription/subscription.routes').then(
        (module) => module.subscriptionRoutes,
      ),
  },
  {
    path: 'sequences',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/sequence/sequence.routes').then((module) => module.sequenceRoutes),
  },
  {
    path: 'invoices',
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
    path: 'client-requests',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/request/request.routes').then((module) => module.requestRoutes),
  },
  // {
  //   path: 'settings',
  //   canActivate: [AuthenticationGuard],
  //   component: SystemSettings,
  // },
  {
    path: 'settings',
    canActivate: [AuthenticationGuard],
    loadChildren: () => import('./views/settings/settings.routes').then((m) => m.settingsRoutes),
  },
  {
    path: 'no-access',
    data: { title: 'No access' },
    loadComponent: () => import('./views/no-access/no-access').then((m) => m.NoAccess),
  },
];
