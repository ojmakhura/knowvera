import * as ViewModule from './invoice-details';

describe('invoice-details module', () => {
  it('should load module exports', () => {
    expect(ViewModule).toBeTruthy();
    expect(Object.keys(ViewModule).length).toBeGreaterThan(0);
  });
});
