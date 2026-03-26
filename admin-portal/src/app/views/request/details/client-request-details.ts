import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, signal } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';

type LabeledValue = {
  label: string;
  value: string;
};

type IdentityField = {
  label: string;
  value: string;
  tone?: 'primary' | 'default';
  icon?: string;
};

@Component({
  selector: 'app-client-request-details',
  templateUrl: './client-request-details.html',
  styleUrls: ['./client-request-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule
  ]
})
export class ClientRequestDetails {
  readonly requestStatus = signal<ClientRequestStatus>(ClientRequestStatus.CONTACTED);
  readonly targetKycStatus = signal<KycComplianceStatus>(KycComplianceStatus.ABSENT);
  readonly verificationPercent = signal(15);

  readonly coreIdentity = signal<IdentityField[]>([
    { label: 'Full Legal Name', value: 'Alexander J. Sterling', tone: 'primary' },
    {
      label: 'Identity Type',
      value: 'PASSPORT_INTERNATIONAL',
      icon: 'badge',
    },
    { label: 'Registration No.', value: 'GB-8829-X-442' },
    {
      label: 'Email Address',
      value: 'a.sterling@private-equity.co.uk',
      tone: 'primary',
    },
  ]);

  readonly organisationContext = signal<LabeledValue[]>([
    { label: 'Organisation Name', value: 'Sterling Global Assets Ltd' },
    { label: 'Internal Org ID', value: 'ORG-9920-ALPHA' },
    { label: 'Org Registration No.', value: 'SC-8812903321' },
  ]);

  readonly auditFields = signal<LabeledValue[]>([
    { label: 'Created At', value: '2023-11-14 09:22:11' },
    { label: 'Created By', value: 'System Gateway Alpha' },
    { label: 'Last Modified', value: '2024-02-01 14:15:00' },
    { label: 'Modified By', value: 'Compliance_Officer_32' },
  ]);

  readonly progressSegments = computed(() => {
    const percent = Math.max(0, Math.min(this.verificationPercent(), 100));
    const filled = Math.max(1, Math.round(percent / 20));

    return Array.from({ length: 5 }, (_, index) => index < filled);
  });

  backToDashboard(): void {}

  updateStatus(): void {}

  archiveRequest(): void {}

  flagForReview(): void {}

  approveRequest(): void {}

  downloadFile(): void {}

  statusLabel(status: ClientRequestStatus): string {
    switch (status) {
      case ClientRequestStatus.CONTACTED:
        return 'In Review';
      case ClientRequestStatus.PENDING:
        return 'Pending';
      case ClientRequestStatus.ACCEPTED:
        return 'Accepted';
      case ClientRequestStatus.REJECTED:
        return 'Rejected';
    }
  }

  statusClass(status: ClientRequestStatus): string {
    switch (status) {
      case ClientRequestStatus.ACCEPTED:
        return 'accepted';
      case ClientRequestStatus.PENDING:
        return 'pending';
      case ClientRequestStatus.REJECTED:
        return 'rejected';
      case ClientRequestStatus.CONTACTED:
      default:
        return 'review';
    }
  }

  targetStatusLabel(status: KycComplianceStatus): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'Current';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      case KycComplianceStatus.INCOMPLETE:
        return 'Incomplete';
      case KycComplianceStatus.ABSENT:
      default:
        return 'Absent';
    }
  }

  targetStatusClass(status: KycComplianceStatus): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'current';
      case KycComplianceStatus.EXPIRED:
        return 'expired';
      case KycComplianceStatus.INCOMPLETE:
        return 'incomplete';
      case KycComplianceStatus.ABSENT:
      default:
        return 'absent';
    }
  }

  iconForAudit(label: string): string {
    switch (label) {
      case 'Created At':
        return 'calendar_today';
      case 'Created By':
        return 'account_circle';
      case 'Last Modified':
        return 'history';
      case 'Modified By':
      default:
        return 'edit_note';
    }
  }
}
