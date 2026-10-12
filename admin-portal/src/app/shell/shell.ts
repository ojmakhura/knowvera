import { Component, ElementRef, HostListener, inject, OnInit, signal } from '@angular/core';
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

  availableLanguages = this.translationService.getAvailableLanguages();

  protected readonly toast = toast;

  static childRoutes(routes: Routes): Route {
    return {
      path: '',
      component: Shell,
      children: routes,
    };
  }

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

  get userDisplayName(): string {
    const userProfile = this.profile();
    if (!userProfile) return 'Compliance Officer';
    const fullName = `${userProfile.firstName || ''} ${userProfile.lastName || ''}`.trim();
    return fullName || userProfile.username || 'Compliance Officer';
  }

  get userEmail(): string {
    return this.profile()?.email || this.profile()?.username || 'admin@vault-tech.io';
  }

  get userInitials(): string {
    const userProfile = this.profile();
    if (!userProfile) return 'CO';
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