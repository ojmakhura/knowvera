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
  Signal,
  signal,
} from '@angular/core';
import { form } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
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
  imports: [RouterLink, TranslateModule, CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './document-type.html',
  styleUrls: ['./document-type.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentTypeComponent implements OnInit, AfterViewInit, OnDestroy {
  searchDocumentTypesVarsForm: SearchDocumentTypesVarsForm = new SearchDocumentTypesVarsForm();
  searchDocumentTypesSignal = signal(this.searchDocumentTypesVarsForm);
  searchDocumentTypesSignalForm = form(this.searchDocumentTypesSignal, (path) => {});
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);

  loaderMessage = signal('');
  messages: Signal<any> = signal({});
  success = signal(false);
  loading = signal(false);
  error = signal(false);
  selected: any = null;
  currentPage = signal(0);
  pageSize = signal(10);
  totalElements = linkedSignal(() => this.documentTypeApiStore.dataPage().page?.totalElements || 0);
  totalPages = linkedSignal(() => this.documentTypeApiStore.dataPage().page?.totalPages || 0);
  toaster: ToastrService = inject(ToastrService);

  protected router: Router = inject(Router);

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
      const page = this.documentTypeApiStore.dataPage();

      if (page && Array.isArray(page.content)) {
        this.searchDocumentTypesSignal.update((state) => ({
          ...state,
          documentTypes: page.content,
        }));

        this.currentPage.set(page.page.number || 0);
        this.totalElements.set(page.page.totalElements || 0);
        this.totalPages.set(page.page.totalPages || 0);
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

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {
    let criteria = this.searchDocumentTypesSignal().criteria;

    this.loading.set(true);
    this.loaderMessage.set(`Loading page ${pageNumber} document types.`);

    this.documentTypeApiStore.pagedSearch({
      criteria: criteria ? criteria : '',
      pageNumber,
      pageSize,
    });
  }

  openCreate(): void {
    this.router.navigate(['document', 'type', 'edit']);
  }

  openEdit(id: string): void {
    this.router.navigate(['document', 'type', 'edit'], {
      queryParams: {
        id,
      },
    });
  }
}
