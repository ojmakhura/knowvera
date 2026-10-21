import { Component, computed, ElementRef, HostListener, inject, linkedSignal, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Title } from '@angular/platform-browser';
import { Route, Router, RouterModule, RouterOutlet, Routes } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from '@core/services/translation.service';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from '@app/store/app-env.state';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Loader } from '@app/@shared/loader/loader';
import { MenuService } from '@app/services/menu.service';
import { toast, NgxSonnerToaster } from 'ngx-sonner';
import { KeycloakHelperApiStore } from '@app/store/bw/co/knowvera/keycloak/keycloak-helper-api.store';
import { settingsMenuItems, ShellMenuItem } from './navigation';

@Component({
  selector: 'app-shell',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    TranslateModule,
    MatIconModule,
    MatButtonModule,
    Loader,
    NgxSonnerToaster
  ],
  templateUrl: './shell.html',
  styleUrls: ['./shell.scss'],
})
export class Shell implements OnInit {
  protected readonly accountMenuOpen = signal(false);
  private elementRef = inject(ElementRef<HTMLElement>);
  private titleService = inject(Title);
  protected router = inject(Router);
  protected translationService = inject(TranslationService);
  protected keycloak = inject(Keycloak);
  protected menuService = inject(MenuService);
  readonly appEnvState = inject(AppEnvStore);
  profile = this.appEnvState.profile;
  readonly keycloakHelperApiStore = inject(KeycloakHelperApiStore);

  availableLanguages = this.translationService.getAvailableLanguages();
  permissions = computed(() => this.keycloakHelperApiStore.permissions() ?? []);

  /** The current menu (main or settings), limited to the pages the user's permissions allow. */
  readonly menuItems = computed(() => this.menuService.items().filter((item) => canAccess(item, this.permissions())));
  /** First settings page the user may open; null hides the Settings links. */
  readonly settingsLink = computed(
    () => settingsMenuItems.find((item) => canAccess(item, this.permissions()))?.routerLink ?? null,
  );

  protected readonly toast = toast;

  static childRoutes(routes: Routes): Route {
    return {
      path: '',
      component: Shell,
      children: routes,
    };
  }

  userDisplayName = linkedSignal(() => {
    const userProfile = this.profile();
    if (!userProfile) return 'Compliance Officer';
    const fullName = `${userProfile.firstName || ''} ${userProfile.lastName || ''}`.trim();
    return fullName || userProfile.username || 'Compliance Officer';
  });

  readonly profileUrl = linkedSignal(() => this.appEnvState.accountUri() || '/settings');

  readonly title = computed(() => {
    return this.titleService.getTitle();
  });

  readonly currentLanguage = computed(() => {
    return this.translationService.getCurrentLanguage();
  });

  readonly userEmail = computed(() => {
    return this.profile()?.email || this.profile()?.username || '-';
  });

  readonly userInitials = computed(() => {
    const userProfile = this.profile();
    if (!userProfile) return 'CO';
    const first = userProfile.firstName?.[0] || '';
    const last = userProfile.lastName?.[0] || '';
    const initials = `${first}${last}`.toUpperCase();
    return initials || userProfile.username?.slice(0, 2).toUpperCase() || 'CO';
  });

  ngOnInit(): void {
  }

  logout() {
    this.accountMenuOpen.set(false);
    this.keycloak.logout();
    this.appEnvState.reset();
  }

  toggleAccountMenu(event: MouseEvent): void {
    event.stopPropagation();
    this.accountMenuOpen.set(!this.accountMenuOpen());
  }

  closeAccountMenu(): void {
    this.accountMenuOpen.set(false);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as Node | null;
    if (!target) return;
    if (!this.elementRef.nativeElement.contains(target)) {
      this.accountMenuOpen.set(false);
    }
  }

  @HostListener('document:keydown.escape')
  onEscapeKey(): void {
    this.accountMenuOpen.set(false);
  }

  login() {
    try {
      this.keycloak.login({ scope: 'openid profile email organization branch' });
    } catch (e) {
      console.error('Login failed', e);
    }
  }

  changeLanguage(lang: string): void {
    this.translationService.setLanguage(lang);
  }
}

/** Whether a user holding the `granted` permission scopes may see the menu item. */
export function canAccess(item: ShellMenuItem, granted: readonly string[]): boolean {
  return (
    item.permissions.length === 0 ||
    item.permissions.some((permission) => granted.includes(permission))
  );
}