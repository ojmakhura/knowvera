import { AuthenticationGuard } from './authentication.guard';

describe('AuthenticationGuard', () => {
  it('should be defined', () => {
    expect(AuthenticationGuard).toBeTruthy();
  });

  it('should expose a function guard', () => {
    expect(typeof AuthenticationGuard).toBe('function');
  });
});
