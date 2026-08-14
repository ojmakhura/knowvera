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
import { Router } from '@angular/router';

// Material Imports
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { DocumentVerificationStatus } from '@app/models/bw/co/knowvera/document/document-verification-status';
import { DocumentSearchCriteria } from '@app/models/bw/co/knowvera/document/document-search-criteria';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { SearchObject } from '@app/models/search-object';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { DocumentApiStore } from '@app/store/bw/co/knowvera/document/document-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/knowvera/document/type/document-type-api.store';
import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';
import { form, FormField } from '@angular/forms/signals';
import { TranslateModule } from '@ngx-translate/core';
import { DocumentListDTO } from '@app/models/bw/co/knowvera/document/document-list-dto';

export class SearchDocumentsVarsForm {
  fileName: string = '';
  documentType: string = '';
  target: TargetEntity | '' = '';
  targetId: string = '';
  verificationStatus: DocumentVerificationStatus | '' = '';
  documents: Array<DocumentListDTO> = [];
}

@Component({
  selector: 'app-documents',
  standalone: true,
  templateUrl: './documents.html',
  styleUrls: ['./documents.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatPaginatorModule,
    MatChipsModule,
    MatTooltipModule,
    MatProgressBarModule,
    FormsModule,
    FormField,
    TranslateModule,
    Loader
  ],
})
export class Documents implements OnInit {
  searchDocumentsVarsForm = new SearchDocumentsVarsForm();
  searchDocumentsSignal = signal(this.searchDocumentsVarsForm);
  searchDocumentsForm = form(this.searchDocumentsSignal, (path) => {});

  readonly documentApiStore = inject(DocumentApiStore);
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);
  private readonly documentApi = inject(DocumentApi);
  private readonly toaster = inject(ToastrService);
  protected readonly rows = signal<DocumentListDTO[]>([]);
  protected readonly dataSource = new MatTableDataSource<DocumentListDTO>([]);
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly router = inject(Router);
  protected readonly loading = linkedSignal(() => this.documentApiStore.loading());
  protected readonly loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  protected readonly messages = linkedSignal(() => this.documentApiStore.messages());
  protected readonly success = linkedSignal(() => this.documentApiStore.success());
  protected readonly error = linkedSignal(() => this.documentApiStore.error());
  protected readonly documentTypeOptions = linkedSignal<DocumentTypeDTO[]>(() =>
    this.documentTypeApiStore.dataList(),
  );

  protected readonly toastr = inject(ToastrService);
  protected readonly targetOptions = Object.values(TargetEntity);
  protected readonly statusOptions = Object.values(DocumentVerificationStatus);

  displayedColumns: string[] = ['fileName', 'documentType', 'target', 'analyticsStatus', 'status', 'actions'];

  constructor() {
    effect(() => {
      const page = this.documentApiStore.dataPage();

      if (!page) {
        return;
      }

      this.rows.set(page.content || []);
      this.dataSource.data = page.content || [];
      this.currentPage.set(page.page?.number || 0);
      this.pageSize.set(page.page?.size || 10);
      this.totalElements.set(page.page?.totalElements || 0);
      this.totalPages.set(page.page?.totalPages || 0);

      this.searchDocumentsSignal.update((state) => ({
        ...state,
        documents: page.content || [],
      }));
    });

    effect(() => {
      let error = this.error();

      if(error) {
        console.error('Error state changed:', error, this.messages());
        this.toastr.error(this.messages()[0] || 'An error occurred while fetching documents.');
      }
    });

    effect(() => {
      let success = this.success();
      
      if(success) {
        console.log('Success state changed:', success, this.messages());
        this.toastr.success(this.messages()[0] || 'Documents fetched successfully.');
      }
    });
  }

  ngOnInit(): void {
    this.documentTypeApiStore.getAll();
    this.doSearch();
  }

  // updateField(
  //   field: keyof SearchDocumentsVarsForm,
  //   value: string | TargetEntity | DocumentVerificationStatus,
  // ): void {
  //   this.searchDocumentsSignal.update((state) => ({
  //     ...state,
  //     [field]: value,
  //   }));
  // }

  resetSearch(): void {
    this.searchDocumentsSignal.set(new SearchDocumentsVarsForm());
    this.doSearch();
  }

  doSearch(pageNumber: number = 0, pageSize: number = this.pageSize()): void {
    const value = this.searchDocumentsSignal();
    const criteria = new SearchObject<DocumentSearchCriteria>();

    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = {
      fileName: value.fileName || null,
      documentType: value.documentType || null,
      target: value.target || null,
      targetId: value.targetId || null,
      verificationStatus: value.verificationStatus || null,
      documentTypeId: null,
    };

    this.documentApiStore.searchPaged({
      criteria,
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageSize.set(event.pageSize);
    this.doSearch(event.pageIndex, event.pageSize);
  }

  openDetails(id: string): void {
    this.router.navigate(['/', 'documents', 'details', id]);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'documents', 'edit', id]);
  }

  downloadDocument(row: DocumentDTO): void {
    const request = row.id
      ? this.documentApi.downloadFile(row.id)
      : row.url
        ? this.documentApi.downloadFileByUrl(row.url)
        : null;

    if (!request) {
      this.toaster.error('No downloadable file reference was found for this document.');
      return;
    }

    request.subscribe({
      next: (blob: Blob) => this.saveBlob(blob, this.downloadFileNameOf(row)),
      error: () => this.toaster.error('Failed to download document.'),
    });
  }

  targetLabelOf(row: DocumentDTO): string {
    return row.targetLabel || row.target || '—';
  }

  targetOptionLabel(target: TargetEntity): string {
    return target.replaceAll('_', ' ');
  }

  documentTypeLabelOf(row: DocumentDTO): string {
    return row.documentType || 'Unclassified document';
  }

  statusLabel(status: string | null | undefined): string {
    switch (status) {
      case DocumentVerificationStatus.VERIFIED:
        return 'Verified';
      case DocumentVerificationStatus.MANUAL_REVIEW:
        return 'Pending Review';
      case DocumentVerificationStatus.REJECTED:
        return 'Flagged';
      default:
        return 'Unverified';
    }
  }

  statusClass(status: string | null | undefined): string {
    switch (status) {
      case DocumentVerificationStatus.VERIFIED:
        return 'status-verified';
      case DocumentVerificationStatus.MANUAL_REVIEW:
        return 'status-pending';
      case DocumentVerificationStatus.REJECTED:
        return 'status-flagged';
      default:
        return 'status-unverified';
    }
  }

  documentIcon(row: DocumentDTO): string {
    if (row.verificationStatus === DocumentVerificationStatus.REJECTED) {
      return 'report';
    }

    if ((row.documentType || '').toLowerCase().includes('financial')) {
      return 'account_balance';
    }

    if ((row.documentType || '').toLowerCase().includes('address')) {
      return 'article';
    }

    return 'description';
  }

  showingLabel(): string {
    const total = this.totalElements();

    if (!total) {
      return 'Showing 0 records';
    }

    const start = this.currentPage() * this.pageSize() + 1;
    const end = Math.min(start + this.rows().length - 1, total);
    return `Showing ${start}-${end} of ${total} records`;
  }

  pageReport(): string {
    return `Page ${this.currentPage() + 1} of ${Math.max(this.totalPages(), 1)}`;
  }

  trackByDocument(_: number, row: DocumentListDTO): string {
    return row.id || row.fileName || `${row.documentType}-${row.targetId}`;
  }

  private downloadFileNameOf(row: DocumentListDTO): string {
    return row.fileName || 'document-download';
  }

  verifiedCount = computed(
    () =>
      this.rows().filter((r) => r.verificationStatus === DocumentVerificationStatus.VERIFIED)
        .length,
  );

  private saveBlob(blob: Blob, fileName: string): void {
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');

    anchor.href = url;
    anchor.download = fileName;
    anchor.click();
    window.URL.revokeObjectURL(url);
  }
}
