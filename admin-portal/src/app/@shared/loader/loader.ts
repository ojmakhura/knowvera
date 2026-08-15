import { CommonModule } from '@angular/common';
import { Component, Input, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoaderState } from '@app/@shared/loader/loader.state';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.html',
  styleUrls: ['./loader.scss'],
  imports: [CommonModule, MatProgressSpinnerModule, MatCardModule, TranslateModule],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true,
})
export class Loader {

  loaderState = inject(LoaderState);

  isLoading = computed(() => this.loaderState.isLoading());
  type = computed(() => this.loaderState.type());
  size = computed(() => this.loaderState.size());
  color = computed(() => this.loaderState.color());
  message = computed(() => this.loaderState.message());
  overlay = computed(() => this.loaderState.overlay());
  fullScreen = computed(() => this.loaderState.fullScreen());

  diameter = computed(() => {
    switch (this.size()) {
      case 'small': return 32;
      case 'medium': return 48;
      case 'large': return 64;
      default: return 64;
    }
  });

  spinnerColor = computed(() => {
    return this.color() === 'white' ? 'primary' : this.color() as 'primary' | 'accent' | 'warn';
  });

  loaderReset() {
    this.loaderState.isLoading.set(false);
    this.loaderState.message.set(undefined);
    this.loaderState.type.set('spinner');
    this.loaderState.size.set('medium');
    this.loaderState.color.set('primary');
    this.loaderState.overlay.set(true);
    this.loaderState.fullScreen.set(true);
  }
}
