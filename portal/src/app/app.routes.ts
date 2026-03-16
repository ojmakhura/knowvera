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
    data: { title: 'KYC Record' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/kyc-record/kyc-record').then((m) => m.KycRecord),
  },
  {
    path: 'kyc-record/:id',
    data: { title: 'KYC Record' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/kyc-record/kyc-record').then((m) => m.KycRecord),
  },
  {
    path: 'kyc-record/edit',
    data: { title: 'Edit KYC Record' },
    canActivate: [authGuard],
    loadComponent: () =>
      import('./views/kyc-record/edit/edit-kyc-record').then((m) => m.EditKycRecord),
  },
  // Fallback when no prior route is matched
  {
    path: '**', redirectTo: '', pathMatch: 'full'
  },
];
