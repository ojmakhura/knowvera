import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const kycReportRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { title: 'KYC Reports', permissions: ['kyc-records:list'] },
    loadComponent: () => import('./kyc-reports').then((module) => module.KycReports),
  },
  {
    path: ':organisationId/:recordId',
    canActivate: [AuthenticationGuard],
    data: { title: 'KYC Report', permissions: ['kyc-records:view'] },
    loadComponent: () => import('./kyc-report-summary').then((module) => module.KycReportSummary),
  },
];
