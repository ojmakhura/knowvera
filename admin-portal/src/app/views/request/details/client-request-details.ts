import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, computed, inject, Input, OnDestroy, OnInit, signal } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ClientRequestStatus } from '@app/models/bw/co/kyvera/organisation/client/client-request-status';
import { KycComplianceStatus } from '@app/models/bw/co/kyvera/kyc/kyc-compliance-status';
import { ClientRequestApiStore } from '@app/store/bw/co/kyvera/organisation/client/client-request-api.store';

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
export class ClientRequestDetails implements OnInit, AfterViewInit, OnDestroy {
  
  readonly clientRequestApiStore = inject(ClientRequestApiStore);
  readonly clientRequest = this.clientRequestApiStore.data;

  readonly requestReference = computed(() => this.displayValue(this.clientRequest().ref) || this.displayValue(this.id));
  readonly requestStatus = computed<ClientRequestStatus>(
    () => this.clientRequest().status || ClientRequestStatus.PENDING,
  );
  readonly targetKycStatus = computed<KycComplianceStatus>(
    () => this.clientRequest().targetKycStatus || KycComplianceStatus.ABSENT,
  );
  readonly verificationPercent = computed(() => {
    switch (this.targetKycStatus()) {
      case KycComplianceStatus.CURRENT:
        return 100;
      case KycComplianceStatus.INCOMPLETE:
        return 60;
      case KycComplianceStatus.EXPIRED:
        return 30;
      case KycComplianceStatus.ABSENT:
      default:
        return 0;
    }
  });

  readonly coreIdentity = computed<IdentityField[]>(() => {
    const request = this.clientRequest();

    return [
      { label: 'Full Legal Name', value: this.displayValue(request.name), tone: 'primary' },
      {
        label: 'Identity Type',
        value: this.displayValue(request.identityType),
        icon: 'badge',
      },
      { label: 'Registration No.', value: this.displayValue(request.registration) },
      {
        label: 'Email Address',
        value: this.displayValue(request.emailAddress),
        tone: 'primary',
      },
    ];
  });

  readonly organisationContext = computed<LabeledValue[]>(() => {
    const request = this.clientRequest();

    return [
      { label: 'Organisation Name', value: this.displayValue(request.organisation) },
      { label: 'Internal Org ID', value: this.displayValue(request.organisationCode) },
      { label: 'Org Registration No.', value: this.displayValue(request.organisationRegistrationNo) },
    ];
  });

  readonly auditFields = computed<LabeledValue[]>(() => {
    const request = this.clientRequest();

    return [
      { label: 'Created At', value: this.formatDateTime(request.createdAt) },
      { label: 'Created By', value: this.displayValue(request.createdBy) },
      { label: 'Last Modified', value: this.formatDateTime(request.modifiedAt) },
      { label: 'Modified By', value: this.displayValue(request.modifiedBy) },
    ];
  });

  readonly artifactTitle = computed(() => this.displayValue(this.clientRequest().documentType));
  readonly artifactTypeMeta = computed(() => {
    const request = this.clientRequest();
    const typeId = this.displayValue(request.documentTypeId);
    const documentId = this.displayValue(request.documentId);

    if (typeId !== '-') {
      return `Type ID: ${typeId}`;
    }

    if (documentId !== '-') {
      return `Document ID: ${documentId}`;
    }

    return 'Type ID: -';
  });
  readonly artifactFileName = computed(() => this.displayValue(this.clientRequest().fileName));

  readonly progressSegments = computed(() => {
    const percent = Math.max(0, Math.min(this.verificationPercent(), 100));
    const filled = Math.round(percent / 20);

    return Array.from({ length: 5 }, (_, index) => index < filled);
  });

  @Input() id: string | null = null;

  constructor() {
  }

  ngOnInit(): void {
    if(this.id) {

      this.clientRequestApiStore.findById({ id: this.id });
    }
  }

  ngAfterViewInit(): void {

  }

  ngOnDestroy(): void {
  }

  backToDashboard(): void {}

  updateStatus(): void {}

  archiveRequest(): void {}

  flagForReview(): void {}

  approveRequest(): void {}

  downloadFile(): void {}

  private displayValue(value: unknown): string {
    if (value === null || value === undefined || value === '') {
      return '-';
    }

    return String(value);
  }

  private formatDateTime(value: unknown): string {
    if (!value) {
      return '-';
    }

    const date = value instanceof Date ? value : new Date(String(value));

    if (Number.isNaN(date.getTime())) {
      return '-';
    }

    return date.toLocaleString();
  }

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
      default:
        return 'Pending';
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
