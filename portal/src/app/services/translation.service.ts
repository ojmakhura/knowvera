import { Injectable, signal, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

export type Language = 'en' | 'es';

const AVAILABLE_LANGUAGES: Language[] = ['en', 'es'];
const DEFAULT_LANGUAGE: Language = 'en';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private readonly translate = inject(TranslateService);

  currentLang = signal<Language>(DEFAULT_LANGUAGE);
  availableLanguages: Language[] = AVAILABLE_LANGUAGES;

  constructor() {
    this.initializeLanguage();
  }

  private initializeLanguage(): void {
    const savedLang = localStorage.getItem('language');
    const browserLang = this.translate.getBrowserLang();
    const initialLanguage = this.resolveLanguage(savedLang ?? browserLang);

    this.translate.addLangs(AVAILABLE_LANGUAGES);
    this.translate.setDefaultLang(DEFAULT_LANGUAGE);
    this.translate.setFallbackLang(DEFAULT_LANGUAGE);
    this.translate.use(initialLanguage);
    this.currentLang.set(initialLanguage);
  }

  setLanguage(lang: Language): void {
    this.translate.use(lang);
    this.currentLang.set(lang);
    localStorage.setItem('language', lang);
  }

  private resolveLanguage(candidate: string | undefined | null): Language {
    return AVAILABLE_LANGUAGES.includes(candidate as Language)
      ? (candidate as Language)
      : DEFAULT_LANGUAGE;
  }

  instant(key: string, params?: object): string {
    return this.translate.instant(key, params);
  }
}
