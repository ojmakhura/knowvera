import { ChangeDetectionStrategy, Component, OnInit, computed, effect, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { KycRecordListDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-list-dto';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';

import { SettingsApiStore } from './../../store/bw/co/centralkyc/settings/settings-api.store';
import { AppEnvStore } from '@app/store/app-env.state';

type RegistryFilter = 'ALL' | 'CURRENT' | 'ATTENTION';

interface DashboardMetric {
  label: string;
  value: string;
  detail: string;
  icon: string;
  tone: 'primary' | 'warning' | 'success' | 'neutral';
}

interface FeaturedRecordCard {
  kind: TargetEntity;
  eyebrow: string;
  title: string;
  subtitle: string;
  icon: string;
  actionLabel: string;
  record: KycRecordDTO | null;
  badgeLabel: string;
  badgeTone: string;
  details: Array<{ label: string; value: string; tone?: 'default' | 'accent' | 'alert' }>;
}

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Dashboard implements OnInit {
  readonly settingsApiStore = inject(SettingsApiStore);
  readonly kycRecordApiStore = inject(KycRecordApiStore);
  readonly router = inject(Router);

  readonly targetEntityEnum = TargetEntity;
  readonly registryFilter = signal<RegistryFilter>('ALL');

  readonly currentIndividualRecord = computed(() => this.kycRecordApiStore.currentIndividualRecord());
  readonly currentOrganisationRecord = computed(() => this.kycRecordApiStore.currentOrganisationRecord());
  readonly records = computed(
    () => ((this.kycRecordApiStore.dataList() ?? []) as unknown as KycRecordListDTO[]),
  );
  readonly totalRecords = computed(() => this.records().length);
  readonly currentRecordsCount = computed(
    () => this.records().filter((record) => this.isCurrentStatus(record.kycStatus)).length,
  );
  readonly attentionRecordsCount = computed(
    () => this.records().filter((record) => this.requiresAttention(record.kycStatus)).length,
  );
  readonly expiringSoonCount = computed(
    () =>
      this.records().filter((record) => {
        if (!record.expiryDate) {
          return false;
        }

        const expiryDate = new Date(record.expiryDate);
        if (Number.isNaN(expiryDate.getTime())) {
          return false;
        }

        const now = new Date();
        const msInDay = 1000 * 60 * 60 * 24;
        const daysUntilExpiry = Math.ceil((expiryDate.getTime() - now.getTime()) / msInDay);
        return daysUntilExpiry >= 0 && daysUntilExpiry <= 45;
      }).length,
  );
  readonly complianceRate = computed(() => {
    const total = this.totalRecords();
    if (!total) {
      return '0%';
    }

    const rate = (this.currentRecordsCount() / total) * 100;
    return `${rate.toFixed(rate % 1 === 0 ? 0 : 1)}%`;
  });

  readonly metrics = computed<DashboardMetric[]>(() => {
    const total = this.totalRecords();
    const current = this.currentRecordsCount();
    const attention = this.attentionRecordsCount();
    const expiringSoon = this.expiringSoonCount();

    return [
      {
        label: 'Active Verifications',
        value: this.formatCount(total),
        detail: total ? `${current} records currently compliant` : 'No records loaded yet',
        icon: 'analytics',
        tone: 'primary',
      },
      {
        label: 'Pending Review',
        value: this.formatCount(attention),
        detail: attention ? 'Records with open remediation work' : 'No open remediation items',
        icon: 'pending_actions',
        tone: 'warning',
      },
      {
        label: 'Compliance Rate',
        value: this.complianceRate(),
        detail: total ? `${current} of ${total} records are current` : 'Awaiting registry activity',
        icon: 'verified',
        tone: 'success',
      },
      {
        label: 'Expiry Watchlist',
        value: this.formatCount(expiringSoon),
        detail: expiringSoon ? 'Expiring within the next 45 days' : 'No imminent record expiries',
        icon: 'schedule',
        tone: 'neutral',
      },
    ];
  });

  readonly filteredRecords = computed(() => {
    const filter = this.registryFilter();
    const records = this.records();

    switch (filter) {
      case 'CURRENT':
        return records.filter((record) => this.isCurrentStatus(record.kycStatus));
      case 'ATTENTION':
        return records.filter((record) => this.requiresAttention(record.kycStatus));
      default:
        return records;
    }
  });

  readonly visibleRecords = computed(() => this.filteredRecords().slice(0, 10));
  readonly resultLabel = computed(() => {
    const filteredCount = this.filteredRecords().length;
    if (!filteredCount) {
      return 'No registry results available';
    }

    return `Showing 1-${Math.min(10, filteredCount)} of ${filteredCount} results`;
  });

  readonly filterButtonLabel = computed(() => {
    switch (this.registryFilter()) {
      case 'CURRENT':
        return 'Filter: Current';
      case 'ATTENTION':
        return 'Filter: Needs Attention';
      default:
        return 'Filter: All Records';
    }
  });

  appEnvStore = inject(AppEnvStore);
  readonly hasOrg = computed(() => !!this.appEnvStore.userOrganisation());

  readonly featuredCards = computed<FeaturedRecordCard[]>(() => [
    this.buildFeaturedCard(
      this.currentIndividualRecord(),
      TargetEntity.INDIVIDUAL,
      'Current Records',
      'person',
      'View Full Profile',
    ),
    this.buildFeaturedCard(
      this.currentOrganisationRecord(),
      TargetEntity.ORGANISATION,
      'Current Records',
      'apartment',
      'Address Deficiencies',
    ),
  ]);

  targetEntity = Object.values(TargetEntity);

  constructor() {

    effect(() => {
      const org = this.appEnvStore.userOrganisation();
      console.log('User organisation updated:', org);
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getAll();
    this.loadDashboardData();
  }

  formatDate(date: Date | string | null | undefined): string {
    if (!date) {
      return 'N/A';
    }

    const dateObj = date instanceof Date ? date : new Date(date);
    if (Number.isNaN(dateObj.getTime())) {
      return 'N/A';
    }

    return dateObj.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  }

  statusLabel(status: KycComplianceStatus | string | null | undefined): string {
    switch ((status || '').toString().toUpperCase()) {
      case KycComplianceStatus.CURRENT:
        return 'Verified';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      case KycComplianceStatus.ABSENT:
        return 'Missing';
      case KycComplianceStatus.INCOMPLETE:
        return 'Pending Action';
      case KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED:
        return 'Document Review';
      default:
        return 'In Review';
    }
  }

  statusTone(status: KycComplianceStatus | string | null | undefined): string {
    switch ((status || '').toString().toUpperCase()) {
      case KycComplianceStatus.CURRENT:
        return 'verified';
      case KycComplianceStatus.EXPIRED:
        return 'expired';
      case KycComplianceStatus.ABSENT:
      case KycComplianceStatus.INCOMPLETE:
      case KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED:
        return 'attention';
      default:
        return 'review';
    }
  }

  recordTitle(record: KycRecordDTO | KycRecordListDTO | null): string {
    if (!record) {
      return 'KYC Record';
    }

    if ('ownerDetails' in record && record.ownerDetails?.name) {
      return record.ownerDetails.name;
    }

    if ('name' in record && record.name) {
      return record.name;
    }

    return record.ref || 'KYC Record';
  }

  recordReference(record: KycRecordDTO | KycRecordListDTO | null): string {
    if (!record) {
      return 'Pending allocation';
    }

    if ('ownerDetails' in record && record.ownerDetails?.identityNo) {
      return record.ownerDetails.identityNo;
    }

    return record.ref || record.id || 'Pending allocation';
  }

  rowTypeLabel(record: KycRecordListDTO): string {
    return record.ref?.toUpperCase().includes('ORG') ? 'Organisation' : 'Individual';
  }

  rowIcon(record: KycRecordListDTO): string {
    return this.rowTypeLabel(record) === 'Organisation' ? 'corporate_fare' : 'person';
  }

  rowUploadDate(_record: KycRecordListDTO): string {
    return 'N/A';
  }

  cycleFilter(): void {
    switch (this.registryFilter()) {
      case 'ALL':
        this.registryFilter.set('CURRENT');
        return;
      case 'CURRENT':
        this.registryFilter.set('ATTENTION');
        return;
      default:
        this.registryFilter.set('ALL');
    }
  }

  viewRecord(record: { id: string | null | undefined }): void {
    if (!record.id) {
      return;
    }

    this.router.navigate(['/kyc-record', record.id]);
  }

  editRecord(record: { id: string | null | undefined; target?: TargetEntity | string | null | undefined }): void {
    if (!record.id) {
      return;
    }

    const queryParams: { id: string; target?: TargetEntity | string } = { id: record.id };
    if (record.target) {
      queryParams.target = record.target;
    }

    this.router.navigate(['/kyc-record', 'edit'], { queryParams });
  }

  createRecord(type: TargetEntity): void {
    this.router.navigate(['/kyc-record', 'edit'], {
      queryParams: { target: type },
    });
  }

  refreshRecords(): void {
    this.loadDashboardData();
  }

  exportRegistry(): void {
    const records = this.filteredRecords();
    if (!records.length || typeof document === 'undefined' || typeof URL === 'undefined') {
      return;
    }

    const header = ['Client Name', 'Type', 'Identity No.', 'Status', 'Upload Date', 'Expiry'];
    const rows = records.map((record) => [
      this.escapeCsvValue(this.recordTitle(record)),
      this.escapeCsvValue(this.rowTypeLabel(record)),
      this.escapeCsvValue(this.recordReference(record)),
      this.escapeCsvValue(this.statusLabel(record.kycStatus)),
      this.escapeCsvValue(this.rowUploadDate(record)),
      this.escapeCsvValue(this.formatDate(record.expiryDate)),
    ]);

    const csv = [header.join(','), ...rows.map((row) => row.join(','))].join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'institutional-records-registry.csv';
    link.click();
    URL.revokeObjectURL(url);
  }

  handleFeaturedAction(card: FeaturedRecordCard): void {
    if (card.record) {
      this.viewRecord(card.record);
      return;
    }

    this.createRecord(card.kind);
  }

  private loadDashboardData(): void {
    this.kycRecordApiStore.findMyCurrentIndividualRecord();
    this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    this.kycRecordApiStore.findMyRecords();
  }

  private buildFeaturedCard(
    record: KycRecordDTO | null,
    kind: TargetEntity,
    eyebrow: string,
    icon: string,
    actionLabel: string,
  ): FeaturedRecordCard {
    const entityLabel = kind === TargetEntity.ORGANISATION ? 'Organisation' : 'Individual';

    return {
      kind,
      eyebrow,
      title: this.recordTitle(record),
      subtitle: record?.ownerDetails?.emailAddress || `${entityLabel} profile`,
      icon,
      actionLabel: record ? actionLabel : `Create ${entityLabel} Record`,
      record,
      badgeLabel: record ? this.statusLabel(record.kycStatus) : 'No active record',
      badgeTone: record ? this.statusTone(record.kycStatus) : 'review',
      details: [
        {
          label: kind === TargetEntity.ORGANISATION ? 'Entity ID' : 'Identity Number',
          value: this.recordReference(record),
        },
        {
          label: 'Status Date',
          value: this.formatDate(record?.modifiedAt || record?.uploadDate || record?.createdAt),
        },
        {
          label: 'Expiry Date',
          value: this.formatDate(record?.expiryDate),
        },
        {
          label: kind === TargetEntity.ORGANISATION ? 'Entity Type' : 'Profile Type',
          value: entityLabel,
          tone: kind === TargetEntity.ORGANISATION ? 'alert' : 'accent',
        },
      ],
    };
  }

  private isCurrentStatus(status: KycComplianceStatus | string | null | undefined): boolean {
    return (status || '').toString().toUpperCase() === KycComplianceStatus.CURRENT;
  }

  private requiresAttention(status: KycComplianceStatus | string | null | undefined): boolean {
    const normalizedStatus = (status || '').toString().toUpperCase();
    return [
      KycComplianceStatus.EXPIRED,
      KycComplianceStatus.ABSENT,
      KycComplianceStatus.INCOMPLETE,
      KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED,
    ].includes(normalizedStatus as KycComplianceStatus);
  }

  private formatCount(value: number): string {
    return new Intl.NumberFormat('en-US').format(value);
  }

  private escapeCsvValue(value: string): string {
    return `"${value.replace(/"/g, '""')}"`;
  }
}
