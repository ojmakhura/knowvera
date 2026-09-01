import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, linkedSignal, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { SequenceGeneratorDTO } from '@app/models/bw/co/knowvera/sequence/sequence-generator-dto';
import { SequencePartDTO } from '@app/models/bw/co/knowvera/sequence/sequence-part-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { SequenceGeneratorApiStore } from '@app/store/bw/co/knowvera/sequence/sequence-generator-api.store';
// import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';

export class SearchSequencesVarsForm {
  name: string = '';
  targetEntity: string = '';
  pattern: string = '';
  general: string = '';
}

@Component({
  selector: 'app-sequences',
  standalone: true,
  templateUrl: './sequences.html',
  styleUrls: ['./sequences.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatButtonModule,
    Loader
  ],
})
export class Sequences implements OnInit, AfterViewInit, OnDestroy {
  displayedColumns: string[] = ['name', 'targetEntity', 'patternPreview', 'lastModified', 'actions'];

  searchSequencesVarsForm = new SearchSequencesVarsForm();
  searchSequencesSignal = signal(this.searchSequencesVarsForm);

  readonly sequenceGeneratorApiStore = inject(SequenceGeneratorApiStore);
  loading = linkedSignal(() => this.sequenceGeneratorApiStore.loading());
  error = linkedSignal(() => this.sequenceGeneratorApiStore.error());
  messages = linkedSignal(() => this.sequenceGeneratorApiStore.messages());
  loadingMessage = linkedSignal(() => this.loading() ? 'Loading sequence generators...' : null);
  success = linkedSignal(() => this.messages()?.length ? this.messages()[0] : null);

  // toastr = inject(ToastrService);

  protected readonly allRows = signal<SequenceGeneratorDTO[]>([]);
  protected readonly rows = signal<SequenceGeneratorDTO[]>([]);
  protected readonly dataSource = new MatTableDataSource<SequenceGeneratorDTO>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly targetOptions = Object.values(TargetEntity);
  @ViewChild(MatPaginator) paginator?: MatPaginator;

  constructor() {
    effect(() => {
      const rows = this.sequenceGeneratorApiStore.dataList() || [];
      this.allRows.set(rows);
      this.recomputeRows(0);
    });

    effect(() => {
      const error = this.error();
      if (error) {
        // this.toastr.error(String(error), 'Error loading sequence generators');
      }
    });
    
    effect(() => {
      const message = this.success();
      if (message) {
        // this.toastr.success(String(message), 'Success');
      }
    });
  }

  ngOnInit(): void {
    this.sequenceGeneratorApiStore.getAll();
  }

  ngAfterViewInit(): void {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  ngOnDestroy(): void {}

  updateField(field: keyof SearchSequencesVarsForm, value: string): void {
    this.searchSequencesSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  resetSearch(): void {
    this.searchSequencesSignal.set(new SearchSequencesVarsForm());
    this.recomputeRows(0);
  }

  doSearch(pageNumber: number = 0): void {
    this.recomputeRows(pageNumber);
  }

  onPageChange(event: PageEvent): void {
    this.pageSize.set(event.pageSize);
    this.recomputeRows(event.pageIndex);
  }

  pageNumbers(): number[] {
    return Array.from({ length: this.totalPages() }, (_, index) => index + 1);
  }

  previousPage(): void {
    if (this.currentPage() <= 0) {
      return;
    }

    this.recomputeRows(this.currentPage() - 1);
  }

  nextPage(): void {
    if (this.currentPage() >= this.totalPages() - 1) {
      return;
    }

    this.recomputeRows(this.currentPage() + 1);
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.currentPage()) {
      return;
    }

    this.recomputeRows(page);
  }

  recomputeRows(pageNumber: number): void {
    const value = this.searchSequencesSignal();
    const filtered = this.allRows().filter((row) => {
      const pattern = this.patternOf(row).toLowerCase();
      const haystack = [
        row.name,
        row.targetEntity,
        pattern,
        ...(row.sequenceParts || []).map((part: SequencePartDTO) => `${part.name || ''} ${part.type || ''}`),
      ]
        .join(' ')
        .toLowerCase();

      return (!value.name || String(row.name || '').toLowerCase().includes(value.name.toLowerCase()))
        && (!value.targetEntity || String(row.targetEntity || '') === value.targetEntity)
        && (!value.pattern || pattern.includes(value.pattern.toLowerCase()))
        && (!value.general || haystack.includes(value.general.toLowerCase()));
    });

    const size = this.pageSize();
    const total = filtered.length;
    const totalPages = total ? Math.ceil(total / size) : 0;
    const safePage = totalPages === 0 ? 0 : Math.min(pageNumber, totalPages - 1);
    const start = safePage * size;

    this.totalElements.set(total);
    this.totalPages.set(totalPages);
    this.currentPage.set(safePage);
    this.rows.set(filtered);
    this.dataSource.data = filtered;
  }

  openCreate(): void {
    this.router.navigate(['/', 'sequence', 'edit']);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'sequence', 'edit', id]);
  }

  activeProtocols(): number {
    return new Set(
      this.allRows()
        .map((row) => String(row.targetEntity || '').trim())
        .filter(Boolean),
    ).size;
  }

  showingLabel(): string {
    if (!this.totalElements()) {
      return '0-0';
    }

    const start = this.currentPage() * this.pageSize() + 1;
    const end = Math.min(start + this.pageSize() - 1, this.totalElements());
    return `${start}-${end}`;
  }

  targetLabel(target: TargetEntity | string | null | undefined): string {
    return String(target || 'UNASSIGNED').replaceAll('_', ' ');
  }

  iconOf(row: SequenceGeneratorDTO): string {
    switch (row.targetEntity) {
      case 'DOCUMENT':
        return 'description';
      case 'ORGANISATION':
        return 'corporate_fare';
      case 'INDIVIDUAL':
        return 'person';
      case 'BRANCH':
        return 'account_tree';
      case 'SUBSCRIPTION':
        return 'workspace_premium';
      case 'INVOICE':
        return 'receipt_long';
      case 'QUOTATION':
        return 'request_quote';
      case 'CLIENT_REQUEST':
        return 'assignment_ind';
      case 'KYC_RECORD':
        return 'verified_user';
      case 'CONTACT':
        return 'contact_mail';
      default:
        return 'tag';
    }
  }

  subtitleOf(row: SequenceGeneratorDTO): string {
    const count = row.sequenceParts?.length || 0;
    return `${count} part${count === 1 ? '' : 's'} configured`;
  }

  patternOf(row: SequenceGeneratorDTO): string {
    const parts = [...(row.sequenceParts || [])]
      .sort((left: SequencePartDTO, right: SequencePartDTO) => (left.position || 0) - (right.position || 0))
      .map((part: SequencePartDTO) => part.initialValue || part.currentValue || part.name || String(part.type || '').replaceAll('_', '-'));

    return parts.length ? parts.join('') : '—';
  }

  lastModifiedOf(row: SequenceGeneratorDTO): string {
    return row.id ? `Record #${row.id}` : 'Pending ID';
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
}
