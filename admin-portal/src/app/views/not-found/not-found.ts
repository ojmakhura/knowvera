import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/** Shown for an address no route matches. */
@Component({
  selector: 'app-not-found',
  imports: [RouterLink],
  template: `
    <section class="mx-auto flex min-h-[60vh] max-w-md flex-col items-center justify-center gap-4 px-4 text-center">
      <h1 class="text-2xl font-semibold">Page not found</h1>
      <p class="text-gray-600">This address does not match any page of the admin portal.</p>
      <a class="rounded-md bg-gray-900 px-4 py-2 text-white" routerLink="/organisations">Go to organisations</a>
    </section>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NotFound {}
