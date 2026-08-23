// views/settings/operational-metrics/operational-metrics.ts
import { Component, computed, signal, OnInit, inject, linkedSignal, effect } from '@angular/core';
import Keycloak from 'keycloak-js';
import { form, FormField, max, min } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { LoaderState } from '@app/@shared/loader/loader.state';

class OperationalMetricsModel {
  kycDuration: number | any = null;
  timeToAccountCreation: number | any = null;
  organisationAdminRole: string | any = null;
  normalUserRole: string | any = null;
  documentDurationLimit: number | any = null;
  dataVerificationThreshold: number | any = null;
  maxDataVerificationFailureThreshold: number | any = null;
  user: string | any = null;
}

@Component({
  selector: 'app-operational-metrics',
  imports: [ MatIconModule, FormField ],
  templateUrl: './operational-metrics.html',
  styleUrls: ['./operational-metrics.scss'],
})
export class OperationalMetrics implements OnInit {

  private keycloak = inject(Keycloak);
  settingsApiStore = inject(SettingsApiStore);

  loading = linkedSignal(() => this.settingsApiStore.loading());
  loaderMessage = linkedSignal(() => this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.settingsApiStore.success());
  error = linkedSignal(() => this.settingsApiStore.error());
  messages = linkedSignal(() => this.settingsApiStore.messages());

  operationalMetricsSignal = linkedSignal(() => {
    let storeData = this.settingsApiStore.operationalMetrics();
    let model = new OperationalMetricsModel();
    model.kycDuration = storeData?.kycDuration;
    model.timeToAccountCreation = storeData?.timeToAccountCreation;
    model.organisationAdminRole = storeData?.organisationAdminRole;
    model.normalUserRole = storeData?.normalUserRole;
    model.documentDurationLimit = storeData?.documentDurationLimit;
    model.dataVerificationThreshold = storeData?.dataVerificationThreshold;
    model.maxDataVerificationFailureThreshold = storeData?.maxDataVerificationFailureThreshold;

    return model;
  });

  operationalMetricsForm = form(this.operationalMetricsSignal, (path) => {
    min(path.kycDuration, 1);
    max(path.kycDuration, 5);
  });

  kycValidityYears = signal(3);
  targetCreationHours = signal(24);
  orgAdminRole = signal('Org Manager');
  normalUserRole = signal('Standard User');

  kycValidityLabel = computed(
    () => `${this.kycValidityYears()} Year${this.kycValidityYears() === 1 ? '' : 's'}`,
  );

  loaderState = inject(LoaderState);
  constructor() {

    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  ngOnInit(): void {

    console.log(this.keycloak.realmAccess);
    this.settingsApiStore.getOperationalMetrics();
  }

  save(): void {
    
    this.settingsApiStore.saveOperationalMetrics({ operationalMetrics: this.operationalMetricsSignal()});
  }
}
