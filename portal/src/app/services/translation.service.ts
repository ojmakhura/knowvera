import { Injectable, signal, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

export type Language = 'en' | 'es';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private readonly translate = inject(TranslateService);

  currentLang = signal<Language>('en');
  availableLanguages: Language[] = ['en', 'es'];

  constructor() {
    this.initializeLanguage();
  }

  private initializeLanguage(): void {
    const savedLang = localStorage.getItem('language') as Language;
    const defaultLang: Language = savedLang || 'en';

    this.translate.setDefaultLang(defaultLang);
    this.translate.use(defaultLang);
    this.currentLang.set(defaultLang);
  }

  setLanguage(lang: Language): void {
    this.translate.use(lang);
    this.currentLang.set(lang);
    localStorage.setItem('language', lang);
  }

  instant(key: string, params?: object): string {
    return this.translate.instant(key, params);
  }
}
