import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { ChangeDetectionStrategy, Component, effect, inject, linkedSignal, OnInit, Signal, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { IndividualListDTO } from '@app/models/bw/co/centralkyc/individual/individual-list-dto';
import { IndividualSearchCriteria } from '@app/models/bw/co/centralkyc/individual/individual-search-criteria';
import { SearchObject } from '@app/models/search-object';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';

export class SearchIndividualsVarsForm {
  identityNo: string = '';
  firstName: string = '';
  middleName: string = '';
  surname: string = '';
  emailAddress: string = '';
  kycStatus: string = '';
  individuals: Array<IndividualListDTO> = [];
}

@Component({
  selector: 'app-individuals',
  templateUrl: './individuals.html',
  styleUrls: ['./individuals.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatIconModule, MatButtonModule, MatCardModule, MatTableModule, MatPaginatorModule, MatInputModule, MatSelectModule, MatTooltipModule, MatFormFieldModule],
})
export class Individuals implements OnInit {
  searchIndividualsVarsForm = new SearchIndividualsVarsForm();
  searchIndividualsSignal = signal(this.searchIndividualsVarsForm);

  readonly individualApiStore = inject(IndividualApiStore);
  protected readonly rows = signal<IndividualListDTO[]>([]);
  dataSource = new MatTableDataSource<IndividualListDTO>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  loaderMessage: Signal<string> = signal('');
  messages = linkedSignal(() => this.individualApiStore.messages());
  success = linkedSignal(() => this.individualApiStore.success());
  loading = linkedSignal(() => this.individualApiStore.loading());
  error = linkedSignal(() => this.individualApiStore.error());

  displayedColumns: string[] = ['name', 'identityNo', 'email', 'status', 'actions'];
  protected readonly kycStatusOptions = [
    { value: '', label: 'All Statuses' },
    { value: 'CURRENT', label: 'Verified' },
    { value: 'INCOMPLETE', label: 'Pending' },
    { value: 'EXPIRED', label: 'Expired' },
    { value: 'FLAGGED', label: 'Flagged' },
  ];

  constructor() {
    effect(() => {
      const page = this.individualApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.dataSource.data = page.content || [];
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);

      this.searchIndividualsSignal.update((state) => ({
        ...state,
        individuals: page.content || [],
      }));
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  ngAfterViewInit(): void {
    
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

  handlePageEvent(e: PageEvent) {
    this.doSearch(e.pageIndex, e.pageSize);
  }

  updateField(field: keyof SearchIndividualsVarsForm, value: string): void {
    this.searchIndividualsSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  resetSearch(): void {
    this.searchIndividualsSignal.set(new SearchIndividualsVarsForm());
    this.doSearch();
  }

  createNewIndividual(): void {
    this.router.navigate(['/individual', 'edit']);
  }

  statusLabel(status: string | null | undefined): string {
    switch (status) {
      case 'CURRENT':
        return 'Verified';
      case 'INCOMPLETE':
        return 'Pending';
      case 'EXPIRED':
        return 'Expired';
      default:
        return 'Flagged';
    }
  }

  createdLabel(index: number): string {
    const baseDate = new Date(2023, 9, 12);
    baseDate.setDate(baseDate.getDate() + index * 24);
    return baseDate.toLocaleDateString('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
    });
  }

  avatarTone(index: number): 'tone-secondary' | 'tone-tertiary' | 'tone-primary' {
    const tones: Array<'tone-secondary' | 'tone-tertiary' | 'tone-primary'> = [
      'tone-secondary',
      'tone-primary',
      'tone-tertiary',
    ];
    return tones[index % tones.length];
  }

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {
    const value = this.searchIndividualsSignal();
    const criteria = new SearchObject<IndividualSearchCriteria>();

    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = {
      identityNo: value.identityNo || null,
      firstName: value.firstName || null,
      middleName: value.middleName || null,
      surname: value.surname || null,
      emailAddress: value.emailAddress || null,
      kycStatus: value.kycStatus || null,
      identityType: null,
    };

    this.individualApiStore.pagedSearch({
      criteria,
    });
  }

  openDetails(id: string): void {
    this.router.navigate(['/individual', 'details', id]);
  }

  openEdit(id: string): void {
    this.router.navigate(['/individual', 'edit', id]);
  }

  initialsOf(name: string | null | undefined): string {
    if (!name) {
      return 'NA';
    }

    const parts = name.split(' ').filter(Boolean).slice(0, 2);
    return parts.map((part) => part[0]?.toUpperCase() || '').join('') || 'NA';
  }

  statusClass(status: string | null | undefined): 'status-verified' | 'status-pending' | 'status-flagged' {
    switch (status) {
      case 'CURRENT':
        return 'status-verified';
      case 'INCOMPLETE':
        return 'status-pending';
      default:
        return 'status-flagged';
    }
  }
}
