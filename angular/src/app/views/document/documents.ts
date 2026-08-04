import { CommonModule } from '@angular/common';
import { Component, Signal, inject, linkedSignal, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { Router } from '@angular/router';
import { MaterialModule } from '@app/material.module';
import { ActionTemplate } from '@app/models/action-template';
import { ColumnModel } from '@app/models/column.model';
import { Page } from '@app/models/page.model';
import { SearchObject } from '@app/models/search-object';
import { TargetEntity } from '@app/models/bw/co/kyvera/target-entity';
import { DocumentDTO } from '@app/models/bw/co/kyvera/document/document-dto';
import { DocumentSearchCriteria } from '@app/models/bw/co/kyvera/document/document-search-criteria';
import { DocumentVerificationStatus } from '@app/models/bw/co/kyvera/document/document-verification-status';
import { DocumentApiStore } from '@app/store/bw/co/kyvera/document/document-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/kyvera/document/type/document-type-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/kyvera/organisation/organisation-api.store';
import { IndividualApiStore } from '@app/store/bw/co/kyvera/individual/individual-api.store';
import { BranchApiStore } from '@app/store/bw/co/kyvera/organisation/branch/branch-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/kyvera/subscription/kyc-subscription-api.store';
import { KycInvoiceApiStore } from '@app/store/bw/co/kyvera/invoice/kyc-invoice-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/kyvera/organisation/client/client-request-api.store';
import { KycRecordApiStore } from '@app/store/bw/co/kyvera/kyc/kyc-record-api.store';
import { TableComponent } from '@components/table/table';
import { Loader } from '@shared/loader/loader';
import { TranslateModule } from '@ngx-translate/core';
import { DocumentTypeDTO } from '@app/models/bw/co/kyvera/document/type/document-type-dto';
import { OrganisationListDTO } from '@app/models/bw/co/kyvera/organisation/organisation-list-dto';
import { IndividualListDTO } from '@app/models/bw/co/kyvera/individual/individual-list-dto';
import { BranchDTO } from '@app/models/bw/co/kyvera/organisation/branch/branch-dto';
import { KycSubscriptionDTO } from '@app/models/bw/co/kyvera/subscription/kyc-subscription-dto';
import { KycInvoiceDTO } from '@app/models/bw/co/kyvera/invoice/kyc-invoice-dto';
import { ClientRequestDTO } from '@app/models/bw/co/kyvera/organisation/client/client-request-dto';
import { KycRecordDTO } from '@app/models/bw/co/kyvera/kyc/kyc-record-dto';
import { OrganisationSearchCriteria } from '@app/models/bw/co/kyvera/organisation/organisation-search-criteria';
import { IndividualSearchCriteria } from '@app/models/bw/co/kyvera/individual/individual-search-criteria';
import { InvoiceSearchCriteria } from '@app/models/bw/co/kyvera/invoice/invoice-search-criteria';
import { ClientRequestSearchCriteria } from '@app/models/bw/co/kyvera/organisation/client/client-request-search-criteria';
import { KycRecordSearchCriteria } from '@app/models/bw/co/kyvera/kyc/kyc-record-search-criteria';
import { DocumentApi } from '@app/services/bw/co/kyvera/document/document-api';
import { take } from 'rxjs';

class DocumentSearchCriteriaForm {
  target: TargetEntity | any = null;

  documentType: DocumentTypeDTO | any = null;
  documentTypeFilter: string | any = null;

  targetObject: any = null;
  targetObjectFilter: string | any = null;

  fileName: string | any = null;

  verificationStatus: DocumentVerificationStatus | any = null;
}

@Component({
  selector: 'app-documents',
  templateUrl: './documents.html',
  styleUrls: ['./documents.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MaterialModule,
    TableComponent,
    Loader,
    FormField,
  ],
})
export class DocumentsComponent {
  readonly documentApiStore = inject(DocumentApiStore);
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);
  readonly organisationApiStore = inject(OrganisationApiStore);
  readonly individualApiStore = inject(IndividualApiStore);
  readonly branchApiStore = inject(BranchApiStore);
  readonly kycSubscriptionApiStore = inject(KycSubscriptionApiStore);
  readonly kycInvoiceApiStore = inject(KycInvoiceApiStore);
  readonly clientRequestApiStore = inject(ClientRequestApiStore);
  readonly kycRecordApiStore = inject(KycRecordApiStore);
  readonly documentApi = inject(DocumentApi);
  readonly router = inject(Router);

  documentsSearchSignal = signal(new DocumentSearchCriteriaForm());
  documentsSearchSignalForm = form(this.documentsSearchSignal, () => { });

  loading = linkedSignal(() => this.documentApiStore.loading());
  loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  documentsTableSignal: Signal<Array<DocumentDTO> | Page<any> | undefined> = linkedSignal(() =>
    this.documentApiStore.dataPage(),
  );

  documentsTablePaged = false;
  showDocumentsActions = true;
  documentsTableColumnsActions: ActionTemplate[] = [
    {
      id: 'document-download',
      label: 'download',
      icon: 'download',
      tooltip: 'download',
    },
    {
      id: 'document-details',
      label: 'details',
      icon: 'remove_red_eye',
      tooltip: 'details',
    },
    {
      id: 'document-edit',
      label: 'edit',
      icon: 'edit',
      tooltip: 'edit',
    },
  ];
  documentsTableColumns: ColumnModel[] = [
    new ColumnModel('target', 'target', false),
    new ColumnModel('documentType', 'document.type', false),
    new ColumnModel('targetId', 'target.id', false),
    new ColumnModel('fileName', 'file.name', false),
    new ColumnModel('verificationStatus', 'verification.status', false),
  ];

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = Object.keys(this.TargetEntityT);

  DocumentVerificationStatusT: any = DocumentVerificationStatus;
  DocumentVerificationStatusOptions = Object.keys(this.DocumentVerificationStatusT);

  documentTypeOptions = linkedSignal<DocumentTypeDTO[]>(() => this.documentTypeApiStore.dataList());
  organisationOptions = linkedSignal<OrganisationListDTO[]>(() => this.organisationApiStore.dataList());
  individualOptions = linkedSignal<IndividualListDTO[]>(() => this.individualApiStore.dataList());
  branchOptions = linkedSignal<BranchDTO[]>(() => this.branchApiStore.dataList());
  subscriptionOptions = linkedSignal<KycSubscriptionDTO[]>(() => this.kycSubscriptionApiStore.dataList());
  invoiceOptions = linkedSignal<KycInvoiceDTO[]>(() => this.kycInvoiceApiStore.dataList());
  clientRequestOptions = linkedSignal<ClientRequestDTO[]>(() => this.clientRequestApiStore.dataList());
  kycRecordOptions = linkedSignal<KycRecordDTO[]>(() => this.kycRecordApiStore.dataList());

  filteredDocumentTypeOptions = linkedSignal<DocumentTypeDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().documentTypeFilter);

    if (!term) {
      return this.documentTypeOptions();
    }

    return this.documentTypeOptions().filter((item) =>
      this.toSearchTerm(item?.name).includes(term),
    );
  });

  filteredOrganisationOptions = linkedSignal<OrganisationListDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.organisationOptions();
    }

    return this.organisationOptions().filter((item) => {
      const name = this.toSearchTerm(item?.name);
      const registration = this.toSearchTerm(item?.registrationNo);
      return name.includes(term) || registration.includes(term);
    });
  });

  filteredIndividualOptions = linkedSignal<IndividualListDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.individualOptions();
    }

    return this.individualOptions().filter((item) => {
      const name = this.toSearchTerm(item?.name);
      const identityNo = this.toSearchTerm(item?.identityNo);
      return name.includes(term) || identityNo.includes(term);
    });
  });

  filteredBranchOptions = linkedSignal<BranchDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.branchOptions();
    }

    return this.branchOptions().filter((item) => {
      const name = this.toSearchTerm(item?.name);
      const code = this.toSearchTerm(item?.code);
      return name.includes(term) || code.includes(term);
    });
  });

  filteredSubscriptionOptions = linkedSignal<KycSubscriptionDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.subscriptionOptions();
    }

    return this.subscriptionOptions().filter((item) => {
      const reference = this.toSearchTerm(item?.ref);
      const organisationName = this.toSearchTerm(item?.organisationName);
      return reference.includes(term) || organisationName.includes(term);
    });
  });

  filteredInvoiceOptions = linkedSignal<KycInvoiceDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.invoiceOptions();
    }

    return this.invoiceOptions().filter((item) => {
      const reference = this.toSearchTerm(item?.ref);
      const organisationName = this.toSearchTerm(item?.organisationName);
      return reference.includes(term) || organisationName.includes(term);
    });
  });

  filteredClientRequestOptions = linkedSignal<ClientRequestDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.clientRequestOptions();
    }

    return this.clientRequestOptions().filter((item) => {
      const name = this.toSearchTerm(item?.name);
      const registration = this.toSearchTerm(item?.registration);
      return name.includes(term) || registration.includes(term);
    });
  });

  filteredKycRecordOptions = linkedSignal<KycRecordDTO[]>(() => {
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!term) {
      return this.kycRecordOptions();
    }

    return this.kycRecordOptions().filter((item) => {
      const name = this.toSearchTerm(item?.name);
      const identityNo = this.toSearchTerm(item?.identityNo);
      return name.includes(term) || identityNo.includes(term);
    });
  });

  activeTargetObjectOptions = linkedSignal<any[]>(() => {
    const target = this.documentsSearchSignal().target as TargetEntity | null;

    if (target === TargetEntity.ORGANISATION) {
      return this.filteredOrganisationOptions();
    }

    if (target === TargetEntity.INDIVIDUAL) {
      return this.filteredIndividualOptions();
    }

    if (target === TargetEntity.BRANCH) {
      return this.filteredBranchOptions();
    }

    if (target === TargetEntity.SUBSCRIPTION) {
      return this.filteredSubscriptionOptions();
    }

    if (target === TargetEntity.INVOICE) {
      return this.filteredInvoiceOptions();
    }

    if (target === TargetEntity.CLIENT_REQUEST) {
      return this.filteredClientRequestOptions();
    }

    if (target === TargetEntity.KYC_RECORD) {
      return this.filteredKycRecordOptions();
    }

    return [];
  });

  constructor() {
    this.documentApiStore.reset();
    this.documentTypeApiStore.getAll();
  }

  targetSelectionChanged(target: TargetEntity | null): void {
    this.documentsSearchSignal.update((value) => ({
      ...value,
      target,
      targetObject: null,
      targetObjectFilter: null,
    }));

    this.loadTargetOptions(target, true);
  }

  searchTargetObjects(): void {
    const target = this.documentsSearchSignal().target as TargetEntity | null;
    const term = this.toSearchTerm(this.documentsSearchSignal().targetObjectFilter);

    if (!target) {
      return;
    }

    if (!term) {
      this.loadTargetOptions(target, false);
      return;
    }

    if (target === TargetEntity.ORGANISATION) {
      const criteria = new SearchObject<OrganisationSearchCriteria>();
      criteria.criteria = new OrganisationSearchCriteria();
      criteria.criteria.name = term;
      this.organisationApiStore.search({ criteria });
      return;
    }

    if (target === TargetEntity.INDIVIDUAL) {
      const criteria = new SearchObject<IndividualSearchCriteria>();
      criteria.criteria = new IndividualSearchCriteria();
      criteria.criteria.surname = term;
      criteria.criteria.firstName = term;
      criteria.criteria.identityNo = term;
      this.individualApiStore.search({ criteria });
      return;
    }

    if (target === TargetEntity.BRANCH) {
      this.branchApiStore.search({ criteria: term });
      return;
    }

    if (target === TargetEntity.SUBSCRIPTION) {
      this.kycSubscriptionApiStore.search({ criteria: term });
      return;
    }

    if (target === TargetEntity.INVOICE) {
      const criteria = new SearchObject<InvoiceSearchCriteria>();
      criteria.criteria = new InvoiceSearchCriteria();
      criteria.criteria.ref = term;
      criteria.criteria.organisationName = term;
      this.kycInvoiceApiStore.search({ criteria });
      return;
    }

    if (target === TargetEntity.CLIENT_REQUEST) {
      const criteria = new SearchObject<ClientRequestSearchCriteria>();
      criteria.criteria = new ClientRequestSearchCriteria();
      criteria.criteria.name = term;
      criteria.criteria.registration = term;
      this.clientRequestApiStore.search({ criteria });
      return;
    }

    if (target === TargetEntity.KYC_RECORD) {
      const criteria = new KycRecordSearchCriteria();
      criteria.name = term;
      criteria.registration = term;
      this.kycRecordApiStore.search({ criteria });
    }
  }

  hasTargetObjectOptions(): boolean {
    const target = this.documentsSearchSignal().target as TargetEntity | null;
    return target === TargetEntity.ORGANISATION
      || target === TargetEntity.INDIVIDUAL
      || target === TargetEntity.BRANCH
      || target === TargetEntity.SUBSCRIPTION
      || target === TargetEntity.INVOICE
      || target === TargetEntity.CLIENT_REQUEST
      || target === TargetEntity.KYC_RECORD;
  }

  targetObjectCompare(o1: any, o2: any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  targetObjectLabelKey(): string {
    const target = this.documentsSearchSignal().target as TargetEntity | null;

    if (target === TargetEntity.ORGANISATION) {
      return 'target.organisation';
    }

    if (target === TargetEntity.INDIVIDUAL) {
      return 'target.individual';
    }

    if (target) {
      return `target.entity.${target}`;
    }

    return 'target.id';
  }

  targetObjectSearchPlaceholderKey(): string {
    const target = this.documentsSearchSignal().target as TargetEntity | null;

    if (target === TargetEntity.ORGANISATION) {
      return 'search.organisation';
    }

    if (target === TargetEntity.INDIVIDUAL) {
      return 'search.individual';
    }

    return 'search';
  }

  targetObjectIcon(): string {
    const target = this.documentsSearchSignal().target as TargetEntity | null;

    if (target === TargetEntity.ORGANISATION) {
      return 'business';
    }

    if (target === TargetEntity.INDIVIDUAL) {
      return 'person';
    }

    if (target === TargetEntity.BRANCH) {
      return 'store';
    }

    if (target === TargetEntity.SUBSCRIPTION) {
      return 'event_note';
    }

    if (target === TargetEntity.INVOICE) {
      return 'receipt_long';
    }

    if (target === TargetEntity.CLIENT_REQUEST) {
      return 'assignment';
    }

    if (target === TargetEntity.KYC_RECORD) {
      return 'fact_check';
    }

    return 'badge';
  }

  formatTargetObject(item: any): string {
    const target = this.documentsSearchSignal().target as TargetEntity | null;

    if (target === TargetEntity.ORGANISATION) {
      return item?.registrationNo ? `${item?.name} - ${item?.registrationNo}` : `${item?.name || ''}`;
    }

    if (target === TargetEntity.INDIVIDUAL) {
      return item?.identityNo ? `${item?.name} - ${item?.identityNo}` : `${item?.name || ''}`;
    }

    if (target === TargetEntity.BRANCH) {
      return item?.code ? `${item?.name} - ${item?.code}` : `${item?.name || ''}`;
    }

    if (target === TargetEntity.SUBSCRIPTION || target === TargetEntity.INVOICE) {
      return item?.organisationName ? `${item?.ref} - ${item?.organisationName}` : `${item?.ref || ''}`;
    }

    if (target === TargetEntity.CLIENT_REQUEST) {
      return item?.registration ? `${item?.name} - ${item?.registration}` : `${item?.name || ''}`;
    }

    if (target === TargetEntity.KYC_RECORD) {
      return item?.identityNo ? `${item?.name} - ${item?.identityNo}` : `${item?.name || ''}`;
    }

    return item?.name || item?.ref || item?.id || '';
  }

  documentTypeCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  organisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  individualCompare(o1: IndividualListDTO | any, o2: IndividualListDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  branchCompare(o1: BranchDTO | any, o2: BranchDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  subscriptionCompare(o1: KycSubscriptionDTO | any, o2: KycSubscriptionDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  invoiceCompare(o1: KycInvoiceDTO | any, o2: KycInvoiceDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  clientRequestCompare(o1: ClientRequestDTO | any, o2: ClientRequestDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  kycRecordCompare(o1: KycRecordDTO | any, o2: KycRecordDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  documentsSearch(): void {
    const value = this.documentsSearchSignal();
    const criteria = new DocumentSearchCriteria();

    criteria.target = value.target || null;
    criteria.targetId = value.targetObject?.id || null;
    criteria.documentTypeId = value.documentType?.id || null;
    criteria.documentType = value.documentType?.name || null;
    criteria.fileName = value.fileName || null;
    criteria.verificationStatus = value.verificationStatus || null;

    const searchObject = new SearchObject<DocumentSearchCriteria>();
    searchObject.criteria = criteria;
    searchObject.pageNumber = 0;
    searchObject.pageSize = 10;

    this.documentApiStore.searchPaged({
      criteria: searchObject,
    });
  }

  documentsFormReset(): void {
    this.documentsSearchSignal.set(new DocumentSearchCriteriaForm());
    this.documentApiStore.reset();
  }

  documentsTableActionClicked(event: any): void {
    switch (event.action) {
      case 'document-download':
        this.downloadDocument(event.row);
        break;
      case 'document-details':
        this.router.navigate(['/documents/details'], {
          queryParams: { id: event.row?.id },
        });
        break;
      case 'document-edit':
        this.router.navigate(['/documents/edit', event.row?.id]);
        break;
    }
  }

  private toSearchTerm(value: string | null | undefined): string {
    return (value || '').toLowerCase().trim();
  }

  private downloadDocument(documentItem: DocumentDTO): void {
    if (!documentItem?.id) {
      return;
    }

    this.documentApi.downloadFile(documentItem.id).pipe(take(1)).subscribe({
      next: (blob: Blob) => {
        const objectUrl = URL.createObjectURL(blob);
        const anchor = window.document.createElement('a');
        anchor.href = objectUrl;
        anchor.download = documentItem.fileName || `document-${documentItem.id}`;
        anchor.click();
        URL.revokeObjectURL(objectUrl);
      },
    });
  }

  private loadTargetOptions(target: TargetEntity | null, loadOnlyWhenEmpty: boolean): void {
    if (target === TargetEntity.ORGANISATION && (!loadOnlyWhenEmpty || !this.organisationOptions().length)) {
      this.organisationApiStore.getAll();
    }

    if (target === TargetEntity.INDIVIDUAL && (!loadOnlyWhenEmpty || !this.individualOptions().length)) {
      this.individualApiStore.getAll();
    }

    if (target === TargetEntity.BRANCH && (!loadOnlyWhenEmpty || !this.branchOptions().length)) {
      this.branchApiStore.getAll();
    }

    if (target === TargetEntity.SUBSCRIPTION && (!loadOnlyWhenEmpty || !this.subscriptionOptions().length)) {
      this.kycSubscriptionApiStore.getAll();
    }

    if (target === TargetEntity.INVOICE && (!loadOnlyWhenEmpty || !this.invoiceOptions().length)) {
      this.kycInvoiceApiStore.getAll();
    }

    if (target === TargetEntity.CLIENT_REQUEST && (!loadOnlyWhenEmpty || !this.clientRequestOptions().length)) {
      this.clientRequestApiStore.getAll();
    }

    if (target === TargetEntity.KYC_RECORD && (!loadOnlyWhenEmpty || !this.kycRecordOptions().length)) {
      this.kycRecordApiStore.getAll();
    }
  }

}
