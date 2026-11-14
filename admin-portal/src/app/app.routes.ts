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
    path: 'forms',
    canActivate: [AuthenticationGuard],
    loadChildren: () => import('./views/form/form.routes').then((module) => module.formRoutes),
  },
  {
    path: 'invoices',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/invoice/invoice.routes').then((module) => module.invoiceRoutes),
  },
  {
    // A client's KYC report as the organisation asking for it configured it
    path: 'kyc-reports',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/kyc-report/kyc-report.routes').then((module) => module.kycReportRoutes),
  },
  {
    path: 'records',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/records/records.routes').then((module) => module.recordsRoutes),
  },
  {
    // Sanctions screening and suspicious transaction cases
    path: 'compliance',
    canActivate: [AuthenticationGuard],
    loadChildren: () =>
      import('./views/compliance/compliance.routes').then((module) => module.complianceRoutes),
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
  {
    path: '**',
    canActivate: [AuthenticationGuard],
    data: { title: 'Page not found' },
    loadComponent: () => import('./views/not-found/not-found').then((m) => m.NotFound),
  },
];
