import { Injectable, computed, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { menuItems, settingsMenuItems, ShellMenuItem } from '@app/shell/navigation';
// import { menuItems, settingsMenuItems, ShellMenuItem } from './navigation';

export type ShellMode = 'normal' | 'settings';

@Injectable({ providedIn: 'root' })
export class MenuService {
  private router = inject(Router);

  readonly mode = toSignal<ShellMode>(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map((e) => (e.urlAfterRedirects.startsWith('/settings') ? 'settings' : 'normal')),
    ),
    // { initialValue: this.router.url.startsWith('/settings') ? 'settings' : 'normal' },
  );

  readonly items = computed<ShellMenuItem[]>(() =>
    this.mode() === 'normal' ? menuItems : settingsMenuItems,
  );

  readonly sidebarTitle = computed(() =>
    this.mode() === 'normal' ? 'Knowvera' : 'System Settings',
  );

  readonly sidebarSubtitle = computed(() =>
    this.mode() === 'normal' ? 'Compliance Portal' : 'Configuration',
  );

  exitSettings(): void {
    this.router.navigateByUrl('/organisation');
  }
}