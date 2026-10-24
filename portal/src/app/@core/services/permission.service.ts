import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';

/**
 * Suffix of owner-scoped permissions (keycloak/generate_authz.py): this portal's users mostly
 * hold "<resource>:<scope>-own", which unlocks the same pages for their own records.
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

/** The signed-in user's permissions (GET /keycloak/permissions), loaded once and shared. */
@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  private http = inject(HttpClient);
  private request?: Promise<readonly string[]>;

  /** The loaded permissions; empty until load() completes. */
  readonly permissions = signal<readonly string[]>([]);

  /** Whether load() has finished (also after a failure, with no permissions). */
  readonly loaded = signal(false);

  load(): Promise<readonly string[]> {
    this.request ??= firstValueFrom(this.http.get<string[]>('/keycloak/permissions'))
      .then((permissions) => {
        this.permissions.set(permissions ?? []);
        this.loaded.set(true);
        return this.permissions();
      })
      .catch(() => {
        // Try again on the next navigation; meanwhile the user has no permissions
        this.request = undefined;
        this.loaded.set(true);
        return [] as readonly string[];
      });
    return this.request;
  }

  has(required: readonly string[] | undefined): boolean {
    return hasAnyPermission(this.permissions(), required);
  }
}
