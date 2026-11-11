import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const complianceRoutes: Routes = [
  {
    path: 'screening',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['screening:list'] },
    loadComponent: () => import('./screening-queue').then((module) => module.ScreeningQueue),
  },
  {
    path: 'suspicion-cases',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['suspicion-cases:list'] },
    loadComponent: () => import('./suspicion-cases').then((module) => module.SuspicionCases),
  },
  {
    path: 'suspicion-cases/:id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['suspicion-cases:view'] },
    loadComponent: () => import('./suspicion-case-details').then((module) => module.SuspicionCaseDetails),
  },
];
