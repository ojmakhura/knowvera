// views/settings/financial-settings/financial-settings.ts
import { CommonModule } from '@angular/common';
import { Component, effect, inject, linkedSignal, OnInit, signal } from '@angular/core';
import { form, min, FormField } from '@angular/forms/signals';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { FinancialSettings as FinancialSettingsDTO } from '@app/models/bw/co/knowvera/settings/financial-settings';
import { SalaryRangeDTO } from '@app/models/bw/co/knowvera/settings/salary-range-dto';
import {
  SalaryRangeDialog,
  SalaryRangeDialogData,
  SalaryRangeDialogResult,
} from '../salary-range-dialog/salary-range-dialog';
import { LoaderState } from '@app/@shared/loader/loader.state';

class FinancialSettingsModel {
  salaryRanges: Array<SalaryRangeDTO> = [];
  vat: number | any = null;
  user: string | any = null;
}

@Component({
  selector: 'app-financial-settings',
  imports: [CommonModule, MatIconModule, FormField],
  templateUrl: './financial-settings.html',
  styleUrls: ['./financial-settings.scss'],
})
export class FinancialSettings implements OnInit {
  settingsApiStore = inject(SettingsApiStore);

  loading = linkedSignal(() => this.settingsApiStore.loading());
  loaderMessage = linkedSignal(() => this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.settingsApiStore.success());
  error = linkedSignal(() => this.settingsApiStore.error());
  messages = linkedSignal(() => this.settingsApiStore.messages());

  private readonly dialog = inject(MatDialog);

  financialSettingsSignal = linkedSignal(() => {
    let storeData = this.settingsApiStore.financialSettings();
    let model = new FinancialSettingsModel();
    model.salaryRanges = storeData?.salaryRanges ?? [];
    model.vat = storeData?.vat ?? 0;
    model.user = storeData?.user ?? null;

    return model;
  });

  financialSettingsForm = form(this.financialSettingsSignal, (path) => {
    min(path.vat, 0, { message: 'VAT rate cannot be negative' });
  });

  // Not part of the FinancialSettings backend model yet, so these stay component-local and unsaved.
  reducedVatRate = signal(5.0);
  autoApprovalMax = signal(10000);

  loaderState = inject(LoaderState);
  constructor() {

    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  openAddSalaryRangeDialog(): void {
    this.dialog
      .open<SalaryRangeDialog, SalaryRangeDialogData, SalaryRangeDialogResult>(SalaryRangeDialog, {
        width: 'min(96vw, 420px)',
        data: {},
      })
      .afterClosed()
      .subscribe((result) => {
        console.log('Salary range dialog closed with result:', result);
        if (result && result !== 'removed') {
          
          this.settingsApiStore.saveSalaryRange({ salaryRange: result });

        }
      });
  }

  openEditSalaryRangeDialog(range: SalaryRangeDTO, index: number): void {
    this.dialog
      .open<SalaryRangeDialog, SalaryRangeDialogData, SalaryRangeDialogResult>(SalaryRangeDialog, {
        width: 'min(96vw, 420px)',
        data: { range },
      })
      .afterClosed()
      .subscribe((result) => {
        if (result === 'removed') {

          if (range.id) {

            this.settingsApiStore.removeSalaryRange(range.id);
            
          } else {

            this.financialSettingsSignal.update((value) => ({
              ...value,
              salaryRanges: value.salaryRanges.filter((_, i) => i !== index),
            }));
          }

        } else if (result) {
          
          this.settingsApiStore.saveSalaryRange({ salaryRange: result });

        }
      });
  }

  updateReducedVatRate(raw: string): void {
    this.reducedVatRate.set(raw === '' ? 0 : Number(raw));
  }

  updateAutoApprovalMax(raw: string): void {
    const parsed = Number(raw.replace(/,/g, ''));
    this.autoApprovalMax.set(Number.isNaN(parsed) ? 0 : parsed);
  }

  ngOnInit(): void {
    this.settingsApiStore.getFinancialSettings();
  }

  discard(): void {
    this.settingsApiStore.getFinancialSettings();
  }

  save(): void {
    if (this.financialSettingsForm().invalid()) {
      return;
    }

    const value = this.financialSettingsSignal();
    const financialSettings = new FinancialSettingsDTO();
    financialSettings.salaryRanges = value.salaryRanges;
    financialSettings.vat = value.vat;
    financialSettings.user = value.user;

    this.settingsApiStore.saveFinancialSettings({ financialSettings });
  }
}
