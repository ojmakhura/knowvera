import { Routes } from '@angular/router';
import { routes } from '../app.routes';
import { individualRoutes } from '../views/individual/individual-routes';
import { menuItems, mobileMenuItems } from './navigation';

/** data.permissions of the page a menu link opens (a lazy area's '' child counts for the area). */
function routePermissions(link: string): string[] | undefined {
  const path = link.replace(/^\//, '');
  const route = routes.find((r) => r.path === path);
  const children: Routes = path === 'individual' ? individualRoutes : [];
  const page = children.find((r) => r.path === '') ?? route;
  return (page?.data?.['permissions'] as string[] | undefined) ?? [];
}

describe('portal menu', () => {
  for (const item of [...menuItems, ...mobileMenuItems]) {
    it(`${item.routerLink} needs the same permissions as its page`, () => {
      expect(item.permissions).toEqual(routePermissions(item.routerLink));
    });
  }
});
