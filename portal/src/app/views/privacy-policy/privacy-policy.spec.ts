import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PrivacyPolicy } from './privacy-policy';

describe('PrivacyPolicy', () => {
  let component: PrivacyPolicy;
  let fixture: ComponentFixture<PrivacyPolicy>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrivacyPolicy],
      providers: [
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
    .compileComponents();

    fixture = TestBed.createComponent(PrivacyPolicy);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
