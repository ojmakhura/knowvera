import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuardData, createAuthGuard } from 'keycloak-angular';

/** Client role of this portal's Keycloak client (PORTAL_ROLES in keycloak/generate_authz.py). */
const PORTAL_ROLE = 'PORTAL_USER';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles } = authData;

  const router = inject(Router);

  if(_.url.startsWith('/register')) {

    if(!authenticated) {

      return false;
    }

    return router.parseUrl('/');
  }

  if (authenticated) {
    // Signed in, but only users granted this portal's role may use it
    const clientId = authData.keycloak.clientId ?? '';
    return (grantedRoles.resourceRoles[clientId] ?? []).includes(PORTAL_ROLE) || router.parseUrl('/no-access');
  }

  return router.parseUrl('/forbidden');
};

export const authGuard = createAuthGuard<CanActivateFn>(isAccessAllowed);
