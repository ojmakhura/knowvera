import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuardData, createAuthGuard } from 'keycloak-angular';
import { hasAnyPermission, userPermissions } from './permissions';

/** Client role of this portal's Keycloak client (PORTAL_ROLES in keycloak/generate_authz.py). */
const PORTAL_ROLE = 'ADMIN_PORTAL_USER';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles } = authData;
  // Injected before any await: guards may only inject synchronously
  const permissions = userPermissions();

  if(!authenticated) {

    authData.keycloak.login({
      scope: 'openid profile email organization branch',
    });
  }

  const router = inject(Router);

  if (authenticated) {
    // Signed in, but only users granted this portal's role may use it
    const clientId = authData.keycloak.clientId ?? '';
    if (!(grantedRoles.resourceRoles[clientId] ?? []).includes(PORTAL_ROLE)) {
      return router.parseUrl('/no-access');
    }

    // The route's permissions (data.permissions, any one is enough), as the API checks them
    const required = route.data?.['permissions'] as string[] | undefined;
    return hasAnyPermission(await permissions(), required) || router.parseUrl('/no-access');
  }

  return router.parseUrl('/forbidden');
};

export const AuthenticationGuard = createAuthGuard<CanActivateFn>(isAccessAllowed);
