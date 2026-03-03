import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Component, inject, ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { Title } from '@angular/platform-browser';
import { Route, Router, RouterModule, RouterOutlet, Routes } from '@angular/router';
import { MaterialModule } from '@app/material.module';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from '@core/services/translation.service';
import * as nav from './navigation';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from '@app/store/app-env.state';

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, MaterialModule, RouterModule, TranslateModule],
  templateUrl: './shell.html',
  styleUrl: './shell.scss',
})
export class Shell {
  @ViewChild('sidenav') sidenav!: MatDrawer;

  protected readonly currentYear = new Date().getFullYear();
  private breakpoint = inject(BreakpointObserver);
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
    this.keycloak.logout();
    this.appEnvState.reset();
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
