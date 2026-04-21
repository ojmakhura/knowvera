import { Component, signal, inject } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from '../services/translation.service';

@Component({
  selector: 'app-translation-example',
  imports: [TranslateModule],
  template: `
    <div class="card">
      <div class="card-header">
        <h3>Advanced Translation Examples</h3>
      </div>
      <div class="card-body">
        <!-- Simple Translation -->
        <div class="mb-3">
          <h5>Simple Translation:</h5>
          <p>{{ 'app.welcome' | translate }}</p>
        </div>

        <!-- Translation with Parameters -->
        <div class="mb-3">
          <h5>Translation with Parameters:</h5>
          <p>{{ 'greeting' | translate: { name: userName() } }}</p>
        </div>

        <!-- Programmatic Translation -->
        <div class="mb-3">
          <h5>Programmatic Translation:</h5>
          <p>{{ programmaticTranslation() }}</p>
        </div>

        <!-- Nested Keys -->
        <div class="mb-3">
          <h5>Nested Translation Keys:</h5>
          <ul>
            <li>{{ 'common.save' | translate }}</li>
            <li>{{ 'common.cancel' | translate }}</li>
            <li>{{ 'common.delete' | translate }}</li>
          </ul>
        </div>
      </div>
    </div>
  `,
  styles: `
    .card {
      margin-top: 1rem;
    }
  `
})
export class TranslationExampleComponent {
  private readonly translationService = inject(TranslationService);

  userName = signal('John Doe');
  programmaticTranslation = signal('');

  constructor() {
    // Example of getting translation programmatically
    this.programmaticTranslation.set(
      this.translationService.instant('common.loading')
    );
  }
}
