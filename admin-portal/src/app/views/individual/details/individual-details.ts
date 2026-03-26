import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { EmploymentRecordDTO } from '@app/models/bw/co/centralkyc/individual/employment/employment-record-dto';
import { PhoneNumber } from '@app/models/bw/co/centralkyc/phone-number';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';

@Component({
  selector: 'app-individual-details',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './individual-details.html',
  styleUrl: './individual-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class IndividualDetails implements OnInit, AfterViewInit, OnDestroy {
  @Input() id: string | null = null;
  readonly individualApiStore = inject(IndividualApiStore);
  readonly router = inject(Router);

  individual = linkedSignal(() => this.individualApiStore.data());

  loaderMessage = linkedSignal(() => this.individualApiStore.loaderMessage());
  messages = linkedSignal(() => this.individualApiStore.messages());
  success = linkedSignal(() => this.individualApiStore.success());
  loading = linkedSignal(() => this.individualApiStore.loading());
  error = linkedSignal(() => this.individualApiStore.error());

  constructor() {}

  ngOnInit(): void {

    if (this.id) {

      this.individualApiStore.findById({ id: this.id });
    }
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  openEdit(): void {
    this.router.navigate(['/individual', 'edit', this.individual().id || this.id]);
  }

  fullName(): string {
    const individual = this.individual();
    return [individual.firstName, individual.middleName, individual.surname]
      .filter(Boolean)
      .join(' ') || 'Unknown Individual';
  }

  summaryText(): string {
    const individual = this.individual();
    const status = this.statusLabel(individual.kycStatus);
    const verifiedDate = this.formatDate(individual.latestKyc?.createdAt || individual.modifiedAt);
    return `${status} individual record. Last verified on ${verifiedDate}. This profile is restricted to authorized personnel only.`;
  }

  statusLabel(status: string | null | undefined): string {
    switch (status) {
      case 'CURRENT':
        return 'Verified Profile';
      case 'INCOMPLETE':
        return 'Pending Review';
      case 'EXPIRED':
        return 'Expired Profile';
      default:
        return 'Flagged Profile';
    }
  }

  statusClass(status: string | null | undefined): string {
    switch (status) {
      case 'CURRENT':
        return 'verified';
      case 'INCOMPLETE':
        return 'pending';
      case 'EXPIRED':
        return 'expired';
      default:
        return 'flagged';
    }
  }

  formatDate(value: Date | string | null | undefined): string {
    if (!value) {
      return 'Not available';
    }

    const date = value instanceof Date ? value : new Date(value);
    if (Number.isNaN(date.getTime())) {
      return String(value);
    }

    return new Intl.DateTimeFormat('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
    }).format(date);
  }

  formatDateTime(value: Date | string | null | undefined): string {
    if (!value) {
      return 'Not available';
    }

    const date = value instanceof Date ? value : new Date(value);
    if (Number.isNaN(date.getTime())) {
      return String(value);
    }

    return new Intl.DateTimeFormat('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  }

  phoneNumbers(): PhoneNumber[] {
    return this.individual().phoneNumbers || [];
  }

  latestEmploymentRecord(): EmploymentRecordDTO | null {
    const records = this.individual().employmentRecords || [];
    return records.length ? records[0] : null;
  }

  documents(): DocumentDTO[] {
    return this.individual().latestKyc?.documents || [];
  }

  postalAddressSameAsPhysical(): boolean {
    const individual = this.individual();
    return !individual.postalAddress || individual.postalAddress === individual.physicalAddress;
  }

  recordHash(): string {
    const base = this.individual().id || this.individual().identityNo || 'record';
    return `${String(base).slice(0, 4)}...${String(base).slice(-8)}`;
  }
}
