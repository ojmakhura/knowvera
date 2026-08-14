import * as ViewModule from './create-client-request-dialog';

describe('create-client-request-dialog module', () => {
  it('should load module exports', () => {
    expect(ViewModule).toBeTruthy();
    expect(Object.keys(ViewModule).length).toBeGreaterThan(0);
  });
});
