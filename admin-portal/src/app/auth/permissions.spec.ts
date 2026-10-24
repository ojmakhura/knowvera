import { hasAnyPermission } from './permissions';
import { routes } from '../app.routes';
import { settingsMenuItems } from '../shell/navigation';

describe('hasAnyPermission', () => {
  const granted = ['documents:list', 'documents:view', 'kyc-records:view-own'];

  it('allows routes without permissions', () => {
    expect(hasAnyPermission([], undefined)).toBe(true);
    expect(hasAnyPermission([], [])).toBe(true);
  });

  it('needs any one of the required permissions', () => {
    expect(hasAnyPermission(granted, ['documents:edit', 'documents:view'])).toBe(true);
    expect(hasAnyPermission(granted, ['documents:edit'])).toBe(false);
  });

  it('accepts the owner-scoped variant of a permission', () => {
    expect(hasAnyPermission(granted, ['kyc-records:view'])).toBe(true);
    expect(hasAnyPermission(granted, ['kyc-records:edit'])).toBe(false);
  });

  it('does not treat a longer scope name as a match', () => {
    expect(hasAnyPermission(['documents:list'], ['documents:lis'])).toBe(false);
  });
});

describe('route protection', () => {
  it('guards every feature area of the portal', () => {
    const open = routes.filter((route) => route.path !== 'no-access' && !route.canActivate?.length);
    expect(open.map((route) => route.path)).toEqual([]);
  });

  it('gives every settings menu item a permission', () => {
    expect(settingsMenuItems.filter((item) => item.permissions.length === 0)).toEqual([]);
  });
});
