export interface ShellMenuItem {
  routerLink: string;
  titleKey: string;
  label: string;
  /** Permission scopes (any one is enough) that unlock the item; empty means every signed-in user. */
  permissions: string[];
  icon: string;
  exact?: boolean;
}

export const menuItems: ShellMenuItem[] = [
  {
    routerLink: '/organisations',
    titleKey: 'organisation.title',
    icon: 'business',
    permissions: ['organisations:list'],
    label: 'Organisations',
  },
  {
    routerLink: '/individuals',
    titleKey: 'individual.title',
    icon: 'person',
    permissions: ['individuals:list'],
    label: 'Individuals',
  },
  {
    routerLink: '/records',
    titleKey: 'kyc.record.title',
    icon: 'verified_user',
    permissions: ['kyc-records:list'],
    label: 'KYC Records',
  },
  {
    routerLink: '/subscriptions',
    titleKey: 'kyc.subscription.title',
    icon: 'subscriptions',
    permissions: ['subscriptions:list'],
    label: 'Subscriptions',
  },
  {
    routerLink: '/invoices',
    titleKey: 'invoice.title',
    icon: 'receipt_long',
    permissions: ['invoices:list'],
    label: 'Invoices',
  },
  {
    routerLink: '/client-requests',
    titleKey: 'client.request.title',
    icon: 'assignment',
    permissions: ['client-requests:list'],
    label: 'Client Requests',
  },
  {
    routerLink: '/documents',
    titleKey: 'documents.title',
    icon: 'description',
    permissions: ['documents:list'],
    label: 'Documents',
  },
  {
    routerLink: '/document-types',
    titleKey: 'document.type.title',
    icon: 'category',
    permissions: ['document-types:list'],
    label: 'Document Types',
  },
  {
    routerLink: '/sequences',
    titleKey: 'sequence.title',
    icon: 'timeline',
    permissions: ['sequences:list'],
    label: 'Sequences',
  },
];

export const settingsMenuItems: ShellMenuItem[] = [
  {
    routerLink: '/settings/identity',
    titleKey: 'settings.platformIdentity.title',
    icon: 'branding_watermark',
    permissions: ['settings:list'],
    label: 'Platform Identity',
  },
  {
    routerLink: '/settings/metrics',
    titleKey: 'settings.operationalMetrics.title',
    icon: 'analytics',
    permissions: ['settings:list'],
    label: 'Operational Metrics',
  },
  {
    routerLink: '/settings/documents',
    titleKey: 'settings.documentRequirements.title',
    icon: 'topic',
    permissions: ['document-types:list'],
    label: 'Document Requirements',
  },
  {
    routerLink: '/settings/field-groups',
    titleKey: 'settings.fieldGroups.title',
    icon: 'account_tree',
    permissions: ['kyc-field-groups:list'],
    label: 'FIend Groups',
  },
  {
    routerLink: '/settings/tool-selectors',
    titleKey: 'settings.toolSelectors.title',
    icon: 'settings_input_component',
    permissions: ['settings:list'],
    label: 'Tool Selectors',
  },
  {
    routerLink: '/settings/templates',
    titleKey: 'settings.templates.title',
    icon: 'description',
    permissions: ['document-types:list'],
    label: 'Templates',
  },
  {
    routerLink: '/settings/financials',
    titleKey: 'settings.financials.title',
    icon: 'payments',
    permissions: ['settings:list'],
    label: 'Financials',
  },
];
