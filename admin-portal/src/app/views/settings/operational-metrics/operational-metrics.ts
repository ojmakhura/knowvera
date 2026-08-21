// views/settings/operational-metrics/operational-metrics.ts
import { Component, computed, signal, OnInit, inject, } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';

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

  settingsApiStore = inject(SettingsApiStore);

  operationalMetricsSignal = signal(new OperationalMetricsModel());
  operationalMetricsForm = form(this.operationalMetricsSignal, (path) => {});

  kycValidityYears = signal(3);
  targetCreationHours = signal(24);
  orgAdminRole = signal('Org Manager');
  normalUserRole = signal('Standard User');

  kycValidityLabel = computed(
    () => `${this.kycValidityYears()} Year${this.kycValidityYears() === 1 ? '' : 's'}`,
  );

  constructor() {
  }

  ngOnInit(): void {
  }

  save(): void {
    
  }
}
