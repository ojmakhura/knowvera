import { Injectable, computed, inject } from '@angular/core';
import { Location } from '@angular/common';
import { NavigationEnd, Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { menuItems, settingsMenuItems, ShellMenuItem } from '@app/shell/navigation';
// import { menuItems, settingsMenuItems, ShellMenuItem } from './navigation';

export type ShellMode = 'normal' | 'settings';

@Injectable({ providedIn: 'root' })
export class MenuService {
  private router = inject(Router);
  private location = inject(Location);

  readonly mode = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map((e): ShellMode => (e.urlAfterRedirects.startsWith('/settings') ? 'settings' : 'normal')),
    ),
    // router.url is still '/' before the first navigation, so read the browser path instead.
    { initialValue: (this.location.path().startsWith('/settings') ? 'settings' : 'normal') as ShellMode },
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
    this.router.navigateByUrl('/organisations');
  }
}