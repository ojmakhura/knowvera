import * as ViewModule from './organisation.routes';

describe('organisation.routes module', () => {
  it('should load module exports', () => {
    expect(ViewModule).toBeTruthy();
    expect(Object.keys(ViewModule).length).toBeGreaterThan(0);
  });
});
