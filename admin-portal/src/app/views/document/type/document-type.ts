
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  linkedSignal,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { DocumentTypeApiStore } from '@app/store/bw/co/centralkyc/document/type/document-type-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

export class SearchDocumentTypesVarsForm {
  criteria: string | any = null;
  documentTypes: Array<DocumentTypeDTO> = [];
}

@Component({
  selector: 'app-document-type',
  imports: [TranslateModule, CommonModule, MatIconModule, MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatChipsModule,
    MatTooltipModule],
  templateUrl: './document-type.html',
  styleUrls: ['./document-type.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentTypeComponent implements OnInit, AfterViewInit, OnDestroy {
  displayedColumns: string[] = ['code', 'name', 'description', 'fields', 'prompts', 'actions'];

  searchDocumentTypesVarsForm: SearchDocumentTypesVarsForm = new SearchDocumentTypesVarsForm();
  searchDocumentTypesSignal = signal(this.searchDocumentTypesVarsForm);
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);

  readonly rows = linkedSignal(() => this.searchDocumentTypesSignal().documentTypes || []);
  loaderMessage = linkedSignal(() => this.documentTypeApiStore.loaderMessage());
  messages = linkedSignal(() => this.documentTypeApiStore.messages());
  success = linkedSignal(() => this.documentTypeApiStore.success());
  loading = linkedSignal(() => this.documentTypeApiStore.loading());
  error = linkedSignal(() => this.documentTypeApiStore.error());
  currentPage = signal(0);
  pageSize = signal(10);
  totalElements = signal(0);
  totalPages = signal(0);
  toaster: ToastrService = inject(ToastrService);

  protected router: Router = inject(Router);

  constructor() {
    effect(() => {
      const messages = this.messages();

      if (this.success() && !this.loading() && messages.length) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading() && messages.length) {
        this.toaster.error(messages[0]);
      }
    });

    effect(() => {
      const page = this.documentTypeApiStore.dataPage();

      if (page && Array.isArray(page.content)) {
        this.searchDocumentTypesSignal.update((state) => ({
          ...state,
          documentTypes: page.content,
        }));

        this.currentPage.set(page.page?.number || 0);
        this.pageSize.set(page.page?.size || 10);
        this.totalElements.set(page.page?.totalElements || 0);
        this.totalPages.set(page.page?.totalPages || 0);
      }
    });
  }

  ngOnInit(): void {
    this.doSearch();
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  clearCriteria(): void {
    this.searchDocumentTypesSignal.update((state) => ({
      ...state,
      criteria: '',
    }));

    this.doSearch();
  }

  onCriteriaInput(event: Event): void {
    const criteria = (event.target as HTMLInputElement)?.value ?? '';

    this.searchDocumentTypesSignal.update((state) => ({
      ...state,
      criteria,
    }));
  }

  pageNumbers(): number[] {
    return Array.from({ length: this.totalPages() }, (_, index) => index + 1);
  }

  previousPage(): void {
    if (this.currentPage() <= 0) {
      return;
    }

    this.goToPage(this.currentPage() - 1);
  }

  nextPage(): void {
    if (this.currentPage() >= this.totalPages() - 1) {
      return;
    }

    this.goToPage(this.currentPage() + 1);
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.currentPage()) {
      return;
    }

    this.doSearch(page, this.pageSize());
  }

  firstPage(): void {
    this.goToPage(0);
  }

  lastPage(): void {
    const lastIndex = this.totalPages() - 1;

    if (lastIndex >= 0) {
      this.goToPage(lastIndex);
    }
  }

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {
    const criteria = this.searchDocumentTypesSignal().criteria;

    this.documentTypeApiStore.pagedSearch({
      criteria: criteria ? criteria : '',
      pageNumber,
      pageSize,
    });
  }

  onPageChange(event: any): void {
    this.pageSize.set(event.pageSize);
    this.doSearch(event.pageIndex, event.pageSize);
  }

  showingCount(): number {
    return this.rows().length;
  }

  fieldCount(documentType: DocumentTypeDTO): number {
    return documentType.expectedFields?.length || 0;
  }

  promptCount(documentType: DocumentTypeDTO): number {
    return (documentType.validationPrompts?.length || 0) + (documentType.textExtractionPrompts?.length || 0);
  }

  openDetails(id: string): void {
    this.router.navigate(['/', 'document', 'type', 'edit', id]);
  }

  openCreate(): void {
    this.router.navigate(['/', 'document', 'type', 'edit']);
  }

  openEdit(id: string): void {
    this.router.navigate(['/', 'document', 'type', 'edit', id]);
  }

  trackByPage(_: number, page: number): number {
    return page;
  }
}
