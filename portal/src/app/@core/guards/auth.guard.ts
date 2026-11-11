import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuardData, createAuthGuard } from 'keycloak-angular';
import { PermissionService, hasAnyPermission } from '@app/@core/services/permission.service';

/** Client role of this portal's Keycloak client (PORTAL_ROLES in keycloak/generate_authz.py). */
const PORTAL_ROLE = 'PORTAL_USER';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles } = authData;

  const router = inject(Router);
  const permissionService = inject(PermissionService);

  if(_.url.startsWith('/register')) {
    // Registration links are opened by signed-out users; signed-in users go home
    return !authenticated || router.parseUrl('/');
  }

  if (authenticated) {
    // Signed in, but only users granted this portal's role may use it
    const clientId = authData.keycloak.clientId ?? '';
    if (!(grantedRoles.resourceRoles[clientId] ?? []).includes(PORTAL_ROLE)) {
      return router.parseUrl('/no-access');
    }

    // The route's permissions (data.permissions, any one is enough), as the API checks them
    const required = route.data?.['permissions'] as string[] | undefined;
    if (!required?.length) {
      return true;
    }
    return hasAnyPermission(await permissionService.load(), required) || router.parseUrl('/no-access');
  }

  return router.parseUrl('/forbidden');
};

export const authGuard = createAuthGuard<CanActivateFn>(isAccessAllowed);
