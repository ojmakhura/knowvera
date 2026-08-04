import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, effect, inject, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink } from '@angular/router';
import { Loader } from '@app/@shared/loader/loader';
import { KycComplianceStatus } from '@app/models/bw/co/kyvera/kyc/kyc-compliance-status';
import { OrganisationListDTO } from '@app/models/bw/co/kyvera/organisation/organisation-list-dto';
import { ClientRequestDTO } from '@app/models/bw/co/kyvera/organisation/client/client-request-dto';
import { ClientRequestSearchCriteria } from '@app/models/bw/co/kyvera/organisation/client/client-request-search-criteria';
import { ClientRequestStatus } from '@app/models/bw/co/kyvera/organisation/client/client-request-status';
import { TargetEntity } from '@app/models/bw/co/kyvera/target-entity';
import { SearchObject } from '@app/models/search-object';
import { OrganisationApiStore } from '@app/store/bw/co/kyvera/organisation/organisation-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/kyvera/organisation/client/client-request-api.store';

type ClientRequestSearchForm = {
  name: string;
  emailAddress: string;
  organisationId: string;
  status: ClientRequestStatus | '';
  target: TargetEntity | '';
  targetId: string;
};

const INITIAL_FILTERS: ClientRequestSearchForm = {
  name: '',
  emailAddress: '',
  organisationId: '',
  status: '',
  target: '',
  targetId: '',
};

@Component({
  selector: 'app-client-requests',
  templateUrl: './client-requests.html',
  styleUrls: ['./client-requests.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    Loader,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatTooltipModule,
    RouterLink
],
})
export class ClientRequests implements OnInit {
  private readonly router = inject(Router);
  private readonly organisationApiStore = inject(OrganisationApiStore);
  private readonly clientRequestApiStore = inject(ClientRequestApiStore);

  readonly displayedColumns = ['client', 'ref', 'status', 'organisation', 'actions'];
  readonly filters = signal<ClientRequestSearchForm>({ ...INITIAL_FILTERS });
  readonly organisations = signal<OrganisationListDTO[]>([]);
  readonly rows = signal<ClientRequestDTO[]>([]);
  readonly dataSource = new MatTableDataSource<ClientRequestDTO>([]);
  readonly currentPage = signal(0);
  readonly pageSize = signal(10);
  readonly totalElements = signal(0);
  readonly statusOptions = Object.values(ClientRequestStatus);
  readonly targetOptions = [TargetEntity.INDIVIDUAL, TargetEntity.ORGANISATION];
  readonly loading = computed(() => this.clientRequestApiStore.loading());
  readonly totalPages = signal(0);
  readonly showingStart = computed(() => {
    if (!this.totalElements()) {
      return 0;
    }

    return this.currentPage() * this.pageSize() + 1;
  });
  readonly showingEnd = computed(() => {
    if (!this.totalElements()) {
      return 0;
    }

    return Math.min(this.totalElements(), this.showingStart() + this.rows().length - 1);
  });

  constructor() {
    effect(() => {
      const page = this.clientRequestApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.dataSource.data = page.content || [];
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || page.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || page.totalPages || 0);
    });

    effect(() => {
      this.organisations.set(this.organisationApiStore.dataList() || []);
    });
  }

  ngOnInit(): void {
    this.organisationApiStore.getAll();
    this.search();
  }

  updateField<K extends keyof ClientRequestSearchForm>(field: K, value: ClientRequestSearchForm[K]): void {
    this.filters.update((current) => ({
      ...current,
      [field]: value,
    }));
  }

  clearFilters(): void {
    this.filters.set({ ...INITIAL_FILTERS });
    this.search(0, this.pageSize());
  }

  onSubmit(): void {
    this.search(0, this.pageSize());
  }

  handlePageEvent(event: PageEvent): void {
    this.search(event.pageIndex, event.pageSize);
  }

  createRequest(): void {
    this.router.navigate(['client-request', 'edit']);
  }

  openDetails(id: string | null | undefined): void {
    if (!id) {
      return;
    }

    this.router.navigate(['client-request', 'details', id]);
  }

  openEdit(id: string | null | undefined): void {
    if (!id) {
      return;
    }

    this.router.navigate(['client-request', 'edit', id]);
  }

  statusLabel(status: ClientRequestStatus | null | undefined): string {
    switch (status) {
      case ClientRequestStatus.ACCEPTED:
        return 'Approved';
      case ClientRequestStatus.REJECTED:
        return 'Rejected';
      case ClientRequestStatus.CONTACTED:
        return 'Contacted';
      case ClientRequestStatus.PENDING:
      default:
        return 'Pending';
    }
  }

  statusClass(status: ClientRequestStatus | null | undefined): string {
    switch (status) {
      case ClientRequestStatus.ACCEPTED:
        return 'approved';
      case ClientRequestStatus.REJECTED:
        return 'rejected';
      case ClientRequestStatus.CONTACTED:
      case ClientRequestStatus.PENDING:
      default:
        return 'pending';
    }
  }

  statusIcon(status: ClientRequestStatus | null | undefined): string {
    switch (status) {
      case ClientRequestStatus.ACCEPTED:
        return 'verified';
      case ClientRequestStatus.REJECTED:
        return 'cancel';
      case ClientRequestStatus.CONTACTED:
        return 'mark_email_read';
      case ClientRequestStatus.PENDING:
      default:
        return 'schedule';
    }
  }

  targetLabel(target: TargetEntity | null | undefined): string {
    switch (target) {
      case TargetEntity.INDIVIDUAL:
        return 'Natural Person';
      case TargetEntity.ORGANISATION:
        return 'Organisation';
      default:
        return target ? target.replace(/_/g, ' ') : 'Unassigned';
    }
  }

  kycLabel(status: KycComplianceStatus | null | undefined): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'KYC Current';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      case KycComplianceStatus.INCOMPLETE:
        return 'Incomplete';
      case KycComplianceStatus.ABSENT:
        return 'Not Filed';
      default:
        return 'In Review';
    }
  }

  kycClass(status: KycComplianceStatus | null | undefined): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'current';
      case KycComplianceStatus.EXPIRED:
      case KycComplianceStatus.ABSENT:
        return 'flagged';
      case KycComplianceStatus.INCOMPLETE:
      default:
        return 'review';
    }
  }

  documentName(row: ClientRequestDTO): string {
    return row.fileName || 'No document attached';
  }

  documentType(row: ClientRequestDTO): string {
    return row.documentType || 'Awaiting submission';
  }

  organisationSubtitle(row: ClientRequestDTO): string {
    return row.organisationRegistrationNo || row.organisationId || 'Organisation context unavailable';
  }

  trackByRequest(index: number, row: ClientRequestDTO): string | number {
    return row.id || row.registration || index;
  }

  private search(pageNumber: number = 0, pageSize: number = 10): void {
    const criteria = new SearchObject<ClientRequestSearchCriteria>();
    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = this.buildCriteria();

    this.clientRequestApiStore.pagedSearch({ criteria });
  }

  private buildCriteria(): ClientRequestSearchCriteria {
    const values = this.filters();
    const criteria = new ClientRequestSearchCriteria();

    criteria.name = this.clean(values.name);
    criteria.emailAddress = this.clean(values.emailAddress);
    criteria.organisationId = this.clean(values.organisationId);
    criteria.target = values.target || null;
    criteria.targetId = this.clean(values.targetId);
    criteria.statuses = values.status ? [values.status] : null;

    return criteria;
  }

  private clean(value: string | null | undefined): string | null {
    const trimmed = value?.trim();
    return trimmed ? trimmed : null;
  }
}
