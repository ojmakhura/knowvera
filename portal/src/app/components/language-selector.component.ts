import { Component, inject } from '@angular/core';
import { TranslationService, type Language } from '../services/translation.service';

@Component({
  selector: 'app-language-selector',
  imports: [],
  template: `
    <div class="language-selector">
      @for (lang of translationService.availableLanguages; track lang) {
        <button
          type="button"
          class="btn btn-sm"
          [class.btn-primary]="translationService.currentLang() === lang"
          [class.btn-outline-primary]="translationService.currentLang() !== lang"
          (click)="selectLanguage(lang)">
          {{ lang.toUpperCase() }}
        </button>
      }
    </div>
  `,
  styles: `
    .language-selector {
      display: flex;
      gap: 0.5rem;

      button {
        min-width: 50px;
      }
    }
  `
})
export class LanguageSelectorComponent {
  readonly translationService = inject(TranslationService);

  selectLanguage(lang: Language): void {
    this.translationService.setLanguage(lang);
  }
}
