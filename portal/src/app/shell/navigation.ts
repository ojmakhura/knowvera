export interface ShellMenuItem {
  routerLink: string;
  title: string;
  icon: string;
  exact?: boolean;
}

export const menuItems: ShellMenuItem[] = [
  { routerLink: '/dashboard', title: 'Dashboard', icon: 'dashboard', exact: true },
  { routerLink: '/individual', title: 'My Identity', icon: 'fingerprint' },
  { routerLink: '/kyc-record', title: 'KYC', icon: 'verified_user' },
  { routerLink: '/organisation', title: 'My Organisation', icon: 'business' },
  { routerLink: '/client-request', title: 'Client Requests', icon: 'request_quote' }
];

// The sidebar is hidden below md, so phones get a bottom bar instead.
export const mobileMenuItems: ShellMenuItem[] = [
  { routerLink: '/dashboard', title: 'Dashboard', icon: 'dashboard', exact: true },
  { routerLink: '/individual', title: 'My Identity', icon: 'fingerprint' },
  { routerLink: '/kyc-record', title: 'Personal KYC', icon: 'verified_user' },
  { routerLink: '/organisation', title: 'Institutions', icon: 'business' },
];
