import { TestBed } from '@angular/core/testing';
import { signal, WritableSignal } from '@angular/core';
import { provideRouter } from '@angular/router';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import Keycloak from 'keycloak-js';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from '@core/services/translation.service';
import { AppEnvStore } from './store/app-env.state';
import { SettingsApiStore } from './store/bw/co/centralkyc/settings/settings-api.store';
import { App } from './app';

describe('App', () => {
  let keycloakEventSignal: WritableSignal<{ type: KeycloakEventType; args?: unknown }>;

  let appEnvStoreMock: {
    env: WritableSignal<any>;
    accountUri: WritableSignal<string>;
    profile: WritableSignal<any>;
    getEnv: ReturnType<typeof vi.fn>;
    setIsLoggedIn: ReturnType<typeof vi.fn>;
    setProfile: ReturnType<typeof vi.fn>;
    reset: ReturnType<typeof vi.fn>;
    setAccountUri: ReturnType<typeof vi.fn>;
  };

  let translationServiceMock: {
    getAvailableLanguages: ReturnType<typeof vi.fn>;
    getCurrentLanguage: ReturnType<typeof vi.fn>;
    setLanguage: ReturnType<typeof vi.fn>;
  };

  let keycloakMock: {
    authenticated: boolean;
    loadUserProfile: ReturnType<typeof vi.fn>;
    login: ReturnType<typeof vi.fn>;
    logout: ReturnType<typeof vi.fn>;
  };

  let settingsApiStoreMock: {
    getAll: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    keycloakEventSignal = signal({
      type: KeycloakEventType.Ready,
      args: false,
    });

    appEnvStoreMock = {
      env: signal<any>(null),
      accountUri: signal(''),
      profile: signal<any>(null),
      getEnv: vi.fn(),
      setIsLoggedIn: vi.fn(),
      setProfile: vi.fn(),
      reset: vi.fn(),
      setAccountUri: vi.fn(),
    };

    translationServiceMock = {
      getAvailableLanguages: vi.fn(() => []),
      getCurrentLanguage: vi.fn(() => 'en'),
      setLanguage: vi.fn(),
    };

    keycloakMock = {
      authenticated: false,
      loadUserProfile: vi.fn(() => Promise.resolve({})),
      login: vi.fn(() => Promise.resolve()),
      logout: vi.fn(() => Promise.resolve()),
    };

    settingsApiStoreMock = {
      getAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [App, TranslateModule.forRoot()],
      providers: [
        provideRouter([]),
        {
          provide: KEYCLOAK_EVENT_SIGNAL,
          useValue: keycloakEventSignal,
        },
        { provide: Keycloak, useValue: keycloakMock },
        { provide: TranslationService, useValue: translationServiceMock },
        { provide: AppEnvStore, useValue: appEnvStoreMock },
        { provide: SettingsApiStore, useValue: settingsApiStoreMock },
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render shell', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('app-shell')).toBeTruthy();
  });

  it('should load settings during construction', () => {
    TestBed.createComponent(App);

    expect(settingsApiStoreMock.getAll).toHaveBeenCalledTimes(1);
  });

  it('should fetch env and profile when keycloak is ready and authenticated', async () => {
    keycloakMock.authenticated = true;
    keycloakMock.loadUserProfile.mockResolvedValue({
      firstName: 'Jane',
      lastName: 'Doe',
      email: 'jane@example.com',
      username: 'jdoe',
    });
    keycloakEventSignal.set({
      type: KeycloakEventType.Ready,
      args: true,
    });

    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    await fixture.whenStable();

    expect(appEnvStoreMock.getEnv).toHaveBeenCalledTimes(1);
    expect(appEnvStoreMock.setIsLoggedIn).toHaveBeenCalledWith(true);
    expect(keycloakMock.loadUserProfile).toHaveBeenCalledTimes(1);
    expect(appEnvStoreMock.setProfile).toHaveBeenCalledWith({
      firstName: 'Jane',
      lastName: 'Doe',
      email: 'jane@example.com',
      username: 'jdoe',
    });
  });

  it('should reset app environment when auth logout event is emitted', () => {
    const fixture = TestBed.createComponent(App);

    keycloakEventSignal.set({ type: KeycloakEventType.AuthLogout });
    fixture.detectChanges();

    expect(appEnvStoreMock.reset).toHaveBeenCalledTimes(1);
  });

  it('should set account uri when environment data becomes available', () => {
    const fixture = TestBed.createComponent(App);

    appEnvStoreMock.env.set({
      authDomain: 'https://auth.example.com',
      realm: 'central',
      clientId: 'admin-portal',
      redirectUri: 'https://portal.example.com',
    });
    fixture.detectChanges();

    expect(appEnvStoreMock.setAccountUri).toHaveBeenCalledTimes(1);
    expect(appEnvStoreMock.setAccountUri).toHaveBeenCalledWith(
      "https://auth.example.com/realms/central/account?referrer=' + admin-portal&referrer_uri=' + https%3A%2F%2Fportal.example.com",
    );
  });
});
