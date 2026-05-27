import { documentRoutes } from './document.routes';

describe('documentRoutes', () => {
  it('should define expected paths', () => {
    expect(documentRoutes.map((route) => route.path)).toEqual([
      '',
      'edit',
      'edit/:id',
      'details',
      'details/:id',
    ]);
  });

  it('should provide lazy load functions for every route', async () => {
    for (const route of documentRoutes) {
      expect(typeof route.loadComponent).toBe('function');
      const loaded = await route.loadComponent?.();
      expect(loaded).toBeTruthy();
    }
  });
});
