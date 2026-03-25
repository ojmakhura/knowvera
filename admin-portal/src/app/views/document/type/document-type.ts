import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  OnDestroy,
  OnInit,
  Signal,
  signal,
  ViewChild,
} from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { Router, RouterLink } from '@angular/router';
import { TableComponent } from '@app/components/table/table';
import { ActionTemplate } from '@app/models/action-template';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { ColumnModel } from '@app/models/column.model';
import { Page } from '@app/models/page.model';
import { DocumentTypeApiStore } from '@app/store/bw/co/centralkyc/document/type/document-type-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

export class SearchDocumentTypesVarsForm {
  criteria: string | any = null;
  documentTypes: Array<DocumentTypeDTO> = [];
}

@Component({
  selector: 'app-document-type',
  imports: [RouterLink, TranslateModule, FormField],
  templateUrl: './document-type.html',
  styleUrls: ['./document-type.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentTypeComponent implements OnInit, AfterViewInit, OnDestroy {
  
  searchDocumentTypesVarsForm: SearchDocumentTypesVarsForm = new SearchDocumentTypesVarsForm();
  searchDocumentTypesSignal = signal(this.searchDocumentTypesVarsForm);
  searchDocumentTypesSignalForm = form(this.searchDocumentTypesSignal, (path) => {});
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);
  @ViewChild('documentTypesTable') documentTypesTable!: TableComponent<Array<DocumentTypeDTO>>;
  documentTypesTableSignal: Signal<Array<DocumentTypeDTO> | Page<any> | undefined> =
    signal(undefined);
  documentTypesTablePaged: boolean = true;

  documentTypesTableColumns: ColumnModel[] = [
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  documentTypesTableColumnsActions: ActionTemplate[] = [
    {
      id: 'document-type-edit',
      label: 'edit',
      icon: 'edit',
      tooltip: 'edit',
    },
  ];

  showDocumentTypesActions = true;

  loaderMessage = signal('');
  messages: Signal<any> = signal({});
  success = signal(false);
  loading = signal(false);
  error = signal(false);
  selected: any = null;
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
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  documentTypesTableLoadEmitter(event: any): void {}
  // Should be overriden to handle the actions
  doSearchDocumentTypesEdit(form: any): any {}

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

  documentTypesTableActionClicked(event: any) {
    switch (event.action) {

      case 'search-document-types-edit':

      this.router.navigate(['document', 'type', 'edit'], {queryParams: {
        id: event.row.id
      }})
        break
    }
  }
}
