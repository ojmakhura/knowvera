import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  linkedSignal,
  OnInit,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { IndividualIdentityType } from '@app/models/bw/co/knowvera/individual/individual-identity-type';
import { KycComplianceStatus } from '@app/models/bw/co/knowvera/kyc/kyc-compliance-status';
import { KycRecordDTO } from '@app/models/bw/co/knowvera/kyc/kyc-record-dto';
import { KycRecordSearchCriteria } from '@app/models/bw/co/knowvera/kyc/kyc-record-search-criteria';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { SearchObject } from '@app/models/search-object';
import { KycRecordApiStore } from '@app/store/bw/co/knowvera/kyc/kyc-record-api.store';
import { swalFire } from '@app/@shared/swal';

export class SearchRecordsVarsForm {
  firstName = '';
  middleName = '';
  surname = '';
  emailAddress = '';
  identityNo = '';
  identityType: IndividualIdentityType | '' = '';
  expiryFrom = null;
  expiryTo = null;
  uploadedFrom = null;
  uploadedTo = null;
  statuses: KycComplianceStatus[] = [];
  target: TargetEntity | any = null;
}

@Component({
  selector: 'app-records',
  standalone: true,
  templateUrl: './records.html',
  styleUrls: ['./records.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTooltipModule,
    MatIconModule,
    RouterLink
],
})
export class Records implements OnInit {
  private readonly kycRecordApiStore = inject(KycRecordApiStore);
  private readonly router = inject(Router);
  private readonly toaster = inject(ToastrService);

  readonly searchRecordsSignal = signal(new SearchRecordsVarsForm());
  readonly dataSource = new MatTableDataSource<KycRecordDTO>([]);
  readonly rows = signal<KycRecordDTO[]>([]);
  readonly currentPage = signal(0);
  readonly pageSize = signal(10);
  readonly totalElements = signal(0);
  readonly pendingDeleteId = signal<string | null>(null);

  readonly loading = linkedSignal(() => this.kycRecordApiStore.loading());
  readonly loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());

  readonly displayedColumns: string[] = [
    'ref',
    'name',
    'identityNo',
    'kycStatus',
    'expiryDate',
    'actions',
  ];

  readonly currentCount = computed(() => this.rows().filter(r => r.kycStatus === KycComplianceStatus.CURRENT).length);
  readonly flaggedCount = computed(() => this.rows().filter(r => r.kycStatus === KycComplianceStatus.EXPIRED || r.kycStatus === KycComplianceStatus.ABSENT).length);

  readonly identityTypeOptions = [
    { label: 'All Types', value: '' },
    { label: 'Omang', value: IndividualIdentityType.OMANG },
    { label: 'Passport', value: IndividualIdentityType.PASSPORT },
    { label: 'Residence Permit', value: IndividualIdentityType.RESIDENCE_PERMIT },
    { label: 'Birth Certificate', value: IndividualIdentityType.BIRTH_CERTIFICATE },
  ];

  readonly statusOptions = [
    { label: 'All Statuses', value: '' },
    { label: 'Current', value: KycComplianceStatus.CURRENT },
    { label: 'Expired', value: KycComplianceStatus.EXPIRED },
    { label: 'Absent', value: KycComplianceStatus.ABSENT },
    { label: 'Incomplete', value: KycComplianceStatus.INCOMPLETE },
  ];

  constructor() {
    effect(() => {
      const page = this.kycRecordApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.dataSource.data = page.content || [];
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
    });

    effect(() => {
      const pendingDeleteId = this.pendingDeleteId();

      if (!pendingDeleteId || this.kycRecordApiStore.loading()) {
        return;
      }

      if (this.kycRecordApiStore.error()) {
        this.pendingDeleteId.set(null);
        this.toaster.error(this.kycRecordApiStore.messages()?.[0] || 'Failed to delete record.');
        return;
      }

      if (this.kycRecordApiStore.success()) {
        this.pendingDeleteId.set(null);
        this.toaster.success('Record deleted successfully.');
        this.doSearch(this.currentPage(), this.pageSize());
      }
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  handlePageEvent(event: PageEvent): void {
    this.doSearch(event.pageIndex, event.pageSize);
  }

  updateField(field: keyof SearchRecordsVarsForm, value: string | IndividualIdentityType | ''): void {
    this.searchRecordsSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  toggleStatus(status: KycComplianceStatus): void {
    this.searchRecordsSignal.update((state) => {
      const statuses = new Set(state.statuses);

      if (statuses.has(status)) {
        statuses.delete(status);
      } else {
        statuses.add(status);
      }

      return {
        ...state,
        statuses: Array.from(statuses),
      };
    });
  }

  hasStatus(status: KycComplianceStatus): boolean {
    return this.searchRecordsSignal().statuses.includes(status);
  }

  resetSearch(): void {
    this.searchRecordsSignal.set(new SearchRecordsVarsForm());
    this.doSearch();
  }

  updateStatusFilter(value: KycComplianceStatus | ''): void {
    this.searchRecordsSignal.update((state) => ({
      ...state,
      statuses: value ? [value] : [],
    }));
  }

  initialsOf(name: string | null | undefined): string {
    if (!name) return '?';
    return name
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((part) => part[0].toUpperCase())
      .join('');
  }

  avatarTone(index: number): 'tone-secondary' | 'tone-tertiary' | 'tone-primary' {
    const tones: Array<'tone-secondary' | 'tone-tertiary' | 'tone-primary'> = [
      'tone-secondary',
      'tone-primary',
      'tone-tertiary',
    ];
    return tones[index % tones.length];
  }

  doSearch(pageNumber: number = 0, size: number = this.pageSize()): void {
    const value = this.searchRecordsSignal();
    const criteria = new SearchObject<KycRecordSearchCriteria>();

    const nameParts = [value.firstName, value.middleName, value.surname]
      .map((part) => part.trim())
      .filter(Boolean);

    criteria.pageNumber = pageNumber;
    criteria.pageSize = size;
    criteria.criteria = {
      name: nameParts.length ? nameParts.join(' ') : null,
      registration: value.identityNo?.trim() || null,
      target: value.target,
      targetIds: [],
      statuses: value.statuses.length ? value.statuses : null,
    };

    this.kycRecordApiStore.pagedSearch({ criteria });
  }

  openCreate(): void {
    this.router.navigate(['/', 'records', 'edit']);
  }

  openDetails(row: KycRecordDTO): void {
    if (!row?.targetId) {
      return;
    }

    if (row.target === TargetEntity.ORGANISATION) {
      this.router.navigate(['/', 'organisation', 'details', row.targetId]);
      return;
    }

    this.router.navigate(['/', 'individuals', 'details', row.targetId]);
  }

  deleteRecord(row: KycRecordDTO): void {
    if (!row?.id) {
      this.toaster.warning('Cannot delete a record without an id.');
      return;
    }

    swalFire({
      title: 'Delete Record',
      text: `Are you sure you want to delete "${row.ref || row.ownerDetails?.name || 'this record'}"? This cannot be undone.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      reverseButtons: true,
    }).then((result) => {
      if (!result.isConfirmed) {
        return;
      }

      this.pendingDeleteId.set(row.id);
      this.kycRecordApiStore.remove({ id: row.id });
    });
  }

  exportCsv(): void {
    // Placeholder interaction until export endpoint/contract is available.
    const records = this.rows();

    if (!records.length) {
      return;
    }

    const csvRows = [
      ['Reference', 'Name', 'Identity No', 'Identity Type', 'Status', 'Expiry Date'].join(','),
      ...records.map((row) => [
        this.escapeCsv(row.ref),
        this.escapeCsv(row.ownerDetails.name),
        this.escapeCsv(row.ownerDetails.identityNo),
        this.escapeCsv(this.identityTypeLabel(row.ownerDetails.identityType)),
        this.escapeCsv(this.statusLabel(row.kycStatus)),
        this.escapeCsv(this.formatDate(row.expiryDate)),
      ].join(',')),
    ];

    const blob = new Blob([csvRows.join('\n')], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', 'kyc-records.csv');
    link.click();
    URL.revokeObjectURL(url);
  }

  statusLabel(status: KycComplianceStatus | null | undefined): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'Current';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      case KycComplianceStatus.ABSENT:
        return 'Absent';
      case KycComplianceStatus.INCOMPLETE:
        return 'Incomplete';
      default:
        return 'Unknown';
    }
  }

  statusClass(status: KycComplianceStatus | null | undefined): string {
    switch (status) {
      case KycComplianceStatus.CURRENT:
        return 'status-current';
      case KycComplianceStatus.EXPIRED:
        return 'status-expired';
      case KycComplianceStatus.ABSENT:
        return 'status-absent';
      case KycComplianceStatus.INCOMPLETE:
        return 'status-incomplete';
      default:
        return 'status-absent';
    }
  }

  identityTypeLabel(type: IndividualIdentityType | null | undefined): string {
    switch (type) {
      case IndividualIdentityType.OMANG:
        return 'Omang';
      case IndividualIdentityType.PASSPORT:
        return 'Passport';
      case IndividualIdentityType.RESIDENCE_PERMIT:
        return 'Residence Permit';
      case IndividualIdentityType.BIRTH_CERTIFICATE:
        return 'Birth Certificate';
      default:
        return 'Unknown';
    }
  }

  formatDate(value: Date | string | null | undefined): string {
    if (!value) {
      return 'N/A';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return String(value);
    }

    return date.toLocaleDateString(undefined, {
      month: 'short',
      day: '2-digit',
      year: 'numeric',
    });
  }

  showingSummary(): string {
    const total = this.totalElements();
    const loaded = this.rows().length;

    if (!total) {
      return 'No records found';
    }

    const start = this.currentPage() * this.pageSize() + 1;
    const end = Math.min(start + loaded - 1, total);

    return `Showing ${start}-${end} of ${total} records found`;
  }

  trackByRecord(_: number, row: KycRecordDTO): string {
    return row.id || row.ref || row.targetId || `${row.ownerDetails.identityNo}-${row.uploadDate}`;
  }

  // private applyClientSideFilters(rows: KycRecordDTO[]): KycRecordDTO[] {
  //   const form = this.searchRecordsSignal();

  //   return rows.filter((row) => {
  //     const lowerName = (row.ownerDetails.name || '').toLowerCase();
  //     const first = form.firstName.trim().toLowerCase();
  //     const middle = form.middleName.trim().toLowerCase();
  //     const surname = form.surname.trim().toLowerCase();
  //     const identityNo = form.identityNo.trim().toLowerCase();
  //     const emailAddress = form.emailAddress.trim().toLowerCase();

  //     const nameMatch =
  //       (!first || lowerName.includes(first)) &&
  //       (!middle || lowerName.includes(middle)) &&
  //       (!surname || lowerName.includes(surname));

  //     const identityNoMatch = !identityNo || (row.ownerDetails.identityNo || '').toLowerCase().includes(identityNo);
  //     const emailMatch = !emailAddress || (row.ownerDetails.emailAddress || '').toLowerCase().includes(emailAddress);
  //     const identityTypeMatch = !form.identityType || row.ownerDetails.identityType === form.identityType;
  //     const statusMatch = !form.statuses.length || form.statuses.includes(row.kycStatus);
  //     const expiryMatch = this.matchDateRange(row.expiryDate, form.expiryFrom, form.expiryTo);
  //     const uploadMatch = this.matchDateRange(row.uploadDate, form.uploadedFrom, form.uploadedTo);

  //     return (
  //       nameMatch &&
  //       identityNoMatch &&
  //       emailMatch &&
  //       identityTypeMatch &&
  //       statusMatch &&
  //       expiryMatch &&
  //       uploadMatch
  //     );
  //   });
  // }

  private matchDateRange(
    value: Date | string | null | undefined,
    from: string,
    to: string,
  ): boolean {
    if (!from && !to) {
      return true;
    }

    if (!value) {
      return false;
    }

    const valueDate = new Date(value);

    if (Number.isNaN(valueDate.getTime())) {
      return false;
    }

    const fromDate = from ? new Date(from) : null;
    const toDate = to ? new Date(to) : null;

    if (fromDate && valueDate < fromDate) {
      return false;
    }

    if (toDate) {
      // Compare as inclusive end-of-day.
      toDate.setHours(23, 59, 59, 999);
      if (valueDate > toDate) {
        return false;
      }
    }

    return true;
  }

  private escapeCsv(value: unknown): string {
    const escaped = String(value ?? '').replace(/"/g, '""');
    return `"${escaped}"`;
  }
}
