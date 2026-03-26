import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { KycSubsciptionStatus } from '@app/models/bw/co/centralkyc/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/centralkyc/subscription/kyc-subscription-dto';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';

export class SearchSubscriptionsVarsForm {
  criteria: string = '';
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
  imports: [
    CommonModule, 
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatPaginatorModule,
    MatChipsModule,
    MatTooltipModule,
  ],
})
export class Subscriptions implements OnInit {
  searchSubscriptionsVarsForm = new SearchSubscriptionsVarsForm();
  searchSubscriptionsSignal = signal(this.searchSubscriptionsVarsForm);

  readonly kycSubscriptionApiStore = inject(KycSubscriptionApiStore);
  protected readonly rows = signal<KycSubscriptionDTO[]>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly subscriptionStatus = KycSubsciptionStatus;
  protected readonly statusOptions = Object.values(KycSubsciptionStatus);
  protected readonly quickFilters = [
    { label: 'All Subscriptions', value: '' },
    { label: 'Active', value: KycSubsciptionStatus.ACTIVE },
    { label: 'Inactive', value: KycSubsciptionStatus.INACTIVE },
    { label: 'Cancelled', value: KycSubsciptionStatus.CANCELLED },
  ];

  displayedColumns: string[] = ['ref', 'organisation', 'owner', 'period', 'amount', 'status', 'actions'];

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

  updateField(field: keyof SearchSubscriptionsVarsForm, value: string): void {
    this.searchSubscriptionsSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  updateCriteria(value: string): void {
    this.updateField('criteria', value);
  }

  onSearchSubmit(): void {
    this.doSearch(0, this.pageSize());
  }

  applyStatusFilter(status: string): void {
    this.updateField('status', status);
    this.doSearch(0, this.pageSize());
  }

  isStatusFilterActive(status: string): boolean {
    return this.searchSubscriptionsSignal().status === status;
  }

  resetSearch(): void {
    this.searchSubscriptionsSignal.set(new SearchSubscriptionsVarsForm());
    this.doSearch();
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

  onPageChange(event: PageEvent): void {
    this.pageSize.set(event.pageSize);
    this.doSearch(event.pageIndex, event.pageSize);
  }

  searchText(): string {
    const value = this.searchSubscriptionsSignal();

    return [value.criteria, value.status]
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

  organisationSubtitle(row: KycSubscriptionDTO): string {
    return row.organisationRegistrationNo || row.organisationCode || 'No organisation metadata';
  }

  pageCount(): number {
    return this.rows().length;
  }

  summaryCount(status: KycSubsciptionStatus): number {
    return this.rows().filter((row) => row.status === status).length;
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

  statusClass(status: string | null | undefined): string {
    switch (status) {
      case 'ACTIVE':
        return 'active';
      case 'INACTIVE':
        return 'inactive';
      case 'CANCELLED':
        return 'cancelled';
      default:
        return 'unknown';
    }
  }

  statusLabel(status: string | null | undefined): string {
    return status ? `${status.charAt(0)}${status.slice(1).toLowerCase()}` : 'Unknown';
  }
}
