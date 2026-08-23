import { Routes } from '@angular/router';

export const settingsRoutes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'identity',
  },
  {
    path: 'identity',
    loadComponent: () =>
      import('./platform-identity/platform-identity').then((m) => m.PlatformIdentity),
  },
  {
    path: 'metrics',
    loadComponent: () =>
      import('./operational-metrics/operational-metrics').then((m) => m.OperationalMetrics),
  },
  {
    path: 'documents',
    loadComponent: () =>
      import('./document-requirements/document-requirements').then((m) => m.DocumentRequirements),
  },
  {
    path: 'field-groups',
    loadComponent: () =>
      import('./field-groups/field-groups').then((m) => m.FieldGroups),
  },
  {
    path: 'tool-selectors',
    loadComponent: () =>
      import('./tool-selectors/tool-selectors').then((m) => m.ToolSelectors),
  },
  {
    path: 'templates',
    loadComponent: () =>
      import('./template-mappings/template-mappings').then((m) => m.TemplateMappings),
  },
  {
    path: 'financials',
    loadComponent: () =>
      import('./financial-settings/financial-settings').then((m) => m.FinancialSettings),
  },
];