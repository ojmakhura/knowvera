import { individualRoutes } from './individual.routes';

describe('individualRoutes', () => {
  it('should define expected paths', () => {
    expect(individualRoutes.map((route) => route.path)).toEqual([
      '',
      'edit',
      'edit/:id',
      'details',
      'details/:id',
    ]);
  });

  it('should expose lazy load functions', async () => {
    for (const route of individualRoutes) {
      expect(typeof route.loadComponent).toBe('function');
      const loaded = await route.loadComponent?.();
      expect(loaded).toBeTruthy();
    }
  });
});
