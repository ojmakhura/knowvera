// views/settings/platform-identity/platform-identity.ts
import { Component, effect, inject, linkedSignal, OnInit, signal } from '@angular/core';
import { form, FormField, FormRoot } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { LoaderState } from '@app/@shared/loader/loader.state';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';

class PlatformIdentityModel {
  // Native text inputs bound with [formField] must start as '' — signal forms treats a null
  // model value on <input type="text"> as numeric and rejects non-numeric input.
  platformName: string | any = '';
  platformUrl: string | any = null;
  supportContact: string | any = null;
  kycPortalLink: string | any = null;
  user: string | any = null;
}

@Component({
  selector: 'app-platform-identity',
  imports: [ MatIconModule, FormField, FormRoot],
  templateUrl: './platform-identity.html',
  styleUrls: ['./platform-identity.scss'],
})
export class PlatformIdentity implements OnInit {

  loaderState = inject(LoaderState);
  
  platformIdentitySignal = linkedSignal(() => {
    let storeData = this.settingsApiStore.platformIdentity();
    let model = new PlatformIdentityModel();
    model.platformName = storeData?.platformName ?? '';
    model.platformUrl = storeData?.platformUrl ?? null;
    model.supportContact = storeData?.supportContact ?? null;
    model.kycPortalLink = storeData?.kycPortalLink ?? null;
    // model.user = storeData?.user ?? null;

    return model;
  });
  platformIdentityForm = form(this.platformIdentitySignal, (path) => {

  }, { submission: { action: async () => this.save() } });

  settingsApiStore = inject(SettingsApiStore);

  loaderMessage = linkedSignal(() => this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.settingsApiStore.success());
  error = linkedSignal(() => this.settingsApiStore.error());
  messages = linkedSignal(() => this.settingsApiStore.messages());

  constructor() {
    
    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  ngOnInit(): void {

    this.settingsApiStore.getPlatformIdentity();
  }

  save(): void {
      
    const platformIdentity = { ...this.platformIdentitySignal() };
    platformIdentity.platformName = platformIdentity.platformName || null;
    this.settingsApiStore.savePlatformIdentity({ platformIdentity });
  }
}