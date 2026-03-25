import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Component, ElementRef, HostListener, inject, signal, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { Title } from '@angular/platform-browser';
import { Route, Router, RouterModule, RouterOutlet, Routes } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from '@core/services/translation.service';
import * as nav from './navigation';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from '@app/store/app-env.state';

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterModule, TranslateModule],
  templateUrl: './shell.html',
  styleUrl: './shell.scss',
})
export class Shell {
  @ViewChild('sidenav') sidenav!: MatDrawer;

  protected readonly currentYear = new Date().getFullYear();
  protected readonly accountMenuOpen = signal(false);
  private breakpoint = inject(BreakpointObserver);
  private elementRef = inject(ElementRef<HTMLElement>);
  private titleService = inject(Title);
  protected router = inject(Router);
  protected translationService = inject(TranslationService);
  private keycloak = inject(Keycloak);
  readonly appEnvState = inject(AppEnvStore);
  profile = this.appEnvState.profile;

  menus: any[] = [];
  availableLanguages = this.translationService.getAvailableLanguages();

  static childRoutes(routes: Routes): Route {
    return {
      path: '',
      component: Shell,
      children: routes,
    };
  }

  ngOnInit() {
    this.menus = nav.menuItems;

    // Watch for breakpoint changes and adjust drawer accordingly
    this.breakpoint.observe([Breakpoints.Small, Breakpoints.XSmall]).subscribe(result => {
      if (this.sidenav) {
        if (result.matches) {
          // Mobile: close drawer
          this.sidenav.close();
        } else {
          // Desktop: open drawer
          this.sidenav.open();
        }
      }
    });
  }

  logout() {
    console.log('Logout clicked');
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

  get userDisplayName(): string {
    const userProfile = this.profile();

    if (!userProfile) {
      return 'Compliance Officer';
    }

    const fullName = `${userProfile.firstName || ''} ${userProfile.lastName || ''}`.trim();
    return fullName || userProfile.username || 'Compliance Officer';
  }

  get userEmail(): string {
    return this.profile()?.email || this.profile()?.username || 'admin@vault-tech.io';
  }

  get userInitials(): string {
    const userProfile = this.profile();

    if (!userProfile) {
      return 'CO';
    }

    const first = userProfile.firstName?.[0] || '';
    const last = userProfile.lastName?.[0] || '';
    const initials = `${first}${last}`.toUpperCase();

    return initials || userProfile.username?.slice(0, 2).toUpperCase() || 'CO';
  }

  get profileUrl(): string {
    return this.appEnvState.accountUri() || '/settings';
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as Node | null;

    if (!target) {
      return;
    }

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
      this.keycloak.login({
        scope: 'openid profile email organization branch',
      });
    } catch (e) {
      console.error('Login failed', e);
    }
  }

  get isMobile(): boolean {
    return this.breakpoint.isMatched(Breakpoints.Small) || this.breakpoint.isMatched(Breakpoints.XSmall);
  }

  get title(): string {
    return this.titleService.getTitle();
  }

  get currentLanguage(): string {
    return this.translationService.getCurrentLanguage();
  }

  changeLanguage(lang: string): void {
    this.translationService.setLanguage(lang);
  }

}
