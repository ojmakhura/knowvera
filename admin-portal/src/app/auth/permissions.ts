import { Injector, inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { filter, firstValueFrom, take } from 'rxjs';
import { KeycloakHelperApiStore } from '@app/store/bw/co/knowvera/keycloak/keycloak-helper-api.store';

/**
 * Suffix of owner-scoped permissions (keycloak/generate_authz.py): "documents:view-own" lets the
 * holder do "documents:view" on their own records, so it unlocks the same pages.
 */
const OWN = '-own';

/** Whether `granted` holds any of `required` (as such or as its "-own" variant); empty means anyone. */
export function hasAnyPermission(granted: readonly string[], required: readonly string[] | undefined): boolean {
  return (
    !required ||
    required.length === 0 ||
    required.some((permission) => granted.includes(permission) || granted.includes(permission + OWN))
  );
}

/**
 * The signed-in user's permissions (GET /keycloak/permissions), loading them on first use.
 * Call from an injection context, such as a route guard, before any `await`.
 */
export function userPermissions(): () => Promise<readonly string[]> {
  const store = inject(KeycloakHelperApiStore);
  const injector = inject(Injector);

  return async () => {
    if (!store.permissionsLoaded()) {
      store.getUserPermissions();
      await firstValueFrom(toObservable(store.permissionsLoaded, { injector }).pipe(filter(Boolean), take(1)));
    }
    return store.permissions() ?? [];
  };
}
