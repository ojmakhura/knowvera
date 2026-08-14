import * as ViewModule from './sequences';

describe('sequences module', () => {
  it('should load module exports', () => {
    expect(ViewModule).toBeTruthy();
    expect(Object.keys(ViewModule).length).toBeGreaterThan(0);
  });
});
