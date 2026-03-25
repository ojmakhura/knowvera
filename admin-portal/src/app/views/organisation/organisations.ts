import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  linkedSignal,
  OnDestroy,
  OnInit,
  Signal,
  signal,
} from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { Loader } from '@app/@shared/loader/loader';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { SearchObject } from '@app/models/search-object';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

export class SearchOrganisationsVarsForm {
  criteria: string | any = null;
  organisations: Array<OrganisationListDTO> = [];
}

@Component({
  selector: 'app-organisations',
  imports: [RouterLink, FormField, TranslateModule, MatIconModule, Loader],
  templateUrl: './organisations.html',
  styleUrls: ['./organisations.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Organisations implements OnInit, AfterViewInit, OnDestroy {

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
  currentPage = signal(0);
  pageSize = signal(10);
  totalElements = signal(0);
  totalPages = signal(0);
  router = inject(Router);

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
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

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
    this.router.navigate(['organisation', 'details', id]);
  }
}
