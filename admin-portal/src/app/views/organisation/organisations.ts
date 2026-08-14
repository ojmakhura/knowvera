import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
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
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';

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
    MatInputModule,
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

  toaster: ToastrService = inject(ToastrService);
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

  displayedColumns: string[] = ['name', 'registrationNo', 'code', 'contactEmailAddress', 'kycStatus', 'clientStatus', 'actions'];

  handlePageEvent(event: PageEvent): void {
    this.doSearch(event.pageIndex, event.pageSize);
  }

  constructor() {
    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
      }
    });

    effect(() => {
      const page = this.organisationApiStore.dataPage();

      if (!page) {
        return;
      }

      this.organisations.set(page.content || []);
      this.dataSource.data = page.content || [];
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);
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
    if (organisation.kycStatus === 'FLAGGED' || organisation.status === 'SUSPENDED') {
      return 'warning_amber';
    }

    if (organisation.isClient) {
      return 'account_balance';
    }

    return 'apartment';
  }

  kycStatusClass(status: string | null | undefined): string {
    if (status === 'FLAGGED') {
      return 'flagged';
    }

    if (status?.includes('PENDING')) {
      return 'pending';
    }

    return 'verified';
  }

  clientStatusClass(status: string | null | undefined): string {
    if (status === 'SUSPENDED') {
      return 'suspended';
    }

    if (status === 'ONBOARDING' || status === 'PROSPECT') {
      return 'onboarding';
    }

    return 'active';
  }

  clientCount = computed(() => this.organisations().filter(o => o.isClient).length);

  resetSearch(): void {
    this.searchOrganisationsSignal.set(new SearchOrganisationsVarsForm());
    this.doSearch(0, this.pageSize());
  }
}
