import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';

type AuditField = {
  label: string;
  value: string;
};

type TargetRecord = {
  id: string;
  name: string;
  reference: string;
};

type OrganisationRecord = {
  id: string;
  name: string;
  registration: string;
};

@Component({
  selector: 'app-client-request-edit',
  templateUrl: './client-request-edit.html',
  styleUrls: ['./client-request-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatDividerModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule
  ]
})
export class ClientRequestEdit {
  readonly targetOptions = [TargetEntity.ORGANISATION, TargetEntity.INDIVIDUAL];
  readonly requestStatuses = [
    ClientRequestStatus.CONTACTED,
    ClientRequestStatus.PENDING,
    ClientRequestStatus.ACCEPTED,
    ClientRequestStatus.REJECTED,
  ];
  readonly kycStatuses = [
    KycComplianceStatus.CURRENT,
    KycComplianceStatus.INCOMPLETE,
    KycComplianceStatus.ABSENT,
    KycComplianceStatus.EXPIRED,
  ];

  readonly auditFields = signal<AuditField[]>([
    { label: 'Request ID', value: 'REQ-90021-A' },
    { label: 'Created At', value: 'Oct 24, 2023 • 14:32' },
    { label: 'Created By', value: 'System / Auto-Provision' },
    { label: 'Modified At', value: 'Jan 12, 2024 • 09:15' },
    { label: 'Modified By', value: 'm.sterling@veritas.io' },
  ]);

  readonly targetFilter = signal('Alexander');
  readonly organisationFilter = signal('Veritas');
  readonly selectedTargetType = signal<TargetEntity>(TargetEntity.INDIVIDUAL);
  readonly selectedTarget = signal<TargetRecord>({
    id: '550e8400-e29b-41d4-a716-446655440000',
    name: 'Alexander Vance Sterling',
    reference: 'UUID: 550e8400-e29b-41d4-a716-446655440000',
  });
  readonly targetResults = signal<TargetRecord[]>([
    {
      id: '550e8400-e29b-41d4-a716-446655440000',
      name: 'Alexander Vance Sterling',
      reference: 'UUID: 550e8400-e29b-41d4-a716-446655440000',
    },
    {
      id: '770e8400-e29b-41d4-a716-446655440111',
      name: 'Alicia Sterling',
      reference: 'UUID: 770e8400-e29b-41d4-a716-446655440111',
    },
  ]);
  readonly organisationResults = signal<OrganisationRecord[]>([
    {
      id: 'org-01',
      name: 'Global Tech Holdings LLC',
      registration: 'Registration: US-DE-99201',
    },
    {
      id: 'org-02',
      name: 'Veritas Financial Partners',
      registration: 'Registration: UK-GB-11200',
    },
  ]);
  readonly selectedOrganisationId = signal('org-02');
  readonly documentId = signal('DOC-2024-XP-001');
  readonly fileName = signal('KYC_Passport_Sterling_V2.pdf');
  readonly selectedRequestStatus = signal<ClientRequestStatus>(ClientRequestStatus.PENDING);
  readonly selectedKycStatus = signal<KycComplianceStatus>(KycComplianceStatus.INCOMPLETE);

  discardChanges(): void {
    this.targetFilter.set('Alexander');
    this.organisationFilter.set('Veritas');
    this.selectedTargetType.set(TargetEntity.INDIVIDUAL);
    this.selectedRequestStatus.set(ClientRequestStatus.PENDING);
    this.selectedKycStatus.set(KycComplianceStatus.INCOMPLETE);
    this.selectedOrganisationId.set('org-02');
    this.documentId.set('DOC-2024-XP-001');
    this.fileName.set('KYC_Passport_Sterling_V2.pdf');
  }

  saveChanges(): void {}

  selectTarget(record: TargetRecord): void {
    this.selectedTarget.set(record);
  }

  selectOrganisation(record: OrganisationRecord): void {
    this.selectedOrganisationId.set(record.id);
  }

  setTargetType(value: TargetEntity): void {
    this.selectedTargetType.set(value);
  }

  setRequestStatus(value: ClientRequestStatus): void {
    this.selectedRequestStatus.set(value);
  }

  setKycStatus(value: KycComplianceStatus): void {
    this.selectedKycStatus.set(value);
  }

  updateTargetFilter(value: string): void {
    this.targetFilter.set(value);
  }

  updateOrganisationFilter(value: string): void {
    this.organisationFilter.set(value);
  }

  updateDocumentId(value: string): void {
    this.documentId.set(value);
  }

  updateFileName(value: string): void {
    this.fileName.set(value);
  }

  targetTypeLabel(value: TargetEntity): string {
    return value === TargetEntity.INDIVIDUAL ? 'Individual' : 'Organisation';
  }

  requestStatusLabel(value: ClientRequestStatus): string {
    switch (value) {
      case ClientRequestStatus.CONTACTED:
        return 'In Review';
      case ClientRequestStatus.PENDING:
        return 'Pending';
      case ClientRequestStatus.ACCEPTED:
        return 'Completed';
      case ClientRequestStatus.REJECTED:
        return 'Rejected';
    }
  }

  kycStatusLabel(value: KycComplianceStatus): string {
    switch (value) {
      case KycComplianceStatus.CURRENT:
        return 'Verified';
      case KycComplianceStatus.INCOMPLETE:
        return 'Unverified';
      case KycComplianceStatus.ABSENT:
        return 'Flagged';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      default:
        return 'Unverified';
    }
  }

  isOrganisationSelected(record: OrganisationRecord): boolean {
    return this.selectedOrganisationId() === record.id;
  }

  isCurrentKycStatus(value: KycComplianceStatus): boolean {
    return this.selectedKycStatus() === value;
  }

  isCurrentRequestStatus(value: ClientRequestStatus): boolean {
    return this.selectedRequestStatus() === value;
  }

  kycToneClass(value: KycComplianceStatus): string {
    switch (value) {
      case KycComplianceStatus.CURRENT:
        return 'verified';
      case KycComplianceStatus.INCOMPLETE:
        return 'unverified';
      case KycComplianceStatus.ABSENT:
        return 'flagged';
      case KycComplianceStatus.EXPIRED:
        return 'expired';
      default:
        return 'underified';
    }
  }

  requestToneClass(value: ClientRequestStatus): string {
    switch (value) {
      case ClientRequestStatus.PENDING:
        return 'active';
      case ClientRequestStatus.ACCEPTED:
        return 'success';
      case ClientRequestStatus.REJECTED:
        return 'danger';
      case ClientRequestStatus.CONTACTED:
      default:
        return 'neutral';
    }
  }
}
