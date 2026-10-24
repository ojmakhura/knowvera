import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const settingsRoutes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'identity',
  },
  {
    path: 'identity',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['settings:list'] },
    loadComponent: () =>
      import('./platform-identity/platform-identity').then((m) => m.PlatformIdentity),
  },
  {
    path: 'metrics',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['settings:list'] },
    loadComponent: () =>
      import('./operational-metrics/operational-metrics').then((m) => m.OperationalMetrics),
  },
  {
    path: 'documents',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['document-types:list'] },
    loadComponent: () =>
      import('./document-requirements/document-requirements').then((m) => m.DocumentRequirements),
  },
  {
    path: 'field-groups',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['kyc-field-groups:list'] },
    loadComponent: () =>
      import('./field-groups/field-groups').then((m) => m.FieldGroups),
  },
  {
    path: 'tool-selectors',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['settings:list'] },
    loadComponent: () =>
      import('./tool-selectors/tool-selectors').then((m) => m.ToolSelectors),
  },
  {
    path: 'templates',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['document-types:list'] },
    loadComponent: () =>
      import('./template-mappings/template-mappings').then((m) => m.TemplateMappings),
  },
  {
    path: 'financials',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['settings:list'] },
    loadComponent: () =>
      import('./financial-settings/financial-settings').then((m) => m.FinancialSettings),
  },
];