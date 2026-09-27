export interface ShellMenuItem {
  routerLink: string;
  title: string;
  icon: string;
  exact?: boolean;
}

export const menuItems: ShellMenuItem[] = [
  { routerLink: '/dashboard', title: 'Dashboard', icon: 'dashboard', exact: true },
  { routerLink: '/individual', title: 'Identities', icon: 'fingerprint' },
  { routerLink: '/kyc-record', title: 'Verifications', icon: 'verified_user' },
  { routerLink: '/organisation', title: 'Institutions', icon: 'business' },
];

// The sidebar is hidden below md, so phones get a bottom bar instead.
export const mobileMenuItems: ShellMenuItem[] = [
  { routerLink: '/dashboard', title: 'Dashboard', icon: 'dashboard', exact: true },
  { routerLink: '/individual', title: 'Identities', icon: 'fingerprint' },
  { routerLink: '/kyc-record', title: 'Cases', icon: 'folder_special' },
  { routerLink: '/organisation', title: 'Settings', icon: 'settings' },
];
