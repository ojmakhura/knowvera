import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuardData, createAuthGuard } from 'keycloak-angular';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles } = authData;

  console.log(authData, _.url, grantedRoles);
  const router = inject(Router);

  if(_.url.startsWith('/register')) {

    if(!authenticated) {

      return false;
    }

    return router.parseUrl('/');
  }

  const hasRequiredRole = (role: string): boolean =>
    Object.values(grantedRoles.resourceRoles).some((roles) => roles.includes(role));

  if (authenticated) {
    return true;
  }

  return router.parseUrl('/forbidden');
};

export const authGuard = createAuthGuard<CanActivateFn>(isAccessAllowed);
