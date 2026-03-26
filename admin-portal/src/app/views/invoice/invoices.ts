import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  linkedSignal,
  OnInit,
  signal,
} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { InvoiceSearchCriteria } from '@app/models/bw/co/centralkyc/invoice/invoice-search-criteria';
import { KycInvoiceDTO } from '@app/models/bw/co/centralkyc/invoice/kyc-invoice-dto';
import { SearchObject } from '@app/models/search-object';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';

export class SearchInvoicesVarsForm {
  ref: string = '';
  organisationName: string = '';
  organisationRegistrationNo: string = '';
  paid: string = '';
  invoices: Array<KycInvoiceDTO> = [];
}

@Component({
  selector: 'app-invoices',
  standalone: true,
  templateUrl: './invoices.html',
  styleUrls: ['./invoices.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatTooltipModule,
    MatProgressBarModule,
  ],
})
export class Invoices implements OnInit {
  searchInvoicesVarsForm = new SearchInvoicesVarsForm();
  searchInvoicesSignal = signal(this.searchInvoicesVarsForm);

  readonly kycInvoiceApiStore = inject(KycInvoiceApiStore);
  protected readonly rows = signal<KycInvoiceDTO[]>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly loading = linkedSignal(() => this.kycInvoiceApiStore.loading());
  protected readonly loaderMessage = linkedSignal(() => this.kycInvoiceApiStore.loaderMessage());
  protected readonly paidOptions = [
    { label: 'All Statuses', value: '' },
    { label: 'Paid', value: 'true' },
    { label: 'Unpaid', value: 'false' },
  ];

  displayedColumns: string[] = ['ref', 'organisation', 'billingDate', 'amount', 'status', 'actions'];

  constructor() {
    effect(() => {
      const page = this.kycInvoiceApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);

      this.searchInvoicesSignal.update((state) => ({
        ...state,
        invoices: page.content || [],
      }));
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  updateField(field: keyof SearchInvoicesVarsForm, value: string): void {
    this.searchInvoicesSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  handlePageEvent(e: PageEvent) {
    this.doSearch(e.pageIndex, e.pageSize);
  }

  resetSearch(): void {
    this.searchInvoicesSignal.set(new SearchInvoicesVarsForm());
    this.doSearch();
  }

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {
    const value = this.searchInvoicesSignal();
    const criteria = new SearchObject<InvoiceSearchCriteria>();

    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = {
      ref: value.ref || null,
      organisationName: value.organisationName || null,
      organisationRegistrationNo: value.organisationRegistrationNo || null,
      organisatonId: null,
      organisatonCode: null,
      paid: value.paid === '' ? null : value.paid === 'true',
    };

    this.kycInvoiceApiStore.pagedSearch({ criteria });
  }

  openCreate(): void {
    this.router.navigate(['/', 'invoice', 'edit']);
  }

  openDetails(id: string): void {
    this.router.navigate(['/', 'invoice', 'details', id]);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'invoice', 'edit', id]);
  }

  formatDate(value: Date | string | null | undefined): string {
    if (!value) {
      return '—';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return String(value);
    }

    return date.toLocaleDateString(undefined, {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  }

  formatAmount(value: number | string | null | undefined): string {
    const amount = typeof value === 'string' ? Number(value) : value;

    if (amount === null || amount === undefined || Number.isNaN(amount)) {
      return '—';
    }

    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'EUR',
      minimumFractionDigits: 2,
    }).format(amount);
  }

  statusLabel(row: KycInvoiceDTO): string {
    return row.paid ? 'Verified Paid' : 'Awaiting Settlement';
  }

  statusClass(row: KycInvoiceDTO): 'status-approved' | 'status-pending' {
    return row.paid ? 'status-approved' : 'status-pending';
  }

  organisationInitial(row: KycInvoiceDTO): string {
    return (row.organisationName || '?').trim().charAt(0).toUpperCase() || '?';
  }

  organisationMeta(row: KycInvoiceDTO): string {
    return row.organisationRegistrationNo || row.organisationCode || this.issuedByOf(row);
  }

  issuedByOf(row: KycInvoiceDTO): string {
    return row.createdBy || row.modifiedBy || 'System';
  }

  showingLabel(): string {
    return `Showing ${this.totalElements()} results`;
  }

  pageReport(): string {
    const total = this.totalElements();

    if (!total) {
      return 'No invoice records available';
    }

    const start = this.currentPage() * this.pageSize() + 1;
    const end = Math.min(total, start + this.rows().length - 1);

    return `Displaying ${start}-${end} of ${total} records`;
  }

  trackByInvoice(_: number, row: KycInvoiceDTO): string {
    return row.id || row.ref || `${row.organisationId}-${row.issueDate}`;
  }
}
