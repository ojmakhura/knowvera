import { AfterViewInit, ChangeDetectionStrategy, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormField, applyEach, form, required } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { QuillModule, QuillModules } from 'ngx-quill';
import { FormsModule } from '@angular/forms';
import { TargetEntity } from '@app/models/bw/co/kyvera/target-entity';
import { OwnerDetails } from '@app/models/bw/co/kyvera/kyc/owner-details';
import { KycComplianceStatus } from '@app/models/bw/co/kyvera/kyc/kyc-compliance-status';
import { DeclarationDTO } from '@app/models/bw/co/kyvera/kyc/declaration-dto';
import { SourceOfFunds } from '@app/models/bw/co/kyvera/source-of-funds';
import { DocumentDTO } from '@app/models/bw/co/kyvera/document/document-dto';
import { VerificationSummaryEntry } from '@app/models/bw/co/kyvera/kyc/verification-summary-entry';
import { EmploymentRecordDTO } from '@app/models/bw/co/kyvera/individual/employment/employment-record-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/kyvera/document/type/document-type-dto';
import { IndividualIdentityType } from '@app/models/bw/co/kyvera/individual/individual-identity-type';
import { PepStatus } from '@app/models/bw/co/kyvera/individual/pep-status';
import { SettingsApiStore } from '@app/store/bw/co/kyvera/settings/settings-api.store';
import { KycRecordApiStore } from '@app/store/bw/co/kyvera/kyc/kyc-record-api.store';
import { DocumentApi } from '@app/services/bw/co/kyvera/document/document-api';
import { ToastrService } from 'ngx-toastr';
import { IndividualApiStore } from '@app/store/bw/co/kyvera/individual/individual-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/kyvera/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { KycRecordDTO } from '@app/models/bw/co/kyvera/kyc/kyc-record-dto';
import { KycRecordApi } from '@app/services/bw/co/kyvera/kyc/kyc-record-api';
import { KycReportSectionDTO } from '@app/models/bw/co/kyvera/kyc/fields/kyc-report-section-dto';
import { AppEnvStore } from '@app/store/app-env.state';

type QueuedDocumentUpload = {
  file: File;
  documentType: DocumentTypeDTO | null;
};

class EditKycRecordValue {
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

const SOURCE_OPTIONS = [
  SourceOfFunds.INVESTMENTS,
  SourceOfFunds.SALARY,
  SourceOfFunds.BUSINESS_INCOME,
  SourceOfFunds.PENSIONS,
  SourceOfFunds.GIFTS,
  SourceOfFunds.REMITTANCE,
  SourceOfFunds.OTHER,
] as const;

@Component({
  selector: 'app-edit-kyc-record',
  imports: [
    CommonModule,
    FormField,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    QuillModule,
    TranslateModule
  ],
  templateUrl: './edit-kyc-record.html',
  styleUrls: ['./edit-kyc-record.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditKycRecord implements OnInit, OnDestroy, AfterViewInit {

  appEnvStore = inject(AppEnvStore);
  settingsApiStore = inject(SettingsApiStore);
  kycRecordApi = inject(KycRecordApi);
  kycRecordApiStore = inject(KycRecordApiStore);
  documentApi = inject(DocumentApi);
  individualApiStore = inject(IndividualApiStore);
  organisationApiStore = inject(OrganisationApiStore);

  indKycDocuments = linkedSignal(() => this.settingsApiStore.data().indKycDocuments);
  orgKycDocuments = linkedSignal(() => this.settingsApiStore.data().orgKycDocuments);

  myRecords = linkedSignal(() => this.kycRecordApiStore.data());

  record = linkedSignal(() => this.kycRecordApiStore.data());

  selectedFile: File | null = null;
  selectedDocumentType: string = '';
  updatingDocument: DocumentDTO | null = null;

  toaster: ToastrService = inject(ToastrService);

  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  error = linkedSignal(() => this.kycRecordApiStore.error());
  messages = linkedSignal(() => this.kycRecordApiStore.messages());
  loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());
  success = linkedSignal(() => this.kycRecordApiStore.success());

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected readonly identityTypes = Object.values(IndividualIdentityType);
  protected readonly pepStatusOptions = Object.values(PepStatus);
  protected readonly sourceOptions = SOURCE_OPTIONS;
  protected readonly availableSources = computed(() =>
    this.sourceOptions.filter(
      (option) => !this.formModel().sourceOfFunds.includes(option),
    ),
  );
  protected readonly showSourcePicker = signal(false);
  protected readonly submitted = signal(false);
  protected readonly saveNotice = signal<string | null>(null);
  protected readonly saveNoticeTone = signal<'success' | 'error' | null>(null);
  protected readonly recordReference = signal(this.route.snapshot.queryParamMap.get('id'));
  // protected readonly kycStatus = computed(() => this.formModel().kycStatus || KycComplianceStatus.INCOMPLETE);
  protected readonly quillModules: QuillModules = {
    toolbar: [
      ['bold', 'italic', 'underline'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      ['link'],
      ['clean'],
    ],
  };


  readonly targetEntityEnum = TargetEntity;
  readonly kycComplianceStatusEnum = KycComplianceStatus;
  readonly identityTypeEnum = IndividualIdentityType;
  readonly sourceOfFundsEnum = SourceOfFunds;
  readonly pepStatusEnum = PepStatus;
  // readonly verificationStatusEnum = VerificationStatus;

  readonly targetEntityOptions = Object.values(TargetEntity);
  readonly kycStatusOptions = Object.values(KycComplianceStatus);
  readonly identityTypeOptions = Object.values(IndividualIdentityType);
  readonly sourceOfFundsOptions = Object.values(SourceOfFunds);
  // readonly verificationStatusOptions = Object.values(VerificationStatus);

  protected readonly formModel = linkedSignal<EditKycRecordValue>(() => {
    const record = this.record();
    console.log(record)
    return record ? {
      id: record.id,
      createdAt: record.createdAt,
      createdBy: record.createdBy,
      modifiedAt: record.modifiedAt,
      modifiedBy: record.modifiedBy,
      ref: record.ref,
      target: record.target || TargetEntity.INDIVIDUAL,
      targetId: record.targetId,
      ownerDetails: record.ownerDetails || {
        emailAddress: null,
        identityNo: null,
        identityType: null,
        name: null,
        phoneNumbers: [],
        physicalAddress: null,
        postalAddress: null,
        id: null,
      },
      expiryDate: record.expiryDate,
      uploadDate: record.uploadDate,
      kycStatus: record.kycStatus ? record.kycStatus : KycComplianceStatus.INCOMPLETE,
      declaration: record.declaration || {
        pepStatus: PepStatus.NOT_PEP,
        pepDetails: null,
        sanctionsMatch: false,
        sanctionsDetails: null,
      },
      sourceOfFunds: record.sourceOfFunds || [],
      sourceOfFundsDetails: record.sourceOfFundsDetails,
      documents: record.documents || [],
      dataVerificationSummaries: record.dataVerificationSummaries || [],
      employmentRecord: record.employmentRecord ? record.employmentRecord : new EmploymentRecordDTO(),
      documentsToUpload: [],
      files: [],
      recordSummary: record.recordSummary,
      recordOwnerFilter: '',
      kycReportSections: record.kycReportSections || [],
    } : new EditKycRecordValue();
  });

  availableDocumentTypes = computed(() => {
    const record = this.formModel();
    const allTypes = record?.target === 'INDIVIDUAL' ? this.indKycDocuments() : this.orgKycDocuments();
    console.log('All types', allTypes);
    console.log(record?.target)
    const uploadedTypes = record?.documents?.map((d: DocumentDTO) => d.documentTypeId) || [];
    return allTypes.filter((type: DocumentTypeDTO) => !uploadedTypes.includes(type.id));
  });

  private targetEntity: TargetEntity | null = null;

  protected readonly editForm = form(this.formModel, (path) => {
    required(path.ownerDetails.name, { message: 'Full legal name is required.' });
    required(path.ownerDetails.identityNo, { message: 'Identity number is required.' });
    required(path.ownerDetails.identityType, { message: 'Select an identity type.' });
    required(path.ownerDetails.emailAddress, { message: 'Email address is required.' });
    required(path.ownerDetails.physicalAddress, { message: 'Physical address is required.' });
    required(path.ownerDetails.postalAddress, { message: 'Postal address is required.' });
    applyEach(path.documentsToUpload, (document) => {
      required(document.documentType, { message: 'Select a document type.' });
    });
  });

  constructor() {
    // when store data updates (e.g., after findById), populate local form
    // effect(() => {
    //   const data = this.kycRecordApiStore.data();
    //   if (data && data.id) {
    //     this.formModel.set({
    //       ...data
    //     });

    //   }

    // });


    effect(() => {
      let individual = this.individualApiStore.data();
      console.log(individual)

      if (individual && this.selectedTarget === TargetEntity.INDIVIDUAL) {

        let name = individual.firstName;
        if (individual.middleName) {
          name += ' ' + individual.middleName;
        }
        if (individual.surname) {
          name += ' ' + individual.surname;
        }


        this.formModel.update((record) => ({
          ...record,
          ownerDetails: {
            name: name,
            emailAddress: individual.emailAddress,
            identityNo: individual.identityNo,
            identityType: individual.identityType,
            phoneNumbers: individual.phoneNumbers,
            physicalAddress: individual.physicalAddress,
            postalAddress: individual.postalAddress,
            id: individual.id,
          },
          declaration: {
            ...record.declaration,
            pepStatus: individual.pepStatus || PepStatus.NOT_PEP,

          },
          targetId: individual.id,
          target: TargetEntity.INDIVIDUAL,
        }));
      }

    });

    effect(() => {

      let org = this.organisationApiStore.data();

      if (org && this.selectedTarget === TargetEntity.ORGANISATION) {
        this.formModel.update((record) => ({
          ...record,
          ownerDetails: {
            name: org.name,
            emailAddress: org.contactEmailAddress,
            identityNo: org.registrationNo,
            physicalAddress: org.physicalAddress,
            postalAddress: org.postalAddress,
            id: org.id,
            identityType: null,
            phoneNumbers: org.phoneNumbers,
          },
          declaration: {
            ...record.declaration,
            pepStatus: PepStatus.NOT_PEP,
          },
          targetId: org.id,
          target: TargetEntity.ORGANISATION,
        }));
      }

    });

    effect(() => {
      let error = this.error();
      if (error) {
        // this.toastr.error(error, (this.messages() || []).join(', ') || 'Error');
      }
      // this.savingInProgress = false;
    });
  }

  get selectedTarget(): TargetEntity | null {
    return this.targetEntity;
  }

  ngOnInit(): void {
    this.individualApiStore.reset();
    this.organisationApiStore.reset();
    this.settingsApiStore.getAll();
    // this.kycRecordApiStore.findMyCurrentIndividualRecord();
    // this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    this.kycRecordApiStore.findMyRecords();

    // this.kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).subscribe({
    //   next: (record) => {
    //     console.log(record)
    //   }
    // })

    this.route.queryParams.subscribe((params: Params) => {
      const target = params['target'] as TargetEntity;
      const id = params['id'] as string | undefined;
      if (target) {
        this.targetEntity = target;
        // pre-fill target on new record
        this.formModel.update((record: EditKycRecordValue) => ({ ...record, target }));

        if (target === TargetEntity.INDIVIDUAL) {
          this.formModel.update((record: EditKycRecordValue) => ({
            ...record,
            target: TargetEntity.INDIVIDUAL
          }));
          this.individualApiStore.loadMe();
        } else if (target === TargetEntity.ORGANISATION) {
          this.formModel.update((record: EditKycRecordValue) => ({
            ...record,
            target: TargetEntity.ORGANISATION
          }));
          this.organisationApiStore.loadMyOrganisation();
        }

      }

      if (id) {
        // load existing record
        this.kycRecordApiStore.findById({ id });
      }
    });
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
  }

  protected addSource(source: string): void {
    this.formModel.update((value) => ({
      ...value,
      sourceOfFunds: [...value.sourceOfFunds, source],
    }));

    if (!this.availableSources().length) {
      this.showSourcePicker.set(false);
    }
  }

  protected removeSource(source: string): void {
    this.formModel.update((value) => ({
      ...value,
      sourceOfFunds: value.sourceOfFunds.filter((current: SourceOfFunds) => current !== source),
    }));
  }

  protected toggleSourcePicker(): void {
    if (!this.availableSources().length) {
      this.showSourcePicker.set(false);
      return;
    }

    this.showSourcePicker.update((open) => !open);
  }

  protected onDocumentSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.item(0);
    if (!file) {
      return;
    }

    this.formModel.update((value) => ({
      ...value,
      documentsToUpload: [
        {
          file,
          documentType: null,
        },
        ...value.documentsToUpload,
      ],
    }));

    input.value = '';
  }

  protected removeDocument(name: string): void {
    this.formModel.update((value) => ({
      ...value,
      documents: value.documents.filter((document: DocumentDTO) => document.fileName !== name),
    }));
  }

  protected removeQueuedDocument(name: string): void {
    this.formModel.update((value) => ({
      ...value,
      documentsToUpload: value.documentsToUpload.filter((document) => document.file.name !== name),
    }));
  }

  protected updateQueuedDocumentType(fileName: string, documentTypeId: string | null): void {
    const selectedType = this.availableDocumentTypes().find((type: DocumentTypeDTO) => type.id === documentTypeId) ?? null;

    this.formModel.update((value) => ({
      ...value,
      documentsToUpload: value.documentsToUpload.map((document) =>
        document.file.name === fileName
          ? {
            ...document,
            documentType: selectedType,
          }
          : document,
      ),
    }));
  }

  protected updatePepDetails(value: string): void {
    this.formModel.update((record) => ({
      ...record,
      declaration: {
        ...record.declaration,
        pepDetails: value,
      },
    }));
  }

  protected updateSanctionsDetails(value: string): void {
    this.formModel.update((record) => ({
      ...record,
      declaration: {
        ...record.declaration,
        sanctionsDetails: value,
      },
    }));
  }

  protected submit(): void {
    const formValue = this.formModel();

    let record: KycRecordDTO = {
      id: formValue.id,
      ref: formValue.ref,
      target: formValue.target,
      targetId: formValue.targetId,
      ownerDetails: formValue.ownerDetails,
      expiryDate: formValue.expiryDate,
      uploadDate: formValue.uploadDate,
      kycStatus: formValue.kycStatus,
      declaration: formValue.declaration,
      sourceOfFunds: formValue.sourceOfFunds,
      sourceOfFundsDetails: formValue.sourceOfFundsDetails,
      documents: formValue.documents,
      createdAt: formValue.createdAt,
      createdBy: formValue.createdBy,
      modifiedAt: formValue.modifiedAt,
      modifiedBy: formValue.modifiedBy,
      employmentRecord: formValue.employmentRecord,
      dataVerificationSummaries: formValue.dataVerificationSummaries,
      recordSummary: formValue.recordSummary,
      kycReportSections: formValue.kycReportSections,
    }

    let files: File[] = formValue.documentsToUpload.map((doc) => doc.file);
    let docs: DocumentDTO[] = formValue.documentsToUpload.map((doc) => ({
      target: TargetEntity.KYC_RECORD,
      targetId: formValue.id,
      documentTypeId: doc.documentType?.id || '',
      documentType: doc.documentType?.name || '',
      fileName: doc.file.name,
    } as DocumentDTO));

    record.documents = [...(record.documents || []), ...docs];
    console.log(files, docs)

    if (!record.id) {
      console.log('Creating new record with documents...', record, files);
      this.kycRecordApiStore.createNew({
        record: record,
        files: files
      });
    } else {

      this.loading.set(true);
      this.kycRecordApi.save(record).subscribe({
        next: (savedRecord) => {
          console.log('Record saved, now uploading documents...', savedRecord);
          if ((files || []).length > 0) {
            this.kycRecordApiStore.updateRecordFiles({
              id: record.id,
              documents: docs,
              files: files
            });
          } else {

            this.loading.set(false);
          }
        },
        error: (error) => {
          this.loading.set(false);
          console.error('Error saving record before document upload', error);
        }
      });
    }
  }

  protected navigateBack(): void {
    this.router.navigate(['/dashboard']);
  }

  documentTypeCompare(type1: DocumentTypeDTO, type2: DocumentTypeDTO): boolean {
    return type1.id === type2.id;
  }
}
