import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';

import { TranslationExampleComponent } from './translation-example.component';
import { TranslationService } from '../services/translation.service';

describe('TranslationExampleComponent', () => {
  let component: TranslationExampleComponent;
  let fixture: ComponentFixture<TranslationExampleComponent>;

  const translationServiceMock = {
    instant: () => 'Loading...',
  } as unknown as TranslationService;

  const ngxTranslateServiceMock = {
    instant: () => '',
    get: () => ({ subscribe: () => ({}) }),
    stream: () => ({ subscribe: () => ({}) }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TranslationExampleComponent],
      providers: [
        { provide: TranslationService, useValue: translationServiceMock },
        { provide: TranslateService, useValue: ngxTranslateServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TranslationExampleComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
