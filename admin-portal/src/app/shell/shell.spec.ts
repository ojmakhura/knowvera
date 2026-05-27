import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Shell } from './shell';
import { provideRouter } from '@angular/router';
import { TranslationService } from '@core/services/translation.service';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from '@app/store/app-env.state';
import { BreakpointObserver } from '@angular/cdk/layout';
import { signal } from '@angular/core';
import { of } from 'rxjs';
import { Title } from '@angular/platform-browser';

describe('Shell', () => {
  let component: Shell;
  let fixture: ComponentFixture<Shell>;

  const translationServiceMock = {
    getAvailableLanguages: () => [],
    getCurrentLanguage: () => 'en',
    setLanguage: () => {},
  };

  const keycloakMock = {
    authenticated: false,
    login: () => Promise.resolve(),
    logout: () => Promise.resolve(),
  };

  const appEnvStoreMock = {
    profile: signal<any>(null),
    accountUri: signal(''),
    reset: () => {},
  };

  const breakpointObserverMock = {
    observe: () => of({ matches: false }),
    isMatched: () => false,
  };

  const titleMock = {
    getTitle: () => 'Central KYC Admin Portal',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Shell],
      providers: [
        provideRouter([]),
        { provide: TranslationService, useValue: translationServiceMock },
        { provide: Keycloak, useValue: keycloakMock },
        { provide: AppEnvStore, useValue: appEnvStoreMock },
        { provide: BreakpointObserver, useValue: breakpointObserverMock },
        { provide: Title, useValue: titleMock },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Shell);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render top bar', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.topbar')).toBeTruthy();
  });

  it('should render main content area', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.shell-main')).toBeTruthy();
  });

  it('should render login screen when unauthenticated', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.login-screen')).toBeTruthy();
  });

  it('should not render router-outlet when unauthenticated', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).toBeNull();
  });
});
