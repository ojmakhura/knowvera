import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { KycSubsciptionStatus } from '@app/models/bw/co/centralkyc/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/centralkyc/subscription/kyc-subscription-dto';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { TimePeriod } from '@app/models/bw/co/centralkyc/time-period';
import { form, FormField } from '@angular/forms/signals';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { SearchObject } from '@app/models/search-object';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { SubscriptionSearchCriteria } from '@app/models/bw/co/centralkyc/subscription/subscription-search-criteria';
import { TranslateModule } from '@ngx-translate/core';
import { AppEnvStore } from '@app/store/app-env.state';

export class SearchSubscriptionsVarsForm {
  ref: string | any = null;
  organisation: OrganisationListDTO | any = null;
  organisationFilter: OrganisationListDTO | any = null;
  period: TimePeriod | any = null;
  status: KycSubsciptionStatus | any = null;
  startDate: Date | any = null;
  endDate: Date | any = null
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
    FormField,
    NgxMatSelectSearchModule,
    TranslateModule
  ],
})
export class Subscriptions implements OnInit {
  searchSubscriptionsVarsForm = new SearchSubscriptionsVarsForm();
  searchSubscriptionsSignal = signal(this.searchSubscriptionsVarsForm);
  searchSubscriptionForm = form(this.searchSubscriptionsSignal);

  readonly kycSubscriptionApiStore = inject(KycSubscriptionApiStore);
  readonly organisationApiStore = inject(OrganisationApiStore);
  protected appEnvState = inject(AppEnvStore);

  protected readonly rows = signal<KycSubscriptionDTO[]>([]);
  protected readonly dataSource = new MatTableDataSource<KycSubscriptionDTO>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly subscriptionStatus = KycSubsciptionStatus;
  protected readonly statusOptions = Object.values(KycSubsciptionStatus);
  protected readonly periodOptions = Object.values(TimePeriod);
  // protected readonly quickFilters = [
  //   { label: 'All Subscriptions', value: '' },
  //   { label: 'Active', value: KycSubsciptionStatus.ACTIVE },
  //   { label: 'Inactive', value: KycSubsciptionStatus.INACTIVE },
  //   { label: 'Cancelled', value: KycSubsciptionStatus.CANCELLED },
  // ];

  displayedColumns: string[] = ['ref', 'organisation', 'owner', 'period', 'amount', 'status', 'actions'];

  statuses = Object.values(KycSubsciptionStatus);

  constructor() {
    effect(() => {
      const page = this.kycSubscriptionApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.dataSource.data = page.content || [];
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

  // updateCriteria(value: string): void {
  //   this.updateField('criteria', value);
  // }

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
    const value = this.searchSubscriptionsSignal();

    const criteria = new SearchObject<SubscriptionSearchCriteria>();
    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = {
      ref: value.ref || null,
      organisationId: value.organisation?.id || null,
      period: value.period || null,
      status: value.status || null,
      startDate: value.startDate || null,
      endDate: value.endDate || null
    };

    this.kycSubscriptionApiStore.pagedSearch({
      criteria,
    } as any);
  }

  onPageChange(event: PageEvent): void {
    this.pageSize.set(event.pageSize);
    this.doSearch(event.pageIndex, event.pageSize);
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

  organisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any) {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  filterOrganisation() {
    const filterValue = this.searchSubscriptionsSignal().organisationFilter || '';
    let search = new SearchObject<OrganisationSearchCriteria>();
    search.criteria = {
      name: filterValue,
      isClient: true
    };

    this.organisationApiStore.search({
      criteria: search,
    });
  }
}
