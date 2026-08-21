// views/settings/platform-identity/platform-identity.ts
import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';

class PlatformIdentityModel {
  platformName: string | any = null;
  platformUrl: string | any = null;
  supportContact: string | any = null;
  kycPortalLink: string | any = null;
  user: string | any = null;
}

@Component({
  selector: 'app-platform-identity',
  imports: [ MatIconModule, FormField ],
  templateUrl: './platform-identity.html',
  styleUrls: ['./platform-identity.scss'],
})
export class PlatformIdentity implements OnInit {
  
  platformIdentitySignal = signal(new PlatformIdentityModel());
  platformIdentityForm = form(this.platformIdentitySignal, (path) => {

  });

  settingsApiStore = inject(SettingsApiStore);

  constructor() {
    
    effect(() => {
      const platformIdentity = this.settingsApiStore.platformIdentity();
      this.platformIdentitySignal.update((current) => {
        return {
          ...current,
          platformName: platformIdentity?.platformName || null,
          platformUrl: platformIdentity?.platformUrl || null,
          supportContact: platformIdentity?.supportContact || null,
          kycPortalLink: platformIdentity?.kycPortalLink || null,
          user: platformIdentity?.user || null,
        };
      });
    });
  }

  ngOnInit(): void {

    this.settingsApiStore.getPlatformIdentity();
  }

  save(): void {
      
  }
}