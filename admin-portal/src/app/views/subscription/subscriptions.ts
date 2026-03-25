import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { KycSubsciptionStatus } from '@app/models/bw/co/centralkyc/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/centralkyc/subscription/kyc-subscription-dto';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';

export class SearchSubscriptionsVarsForm {
  ref: string = '';
  organisationName: string = '';
  period: string = '';
  status: string = '';
  subscriptions: Array<KycSubscriptionDTO> = [];
}

@Component({
  selector: 'app-subscriptions',
  standalone: true,
  templateUrl: './subscriptions.html',
  styleUrls: ['./subscriptions.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {},
  imports: [CommonModule, MatIconModule],
})
export class Subscriptions implements OnInit, AfterViewInit, OnDestroy {
  searchSubscriptionsVarsForm = new SearchSubscriptionsVarsForm();
  searchSubscriptionsSignal = signal(this.searchSubscriptionsVarsForm);

  readonly kycSubscriptionApiStore = inject(KycSubscriptionApiStore);
  protected readonly rows = signal<KycSubscriptionDTO[]>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly statusOptions = Object.values(KycSubsciptionStatus);

  constructor() {
    effect(() => {
      const page = this.kycSubscriptionApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);

      this.searchSubscriptionsSignal.update((state) => ({
        ...state,
        subscriptions: page.content || [],
      }));
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  updateField(field: keyof SearchSubscriptionsVarsForm, value: string): void {
    this.searchSubscriptionsSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  resetSearch(): void {
    this.searchSubscriptionsSignal.set(new SearchSubscriptionsVarsForm());
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
    const criteria = this.searchText();

    if (!criteria) {
      this.kycSubscriptionApiStore.getAllPaged({ pageNumber, pageSize });
      return;
    }

    this.kycSubscriptionApiStore.pagedSearch({
      criteria,
      pageNumber,
      pageSize,
    });
  }

  searchText(): string {
    const value = this.searchSubscriptionsSignal();

    return [value.ref, value.organisationName, value.period, value.status]
      .map((item) => item.trim())
      .filter((item) => !!item)
      .join(' ');
  }

  openCreate(): void {
    this.router.navigate(['/', 'subscription', 'edit']);
  }

  openDetails(id: string): void {
    this.router.navigate(['/', 'subscription', 'details', id]);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'subscription', 'edit', id]);
  }

  ownerOf(row: KycSubscriptionDTO): string {
    return row.createdBy || row.modifiedBy || 'System';
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

  periodLabel(row: KycSubscriptionDTO): string {
    const period = row.period || '—';
    const endDate = row.endDate ? this.formatDate(row.endDate) : null;

    return endDate ? `${period} · ends ${endDate}` : String(period);
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

  statusClass(status: string | null | undefined): 'status-approved' | 'status-pending' | 'status-rejected' {
    switch (status) {
      case 'ACTIVE':
        return 'status-approved';
      case 'INACTIVE':
        return 'status-pending';
      default:
        return 'status-rejected';
    }
  }
}
