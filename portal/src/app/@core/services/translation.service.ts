import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { readStoredLanguage, storeLanguage } from './stored-language';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  constructor(private translate: TranslateService) {
    this.initLanguage();
  }

  initLanguage() {
    // Set default language
    this.translate.setDefaultLang('en');
    
    // Get browser language or use stored preference
    const browserLang = readStoredLanguage() || this.translate.getBrowserLang() || 'en';
    const lang = ['en', 'es', 'fr'].includes(browserLang) ? browserLang : 'en';
    
    this.translate.use(lang);
  }

  setLanguage(lang: string) {
    this.translate.use(lang);
    storeLanguage(lang);
  }

  getCurrentLanguage(): string {
    return this.translate.currentLang || 'en';
  }

  getAvailableLanguages() {
    return [
      { code: 'en', name: 'English' },
      { code: 'es', name: 'Español' },
      { code: 'fr', name: 'Français' }
    ];
  }
}
