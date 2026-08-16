// views/settings/financial-settings/financial-settings.ts
import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

interface SalaryRange {
  label: string;
  min: number;
  max: number;
  active: boolean;
}

@Component({
  selector: 'app-financial-settings',
  imports: [CommonModule, MatIconModule, FormsModule],
  templateUrl: './financial-settings.html',
  styleUrls: ['./financial-settings.scss'],
})
export class FinancialSettings {
  salaryRanges = signal<SalaryRange[]>([
    { label: 'Entry Level Tier 1', min: 45000, max: 65000, active: true },
    { label: 'Mid-Level Associate', min: 70000, max: 95000, active: true },
    { label: 'Senior Specialist', min: 100000, max: 140000, active: true },
    { label: 'Executive Legacy', min: 150000, max: 250000, active: false },
  ]);

  standardVatRate = signal(20.0);
  reducedVatRate = signal(5.0);
  autoApprovalMax = signal(10000);

  toggleRange(range: SalaryRange): void {
    this.salaryRanges.update((ranges) =>
      ranges.map((r) => (r === range ? { ...r, active: !r.active } : r)),
    );
  }

  save(): void {
    console.log('Saving financial settings', {
      salaryRanges: this.salaryRanges(),
      standardVatRate: this.standardVatRate(),
      reducedVatRate: this.reducedVatRate(),
      autoApprovalMax: this.autoApprovalMax(),
    });
  }
}