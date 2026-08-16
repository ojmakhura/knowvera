// views/settings/platform-identity/platform-identity.ts
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-platform-identity',
  imports: [ReactiveFormsModule, MatIconModule],
  templateUrl: './platform-identity.html',
  styleUrls: ['./platform-identity.scss'],
})
export class PlatformIdentity {
  private fb = inject(FormBuilder);

  form = this.fb.group({
    platformName: ['Knowvera Enterprise', Validators.required],
    platformUrl: ['https://app.knowvera.com', Validators.required],
    kycPortalLink: ['https://kyc.knowvera.com/verify'],
    supportEmail: ['support@knowvera.com', [Validators.required, Validators.email]],
  });

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    // TODO: wire to a SettingsApi service
    console.log('Saving platform identity', this.form.value);
  }
}