import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { SequenceGeneratorDTO } from '@app/models/bw/co/centralkyc/sequence/sequence-generator-dto';
import { SequencePartDTO } from '@app/models/bw/co/centralkyc/sequence/sequence-part-dto';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { SequenceGeneratorApiStore } from '@app/store/bw/co/centralkyc/sequence/sequence-generator-api.store';

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
  host: {},
  imports: [CommonModule, MatIconModule],
})
export class Sequences implements OnInit, AfterViewInit, OnDestroy {
  searchSequencesVarsForm = new SearchSequencesVarsForm();
  searchSequencesSignal = signal(this.searchSequencesVarsForm);

  readonly sequenceGeneratorApiStore = inject(SequenceGeneratorApiStore);
  protected readonly allRows = signal<SequenceGeneratorDTO[]>([]);
  protected readonly rows = signal<SequenceGeneratorDTO[]>([]);
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
  }

  ngOnInit(): void {
    this.sequenceGeneratorApiStore.getAll();
  }

  ngAfterViewInit(): void {}

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
    this.rows.set(filtered.slice(start, start + size));
  }

  openCreate(): void {
    this.router.navigate(['/', 'sequence', 'edit']);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'sequence', 'edit', id]);
  }

  iconOf(row: SequenceGeneratorDTO): string {
    switch (row.targetEntity) {
      case 'DOCUMENT':
        return 'description';
      case 'ORGANISATION':
        return 'domain';
      case 'INDIVIDUAL':
        return 'person';
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
    return row.id ? `ID ${row.id}` : '—';
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
