import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  linkedSignal,
  OnDestroy,
  OnInit,
  Signal,
  signal,
} from '@angular/core';
import { form } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { OrganisationListDTO } from '@app/models/bw/co/knowvera/organisation/organisation-list-dto';
import { OrganisationSearchCriteria } from '@app/models/bw/co/knowvera/organisation/organisation-search-criteria';
import { SearchObject } from '@app/models/search-object';
import { KycComplianceStatus } from '@app/models/bw/co/knowvera/kyc/kyc-compliance-status';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
// import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';

interface FilterOption {
  label: string;
  value: string;
}

export class SearchOrganisationsVarsForm {
  criteria: string | any = null;
  organisations: Array<OrganisationListDTO> = [];
}

@Component({
  selector: 'app-organisations',
  imports: [
    TranslateModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTooltipModule,
    Loader
  ],
  templateUrl: './organisations.html',
  styleUrls: ['./organisations.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Organisations implements OnInit, OnDestroy {

  searchOrganisationsVarsForm: SearchOrganisationsVarsForm = new SearchOrganisationsVarsForm();
  searchOrganisationsSignal = signal(this.searchOrganisationsVarsForm);
  searchOrganisationsSignalForm = form(this.searchOrganisationsSignal, (path) => {});

  // toaster: ToastrService = inject(ToastrService);
  readonly organisationApiStore = inject(OrganisationApiStore);
  loaderMessage: Signal<string> = signal('');
  messages = linkedSignal(() => this.organisationApiStore.messages());
  success = linkedSignal(() => this.organisationApiStore.success());
  loading = linkedSignal(() => this.organisationApiStore.loading());
  error = linkedSignal(() => this.organisationApiStore.error());

  organisations = signal<OrganisationListDTO[]>([]);
  dataSource = new MatTableDataSource<OrganisationListDTO>([]);
  currentPage = signal(0);
  pageSize = signal(10);
  totalElements = signal(0);
  totalPages = signal(0);
  router = inject(Router);

  displayedColumns: string[] = ['name', 'registrationNo', 'code', 'kycStatus', 'clientStatus', 'actions'];

  readonly kycStatusOptions: FilterOption[] = [
    { label: 'All KYC Statuses', value: '' },
    { label: 'Verified', value: 'kyc-current' },
    { label: 'Pending Review', value: 'kyc-incomplete' },
    { label: 'Flagged', value: 'kyc-failed' },
  ];

  readonly clientStatusOptions: FilterOption[] = [
    { label: 'All Client Statuses', value: '' },
    { label: 'Active Client', value: 'ACTIVE' },
    { label: 'Prospect', value: 'PROSPECT' },
    { label: 'Dormant', value: 'INACTIVE' },
  ];

  kycStatusFilter = signal<string>('');
  clientStatusFilter = signal<string>('');

  filteredOrganisations = computed(() => {
    const kycFilter = this.kycStatusFilter();
    const clientFilter = this.clientStatusFilter();

    return this.organisations().filter((organisation) => {
      if (kycFilter && this.kycStatusClass(organisation.kycStatus) !== kycFilter) {
        return false;
      }

      if (clientFilter && this.getClientStatus(organisation) !== clientFilter) {
        return false;
      }

      return true;
    });
  });

  updateKycStatusFilter(value: string): void {
    this.kycStatusFilter.set(value);
  }

  updateClientStatusFilter(value: string): void {
    this.clientStatusFilter.set(value);
  }

  handlePageEvent(event: PageEvent): void {
    this.doSearch(event.pageIndex, event.pageSize);
  }

  constructor() {
    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        // this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        // this.toaster.error(messages[0]);
      }
    });

    effect(() => {
      const page = this.organisationApiStore.dataPage();

      if (!page) {
        return;
      }

      this.organisations.set(page.content || []);
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);
    });

    effect(() => {
      this.dataSource.data = this.filteredOrganisations();
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  ngOnDestroy(): void {}

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {
    let value = this.searchOrganisationsSignal().criteria;

    let criteria = new SearchObject<OrganisationSearchCriteria>();
    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = value;

    this.organisationApiStore.pagedSearch({
      criteria: criteria,
    });
  }

  getClientStatus(organisation: OrganisationListDTO): string {
    if (!organisation.isClient) {
      return 'PROSPECT';
    }

    return organisation.status || 'ACTIVE';
  }

  clientStatusLabel(status: string): string {
    switch (status) {
      case 'PROSPECT':
        return 'Prospect';
      case 'INACTIVE':
        return 'Dormant';
      default:
        return 'Active Client';
    }
  }

  openEdit(id: string): void {
    this.router.navigate(['organisation', 'edit', id]);
  }

  openDetails(id: string): void {
    this.router.navigate(['organisation', id]);
  }

  createNewOrganisation(): void {
    this.router.navigate(['organisation', 'edit']);
  }

  updateCriteria(value: string): void {
    this.searchOrganisationsSignal.update((current) => ({
      ...current,
      criteria: value,
    }));
  }

  onSearchSubmit(): void {
    this.doSearch(0, this.pageSize());
  }

  organisationSubtitle(organisation: OrganisationListDTO): string {
    const category = organisation.isClient ? 'Client Entity' : 'Prospect Entity';
    return organisation.contactEmailAddress ? `${category} • ${organisation.contactEmailAddress}` : category;
  }

  organisationIcon(organisation: OrganisationListDTO): string {
    if (this.kycStatusClass(organisation.kycStatus) === 'kyc-failed') {
      return 'warning_amber';
    }

    if (organisation.isClient) {
      return 'account_balance';
    }

    return 'apartment';
  }

  kycStatusClass(status: string | null | undefined): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'kyc-current';
      case KycComplianceStatus.INCOMPLETE:
        return 'kyc-incomplete';
      case KycComplianceStatus.EXPIRED:
      case KycComplianceStatus.ABSENT:
      case KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED:
        return 'kyc-failed';
      default:
        return 'kyc-incomplete';
    }
  }

  kycStatusLabel(status: string | null | undefined): string {
    switch (this.kycStatusClass(status)) {
      case 'kyc-current':
        return 'Verified';
      case 'kyc-failed':
        return 'Flagged';
      default:
        return 'Pending Review';
    }
  }

  kycStatusIcon(status: string | null | undefined): string {
    switch (this.kycStatusClass(status)) {
      case 'kyc-current':
        return 'check_circle';
      case 'kyc-failed':
        return 'warning';
      default:
        return 'schedule';
    }
  }

  clientStatusClass(status: string | null | undefined): string {
    if (status === 'PROSPECT') {
      return 'prospect';
    }

    if (status === 'INACTIVE') {
      return 'dormant';
    }

    return 'active';
  }

  clientCount = computed(() => this.organisations().filter(o => o.isClient).length);

  resetSearch(): void {
    this.searchOrganisationsSignal.set(new SearchOrganisationsVarsForm());
    this.kycStatusFilter.set('');
    this.clientStatusFilter.set('');
    this.doSearch(0, this.pageSize());
  }

  exportOrganisations(): void {
    const rows = this.filteredOrganisations();

    if (!rows.length) {
      // this.toaster.info('There are no organisations to export.');
      return;
    }

    const header = ['Name', 'Registration No.', 'Code', 'KYC Status', 'Client Status'];
    const lines = rows.map((organisation) => [
      organisation.name || '',
      organisation.registrationNo || '',
      organisation.code || '',
      this.kycStatusLabel(organisation.kycStatus),
      this.clientStatusLabel(this.getClientStatus(organisation)),
    ].map((value) => `"${String(value).replace(/"/g, '""')}"`).join(','));

    const csv = [header.join(','), ...lines].join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `organisations-${new Date().toISOString().slice(0, 10)}.csv`;
    link.click();
    URL.revokeObjectURL(url);
  }
}
