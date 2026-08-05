import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnInit,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { Loader } from '@app/@shared/loader/loader';
import { disabled, email, form, FormField, readonly, required } from '@angular/forms/signals';
import { KycRecordDTO } from '@app/models/bw/co/knowvera/kyc/kyc-record-dto';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { KycRecordApiStore } from '@app/store/bw/co/knowvera/kyc/kyc-record-api.store';
import { DeclarationDTO } from '@app/models/bw/co/knowvera/kyc/declaration-dto';
import { PepStatus } from '@app/models/bw/co/knowvera/individual/pep-status';
import { KycComplianceStatus } from '@app/models/bw/co/knowvera/kyc/kyc-compliance-status';
import { SourceOfFunds } from '@app/models/bw/co/knowvera/source-of-funds';
import { IndividualIdentityType } from '@app/models/bw/co/knowvera/individual/individual-identity-type';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { IndividualApiStore } from '@app/store/bw/co/knowvera/individual/individual-api.store';
import { SearchObject } from '@app/models/search-object';
import { OrganisationSearchCriteria } from '@app/models/bw/co/knowvera/organisation/organisation-search-criteria';
import { IndividualSearchCriteria } from '@app/models/bw/co/knowvera/individual/individual-search-criteria';
import { QuillEditorComponent, QuillModule } from 'ngx-quill';
import { OwnerDetails } from '@app/models/bw/co/knowvera/kyc/owner-details';
import { EmploymentRecordDTO } from '@app/models/bw/co/knowvera/individual/employment/employment-record-dto';
import { swalFire } from '@app/@shared/swal';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { VerificationSummaryEntry } from '@app/models/bw/co/knowvera/kyc/verification-summary-entry';
import { KycReportSectionDTO } from '@app/models/bw/co/knowvera/kyc/fields/kyc-report-section-dto';
import 'quill/dist/quill.snow.css';

type QueuedDocumentUpload = {
  file: File;
  documentType: DocumentTypeDTO | null;
};

export class EditRecordVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  ref: string | any = null;
  target: TargetEntity = TargetEntity.INDIVIDUAL;
  targetId: string | any = null;
  ownerDetails: OwnerDetails = new OwnerDetails();
  recordOwnerFilter: any = null;
  expiryDate: Date | any = null;
  uploadDate: Date | any = null;
  kycStatus: KycComplianceStatus = KycComplianceStatus.INCOMPLETE;
  declaration: DeclarationDTO | any = null;
  sourceOfFunds: SourceOfFunds[] | any = [];
  sourceOfFundsDetails: string | any = null;
  documents: DocumentDTO[] | any = [];
  files: File[] | any = [];
  documentsToUpload: QueuedDocumentUpload[] = [];
  dataVerificationSummaries: VerificationSummaryEntry[] = [];
  employmentRecord: EmploymentRecordDTO | any = new EmploymentRecordDTO();
  recordSummary: string | any = null;
  kycReportSections: KycReportSectionDTO[] | any = [];

  constructor() {
    this.declaration = new DeclarationDTO();
  }
}

@Component({
  selector: 'app-record-edit',
  templateUrl: './record-edit.html',
  styleUrls: ['./record-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatButtonToggleModule,
    TranslateModule,
    Loader,
    FormField,
    MatDatepickerModule,
    MatListModule,
    MatProgressBarModule,
    NgxMatSelectSearchModule,
    QuillEditorComponent,
    RouterLink
],
})
export class RecordEdit implements OnInit {

  editRecordVarsForm: EditRecordVarsForm = new EditRecordVarsForm();
  editRecordSignal = signal(this.editRecordVarsForm);
  editRecordSignalForm = form(this.editRecordSignal, (path) => {
    disabled(path.ref);
    disabled(path.uploadDate);
    disabled(path.target);
    required(path.ownerDetails.name, { message: 'record.name.required' });
    required(path.ownerDetails, { message: 'record.owner.required' });
    disabled(path.ownerDetails.identityNo);
    disabled(path.ownerDetails.identityType);
    email(path.ownerDetails.emailAddress, { message: 'email.invalid' });
    required(path.ownerDetails.emailAddress, { message: 'email.required' });
    required(path.kycStatus, { message: 'record.kycStatus.required' });
    required(path.declaration.pepStatus, { message: 'pep.status.required' });
  });

  protected route: ActivatedRoute = inject(ActivatedRoute);
  protected router: Router = inject(Router);
  toaster: ToastrService = inject(ToastrService);
  readonly kycRecordApiStore = inject(KycRecordApiStore);
  readonly settingsApiStore = inject(SettingsApiStore);
  readonly organisationApiStore = inject(OrganisationApiStore);
  readonly individualApiStore = inject(IndividualApiStore);
  readonly documentApi = inject(DocumentApi);

  readonly pepStatusOptions = Object.values(PepStatus);
  readonly kycStatusOptions = Object.values(KycComplianceStatus);
  readonly sourceOfFundsOptions = Object.values(SourceOfFunds);
  readonly identityTypeOptions = Object.values(IndividualIdentityType);
  readonly TargetEntity = TargetEntity;  // Export enum for template use

  loaderMessage = signal('');
  messages = linkedSignal(() => this.kycRecordApiStore.messages());
  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  private readonly saveRequested = signal(false);
  readonly allowedDocumentTypes = signal<DocumentTypeDTO[]>([]);
  readonly bulkDocumentType = signal<DocumentTypeDTO | null>(null);
  private readonly quillEditors = new Map<string, any>();

  ownerOptions = linkedSignal(() => {
    const record = this.editRecordSignal();
    const target = record.target;

    if (target === TargetEntity.ORGANISATION) {
      return (this.organisationApiStore.dataList() || []).map((org) => ({
        id: org.id,
        name: org.name,
        identityNo: org.registrationNo,
        emailAddress: org.contactEmailAddress,
        identityType: null,
        postalAddress: org.postalAddress,
        physicalAddress: org.physicalAddress,
      }));
    } else if (target === TargetEntity.INDIVIDUAL) {
      return (this.individualApiStore.dataList() || []).map((ind) => ({
        id: ind.id,
        name: ind.name,
        identityNo: ind.identityNo,
        emailAddress: ind.emailAddress,
        identityType: ind.identityType,
        postalAddress: ind.postalAddress,
        physicalAddress: ind.physicalAddress,
      }));
    }

    return [];
  });

  @Input() id: string | null = null;

  constructor() {
    effect(() => {
      const settings = this.settingsApiStore.data();
      const target = this.editRecordSignal().target;

      if (!settings) {
        return;
      }

      // Update allowed document types based on target entity
      if (target === TargetEntity.INDIVIDUAL) {
        this.allowedDocumentTypes.set(settings.indKycDocuments || []);
      } else if (target === TargetEntity.ORGANISATION) {
        this.allowedDocumentTypes.set(settings.orgKycDocuments || []);
      }
    });

    effect(() => {
      const record = this.kycRecordApiStore.data();

      if (!record || !record.id) {
        return;
      }

      this.editRecordSignal.update((form) => ({
        ...form,
        id: record.id,
        createdAt: record.createdAt,
        createdBy: record.createdBy,
        modifiedAt: record.modifiedAt,
        modifiedBy: record.modifiedBy,
        ref: record.ref,
        target: record.target || TargetEntity.INDIVIDUAL,
        targetId: record.targetId,
        ownerDetails: record.ownerDetails,
        expiryDate: record.expiryDate,
        uploadDate: record.uploadDate,
        kycStatus: record.kycStatus,
        declaration: record.declaration || new DeclarationDTO(),
        sourceOfFunds: record.sourceOfFunds || [],
        sourceOfFundsDetails: record.sourceOfFundsDetails,
        documents: record.documents || [],
      }));

      this.setQuillContent('pepDetails', record.declaration?.pepDetails ?? null);
      this.setQuillContent('sanctionsDetails', record.declaration?.sanctionsDetails ?? null);
      this.setQuillContent('sourceOfFundsDetails', record.sourceOfFundsDetails ?? null);

    });

    this.kycRecordApiStore.reset();
    this.settingsApiStore.getAll();

    effect(() => {
      if (!this.saveRequested() || this.loading()) {
        return;
      }

      if (this.kycRecordApiStore.error()) {
        this.toaster.error(this.messages()[0] || 'Failed to save record.');
        this.saveRequested.set(false);
        return;
      }

      const savedRecord = this.kycRecordApiStore.data();

      if (savedRecord?.id) {
        this.toaster.success(this.messages()[0] || 'Record saved successfully.');
        this.saveRequested.set(false);

        if (!this.id && !this.route.snapshot.queryParamMap.get('id')) {
          this.router.navigate(['/', 'records', 'edit', savedRecord.id]);
        }
      }
    });

    effect(() => {
      let owner = this.editRecordSignalForm.ownerDetails().value();

      this.editRecordSignal.update((form) => ({
        ...form,
        targetId: owner?.id || null
      }));
    });
  }

  ngOnInit(): void {
    if (this.id) {
      this.kycRecordApiStore.findById({ id: this.id });
    }

    const routeId = this.route.snapshot.queryParamMap.get('id');

    if (routeId) {
      this.kycRecordApiStore.findById({ id: routeId });
    }
  }

  saveRecord(): void {
    // if (this.editRecordSignalForm().invalid()) {
    //   this.toaster.error('Complete the required record fields before saving.');
    //   return;
    // }

    // if (this.hasDocumentsWithoutType()) {
    //   this.toaster.error('Select a document type for each file before saving.');
    //   return;
    // }

    let record: KycRecordDTO = {
      id: this.editRecordSignal().id,
      ref: this.editRecordSignal().ref,
      target: this.editRecordSignal().target,
      targetId: this.editRecordSignal().targetId,
      ownerDetails: this.editRecordSignal().ownerDetails,
      expiryDate: this.editRecordSignal().expiryDate,
      uploadDate: this.editRecordSignal().uploadDate,
      kycStatus: this.editRecordSignal().kycStatus,
      declaration: this.editRecordSignal().declaration,
      sourceOfFunds: this.editRecordSignal().sourceOfFunds,
      sourceOfFundsDetails: this.editRecordSignal().sourceOfFundsDetails,
      documents: this.editRecordSignal().documents,
      createdAt: this.editRecordSignal().createdAt,
      createdBy: this.editRecordSignal().createdBy,
      modifiedAt: this.editRecordSignal().modifiedAt,
      modifiedBy: this.editRecordSignal().modifiedBy,
      employmentRecord: this.editRecordSignal().employmentRecord,
      dataVerificationSummaries: this.editRecordSignal().dataVerificationSummaries,
      recordSummary: this.editRecordSignal().recordSummary,
      kycReportSections: this.editRecordSignal().kycReportSections,
    }

    console.log('Saving record with data:', this.editRecordSignal());

    // this.saveRequested.set(true);
    this.kycRecordApiStore.save({
      kycRecord: record,
    });
  }

  cancel(): void {
    this.router.navigate(['/', 'records']);
  }

  toggleSourceOfFunds(fund: SourceOfFunds): void {
    const current = this.editRecordSignal().sourceOfFunds || [];
    const idx = current.findIndex((f: SourceOfFunds) => f === fund);

    if (idx > -1) {
      current.splice(idx, 1);
    } else {
      current.push(fund);
    }

    this.editRecordSignal.update((form) => ({
      ...form,
      sourceOfFunds: [...current],
    }));
  }

  hasSourceOfFunds(fund: SourceOfFunds): boolean {
    return (this.editRecordSignal().sourceOfFunds || []).includes(fund);
  }

  pepStatusLabel(value: PepStatus): string {
    const labels: { [key in PepStatus]: string } = {
      [PepStatus.NOT_PEP]: 'Not PEP',
      [PepStatus.PEP_SELF]: 'PEP (Self)',
      [PepStatus.PEP_RELATIVE]: 'PEP (Relative)',
      [PepStatus.PEP_ASSOCIATE]: 'PEP (Associate)',
    };
    return labels[value] || value;
  }

  identityTypeLabel(value: IndividualIdentityType): string {
    const labels: { [key in IndividualIdentityType]: string } = {
      [IndividualIdentityType.OMANG]: 'Omang',
      [IndividualIdentityType.PASSPORT]: 'Passport',
      [IndividualIdentityType.RESIDENCE_PERMIT]: 'Residence Permit',
      [IndividualIdentityType.BIRTH_CERTIFICATE]: 'Birth Certificate',
    };
    return labels[value] || value;
  }

  kycStatusLabel(value: KycComplianceStatus): string {
    const labels: { [key in KycComplianceStatus]: string } = {
      [KycComplianceStatus.CURRENT]: 'Current',
      [KycComplianceStatus.EXPIRED]: 'Expired',
      [KycComplianceStatus.ABSENT]: 'Absent',
      [KycComplianceStatus.INCOMPLETE]: 'Incomplete',
      [KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED]: 'Rejected',
    };
    return labels[value] || value;
  }

  // sourceOfFundsLabel(value: SourceOfFunds): string {
  //   const labels: { [key in SourceOfFunds]: string } = {
  //     [SourceOfFunds.SALARY]: 'Salary',
  //     [SourceOfFunds.BUSINESS_INCOME]: 'Business Income',
  //     [SourceOfFunds.INVESTMENTS]: 'Investments',
  //     [SourceOfFunds.PENSIONS]: 'Pensions',
  //     [SourceOfFunds.GIFTS]: 'Gifts',
  //     [SourceOfFunds.REMITTANCE]: 'Remittance',
  //     [SourceOfFunds.OTHER]: 'Other',
  //   };
  //   return labels[value] || value;
  // }

  // targetLabel(value: TargetEntity): string {
  //   const labels: { [key in TargetEntity]: string } = {
  //     [TargetEntity.INDIVIDUAL]: 'Individual',
  //     [TargetEntity.ORGANISATION]: 'Organisation',
  //     [TargetEntity.BRANCH]: 'Branch',
  //     [TargetEntity.SUBSCRIPTION]: 'Subscription',
  //     [TargetEntity.INVOICE]: 'Invoice',
  //     [TargetEntity.QUOTATION]: 'Quotation',
  //     [TargetEntity.CLIENT_REQUEST]: 'Client Request',
  //     [TargetEntity.KYC_RECORD]: 'KYC Record',
  //     [TargetEntity.CONTACT]: 'Contact',
  //     [TargetEntity.SETTINGS]: 'Settings',
  //   };
  //   return labels[value] || value;
  // }

  setTarget(target: TargetEntity): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      target,
      ownerDetails: {
        id: null,
        name: null,
        identityNo: null,
        identityType: null,
        emailAddress: null,
        phoneNumbers: [],
        physicalAddress: null,
        postalAddress: null,
      },
    }));
  }

  onDocumentSelected(event: any): void {
    const files: FileList = event.target.files;
    if (files && files.length > 0) {
      const configuredBulkType = this.bulkDocumentType();
      const documentTypes = this.allowedDocumentTypes();
      const defaultDocumentType = configuredBulkType || (documentTypes.length === 1 ? documentTypes[0] : null);
      const newFiles = Array.from(files).filter((file) =>
        !this.editRecordSignal().documentsToUpload.some((entry) => entry.file.name === file.name)
      );

      let uploads = [...this.editRecordSignal().files, ...newFiles];
      let docs = newFiles.map((file) => {

        const doc: DocumentDTO = new DocumentDTO();
        doc.fileName = file.name;

        return doc;
      });

      this.editRecordSignal.update((form) => ({
        ...form,
        documentsToUpload: [
          ...form.documentsToUpload,
          ...newFiles.map((file) => ({
            file,
            documentType: defaultDocumentType,
          })),
        ],
      }));
    }
    // Reset file input
    if (event.target) {
      event.target.value = '';
    }
  }

  setBulkDocumentType(documentType: DocumentTypeDTO | null): void {
    this.bulkDocumentType.set(documentType);
  }

  applyDocumentTypeToAll(): void {
    const selectedType = this.bulkDocumentType();

    if (!selectedType) {
      return;
    }

    this.editRecordSignal.update((form) => ({
      ...form,
      documentsToUpload: form.documentsToUpload.map((entry) => ({
        ...entry,
        documentType: selectedType,
      })),
    }));
  }

  removeDocument(index: number): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      documentsToUpload: form.documentsToUpload.filter((_, i) => i !== index),
    }));
  }

  setUploadDocumentType(index: number, documentType: DocumentTypeDTO | null): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      documentsToUpload: form.documentsToUpload.map((entry, i) =>
        i === index
          ? {
            ...entry,
            documentType,
          }
          : entry
      ),
    }));
  }

  uploadDocuments(): void {
    if (this.hasDocumentsWithoutType()) {
      return;
    }
    const form = this.editRecordSignal();
    const documents: DocumentDTO[] = form.documentsToUpload.map((entry) => ({
      documentTypeId: entry.documentType!.id,
    } as DocumentDTO));
    const files: File[] = form.documentsToUpload.map((entry) => entry.file);

    console.log('Uploading documents for record ID:', form.id);
    console.log('Documents to upload:', documents);
    console.log('Files to upload:', files);
    this.kycRecordApiStore.updateRecordFiles({ id: form.id, documents, files });
  }

  deleteDocument(documentId: string): void {

    swalFire({
      title: 'Are you sure?',
      text: 'This will permanently delete the document.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.kycRecordApiStore.removeRecordFile({ id: this.editRecordSignal().id, documentId });
      }
    });
  }

  downloadDocument(document: DocumentDTO): void {
    const request = document.id
      ? this.documentApi.downloadFile(document.id)
      : document.url
        ? this.documentApi.downloadFileByUrl(document.url)
        : null;

    if (!request) {
      this.toaster.error('No downloadable file reference was found for this document.');
      return;
    }

    request.subscribe({
      next: (blob: Blob) => this.saveBlob(blob, document.fileName || 'document-download'),
      error: () => this.toaster.error('Failed to download document.'),
    });
  }

  // viewDocument(document: DocumentDTO): void {
  //   const value = document.url;

  //   if (value && /^https?:\/\//i.test(value)) {
  //     window.open(value, '_blank', 'noopener');
  //     return;
  //   }

  //   const request = document.id
  //     ? this.documentApi.downloadFile(document.id)
  //     : value
  //       ? this.documentApi.downloadFileByUrl(value)
  //       : null;

  //   if (!request) {
  //     this.toaster.error('No document preview source was found for this document.');
  //     return;
  //   }

  //   request.subscribe({
  //     next: (blob: Blob) => {
  //       const objectUrl = window.URL.createObjectURL(blob);
  //       window.open(objectUrl, '_blank', 'noopener');
  //       window.setTimeout(() => window.URL.revokeObjectURL(objectUrl), 10000);
  //     },
  //     error: () => this.toaster.error('Failed to load document preview.'),
  //   });
  // }

  // editUploadedDocument(document: DocumentDTO): void {
  //   if (!document.id) {
  //     this.toaster.error('This document cannot be edited because it has no ID.');
  //     return;
  //   }

  //   this.router.navigate(['/', 'documents', 'edit', document.id]);
  // }



  hasDocumentsWithoutType(): boolean {
    return this.editRecordSignal().documentsToUpload.some((entry) => !entry.documentType?.id);
  }

  getDocumentTypeName(documentTypeId: string): string {
    const docType = this.allowedDocumentTypes().find((dt) => dt.id === documentTypeId);
    return docType?.name || 'Unknown';
  }

  analyticsStatusLabel(status: string | null | undefined): string {
    if (!status) {
      return 'Unknown';
    }

    return status
      .toString()
      .toLowerCase()
      .replace(/_/g, ' ')
      .replace(/\b\w/g, (c) => c.toUpperCase());
  }

  private saveBlob(blob: Blob, fileName: string): void {
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');

    anchor.href = url;
    anchor.download = fileName;
    anchor.click();
    window.URL.revokeObjectURL(url);
  }

  onQuillEditorCreated(key: string, editor: any): void {
    this.quillEditors.set(key, editor);
    const content = this.getQuillFieldContent(key);
    if (content) {
      editor.clipboard.dangerouslyPasteHTML(content);
    }
  }

  onQuillContentChanged(key: string, event: any): void {
    if (event.source !== 'user') return;
    if (key === 'pepDetails' || key === 'sanctionsDetails') {
      this.updateDeclarationField(key as keyof DeclarationDTO, event.html);
    } else if (key === 'sourceOfFundsDetails') {
      this.editRecordSignal.update(form => ({ ...form, sourceOfFundsDetails: event.html }));
    }
  }

  private getQuillFieldContent(key: string): string | null {
    const s = this.editRecordSignal();
    if (key === 'pepDetails') return s.declaration?.pepDetails ?? null;
    if (key === 'sanctionsDetails') return s.declaration?.sanctionsDetails ?? null;
    if (key === 'sourceOfFundsDetails') return s.sourceOfFundsDetails ?? null;
    return null;
  }

  private setQuillContent(key: string, html: string | null): void {
    const editor = this.quillEditors.get(key);
    if (editor && html) {
      editor.clipboard.dangerouslyPasteHTML(html);
    }
  }

  updateDeclarationField(field: keyof DeclarationDTO, value: any): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      declaration: {
        ...form.declaration,
        [field]: value,
      },
    }));
  }

  documentTypeCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  organisationSearch(): void {}

  individualSearch(): void {}

  recordOwnerCompare(o1: any, o2: any): boolean {
    return o1 && o2 && o1.id === o2.id;
  }

  filterOwners(): void {

    const filterValue = this.editRecordSignal().recordOwnerFilter || '';
    const target = this.editRecordSignal().target;

    console.log('Filtering owners with value:', filterValue, 'for target:', target);

    if (target === TargetEntity.ORGANISATION) {
      let criteria = new SearchObject<OrganisationSearchCriteria>();
      criteria.criteria = {
        name: filterValue,
      };
      this.organisationApiStore.search({ criteria });

    } else if (target === TargetEntity.INDIVIDUAL) {

      let criteria = new SearchObject<IndividualSearchCriteria>(); // Replace 'any' with actual IndividualSearchCriteria if available

      criteria.criteria = {
        name: filterValue,
        identityNo: filterValue,
        emailAddress: filterValue,
      };
      this.individualApiStore.search({ criteria });
    }

  }
}
