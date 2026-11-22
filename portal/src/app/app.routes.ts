import { Routes } from '@angular/router';
import { authGuard } from './@core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    data: { title: 'Home' },
    loadComponent: () => import('./views/home/home').then((m) => m.Home),
  },
  {
    path: 'privacy-policy',
    data: { title: 'Privacy Policy' },
    loadComponent: () =>
      import('./views/privacy-policy/privacy-policy').then(
        (m) => m.PrivacyPolicy
      ),
  },
  {
    path: 'terms-of-service',
    data: { title: 'Terms of Service' },
    loadComponent: () =>
      import('./views/terms-of-service/terms-of-service').then(
        (m) => m.TermsOfService
      ),
  },
  {
    path: 'register',
    data: { title: 'Register' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/register/register').then((m) => m.Register),
  },
  {
    path: 'register/:requestId',
    data: { title: 'Register' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/register/register').then((m) => m.Register),
  },
  {
    path: 'dashboard',
    data: { title: 'Dashboard' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/dashboard/dashboard').then((m) => m.Dashboard),
  },
  {
    path: 'kyc-record',
    data: { permissions: ['kyc-records:view'], title: 'KYC Record' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/kyc-record/kyc-record').then((m) => m.KycRecord),
  },
  {
    path: 'kyc-record/edit',
    data: { permissions: ['kyc-records:edit'], title: 'Edit KYC Record' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/kyc-record/edit/edit-kyc-record').then((m) => m.EditKycRecord),
  },
  {
    path: 'forms/:id',
    data: { permissions: ['form-submissions:view'], title: 'Form' },
    canActivate: [authGuard],
    loadComponent: () => import('./views/form/fill-form').then((m) => m.FillForm),
  },
  {
    path: 'kyc-record/:id',
    data: { permissions: ['kyc-records:view'], title: 'KYC Record' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/kyc-record/kyc-record').then((m) => m.KycRecord),
  },
  {
    path: 'organisation',
    data: { permissions: ['organisations:view'], title: 'My Organisation' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/organisation/details/organisation-details').then((m) => m.OrganisationDetails),
  },
  {
    path: 'organisation/edit',
    data: { permissions: ['organisations:edit'], title: 'Edit Organisation' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/organisation/edit/edit-organisation').then((m) => m.EditOrganisation),
  },
  {
    path: 'client-kyc',
    data: { permissions: ['kyc-records:list'], title: 'Client KYC' },
    canActivate: [authGuard],
    loadComponent: () => import('./views/client-kyc/client-kyc').then((m) => m.ClientKyc),
  },
  {
    path: 'client-kyc/:recordId',
    data: { permissions: ['kyc-records:view'], title: 'Client KYC Report' },
    canActivate: [authGuard],
    loadComponent: () => import('./views/client-kyc/client-kyc-report').then((m) => m.ClientKycReport),
  },
  {
    path: 'document/:id',
    data: { permissions: ['documents:view'], title: 'Document Details' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/document/document-details').then((m) => m.DocumentDetails),
  },
  {
    path: 'individual',
    data: { title: 'My Profile' },
    canActivate: [authGuard],
    loadChildren: () =>
      import('./views/individual/individual-routes').then((m) => m.individualRoutes),
  },
  {
    path: 'client-request',
    data: { permissions: ['client-requests:list'] },
    canActivate: [authGuard],
    loadChildren: () =>
      import('./views/client-request/request.routes').then((module) => module.requestRoutes),
  },
  {
    path: 'no-access',
    data: { title: 'No access' },
    loadComponent: () => import('./views/no-access/no-access').then((m) => m.NoAccess),
  },
  // Fallback when no prior route is matched
  {
    path: '**', redirectTo: '', pathMatch: 'full'
  },
];
