import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import Keycloak from 'keycloak-js';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EditKycRecord } from './edit-kyc-record';

describe('EditKycRecord', () => {
  let component: EditKycRecord;
  let fixture: ComponentFixture<EditKycRecord>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditKycRecord, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        {
          provide: Keycloak,
          useValue: {
            authenticated: false,
            hasRealmRole: () => false,
            hasResourceRole: () => false,
            profile: null,
          },
        },
        {
          provide: ToastrService,
          useValue: {
            success: () => undefined,
            error: () => undefined,
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
    }).compileComponents();

    fixture = TestBed.createComponent(EditKycRecord);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
