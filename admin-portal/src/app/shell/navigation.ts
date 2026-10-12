// export const menuItems: any[] = [
//     {
//         routerLink: "/organisation",
//         titleKey: "organisation.title",
//         icon: "business"
//     },
//     {
//         routerLink: "/individual",
//         titleKey: "individual.title",
//         icon: "person"
//     },
//     {
//         routerLink: "/records",
//         titleKey: "kyc.record.title",
//         icon: "verified_user"
//     },
//     // {
//     //     routerLink: "/user",
//     //     titleKey: "users.title",
//     //     icon: "group"
//     // },
//     {
//         routerLink: "/subscription",
//         titleKey: "kyc.subscription.title",
//         icon: "subscriptions"
//     },
//     {
//         routerLink: "/invoice",
//         titleKey: "invoice.title",
//         icon: "receipt_long"
//     },
//     {
//         routerLink: "/client-request",
//         titleKey: "client.request.title",
//         icon: "assignment"
//     },
//     {
//         routerLink: "/documents",
//         titleKey: "documents.title",
//         icon: "description"
//     },
//     {
//         routerLink: "/document/type",
//         titleKey: "document.type.title",
//         icon: "category"
//     },
//     {
//         routerLink: "/sequence",
//         titleKey: "sequence.title",
//         icon: "timeline"
//     },
//     {
//         routerLink: "/settings",
//         titleKey: "settings.title",
//         icon: "settings"
//     },
// ];
export interface ShellMenuItem {
  routerLink: string;
  titleKey: string;
  icon: string;
  exact?: boolean;
}

export const menuItems: ShellMenuItem[] = [
  { routerLink: '/organisation', titleKey: 'organisation.title', icon: 'business' },
  { routerLink: '/individual', titleKey: 'individual.title', icon: 'person' },
  { routerLink: '/records', titleKey: 'kyc.record.title', icon: 'verified_user' },
  { routerLink: '/subscription', titleKey: 'kyc.subscription.title', icon: 'subscriptions' },
  { routerLink: '/invoice', titleKey: 'invoice.title', icon: 'receipt_long' },
  { routerLink: '/client-request', titleKey: 'client.request.title', icon: 'assignment' },
  { routerLink: '/documents', titleKey: 'documents.title', icon: 'description' },
  { routerLink: '/document/type', titleKey: 'document.type.title', icon: 'category' },
  { routerLink: '/sequence', titleKey: 'sequence.title', icon: 'timeline' },
];

export const settingsMenuItems: ShellMenuItem[] = [
  { routerLink: '/settings/identity', titleKey: 'settings.platformIdentity.title', icon: 'branding_watermark' },
  { routerLink: '/settings/metrics', titleKey: 'settings.operationalMetrics.title', icon: 'analytics' },
  { routerLink: '/settings/documents', titleKey: 'settings.documentRequirements.title', icon: 'topic' },
  { routerLink: '/settings/field-groups', titleKey: 'settings.fieldGroups.title', icon: 'account_tree' },
  { routerLink: '/settings/tool-selectors', titleKey: 'settings.toolSelectors.title', icon: 'settings_input_component' },
  { routerLink: '/settings/templates', titleKey: 'settings.templates.title', icon: 'description' },
  { routerLink: '/settings/financials', titleKey: 'settings.financials.title', icon: 'payments' },
];