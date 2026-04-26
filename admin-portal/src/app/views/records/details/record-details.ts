import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialog } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { ActivatedRoute, Router } from '@angular/router';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { ToastrService } from 'ngx-toastr';
import { AppEnvStore } from '@app/store/app-env.state';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { IndividualIdentityType } from '@app/models/bw/co/centralkyc/individual/individual-identity-type';
import { SourceOfFunds } from '@app/models/bw/co/centralkyc/source-of-funds';
import {
  IndividualUploadDocumentDialogComponent,
  UploadDocumentDialogResult,
} from '@app/views/individual/details/upload-document-dialog';

@Component({
  selector: 'app-record-details',
  templateUrl: './record-details.html',
  styleUrls: ['./record-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DatePipe],
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    TranslateModule,
    MatCardModule,
    MatTooltipModule,
    MatTabsModule,
    MatDialogModule,
  ],
})
export class RecordDetails implements OnInit {
  readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  readonly toaster = inject(ToastrService);
  readonly documentApi = inject(DocumentApi);
  readonly kycRecordApiStore = inject(KycRecordApiStore);
  readonly settingsApiStore = inject(SettingsApiStore);
  readonly dialog = inject(MatDialog);
  protected appEnvState = inject(AppEnvStore);
  datePipe = inject(DatePipe);

  readonly loading = linkedSignal(() => this.kycRecordApiStore.loading());
  readonly loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());
  readonly messages = linkedSignal(() => this.kycRecordApiStore.messages());
  readonly error = linkedSignal(() => this.kycRecordApiStore.error());
  readonly record = linkedSignal(() => this.kycRecordApiStore.data() as KycRecordDTO | null);

  @Input() id: string | null = null;

  readonly TargetEntity = TargetEntity;
  readonly KycComplianceStatus = KycComplianceStatus;
  readonly IndividualIdentityType = IndividualIdentityType;
  readonly SourceOfFunds = SourceOfFunds;
  // readonly VerificationStatus = VerificationStatus;

  selectedSectionTabIndex = 0;
  currentDocumentIndex = 0;

  private lastErrorMessage = '';

  constructor() {
    this.kycRecordApiStore.reset();

    effect(() => {
      const message = this.messages()?.[0] || '';

      if (this.error() && !this.loading() && message && message !== this.lastErrorMessage) {
        this.lastErrorMessage = message;
        this.toaster.error(message);
      }
    });

    effect(() => {
      const record = this.kycRecordApiStore.data();
      console.log('Record data updated:', record);
    });

    effect(() => {
      const documents = this.record()?.documents || [];

      if (documents.length === 0) {
        this.currentDocumentIndex = 0;
        return;
      }

      if (this.currentDocumentIndex >= documents.length) {
        this.currentDocumentIndex = documents.length - 1;
      }
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getAll();

    const routeId = this.route.snapshot.paramMap.get('id') || this.route.snapshot.queryParamMap.get('id');
    const recordId = this.id || routeId;

    if (!recordId) {
      return;
    }

    this.kycRecordApiStore.findById({ id: recordId });
  }

  backToRecords(): void {
    this.router.navigate(['/records']);
  }

  openEdit(): void {
    const id = this.record()?.id || this.id;

    if (!id) {
      return;
    }

    this.router.navigate(['/records', 'edit', id]);
  }

  openDocumentUpload(): void {
    const record = this.record();
    const id = record?.id || this.id;

    if (!id) {
      this.toaster.error('Unable to open document upload without a record id.');
      return;
    }

    const settings = this.settingsApiStore.data();
    const target = record?.target;
    const documentTypes =
      target === TargetEntity.ORGANISATION
        ? settings?.orgKycDocuments || []
        : settings?.indKycDocuments || [];

    const ref = this.dialog.open(IndividualUploadDocumentDialogComponent, {
      data: { documentTypes },
      width: '480px',
    });

    ref.afterClosed().subscribe((result: UploadDocumentDialogResult | undefined) => {
      if (!result) {
        return;
      }

      this.loading.set(true);
      this.loaderMessage.set('Uploading document...');

      this.documentApi
        .upload(TargetEntity.KYC_RECORD, id, result.documentTypeId, result.file)
        .subscribe({
          next: () => {
            this.loading.set(false);
            this.toaster.success('Document uploaded successfully.');
            this.kycRecordApiStore.findById({ id });
          },
          error: (error: any) => {
            this.loading.set(false);
            const message = error?.error?.message || 'Failed to upload document.';
            this.toaster.error(message);
          },
        });
    });
  }

  printView(): void {
    window.print();
  }

  downloadStatement(): void {
    // Download KYC verification statement
    const record = this.record();
    if (!record) {
      this.toaster.error('No record data available for download.');
      return;
    }

    // Placeholder for statement download functionality
    const fileName = `KYC_Record_${record.ref || 'report'}.pdf`;
    this.toaster.info(`Download initiated for ${fileName}`);
  }

  recordRef(): string {
    return this.record()?.ref || 'Record Details';
  }

  recordTitle(): string {
    const record = this.record();
    if (record?.target === TargetEntity.ORGANISATION) {
      return record?.ownerDetails?.name || 'Organisation Record';
    }
    return record?.ownerDetails?.name || 'Individual Record';
  }

  createdSummary(): string {
    const record = this.record();
    return `Created on ${this.formatDate(record?.createdAt)} by ${record?.createdBy || 'System Admin'}`;
  }

  targetTypeDisplay(): string {
    const target = this.record()?.target;
    switch (target) {
      case TargetEntity.INDIVIDUAL:
        return 'Individual';
      case TargetEntity.ORGANISATION:
        return 'Organisation';
      case TargetEntity.BRANCH:
        return 'Branch';
      default:
        return 'Unknown';
    }
  }

  targetAvatar(): string {
    const target = this.record()?.target;
    switch (target) {
      case TargetEntity.INDIVIDUAL:
        return 'I';
      case TargetEntity.ORGANISATION:
        return 'O';
      case TargetEntity.BRANCH:
        return 'B';
      default:
        return '?';
    }
  }

  kycStatusDisplay(): string {
    const status = this.record()?.kycStatus;
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'Current';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      case KycComplianceStatus.ABSENT:
        return 'Absent';
      case KycComplianceStatus.INCOMPLETE:
        return 'Incomplete';
      default:
        return 'Unknown';
    }
  }

  kycStatusClass(): string {
    const status = this.record()?.kycStatus;
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'current';
      case KycComplianceStatus.EXPIRED:
        return 'expired';
      case KycComplianceStatus.ABSENT:
        return 'absent';
      case KycComplianceStatus.INCOMPLETE:
        return 'incomplete';
      default:
        return 'unknown';
    }
  }

  kycCompliancePercentage(): number {
    const record = this.record();

    if (!record) {
      return 0;
    }

    // Calculate based on status
    // CURRENT = 100%, EXPIRED = 50%, INCOMPLETE = 25%, ABSENT = 0%
    switch (record.kycStatus) {
      case KycComplianceStatus.CURRENT:
        return 100;
      case KycComplianceStatus.EXPIRED:
        return 50;
      case KycComplianceStatus.INCOMPLETE:
        return 25;
      case KycComplianceStatus.ABSENT:
        return 0;
      default:
        return 0;
    }
  }

  identityTypeLabel(): string {
    const record = this.record();

    if (!record || record.target !== TargetEntity.INDIVIDUAL) {
      return 'Not applicable';
    }

    const type = record.ownerDetails.identityType as IndividualIdentityType;

    switch (type) {
      case IndividualIdentityType.OMANG:
        return 'OMANG (National ID)';
      case IndividualIdentityType.PASSPORT:
        return 'Passport';
      case IndividualIdentityType.RESIDENCE_PERMIT:
        return 'Residence Permit';
      case IndividualIdentityType.BIRTH_CERTIFICATE:
        return 'Birth Certificate';
      default:
        return 'Unknown';
    }
  }

  pepStatusLabel(): boolean {
    return this.record()?.declaration?.isPEP || false;
  }

  sanctionsMatchLabel(): boolean {
    return this.record()?.declaration?.hasSanctionsMatch || false;
  }

  sourceOfFundsDisplay(): string {
    const sources = this.record()?.sourceOfFunds || [];

    if (sources.length === 0) {
      return 'Not specified';
    }

    const labels: Record<SourceOfFunds, string> = {
      [SourceOfFunds.SALARY]: 'Salary',
      [SourceOfFunds.BUSINESS_INCOME]: 'Business Income',
      [SourceOfFunds.INVESTMENTS]: 'Investments',
      [SourceOfFunds.PENSIONS]: 'Pensions',
      [SourceOfFunds.GIFTS]: 'Gifts',
      [SourceOfFunds.REMITTANCE]: 'Remittance',
      [SourceOfFunds.OTHER]: 'Other',
    };

    return sources.map((s: SourceOfFunds) => labels[s] || s).join(', ');
  }

  // verificationStatusLabel(status: VerificationStatus | null | undefined): string {
  //   switch (status) {
  //     case VerificationStatus.VERIFIED:
  //       return 'Verified';
  //     case VerificationStatus.VERIFICATION_FAILED:
  //       return 'Failed';
  //     case VerificationStatus.UNVERIFIED:
  //     default:
  //       return 'Unverified';
  //   }
  // }

  // verificationStatusClass(status: VerificationStatus | null | undefined): string {
  //   switch (status) {
  //     case VerificationStatus.VERIFIED:
  //       return 'verified';
  //     case VerificationStatus.VERIFICATION_FAILED:
  //       return 'failed';
  //     case VerificationStatus.UNVERIFIED:
  //     default:
  //       return 'unverified';
  //   }
  // }

  verificationByLabel(value: string | null | undefined): string {
    return value || 'Not assigned';
  }

  verificationReportLabel(value: string | null | undefined): string {
    return value || 'No report provided';
  }

  complianceDateLabel(): string {
    const date = this.record()?.uploadDate || this.record()?.createdAt;
    return this.formatDate(date);
  }

  expiryDateLabel(): string {
    const date = this.record()?.expiryDate;
    if (!date) {
      return 'Not set';
    }

    const formatted = this.formatDate(date);
    const expiryDate = this.dateValue(date);

    if (expiryDate && expiryDate < new Date()) {
      return `${formatted} (Expired)`;
    }

    return formatted;
  }

  auditUser(kind: 'created' | 'modified'): string {
    const record = this.record();

    return kind === 'created'
      ? record?.createdBy || 'System'
      : record?.modifiedBy || record?.createdBy || 'System';
  }

  auditTimestamp(kind: 'created' | 'modified'): string {
    const record = this.record();
    const value = kind === 'created'
      ? record?.createdAt
      : record?.modifiedAt || record?.createdAt;

    return this.formatDateTime(value);
  }

  documentCount(): number {
    return this.record()?.documents?.length || 0;
  }

  canDownloadDocuments(): boolean {
    return this.documentCount() > 0;
  }

  downloadDocuments(): void {
    const record = this.record();
    if (!record || !record.documents || record.documents.length === 0) {
      this.toaster.error('No documents available for download.');
      return;
    }

    // Download first document as placeholder
    const doc = record.documents[0];
    this.downloadDocument(doc);
  }

  setSectionTab(index: number): void {
    this.selectedSectionTabIndex = index;
  }

  documentsList(): DocumentDTO[] {
    return this.record()?.documents || [];
  }

  currentDocument(): DocumentDTO | null {
    const documents = this.documentsList();

    if (documents.length === 0) {
      return null;
    }

    return documents[this.currentDocumentIndex] || null;
  }

  hasPreviousDocument(): boolean {
    return this.currentDocumentIndex > 0;
  }

  hasNextDocument(): boolean {
    return this.currentDocumentIndex < this.documentsList().length - 1;
  }

  showPreviousDocument(): void {
    if (!this.hasPreviousDocument()) {
      return;
    }

    this.currentDocumentIndex -= 1;
  }

  showNextDocument(): void {
    if (!this.hasNextDocument()) {
      return;
    }

    this.currentDocumentIndex += 1;
  }

  selectDocument(index: number): void {
    const documents = this.documentsList();
    if (index < 0 || index >= documents.length) {
      return;
    }

    this.currentDocumentIndex = index;
  }

  currentDocumentPositionLabel(): string {
    const total = this.documentsList().length;
    if (total === 0) {
      return '0 / 0';
    }

    return `${this.currentDocumentIndex + 1} / ${total}`;
  }

  documentTypeLabel(document: DocumentDTO | null): string {
    if (!document) {
      return 'Not available';
    }

    return document.documentType || document.documentTypeId || 'Not available';
  }

  documentCreatedAtLabel(document: DocumentDTO | null): string {
    return this.formatDate(document?.createdAt || null);
  }

  documentStatusLabel(document: DocumentDTO | null): string {
    return document?.verificationStatus || 'Unverified';
  }

  dataComparisons(document: DocumentDTO | null): Array<{ field: string; expected: string; extracted: string; matches: boolean }> {
    if (!document?.dataComparisons || !Array.isArray(document.dataComparisons)) {
      return [];
    }

    return document.dataComparisons.map((row: any) => ({
      field: row?.field || 'Unknown Field',
      expected: row?.expected || '—',
      extracted: row?.extracted || '—',
      matches: !!row?.matches,
    }));
  }

  openDocumentView(document: DocumentDTO | null): void {
    if (!document) {
      return;
    }

    if (document.id) {
      this.router.navigate(['/documents', 'details', document.id]);
      return;
    }

    if (document.url) {
      window.open(document.url, '_blank', 'noopener');
      return;
    }

    this.toaster.error('No document view is available.');
  }

  openDocumentEdit(document: DocumentDTO | null): void {
    if (!document?.id) {
      this.toaster.error('This document cannot be edited yet.');
      return;
    }

    this.router.navigate(['/documents', 'edit', document.id]);
  }

  downloadCurrentDocument(): void {
    const document = this.currentDocument();
    if (!document) {
      this.toaster.error('No document selected to download.');
      return;
    }

    this.downloadDocument(document);
  }

  private downloadDocument(document: DocumentDTO): void {
    const request = document.id
      ? this.documentApi.downloadFile(document.id)
      : document.url
        ? this.documentApi.downloadFileByUrl(document.url)
        : null;

    if (!request) {
      this.toaster.error('No downloadable file reference was found for this document.');
      return;
    }

    request.subscribe({
      next: (blob: Blob) => this.saveBlob(blob, document.fileName || 'document.pdf'),
      error: () => this.toaster.error('Failed to download document.'),
    });
  }

  private saveBlob(blob: Blob, fileName: string): void {
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');

    anchor.href = url;
    anchor.download = fileName;
    anchor.click();
    window.URL.revokeObjectURL(url);
  }

  private formatDate(value: Date | string | null | undefined): string {
    const date = this.dateValue(value);

    if (!date) {
      return 'Not recorded';
    }

    return this.datePipe.transform(date, 'dd-MM-yyyy') || 'Invalid date';
  }

  private formatDateTime(value: Date | string | null | undefined): string {
    const date = this.dateValue(value);

    if (!date) {
      return 'Not recorded';
    }

    return this.datePipe.transform(date, 'dd-MM-yyyy HH:mm') || 'Invalid date';
  }

  private dateValue(value: Date | string | null | undefined): Date | null {
    if (!value) {
      return null;
    }

    const date = value instanceof Date ? value : new Date(value);
    return Number.isNaN(date.getTime()) ? null : date;
  }
}
