export interface ShellMenuItem {
  routerLink: string;
  title: string;
  icon: string;
  exact?: boolean;
  /** Permissions (any one is enough) the item's page requires, as in its route's data.permissions; empty: none. */
  permissions: string[];
}

export const menuItems: ShellMenuItem[] = [
  { routerLink: '/dashboard', title: 'Dashboard', icon: 'dashboard', exact: true, permissions: [] },
  { routerLink: '/individual', title: 'My Identity', icon: 'fingerprint', permissions: ['individuals:view'] },
  { routerLink: '/kyc-record', title: 'KYC', icon: 'verified_user', permissions: ['kyc-records:view'] },
  { routerLink: '/organisation', title: 'My Organisation', icon: 'business', permissions: ['organisations:view'] },
  { routerLink: '/client-request', title: 'Client Requests', icon: 'request_quote', permissions: ['client-requests:list'] }
];

// The sidebar is hidden below md, so phones get a bottom bar instead.
export const mobileMenuItems: ShellMenuItem[] = [
  { routerLink: '/dashboard', title: 'Dashboard', icon: 'dashboard', exact: true, permissions: [] },
  { routerLink: '/individual', title: 'My Identity', icon: 'fingerprint', permissions: ['individuals:view'] },
  { routerLink: '/kyc-record', title: 'Personal KYC', icon: 'verified_user', permissions: ['kyc-records:view'] },
  { routerLink: '/organisation', title: 'Institutions', icon: 'business', permissions: ['organisations:view'] },
];
