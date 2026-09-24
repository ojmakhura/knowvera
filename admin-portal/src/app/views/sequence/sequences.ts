import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Router, RouterLink } from '@angular/router';
import { SequenceGeneratorDTO } from '@app/models/bw/co/knowvera/sequence/sequence-generator-dto';
import { SequencePartDTO } from '@app/models/bw/co/knowvera/sequence/sequence-part-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { SequenceGeneratorApiStore } from '@app/store/bw/co/knowvera/sequence/sequence-generator-api.store';
// import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';
import { PAGE_SIZE_OPTIONS, pageWindow, showingRecordsLabel } from '@app/@shared/pagination';

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
    MatTooltipModule,
    RouterLink,
    CommonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatButtonModule,
  ],
})
export class Sequences implements OnInit, OnDestroy {
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

  readonly pageSizeOptions = PAGE_SIZE_OPTIONS;
  readonly pageNumbers = computed(() => pageWindow(this.currentPage(), this.totalPages()));

  changePageSize(size: string | number): void {
    this.pageSize.set(Number(size));
    this.recomputeRows(0);
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
    this.rows.set(filtered.slice(start, start + size));
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
    return showingRecordsLabel(this.currentPage(), this.pageSize(), this.rows().length, this.totalElements());
  }

  pageReport(): string {
    return `Page ${this.currentPage() + 1} of ${Math.max(this.totalPages(), 1)}`;
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
