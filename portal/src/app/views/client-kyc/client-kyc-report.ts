import { ChangeDetectionStrategy, Component, computed, effect, inject, input, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { KycRecordApi } from '@app/services/bw/co/knowvera/kyc/kyc-record-api';
import { AppEnvStore } from '@app/store/app-env.state';
import { KycRecordSummary } from '@app/models/bw/co/knowvera/kyc/kyc-record-summary';
import { KycReportSectionDTO } from '@app/models/bw/co/knowvera/kyc/fields/kyc-report-section-dto';
import { FormFieldValueDTO } from '@app/models/bw/co/knowvera/form/submission/form-field-value-dto';
import { initials, labelOf, pillIcon, pillTone, riskTone } from './client-kyc-labels';

/** One client's KYC report, as the organisation asking for it configured it. */
@Component({
  selector: 'app-client-kyc-report',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, DatePipe, MatIconModule],
  templateUrl: './client-kyc-report.html',
  styles: `
    @media print {
      .kyc-report-actions,
      .kv-breadcrumb {
        display: none;
      }
    }
  `,
})
export class ClientKycReport {
  private readonly kycRecordApi = inject(KycRecordApi);
  private readonly appEnvState = inject(AppEnvStore);

  readonly recordId = input.required<string>();

  protected readonly summary = signal<KycRecordSummary | null>(null);
  protected readonly organisationId = computed<string>(() => this.appEnvState.userOrganisation()?.id || '');
  protected readonly organisationName = computed<string>(() => this.appEnvState.userOrganisation()?.name || '');
  protected readonly loading = signal(true);
  protected readonly failed = signal(false);

  protected readonly sections = computed<KycReportSectionDTO[]>(() =>
    [...(this.summary()?.reportSections || [])].sort((a, b) => (a.position ?? 0) - (b.position ?? 0)),
  );
  protected readonly isIndividual = computed(() => this.summary()?.target !== 'ORGANISATION');

  protected readonly labelOf = labelOf;
  protected readonly pillTone = pillTone;
  protected readonly pillIcon = pillIcon;
  protected readonly clientInitials = computed(() => initials(this.summary()?.name));
  protected readonly riskTone = riskTone;

  constructor() {
    // The API returns the report as the caller's own organisation configured it
    effect(() => {
      const organisationId = this.organisationId();
      if (!organisationId) {
        if (this.appEnvState.userOrganisationLoaded()) {
          this.failed.set(true);
          this.loading.set(false);
        }
        return;
      }
      this.kycRecordApi.findSummaryById(this.recordId(), organisationId).subscribe({
        next: (summary) => {
          this.summary.set(summary);
          this.loading.set(false);
        },
        error: () => {
          this.failed.set(true);
          this.loading.set(false);
        },
      });
    });
  }

  protected print(): void {
    window.print();
  }

  protected answer(value: FormFieldValueDTO): string {
    const values = (value.values || []).filter((v: string) => !!v);
    return values.length ? values.join(', ') : value.value || '—';
  }

  protected list(values: string[] | null | undefined): string {
    return values?.length ? values.map((v) => labelOf(v)).join(', ') : '—';
  }

  protected phones(): string {
    const numbers = (this.summary()?.phoneNumbers || []).map((p: any) => p?.phoneNumber).filter(Boolean);
    return numbers.length ? numbers.join(', ') : '—';
  }
}
