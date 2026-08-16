// views/settings/operational-metrics/operational-metrics.ts
import { Component, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-operational-metrics',
  imports: [FormsModule, MatIconModule],
  templateUrl: './operational-metrics.html',
  styleUrls: ['./operational-metrics.scss'],
})
export class OperationalMetrics {
  kycValidityYears = signal(3);
  targetCreationHours = signal(24);
  orgAdminRole = signal('Org Manager');
  normalUserRole = signal('Standard User');

  kycValidityLabel = computed(
    () => `${this.kycValidityYears()} Year${this.kycValidityYears() === 1 ? '' : 's'}`,
  );

  save(): void {
    console.log('Saving operational metrics', {
      kycValidityYears: this.kycValidityYears(),
      targetCreationHours: this.targetCreationHours(),
      orgAdminRole: this.orgAdminRole(),
      normalUserRole: this.normalUserRole(),
    });
  }
}