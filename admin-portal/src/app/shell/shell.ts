import { Component, ElementRef, HostListener, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Title } from '@angular/platform-browser';
import { Route, Router, RouterModule, RouterOutlet, Routes } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from '@core/services/translation.service';
import * as nav from './navigation';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from '@app/store/app-env.state';
import { MatIconModule } from '@angular/material/icon';

interface RecordRegistry {
  clientName: string;
  clientType: string;
  identityNo: string;
  status: 'Verified' | 'Pending' | 'Action Req';
  uploadDate: string;
  expiry: string;
}

@Component({
  selector: 'app-shell',
  imports: [CommonModule, RouterOutlet, RouterModule, TranslateModule, MatIconModule],
  templateUrl: './shell.html',
  styleUrls: ['./shell.scss'],
})
export class Shell {

  registryData: RecordRegistry[] = [
    {
      clientName: 'Acme Corp Ltd',
      clientType: 'Corporate',
      identityNo: 'ORG-88421-A',
      status: 'Verified',
      uploadDate: 'Oct 12, 2023',
      expiry: 'Oct 12, 2024'
    },
    {
      clientName: 'John Doe',
      clientType: 'Individual (UBO)',
      identityNo: 'IND-33921-X',
      status: 'Pending',
      uploadDate: 'Oct 14, 2023',
      expiry: '-'
    },
    {
      clientName: 'Global Finance LLC',
      clientType: 'Institutional',
      identityNo: 'ORG-11092-B',
      status: 'Action Req',
      uploadDate: 'Sep 01, 2022',
      expiry: 'Sep 01, 2023'
    }
  ];

  protected readonly accountMenuOpen = signal(false);
  private elementRef = inject(ElementRef<HTMLElement>);
  private titleService = inject(Title);
  protected router = inject(Router);
  protected translationService = inject(TranslationService);
  protected keycloak = inject(Keycloak);
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
