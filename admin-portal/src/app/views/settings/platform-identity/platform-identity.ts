// views/settings/platform-identity/platform-identity.ts
import { Component, effect, inject, linkedSignal, OnInit, signal } from '@angular/core';
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
  
  platformIdentitySignal = linkedSignal(() => {
    let storeData = this.settingsApiStore.platformIdentity();
    let model = new PlatformIdentityModel();
    model.platformName = storeData?.platformName ?? null;
    model.platformUrl = storeData?.platformUrl ?? null;
    model.supportContact = storeData?.supportContact ?? null;
    model.kycPortalLink = storeData?.kycPortalLink ?? null;
    // model.user = storeData?.user ?? null;

    return model;
  });
  platformIdentityForm = form(this.platformIdentitySignal, (path) => {

  });

  settingsApiStore = inject(SettingsApiStore);

  constructor() {
    
  }

  ngOnInit(): void {

    this.settingsApiStore.getPlatformIdentity();
  }

  save(): void {
      
  }
}