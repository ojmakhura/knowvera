import { Component, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '@app/@core/services/auth.service';
import { Router } from '@angular/router';

interface Feature {
  icon: string;
  titleKey: string;
  descriptionKey: string;
}

@Component({
  selector: 'app-home',
  imports: [TranslateModule, MatButtonModule, MatIconModule, MatCardModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Home {
  private authService = inject(AuthService);
  private router = inject(Router);

  protected readonly isAuthenticated = this.authService.isAuthenticated;

  protected readonly features: Feature[] = [
    {
      icon: 'verified_user',
      titleKey: 'home.features.secure.title',
      descriptionKey: 'home.features.secure.description'
    },
    {
      icon: 'speed',
      titleKey: 'home.features.fast.title',
      descriptionKey: 'home.features.fast.description'
    },
    {
      icon: 'sync',
      titleKey: 'home.features.compliant.title',
      descriptionKey: 'home.features.compliant.description'
    },
    {
      icon: 'support_agent',
      titleKey: 'home.features.support.title',
      descriptionKey: 'home.features.support.description'
    }
  ];

  constructor() {
    // Redirect to dashboard if already authenticated
    if (this.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  protected async login(): Promise<void> {
    try {
      await this.authService.login();
    } catch (error) {
      console.error('Login failed:', error);
    }
  }
}
