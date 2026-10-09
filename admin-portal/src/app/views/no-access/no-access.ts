import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import Keycloak from 'keycloak-js';

/** Shown to signed-in users whose account may not use this portal (see the route guard). */
@Component({
  selector: 'app-no-access',
  template: `
    <section class="mx-auto flex min-h-[60vh] max-w-md flex-col items-center justify-center gap-4 px-4 text-center">
      <h1 class="text-2xl font-semibold">No access to this portal</h1>
      <p class="text-gray-600">
        You are signed in as <strong>{{ username }}</strong>, but this account cannot use {{ portal }}.
        Sign out and sign in with another account, or ask an administrator for access.
      </p>
      <button type="button" class="rounded-md bg-gray-900 px-4 py-2 text-white" (click)="signOut()">
        Sign out
      </button>
    </section>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NoAccess {
  private readonly keycloak = inject(Keycloak);

  protected readonly portal = 'the admin portal';
  protected readonly username = this.keycloak.tokenParsed?.['preferred_username'] ?? '';

  protected signOut(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }
}
