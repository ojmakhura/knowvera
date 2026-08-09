import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import Keycloak from 'keycloak-js';

import { AppEnvStore } from '@app/store/app-env.state';

interface NavigationItem {
  label: string;
  route: string;
  icon: string;
  exact?: boolean;
}

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, MatIconModule, MatMenuModule, MatDividerModule],
  templateUrl: './shell.html',
  styleUrls: ['./shell.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Shell {
  protected readonly appEnvState = inject(AppEnvStore);
  protected readonly profile = this.appEnvState.profile;

  private readonly keycloak = inject(Keycloak);

  protected readonly isLoggedIn = computed(() => this.appEnvState.isLoggedIn());
  protected readonly displayName = computed(() => {
    const profile = this.profile();

    return profile?.firstName?.trim() || profile?.username || 'Account';
  });

  protected readonly accountUri = computed(() => this.appEnvState.accountUri());

  protected readonly desktopNavigationItems = computed<NavigationItem[]>(() => [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard', exact: true },
    { label: 'Identities', route: '/individual', icon: 'fingerprint' },
    { label: 'Verifications', route: '/kyc-record', icon: 'verified_user' },
    { label: 'Risk Alerts', route: '/dashboard', icon: 'warning' },
    { label: 'Institutions', route: '/organisation', icon: 'business' },
  ]);

  protected readonly mobileNavigationItems = computed<NavigationItem[]>(() => [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard', exact: true },
    { label: 'Identities', route: '/individual', icon: 'fingerprint' },
    { label: 'Cases', route: '/kyc-record', icon: 'folder_special' },
    { label: 'Settings', route: '/organisation', icon: 'settings' },
  ]);

  protected readonly accountInitials = computed(() => {
    const profile = this.profile();
    const first = profile?.firstName?.trim()?.[0] ?? '';
    const last = profile?.lastName?.trim()?.[0] ?? '';
    const username = profile?.username?.trim()?.[0] ?? '';

    return ((`${first}${last}`.trim() || username || 'K')).toUpperCase();
  });

  protected async login(): Promise<void> {
    await this.keycloak.login({
      redirectUri: window.location.origin,
      scope: 'openid profile email organization',
    });
  }

  protected logout(): void {
    this.keycloak.logout();
    this.appEnvState.reset();
  }
}
