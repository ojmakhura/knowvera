import { ChangeDetectionStrategy, Component, computed, effect, inject, OnInit, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AppEnvStore } from '@app/store/app-env.state';
import Keycloak from 'keycloak-js';
import { CommonModule } from '@angular/common';

interface NavigationItem {
  label: string;
  route: string;
  icon: string;
  display: boolean; // Optional property to control visibility
}

@Component({
  selector: 'app-shell',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    TranslateModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule
  ],
  templateUrl: './shell.html',
  styleUrls: ['./shell.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Shell  implements OnInit {
  protected readonly isSidebarCollapsed = signal(false);
  protected readonly isMobileMenuOpen = signal(false);
  private keycloak = inject(Keycloak);
  readonly appEnvState = inject(AppEnvStore);
  profile = this.appEnvState.profile;

  readonly hasOrg = computed(() => !!this.appEnvState.userOrganisation());

  protected readonly sidebarClass = computed(() =>
    this.isSidebarCollapsed() ? 'sidebar--collapsed' : '',
  );

  protected readonly navigationItems = signal<NavigationItem[]>([
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard', display: true },
    // { label: 'Identity Documents', route: '/kyc-record', icon: 'badge', display: true },
    { label: 'Personal Info', route: '/individual', icon: 'person', display: true },
    { label: 'My Organisation', route: '/organisation', icon: 'business', display: true },
    // { label: 'Verification Status', route: '/dashboard', icon: 'fact_check', display: true },
  ]);

  constructor() {
    console.log('Shell component initialized', this.keycloak.profile);

    effect(() => {
      let orgAvailable = this.hasOrg();

      this.navigationItems.update(items =>
        items.map(item => {
          if (item.route === '/organisation') {
            return { ...item, display: orgAvailable };
          }
          return item;
        })
      );
    });
  }
  ngOnInit(): void {
    // No-op for now; component keeps OnInit for future shell-level data hooks.
  }

  protected toggleSidebar(): void {
    this.isSidebarCollapsed.update((collapsed) => !collapsed);
  }

  protected toggleMobileMenu(): void {
    this.isMobileMenuOpen.update((open) => !open);
  }

  protected closeMobileMenu(): void {
    this.isMobileMenuOpen.set(false);
  }

  logout() {
    console.log('Logout clicked');
    this.keycloak.logout();
    this.appEnvState.reset();
  }

  async login(redirectUri?: string): Promise<void> {
    console.log('Login clicked');
    try {
      await this.keycloak.login({
        redirectUri: redirectUri || window.location.origin,
        scope: 'openid profile email organization'
      });
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  }
}
