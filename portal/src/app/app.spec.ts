import { TestBed } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';
import Keycloak from 'keycloak-js';
import { KEYCLOAK_EVENT_SIGNAL } from 'keycloak-angular';
import { of } from 'rxjs';

import { App } from './app';
import { IndividualApi } from './services/bw/co/centralkyc/individual/individual-api';
import { OrganisationApi } from './services/bw/co/centralkyc/organisation/organisation-api';
import { AppEnvStore } from './store/app-env.state';

describe('App', () => {
  const translateServiceMock = {
    getBrowserLang: () => 'en',
    addLangs: () => undefined,
    setDefaultLang: () => undefined,
    setFallbackLang: () => undefined,
    use: () => undefined,
    instant: () => '',
    get: () => of(''),
    stream: () => of(''),
  };

  const keycloakMock = {
    authenticated: false,
    loadUserProfile: async () => ({ firstName: '', lastName: '', email: '', username: '' }),
    loadUserInfo: async () => ({}),
  };

  const appEnvStoreMock = {
    env: () => null,
    userOrganisation: () => null,
    getEnv: () => undefined,
    setIsLoggedIn: () => undefined,
    setProfile: () => undefined,
    setIndividual: () => undefined,
    setUserOrganisation: () => undefined,
    reset: () => undefined,
    setAccountUri: () => undefined,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        { provide: TranslateService, useValue: translateServiceMock },
        { provide: Keycloak, useValue: keycloakMock },
        { provide: KEYCLOAK_EVENT_SIGNAL, useValue: () => ({ type: 'UNHANDLED', args: null }) },
        { provide: IndividualApi, useValue: { loadMe: () => of({}) } },
        { provide: OrganisationApi, useValue: { loadMyOrganisation: () => of({}) } },
        { provide: AppEnvStore, useValue: appEnvStoreMock },
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should expose title signal', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance as any;
    expect(app.title()).toBe('portal');
  });
});
