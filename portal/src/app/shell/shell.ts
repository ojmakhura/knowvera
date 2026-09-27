import { Component, ElementRef, HostListener, computed, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import Keycloak from 'keycloak-js';
import { NgxSonnerToaster } from 'ngx-sonner';
import { toast } from '@app/@shared/toast';

import { AppEnvStore } from '@app/store/app-env.state';
import { Loader } from '@app/@shared/loader/loader';
import { mobileMenuItems, menuItems } from './navigation';

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, MatButtonModule, MatIconModule, NgxSonnerToaster, Loader],
  templateUrl: './shell.html',
  styleUrls: ['./shell.scss'],
})
export class Shell {
  protected readonly appEnvState = inject(AppEnvStore);
  protected readonly profile = this.appEnvState.profile;

  private readonly keycloak = inject(Keycloak);
  private readonly elementRef = inject(ElementRef<HTMLElement>);
  protected readonly toast = toast;

  protected readonly menuItems = menuItems;
  protected readonly mobileMenuItems = mobileMenuItems;
  protected readonly accountMenuOpen = signal(false);

  protected readonly isLoggedIn = computed(() => this.appEnvState.isLoggedIn());

  protected readonly userDisplayName = computed(() => {
    const profile = this.profile();
    const fullName = `${profile?.firstName || ''} ${profile?.lastName || ''}`.trim();

    return fullName || profile?.username || 'Account';
  });

  protected readonly userEmail = computed(() => this.profile()?.email || this.profile()?.username || '');

  protected readonly userInitials = computed(() => {
    const profile = this.profile();
    const initials = `${profile?.firstName?.trim()?.[0] ?? ''}${profile?.lastName?.trim()?.[0] ?? ''}`;

    return (initials || profile?.username?.slice(0, 2) || 'K').toUpperCase();
  });

  protected readonly profileUrl = computed(() => this.appEnvState.accountUri());

  protected toggleAccountMenu(event: MouseEvent): void {
    event.stopPropagation();
    this.accountMenuOpen.update((open) => !open);
  }

  protected closeAccountMenu(): void {
    this.accountMenuOpen.set(false);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as Node | null;
    if (target && !this.elementRef.nativeElement.contains(target)) {
      this.accountMenuOpen.set(false);
    }
  }

  @HostListener('document:keydown.escape')
  onEscapeKey(): void {
    this.accountMenuOpen.set(false);
  }

  protected async login(): Promise<void> {
    await this.keycloak.login({
      redirectUri: window.location.origin,
      scope: 'openid profile email organization',
    });
  }

  protected logout(): void {
    this.accountMenuOpen.set(false);
    this.keycloak.logout();
    this.appEnvState.reset();
  }
}
