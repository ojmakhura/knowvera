import { ChangeDetectionStrategy, Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { KycRecordApi } from '@app/services/bw/co/knowvera/kyc/kyc-record-api';
import { OrganisationApi } from '@app/services/bw/co/knowvera/organisation/organisation-api';
import { KycRecordSummary } from '@app/models/bw/co/knowvera/kyc/kyc-record-summary';
import { KycReportSectionDTO } from '@app/models/bw/co/knowvera/kyc/fields/kyc-report-section-dto';
import { FormFieldValueDTO } from '@app/models/bw/co/knowvera/form/submission/form-field-value-dto';
import { complianceTone, labelOf, riskTone } from './kyc-report-labels';

/** One client's KYC report, as the organisation asking for it configured it. */
@Component({
  selector: 'app-kyc-report-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, DatePipe, MatIconModule],
  templateUrl: './kyc-report-summary.html',
  styles: `
    @media print {
      .kyc-report-actions,
      .kv-breadcrumb {
        display: none;
      }
    }
  `,
})
export class KycReportSummary implements OnInit {
  private readonly kycRecordApi = inject(KycRecordApi);
  private readonly organisationApi = inject(OrganisationApi);

  readonly organisationId = input.required<string>();
  readonly recordId = input.required<string>();

  protected readonly summary = signal<KycRecordSummary | null>(null);
  protected readonly organisationName = signal('');
  protected readonly loading = signal(true);
  protected readonly failed = signal(false);

  protected readonly sections = computed<KycReportSectionDTO[]>(() =>
    [...(this.summary()?.reportSections || [])].sort((a, b) => (a.position ?? 0) - (b.position ?? 0)),
  );
  protected readonly isIndividual = computed(() => this.summary()?.target !== 'ORGANISATION');

  protected readonly labelOf = labelOf;
  protected readonly complianceTone = complianceTone;
  protected readonly riskTone = riskTone;

  ngOnInit(): void {
    this.organisationApi.findListOrganisationById(this.organisationId()).subscribe({
      next: (organisation) => this.organisationName.set(organisation?.name || ''),
      error: () => this.organisationName.set(''),
    });
    this.kycRecordApi.findSummaryById(this.recordId(), this.organisationId()).subscribe({
      next: (summary) => {
        this.summary.set(summary);
        this.loading.set(false);
      },
      error: () => {
        this.failed.set(true);
        this.loading.set(false);
      },
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
