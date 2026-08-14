import * as ViewModule from './add-kyc-field-group-dialog';

describe('add-kyc-field-group-dialog module', () => {
  it('should load module exports', () => {
    expect(ViewModule).toBeTruthy();
    expect(Object.keys(ViewModule).length).toBeGreaterThan(0);
  });
});
