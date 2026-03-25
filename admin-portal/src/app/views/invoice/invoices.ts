import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, OnDestroy, OnInit, signal } from '@angular/core';
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
  host: {},
  imports: [MatIconModule],
})
export class Invoices implements OnInit, AfterViewInit, OnDestroy {
  searchInvoicesVarsForm = new SearchInvoicesVarsForm();
  searchInvoicesSignal = signal(this.searchInvoicesVarsForm);

  readonly kycInvoiceApiStore = inject(KycInvoiceApiStore);
  protected readonly rows = signal<KycInvoiceDTO[]>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly paidOptions = [
    { label: 'All Statuses', value: '' },
    { label: 'Paid', value: 'true' },
    { label: 'Unpaid', value: 'false' },
  ];

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

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  updateField(field: keyof SearchInvoicesVarsForm, value: string): void {
    this.searchInvoicesSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  resetSearch(): void {
    this.searchInvoicesSignal.set(new SearchInvoicesVarsForm());
    this.doSearch();
  }

  pageNumbers(): number[] {
    return Array.from({ length: this.totalPages() }, (_, index) => index + 1);
  }

  previousPage(): void {
    if (this.currentPage() <= 0) {
      return;
    }

    this.doSearch(this.currentPage() - 1, this.pageSize());
  }

  nextPage(): void {
    if (this.currentPage() >= this.totalPages() - 1) {
      return;
    }

    this.doSearch(this.currentPage() + 1, this.pageSize());
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.currentPage()) {
      return;
    }

    this.doSearch(page, this.pageSize());
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

  issuedByOf(row: KycInvoiceDTO): string {
    return row.createdBy || row.modifiedBy || 'System';
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
    return row.paid ? 'PAID' : 'UNPAID';
  }

  statusClass(row: KycInvoiceDTO): 'status-approved' | 'status-pending' {
    return row.paid ? 'status-approved' : 'status-pending';
  }
}
