import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TermsOfService } from './terms-of-service';

describe('TermsOfService', () => {
  let component: TermsOfService;
  let fixture: ComponentFixture<TermsOfService>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TermsOfService],
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

    fixture = TestBed.createComponent(TermsOfService);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
