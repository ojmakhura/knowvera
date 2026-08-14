import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import Keycloak from 'keycloak-js';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { Home } from './home';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Home],
      providers: [
        provideRouter([]),
        {
          provide: Keycloak,
          useValue: {
            authenticated: false,
            login: async () => undefined,
            logout: async () => undefined,
            hasRealmRole: () => false,
            updateToken: async () => true,
          },
        },
        {
          provide: TranslateService,
          useValue: {
            getBrowserLang: () => 'en',
            addLangs: () => undefined,
            setDefaultLang: () => undefined,
            setFallbackLang: () => undefined,
            use: () => undefined,
            instant: () => '',
            get: () => of(''),
            stream: () => of(''),
            onLangChange: of({ lang: 'en', translations: {} }),
            onTranslationChange: of({ lang: 'en', translations: {} }),
            onDefaultLangChange: of({ lang: 'en', translations: {} }),
          },
        },
      ],
    })
    .overrideComponent(Home, {
      set: {
        template: '',
      },
    })
    .compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
