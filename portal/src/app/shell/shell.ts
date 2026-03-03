import { Component, computed, inject, signal } from '@angular/core';
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
})
export class Shell {
  protected readonly isSidebarCollapsed = signal(false);
  protected readonly isMobileMenuOpen = signal(false);
  private keycloak = inject(Keycloak);
  readonly appEnvState = inject(AppEnvStore);
  profile = this.appEnvState.profile;

  constructor() {
    console.log('Shell component initialized', this.keycloak.profile);
  }

  protected readonly sidebarClass = computed(() =>
    this.isSidebarCollapsed() ? 'sidebar--collapsed' : '',
  );

  protected readonly navigationItems = signal<NavigationItem[]>([
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
    { label: 'Profile', route: '/profile', icon: 'person' },
    { label: 'Settings', route: '/settings', icon: 'settings' },
  ]);

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
        scope: 'openid profile email organization branch'
      });
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  }
}
