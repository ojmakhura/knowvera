import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PermissionService, hasAnyPermission } from './permission.service';

describe('hasAnyPermission', () => {
  it('accepts a permission or its owner-scoped variant', () => {
    expect(hasAnyPermission(['individuals:view-own'], ['individuals:view'])).toBe(true);
    expect(hasAnyPermission(['individuals:view-own'], ['individuals:edit'])).toBe(false);
    expect(hasAnyPermission([], undefined)).toBe(true);
  });
});

describe('PermissionService', () => {
  let service: PermissionService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(PermissionService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads the permissions once and shares them', async () => {
    const first = service.load();
    const second = service.load();
    http.expectOne('/keycloak/permissions').flush(['documents:view-own']);

    expect(await first).toEqual(['documents:view-own']);
    expect(await second).toEqual(['documents:view-own']);
    expect(service.has(['documents:view'])).toBe(true);
  });

  it('loads again after a failed request', async () => {
    const failed = service.load();
    http.expectOne('/keycloak/permissions').flush('down', { status: 503, statusText: 'Unavailable' });
    expect(await failed).toEqual([]);

    const retried = service.load();
    http.expectOne('/keycloak/permissions').flush(['kyc-records:view-own']);
    expect(await retried).toEqual(['kyc-records:view-own']);
  });
});
