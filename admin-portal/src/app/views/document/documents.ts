import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentVerificationStatus } from '@app/models/bw/co/centralkyc/document/document-verification-status';
import { DocumentSearchCriteria } from '@app/models/bw/co/centralkyc/document/document-search-criteria';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { SearchObject } from '@app/models/search-object';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';

export class SearchDocumentsVarsForm {
  fileName: string = '';
  documentType: string = '';
  target: string = '';
  verificationStatus: string = '';
  documents: Array<DocumentDTO> = [];
}

@Component({
  selector: 'app-documents',
  standalone: true,
  templateUrl: './documents.html',
  styleUrls: ['./documents.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatIconModule, RouterLink],
})
export class Documents implements OnInit, AfterViewInit, OnDestroy {
  searchDocumentsVarsForm = new SearchDocumentsVarsForm();
  searchDocumentsSignal = signal(this.searchDocumentsVarsForm);

  readonly documentApiStore = inject(DocumentApiStore);
  protected readonly rows = signal<DocumentDTO[]>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly targetOptions = Object.values(TargetEntity);
  protected readonly statusOptions = Object.values(DocumentVerificationStatus);

  constructor() {
    effect(() => {
      const page = this.documentApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);

      this.searchDocumentsSignal.update((state) => ({
        ...state,
        documents: page.content || [],
      }));
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  updateField(field: keyof SearchDocumentsVarsForm, value: string): void {
    this.searchDocumentsSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  resetSearch(): void {
    this.searchDocumentsSignal.set(new SearchDocumentsVarsForm());
    this.doSearch();
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

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {
    const value = this.searchDocumentsSignal();
    const criteria = new SearchObject<DocumentSearchCriteria>();

    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = {
      fileName: value.fileName || null,
      documentType: value.documentType || null,
      target: value.target || null,
      verificationStatus: value.verificationStatus || null,
      documentTypeId: null,
      targetId: null,
    };

    this.documentApiStore.searchPaged({
      criteria,
    });
  }

  openDetails(id: string): void {
    this.router.navigate(['/', 'documents', 'details', id]);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'documents', 'edit', id]);
  }

  targetLabelOf(row: DocumentDTO): string {
    return row.targetLabel || row.target || '—';
  }

  submittedByOf(row: DocumentDTO): string {
    return row.createdBy || row.modifiedBy || 'System';
  }

  statusClass(status: string | null | undefined): 'status-approved' | 'status-pending' | 'status-rejected' {
    switch (status) {
      case 'VERIFIED':
        return 'status-approved';
      case 'UNVERIFIED':
      case 'MANUAL_REVIEW':
        return 'status-pending';
      default:
        return 'status-rejected';
    }
  }
}
