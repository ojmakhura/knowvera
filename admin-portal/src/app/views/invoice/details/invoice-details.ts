import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';
import { ActivatedRoute, Router } from '@angular/router';
import { KycInvoiceDTO } from '@app/models/bw/co/centralkyc/invoice/kyc-invoice-dto';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { ToastrService } from 'ngx-toastr';
import { AppEnvStore } from '@app/store/app-env.state';

type RepositoryFile = {
  key: 'invoice' | 'payment';
  name: string;
  type: string;
  icon: string;
  document: DocumentDTO | null;
};

@Component({
  selector: 'app-invoice-details',
  templateUrl: './invoice-details.html',
  styleUrls: ['./invoice-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    TranslateModule,
    MatCardModule,
    MatTooltipModule,
  ],
})
export class InvoiceDetails implements OnInit {
  readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  readonly toaster = inject(ToastrService);
  readonly documentApi = inject(DocumentApi);
  readonly kycInvoiceApiStore = inject(KycInvoiceApiStore);
  protected appEnvState = inject(AppEnvStore);

  readonly loading = linkedSignal(() => this.kycInvoiceApiStore.loading());
  readonly loaderMessage = linkedSignal(() => this.kycInvoiceApiStore.loaderMessage());
  readonly messages = linkedSignal(() => this.kycInvoiceApiStore.messages());
  readonly error = linkedSignal(() => this.kycInvoiceApiStore.error());
  readonly invoice = linkedSignal(() => this.kycInvoiceApiStore.data() as KycInvoiceDTO | null);

  @Input() id: string | null = null;

  private lastErrorMessage = '';

  constructor() {
    this.kycInvoiceApiStore.reset();

    effect(() => {
      const message = this.messages()?.[0] || '';

      if (this.error() && !this.loading() && message && message !== this.lastErrorMessage) {
        this.lastErrorMessage = message;
        this.toaster.error(message);
      }
    });

    effect(() => {
      const invoice = this.kycInvoiceApiStore.data();

      console.log('Invoice data updated:', invoice);
    });
  }

  ngOnInit(): void {
    const routeId = this.route.snapshot.paramMap.get('id') || this.route.snapshot.queryParamMap.get('id');
    const invoiceId = this.id || routeId;

    if (!invoiceId) {
      return;
    }

    this.kycInvoiceApiStore.findById({ id: invoiceId });
  }

  backToInvoices(): void {
    this.router.navigate(['/invoice']);
  }

  openEdit(): void {
    const id = this.invoice()?.id || this.id;

    if (!id) {
      return;
    }

    this.router.navigate(['/invoice', 'edit', id]);
  }

  printView(): void {
    window.print();
  }

  downloadPrimaryDocument(): void {
    // const primary = this.repositoryFiles().find((file) => file.key === 'invoice');

    // if (!primary) {
    //   this.toaster.error('No invoice document is available.');
    //   return;
    // }

    // this.downloadAttachment(primary);
  }

  // repositoryFiles(): RepositoryFile[] {
  //   const invoice = this.invoice();

  //   if (!invoice) {
  //     return [];
  //   }

  //   return [
  //     {
  //       key: 'invoice',
  //       name: invoice.invoiceDocument?.fileName || `${invoice.ref || 'invoice'}.pdf`,
  //       type: 'Invoice Document',
  //       icon: 'description',
  //       document: invoice.invoiceDocument || null,
  //     },
  //     {
  //       key: 'payment',
  //       name: invoice.proofOfPayment?.fileName || (invoice.paid ? 'Payment proof pending filename' : 'Awaiting payment proof'),
  //       type: 'Proof Of Payment',
  //       icon: 'verified_user',
  //       document: invoice.proofOfPayment || null,
  //     },
  //   ];
  // }

  downloadAttachment(file: RepositoryFile): void {
    if (!file.document) {
      this.toaster.error(`No ${file.type.toLowerCase()} is available.`);
      return;
    }

    this.downloadDocument(file.document, file.name);
  }

  invoiceRef(): string {
    return this.invoice()?.ref || 'Invoice Details';
  }

  createdSummary(): string {
    const invoice = this.invoice();
    return `Created on ${this.formatDate(invoice?.createdAt || invoice?.issueDate)} by ${invoice?.createdBy || 'System Admin'}`;
  }

  amountLabel(): string {
    return this.formatAmount(this.invoice()?.amount);
  }

  paymentStatusLabel(): string {
    return this.invoice()?.paid ? 'Paid' : 'Pending';
  }

  paymentReference(): string {
    return this.invoice()?.paymentReference || 'No payment reference recorded';
  }

  organisationInitial(): string {
    return (this.invoice()?.organisationName || 'V').trim().charAt(0).toUpperCase();
  }

  subscriptionPeriodLabel(): string {
    const period = String(this.invoice()?.subscriptionPeriod || '').toLowerCase();

    if (!period) {
      return 'Custom billing cycle';
    }

    return `${period.charAt(0).toUpperCase()}${period.slice(1)} settlement cycle`;
  }

  serviceTier(): string {
    const amount = Number(this.invoice()?.amount || 0);

    if (amount >= 20000) {
      return 'Enterprise Vault';
    }

    if (amount >= 5000) {
      return 'Professional Vault';
    }

    return 'Core Vault';
  }

  auditUser(kind: 'created' | 'modified'): string {
    const invoice = this.invoice();

    return kind === 'created'
      ? invoice?.createdBy || 'System'
      : invoice?.modifiedBy || invoice?.createdBy || 'System';
  }

  auditTimestamp(kind: 'created' | 'modified'): string {
    const invoice = this.invoice();
    const value = kind === 'created'
      ? invoice?.createdAt || invoice?.issueDate
      : invoice?.modifiedAt || invoice?.createdAt || invoice?.issueDate;

    return this.formatDateTime(value);
  }

  issueDateLabel(): string {
    return this.formatDate(this.invoice()?.issueDate);
  }

  paymentDateLabel(): string {
    return this.invoice()?.paymentDate ? this.formatDate(this.invoice()?.paymentDate) : 'Awaiting settlement';
  }

  coverageLabel(): string {
    const invoice = this.invoice();
    const start = this.formatDate(invoice?.startDate);
    const end = this.formatDate(invoice?.endDate);

    if (start === 'Not recorded' && end === 'Not recorded') {
      return 'Custom coverage period';
    }

    return `${start} - ${end}`;
  }

  canDownload(file: RepositoryFile): boolean {
    return Boolean(file.document?.id || file.document?.url);
  }

  trackFile(_: number, file: RepositoryFile): string {
    return `${file.key}-${file.name}`;
  }

  private downloadDocument(document: DocumentDTO, fileName: string): void {
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
      next: (blob: Blob) => this.saveBlob(blob, fileName),
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

  private formatAmount(amount: number | string | null | undefined): string {
    const numericAmount = Number(amount);

    if (!Number.isFinite(numericAmount)) {
      return 'USD 0.00';
    }

    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(numericAmount);
  }

  private formatDate(value: Date | string | null | undefined): string {
    const date = this.dateValue(value);

    if (!date) {
      return 'Not recorded';
    }

    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    }).format(date);
  }

  private formatDateTime(value: Date | string | null | undefined): string {
    const date = this.dateValue(value);

    if (!date) {
      return 'Not recorded';
    }

    return new Intl.DateTimeFormat('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  }

  private dateValue(value: Date | string | null | undefined): Date | null {
    if (!value) {
      return null;
    }

    const date = value instanceof Date ? value : new Date(value);
    return Number.isNaN(date.getTime()) ? null : date;
  }
}
