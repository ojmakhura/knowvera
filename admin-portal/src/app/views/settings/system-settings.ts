import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, linkedSignal, signal, Inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialogModule, MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { forkJoin } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

import { Loader } from '@app/@shared/loader/loader';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { DocumentTypePurpose } from '@app/models/bw/co/centralkyc/settings/document-type-purpose';
import { SalaryRangeDTO } from '@app/models/bw/co/centralkyc/settings/salary-range-dto';
import { SettingsDTO } from '@app/models/bw/co/centralkyc/settings/settings-dto';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { DocumentTypeApi } from '@app/services/bw/co/centralkyc/document/type/document-type-api';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { form, required, FormField, applyEach } from '@angular/forms/signals';
import { DocumentVerificationStatus } from '@app/models/bw/co/centralkyc/document/document-verification-status';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { TranslateModule } from '@ngx-translate/core';
import { SettingsApi } from '@app/services/bw/co/centralkyc/settings/settings-api';
import { ExpectedFieldDTO } from '@app/models/bw/co/centralkyc/document/type/field/expected-field-dto';
import { ExpectedFieldApi } from '@app/services/bw/co/centralkyc/document/type/field/expected-field-api';
import { KycFieldGroupDTO } from '@app/models/bw/co/centralkyc/settings/kyc/kyc-field-group-dto';

export class EditSettingsVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  kycDuration: number | any = null;
  timeToAccountCreation: number | any = null;
  platformName: string | any = null;
  platformUrl: string | any = null;
  kycPortalLink: string | any = null;
  supportContact: string | any = null;
  organisationAdminRole: string | any = null;
  normalUserRole: string | any = null;
  individualDocuments: Array<DocumentTypeDTO> = [];
  selectedOrgDocument: DocumentTypeDTO | any = null;
  selectedOrgDocumentFilter: DocumentTypeDTO | any = null;
  organisationDocuments: Array<DocumentTypeDTO> = [];
  selectedKycOrgDocument: DocumentTypeDTO | any = null;
  selectedKycOrgDocumentFilter: DocumentTypeDTO | any = null;
  orgKycDocuments: Array<DocumentTypeDTO> = [];
  selectedIndDocument: DocumentTypeDTO | any = null;
  selectedIndDocumentFilter: DocumentTypeDTO | any = null;
  selectedKycIndDocument: DocumentTypeDTO | any = null;
  selectedKycIndDocumentFilter: DocumentTypeDTO | any = null;
  indKycDocuments: Array<DocumentTypeDTO> = [];
  selectedIndividualKycField: ExpectedFieldDTO | any = null;
  selectedOrganisationKycField: ExpectedFieldDTO | any = null;
  invoiceDocumentType: DocumentTypeDTO | any = null;
  invoiceDocumentTypeFilter: DocumentTypeDTO | any = null;
  invoiceTemplateType: DocumentTypeDTO | any = null;
  invoiceTemplateTypeFilter: DocumentTypeDTO | any = null;
  invoiceTemplate: DocumentDTO | any = null;
  quotationDocumentType: DocumentTypeDTO | any = null;
  quotationDocumentTypeFilter: DocumentTypeDTO | any = null;
  quotationTemplateType: DocumentTypeDTO | any = null;
  quotationTemplateTypeFilter: DocumentTypeDTO | any = null;
  quotationTemplate: DocumentDTO | any = null;
  clientRequestFileType: DocumentTypeDTO | any = null;
  clientRequestFileTypeFilter: DocumentTypeDTO | any = null;
  salaryRanges: Array<SalaryRangeDTO> = [];
  vat: number | any = null;
  documentDurationLimit: number | any = null;
  dataVerificationThreshold: number | any = null;
  maxDataVerificationFailureThreshold: number | any = null;
  organisationKycFieldGroups: Array<KycFieldGroupDTO> = [];
  individualKycFieldGroups: Array<KycFieldGroupDTO> = [];
}

@Component({
  selector: 'app-system-settings',
  standalone: true,
  templateUrl: './system-settings.html',
  styleUrls: ['./system-settings.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormsModule,
    Loader,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatSliderModule,
    MatTableModule,
    MatTabsModule,
    MatDialogModule,
    MatCheckboxModule,
    FormField,
    NgxMatSelectSearchModule,
    TranslateModule,
    MatTooltipModule
  ],
})
export class SystemSettings {
  private readonly destroyRef = inject(DestroyRef);
  private readonly toastr = inject(ToastrService);
  private readonly settingsApiStore = inject(SettingsApiStore);
  private readonly settingsApi = inject(SettingsApi);
  private readonly documentTypeApi = inject(DocumentTypeApi);
  private readonly documentApi = inject(DocumentApi);
  private readonly expectedFieldApi = inject(ExpectedFieldApi);
  private readonly dialog = inject(MatDialog);

  readonly resourceLoading = signal(false);
  readonly availableDocumentTypes = signal<DocumentTypeDTO[]>([]);
  readonly availableTemplates = signal<DocumentDTO[]>([]);
  readonly documentTypePurpose = DocumentTypePurpose;
  readonly targetEntity = TargetEntity;

  readonly salaryRangeColumns = ['label', 'min', 'max', 'active', 'actions'];
  documentFilter = '';
  selectedIndividualDocumentId: string | null = null;
  selectedOrganisationDocumentId: string | null = null;
  selectedOrganisationKycDocumentId: string | null = null;
  selectedIndividualKycDocumentId: string | null = null;
  roleOptions: string[] = ['SUPER_ADMIN', 'ADMIN', 'COMPLIANCE_ADMIN', 'VERIFIED_USER', 'USER'];

  editSettingsVarsForm: EditSettingsVarsForm = new EditSettingsVarsForm();
  editSettingsSignal = signal(this.editSettingsVarsForm);
  editSettingsSignalForm = form(this.editSettingsSignal, (path) => {
    required(path.kycDuration, { message: 'kyc.duration.required' })
    required(path.timeToAccountCreation, { message: 'time.to.account.creation.required' })
    required(path.platformName, { message: 'platform.name.required' })
    required(path.platformUrl, { message: 'platform.url.required' })
    required(path.kycPortalLink, { message: 'kyc.portal.link.required' })
    required(path.supportContact, { message: 'support.contact.required' })
    required(path.organisationAdminRole, { message: 'organisation.admin.role.required' })
    required(path.normalUserRole, { message: 'normal.user.role.required' })
    required(path.invoiceDocumentType, { message: 'invoice.document.type.required' })
    required(path.invoiceTemplateType, { message: 'invoice.template.type.required' })
    required(path.quotationDocumentType, { message: 'quotation.document.type.required' })
    required(path.quotationTemplateType, { message: 'quotation.template.type.required' });
    applyEach(path.salaryRanges, (itemPath) => {
      required(itemPath.active, { message: 'Active is required' });
      required(itemPath.min, { message: 'Min is required' });
    })
  });

  invoiceDocumentTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  invoiceTemplateTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  quotationDocumentTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  quotationTemplateTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  clientRequestFileTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  selectedOrgDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  selectedKycOrgDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  selectedIndDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  selectedKycIndDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  readonly individualKycExpectedFieldOptions = signal<ExpectedFieldDTO[]>([]);
  readonly organisationKycExpectedFieldOptions = signal<ExpectedFieldDTO[]>([]);

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = Object.keys(this.TargetEntityT);
  DocumentVerificationStatusT: any = DocumentVerificationStatus;
  DocumentVerificationStatusOptions = Object.keys(this.DocumentVerificationStatusT);
  loaderMessage = signal('');
  messages = linkedSignal(() => this.settingsApiStore.messages());
  success = linkedSignal(() => this.settingsApiStore.success());
  loading = linkedSignal(() => this.settingsApiStore.loading());
  error = linkedSignal(() => this.settingsApiStore.error());
  selected: any = null;
  settings: SettingsDTO = new SettingsDTO();

  constructor() {
    effect(() => {
      const settings = this.settingsApiStore.data();
      this.settings = settings || new SettingsDTO();

      if (!this.hasSettingsPayload(settings)) {
        return;
      }

      this.updateSettingForm(settings);
      this.roleOptions = this.buildRoleOptions(settings);

      // this.expectedFieldApi.findByDocumentType(settings.indKycDocuments?.map((doc: any ) => doc.id) || []).subscribe({
      //   next: (fields) => {
      //     console.log(fields)
      //   },
      //   error: (error) => {

      //   }
      // });
    });

    effect(() => {
      const error = this.error();

      if (error) {
        this.toastr.error(this.messages()[0], 'Error');
      }
    });

    effect(() => {
      const success = this.success();
      if (success) {
        this.toastr.success(this.messages()[0], 'Success');
      }
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.reset();
    this.settingsApiStore.getAll();
    this.loadReferenceData();
  }

  compareDocumentType = (left: DocumentTypeDTO | null, right: DocumentTypeDTO | null): boolean =>
    (left?.id || null) === (right?.id || null);

  compareTemplate = (left: DocumentDTO | null, right: DocumentDTO | null): boolean =>
    (left?.id || null) === (right?.id || null);

  resetForm(): void {
    this.settingsApiStore.reset()
  }

  saveSettings(): void {
    if (this.editSettingsSignalForm().invalid()) {
      this.toastr.error('Complete the required settings fields before saving.');
      return;
    }

    let val: any = this.editSettingsSignal();
    console.log(val)
    let settings = this.getSettings(val);
    this.loading.set(true);
    this.loaderMessage.set(`Saving settings`);

    this.settingsApiStore.save({ settings: settings });
  }

  createNewSalaryRanges(): SalaryRangeDTO {
    return new SalaryRangeDTO();
  }

  salaryRangesAdd() {

    this.editSettingsSignal.update((value) => ({
      ...value,
      salaryRanges: [
        ...value.salaryRanges,
        this.createNewSalaryRanges()
      ]
    }))
  }

  salaryRangesRemove(i: number, selected: SalaryRangeDTO) {

    Swal.fire({
      title: 'Remove salary range?',
      text: `This will remove the salary range with min ${selected.min} and max ${selected.max}.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Remove',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (result.isConfirmed) {

        this.editSettingsSignal.update((value) => {
          const salaryRanges = value.salaryRanges.filter((_: any, index: number) => index !== i);

          return {
            ...value,
            salaryRanges: salaryRanges
          }
        });
      }
    });

  }

  addDocumentRequirement(purpose: DocumentTypePurpose): void {
    console.log('Adding document requirement for purpose:', purpose);
    const documentTypeId = this.selectedDocumentIdFor(purpose);
    console.log('Selected document type ID:', documentTypeId);
    console.log(this.editSettingsSignal())

    if (!documentTypeId) {
      return;
    }

    // this.pendingAction.set('attach');
    this.settingsApiStore.attachDocumentType({ documentTypeId, purpose });
    this.clearSelectedDocumentId(purpose);
  }

  async removeDocumentRequirement(
    purpose: DocumentTypePurpose,
    documentType: DocumentTypeDTO,
  ): Promise<void> {
    const result = await Swal.fire({
      title: 'Remove document requirement?',
      text: `This will detach ${documentType.name} from the selected settings group.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Detach',
      cancelButtonText: 'Cancel',
    });

    if (!result.isConfirmed) {
      return;
    }

    // this.pendingAction.set('detach');
    this.settingsApiStore.detachDocumentType({ documentTypeId: documentType.id, purpose });
  }

  private loadReferenceData(): void {
    this.resourceLoading.set(true);

    forkJoin({
      documentTypes: this.documentTypeApi.getAll(),
      templates: this.documentApi.getAll(),
    })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: ({ documentTypes, templates }) => {
          this.availableDocumentTypes.set((documentTypes || []) as DocumentTypeDTO[]);
          this.availableTemplates.set((templates || []) as DocumentDTO[]);
          this.resourceLoading.set(false);
        },
        error: () => {
          this.resourceLoading.set(false);
          this.toastr.error('Unable to load settings reference data.');
        },
      });
  }

  private hasSettingsPayload(settings: SettingsDTO | null | undefined): boolean {
    return Boolean(
      settings &&
      (settings.id ||
        settings.platformName ||
        settings.platformUrl ||
        settings.kycPortalLink ||
        settings.organisationDocuments?.length ||
        settings.individualDocuments?.length ||
        settings.salaryRanges?.length),
    );
  }

  private cloneSettings(settings: SettingsDTO | null | undefined): SettingsDTO {
    const source = settings || new SettingsDTO();
    const clone = new SettingsDTO();

    clone.id = source.id;
    clone.createdAt = source.createdAt;
    clone.createdBy = source.createdBy;
    clone.modifiedAt = source.modifiedAt;
    clone.modifiedBy = source.modifiedBy;
    clone.kycDuration = source.kycDuration ?? 14;
    clone.timeToAccountCreation = source.timeToAccountCreation ?? 0;
    clone.platformName = source.platformName ?? '';
    clone.platformUrl = source.platformUrl ?? '';
    clone.kycPortalLink = source.kycPortalLink ?? '';
    clone.supportContact = source.supportContact ?? '';
    clone.organisationAdminRole = source.organisationAdminRole ?? 'SUPER_ADMIN';
    clone.normalUserRole = source.normalUserRole ?? 'VERIFIED_USER';
    clone.individualDocuments = [...(source.individualDocuments || [])];
    clone.organisationDocuments = [...(source.organisationDocuments || [])];
    clone.orgKycDocuments = [...(source.orgKycDocuments || [])];
    clone.indKycDocuments = [...(source.indKycDocuments || [])];
    clone.invoiceDocumentType = source.invoiceDocumentType ?? null;
    clone.invoiceTemplateType = source.invoiceTemplateType ?? null;
    clone.invoiceTemplate = source.invoiceTemplate ?? null;
    clone.quotationDocumentType = source.quotationDocumentType ?? null;
    clone.quotationTemplateType = source.quotationTemplateType ?? null;
    clone.quotationTemplate = source.quotationTemplate ?? null;
    clone.clientRequestFileType = source.clientRequestFileType ?? null;
    clone.salaryRanges = (source.salaryRanges || []).map((range: SalaryRangeDTO) => ({ ...range }));
    clone.documentDurationLimit = source.documentDurationLimit ?? null;
    clone.dataVerificationThreshold = source.dataVerificationThreshold ?? null;
    clone.maxDataVerificationFailureThreshold = source.maxDataVerificationFailureThreshold ?? null;
    clone.individualKycFieldGroups = (source.individualKycFieldGroups || []).map((group: KycFieldGroupDTO) => ({
      ...group,
      expectedFields: [...(group?.expectedFields || [])],
    }));
    clone.organisationKycFieldGroups = (source.organisationKycFieldGroups || []).map((group: KycFieldGroupDTO) => ({
      ...group,
      expectedFields: [...(group?.expectedFields || [])],
    }));

    return clone;
  }

  private buildRoleOptions(settings: SettingsDTO): string[] {
    const baseRoles = ['SUPER_ADMIN', 'ADMIN', 'COMPLIANCE_ADMIN', 'VERIFIED_USER', 'USER'];
    return Array.from(
      new Set([...baseRoles, settings.organisationAdminRole, settings.normalUserRole].filter(Boolean)),
    );
  }

  private selectedDocumentIdFor(purpose: DocumentTypePurpose): string | null {
    switch (purpose) {
      case DocumentTypePurpose.INDIVIDUAL:
        return this.editSettingsSignal().selectedIndDocument?.id;
      case DocumentTypePurpose.ORGANISATION:
        return this.editSettingsSignal().selectedOrgDocument?.id;
      case DocumentTypePurpose.ORGANISATION_KYC:
        return this.editSettingsSignal().selectedKycOrgDocument?.id;
      case DocumentTypePurpose.INDIVIDUAL_KYC:
        return this.editSettingsSignal().selectedKycIndDocument?.id;
    }
  }

  private clearSelectedDocumentId(purpose: DocumentTypePurpose): void {
    switch (purpose) {
      case DocumentTypePurpose.INDIVIDUAL:
        this.selectedIndividualDocumentId = null;
        break;
      case DocumentTypePurpose.ORGANISATION:
        this.selectedOrganisationDocumentId = null;
        break;
      case DocumentTypePurpose.ORGANISATION_KYC:
        this.selectedOrganisationKycDocumentId = null;
        break;
      case DocumentTypePurpose.INDIVIDUAL_KYC:
        this.selectedIndividualKycDocumentId = null;
        break;
    }
  }

  private formatAuditDate(value: Date | string | null | undefined): string {
    if (!value) {
      return 'Not verified yet';
    }

    const date = value instanceof Date ? value : new Date(value);
    if (Number.isNaN(date.getTime())) {
      return String(value);
    }

    return new Intl.DateTimeFormat('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
    }).format(date);
  }

  private asNumber(value: number | string | null | undefined): number | null {
    if (value === null || value === undefined || value === '') {
      return null;
    }

    const result = Number(value);
    return Number.isNaN(result) ? null : result;
  }


  updateSettingForm(settings: SettingsDTO) {

    console.log(settings)

    this.editSettingsSignal.set({
      id: settings.id,
      createdAt: settings.createdAt,
      createdBy: settings.createdBy,
      modifiedAt: settings.modifiedAt,
      modifiedBy: settings.modifiedBy,
      kycDuration: settings.kycDuration,
      selectedOrgDocument: null,
      organisationDocuments: settings.organisationDocuments || [],
      selectedKycOrgDocument: null,
      orgKycDocuments: settings.orgKycDocuments || [],
      selectedIndDocument: null,
      individualDocuments: settings.individualDocuments || [],
      selectedKycIndDocument: null,
      indKycDocuments: settings.indKycDocuments || [],
      selectedIndividualKycField: null,
      selectedOrganisationKycField: null,
      invoiceDocumentType: settings.invoiceDocumentType,
      invoiceTemplateType: settings.invoiceTemplateType,
      invoiceTemplate: settings.invoiceTemplate,
      quotationDocumentType: settings.quotationDocumentType,
      quotationTemplateType: settings.quotationTemplateType,
      quotationTemplate: settings.quotationTemplate,
      clientRequestFileType: settings.clientRequestFileType,
      clientRequestFileTypeFilter: '',
      invoiceDocumentTypeFilter: '',
      invoiceTemplateTypeFilter: '',
      quotationDocumentTypeFilter: '',
      quotationTemplateTypeFilter: '',
      selectedOrgDocumentFilter: '',
      selectedKycOrgDocumentFilter: '',
      selectedIndDocumentFilter: '',
      selectedKycIndDocumentFilter: '',
      salaryRanges: settings.salaryRanges || [],
      timeToAccountCreation: settings.timeToAccountCreation || 0,
      platformName: settings.platformName,
      platformUrl: settings.platformUrl,
      supportContact: settings.supportContact,
      kycPortalLink: settings.kycPortalLink,
      organisationAdminRole: settings.organisationAdminRole,
      normalUserRole: settings.normalUserRole,
      vat: settings.vat,
      documentDurationLimit: settings.documentDurationLimit,
      dataVerificationThreshold: settings.dataVerificationThreshold,
      maxDataVerificationFailureThreshold: settings.maxDataVerificationFailureThreshold,
      organisationKycFieldGroups: settings.organisationKycFieldGroups || [],
      individualKycFieldGroups: settings.individualKycFieldGroups || [],
    });

    this.refreshKycExpectedFieldOptions(settings.indKycDocuments || [], settings.orgKycDocuments || []);

  }

  addKycExpectedField(isOrganisation: boolean): void {
    const selectedField = isOrganisation
      ? this.editSettingsSignal().selectedOrganisationKycField
      : this.editSettingsSignal().selectedIndividualKycField;

    if (!selectedField) {
      return;
    }

    this.editSettingsSignal.update((value) => {
      const targetType = isOrganisation ? TargetEntity.ORGANISATION : TargetEntity.INDIVIDUAL;
      const groupLabel = isOrganisation ? 'Organisation KYC Expected Fields' : 'Individual KYC Expected Fields';
      const selectedKey = isOrganisation ? 'selectedOrganisationKycField' : 'selectedIndividualKycField';
      const targetKey = isOrganisation ? 'organisationKycFieldGroups' : 'individualKycFieldGroups';
      const groups = [...((value[targetKey] || []) as KycFieldGroupDTO[])];
      const existingIndex = groups.findIndex((group) => group?.targetType === targetType);
      const targetGroup = existingIndex >= 0 ? groups[existingIndex] : this.createKycGroup(targetType, groupLabel);
      const expectedFields = [...(targetGroup.expectedFields || [])];

      if (!this.includesExpectedField(expectedFields, selectedField)) {
        targetGroup.expectedFields = [...expectedFields, selectedField];
      }

      if (existingIndex >= 0) {
        groups[existingIndex] = targetGroup;
      } else {
        groups.push(targetGroup);
      }

      return {
        ...value,
        [targetKey]: groups,
        [selectedKey]: null,
      } as EditSettingsVarsForm;
    });
  }

  removeKycExpectedField(isOrganisation: boolean, field: ExpectedFieldDTO): void {
    this.editSettingsSignal.update((value) => {
      const targetType = isOrganisation ? TargetEntity.ORGANISATION : TargetEntity.INDIVIDUAL;
      const targetKey = isOrganisation ? 'organisationKycFieldGroups' : 'individualKycFieldGroups';
      const groups = [...((value[targetKey] || []) as KycFieldGroupDTO[])];

      const updatedGroups = groups.map((group) => {
        if (group?.targetType !== targetType) {
          return group;
        }

        return {
          ...group,
          expectedFields: (group.expectedFields || []).filter(
            (item: ExpectedFieldDTO) => !this.sameExpectedField(item, field),
          ),
        } as KycFieldGroupDTO;
      });

      return {
        ...value,
        [targetKey]: updatedGroups,
      } as EditSettingsVarsForm;
    });
  }

  expectedFieldLabel(field: ExpectedFieldDTO | null | undefined): string {
    if (!field) {
      return '';
    }

    const base = field.fieldLabel || field.field || 'Unnamed field';
    const source = field.documentType ? ` (${field.documentType})` : '';

    return `${base}${source}`;
  }

  compareExpectedField = (left: ExpectedFieldDTO | null, right: ExpectedFieldDTO | null): boolean =>
    this.sameExpectedField(left, right);

  private refreshKycExpectedFieldOptions(
    individualKycDocuments: DocumentTypeDTO[],
    organisationKycDocuments: DocumentTypeDTO[],
  ): void {
    this.loadExpectedFieldsForDocuments(individualKycDocuments, (fields) => {
      this.individualKycExpectedFieldOptions.set(fields);
    });

    this.loadExpectedFieldsForDocuments(organisationKycDocuments, (fields) => {
      this.organisationKycExpectedFieldOptions.set(fields);
    });
  }

  private loadExpectedFieldsForDocuments(
    documentTypes: DocumentTypeDTO[],
    setOptions: (fields: ExpectedFieldDTO[]) => void,
  ): void {
    const ids = (documentTypes || []).map((item) => item?.id).filter(Boolean);

    if (ids.length === 0) {
      setOptions([]);
      return;
    }

    this.expectedFieldApi.findByDocumentType(ids).subscribe({
      next: (fields) => {
        setOptions(this.uniqueExpectedFields(fields || []));
      },
      error: (error) => {
        this.toastr.error(error?.error?.message || error?.message || 'Unable to load expected fields');
        setOptions([]);
      },
    });
  }

  private uniqueExpectedFields(fields: ExpectedFieldDTO[]): ExpectedFieldDTO[] {
    const seen = new Set<string>();

    return (fields || []).filter((field) => {
      const key = this.expectedFieldKey(field);
      if (!key || seen.has(key)) {
        return false;
      }

      seen.add(key);
      return true;
    });
  }

  private includesExpectedField(collection: ExpectedFieldDTO[], candidate: ExpectedFieldDTO): boolean {
    return (collection || []).some((item) => this.sameExpectedField(item, candidate));
  }

  private sameExpectedField(
    left: ExpectedFieldDTO | null | undefined,
    right: ExpectedFieldDTO | null | undefined,
  ): boolean {
    if (!left || !right) {
      return false;
    }

    return this.expectedFieldKey(left) === this.expectedFieldKey(right);
  }

  private expectedFieldKey(field: ExpectedFieldDTO | null | undefined): string {
    if (!field) {
      return '';
    }

    if (field.id) {
      return String(field.id);
    }

    const documentTypeId = field.documentTypeId || field.documentType || 'unknown';
    const name = field.field || field.fieldLabel || 'unknown';
    return `${documentTypeId}:${name}`;
  }

  private getSettings(value: any): SettingsDTO {
    let settings: SettingsDTO = new SettingsDTO();
    settings.createdAt = value.createdAt;
    settings.createdBy = value.createdBy;
    settings.modifiedAt = value.modifiedAt;
    settings.modifiedBy = value.modifiedBy;
    settings.id = value.id;
    settings.kycDuration = value.kycDuration;
    settings.organisationDocuments = value.organisationDocuments || [];
    settings.individualDocuments = value.individualDocuments || [];
    settings.indKycDocuments = value.indKycDocuments || [];
    settings.orgKycDocuments = value.orgKycDocuments || [];
    settings.invoiceDocumentType = value.invoiceDocumentType;
    settings.clientRequestFileType = value.clientRequestFileType;
    settings.invoiceTemplateType = value.invoiceTemplateType;
    settings.invoiceTemplate = value.invoiceTemplate;
    settings.quotationDocumentType = value.quotationDocumentType;
    settings.quotationTemplateType = value.quotationTemplateType;
    settings.quotationTemplate = value.quotationTemplate;
    settings.salaryRanges = value.salaryRanges || [];
    settings.platformName = value.platformName;
    settings.platformUrl = value.platformUrl;
    settings.supportContact = value.supportContact;
    settings.kycPortalLink = value.kycPortalLink;
    settings.organisationAdminRole = value.organisationAdminRole;
    settings.normalUserRole = value.normalUserRole;
    settings.timeToAccountCreation = value.timeToAccountCreation;
    settings.vat = value.vat;
    settings.documentDurationLimit = this.asNumber(value.documentDurationLimit);
    settings.dataVerificationThreshold = this.asNumber(value.dataVerificationThreshold);
    settings.maxDataVerificationFailureThreshold = this.asNumber(value.maxDataVerificationFailureThreshold);
    settings.individualKycFieldGroups = this.normalizeKycGroups(
      value.individualKycGroupFields,
      this.settings.individualKycFieldGroups,
      TargetEntity.INDIVIDUAL,
      'Individual KYC Expected Fields',
    );
    settings.organisationKycFieldGroups = this.normalizeKycGroups(
      value.organisationKycGroupFields,
      this.settings.organisationKycFieldGroups,
      TargetEntity.ORGANISATION,
      'Organisation KYC Expected Fields',
    );

    return settings;
  }

  private extractExpectedFieldsFromGroups(
    groups: KycFieldGroupDTO[] | null | undefined,
    legacyFields: ExpectedFieldDTO[] | null | undefined,
  ): ExpectedFieldDTO[] {
    const fromGroups = (groups || []).flatMap((group) => group?.expectedFields || []);
    if (fromGroups.length > 0) {
      return this.uniqueExpectedFields(fromGroups);
    }

    return this.uniqueExpectedFields(legacyFields || []);
  }

  private buildKycFieldGroups(
    existingGroups: KycFieldGroupDTO[] | null | undefined,
    targetType: TargetEntity,
    label: string,
    fields: ExpectedFieldDTO[],
  ): KycFieldGroupDTO[] {
    const existing = (existingGroups || []).find((group) => group?.targetType === targetType);
    const group = new KycFieldGroupDTO();

    group.id = existing?.id ?? null;
    group.createdAt = existing?.createdAt ?? null;
    group.createdBy = existing?.createdBy ?? null;
    group.modifiedAt = existing?.modifiedAt ?? null;
    group.modifiedBy = existing?.modifiedBy ?? null;
    group.label = existing?.label || label;
    group.description = existing?.description || null;
    group.targetType = targetType;
    group.expectedFields = this.uniqueExpectedFields(fields || []);

    return [group];
  }

  private createKycGroup(targetType: TargetEntity, label: string): KycFieldGroupDTO {
    const group = new KycFieldGroupDTO();
    group.targetType = targetType;
    group.label = label;
    group.description = null;
    group.expectedFields = [];
    return group;
  }

  private normalizeKycGroups(
    groups: KycFieldGroupDTO[] | null | undefined,
    existingGroups: KycFieldGroupDTO[] | null | undefined,
    targetType: TargetEntity,
    fallbackLabel: string,
  ): KycFieldGroupDTO[] {
    const normalizedGroups = (groups || []).map((group) => {
      const normalized = new KycFieldGroupDTO();
      normalized.id = group?.id ?? null;
      normalized.createdAt = group?.createdAt ?? null;
      normalized.createdBy = group?.createdBy ?? null;
      normalized.modifiedAt = group?.modifiedAt ?? null;
      normalized.modifiedBy = group?.modifiedBy ?? null;
      normalized.label = group?.label || fallbackLabel;
      normalized.description = group?.description || null;
      normalized.targetType = group?.targetType || targetType;
      normalized.expectedFields = this.uniqueExpectedFields(group?.expectedFields || []);
      return normalized;
    });

    if (normalizedGroups.length > 0) {
      return normalizedGroups;
    }

    return this.buildKycFieldGroups(existingGroups, targetType, fallbackLabel, []);
  }

  filterSelectedOrgDocument(): void {
    const search = this.editSettingsSignal().selectedOrgDocumentFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.selectedOrgDocumentFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterInvoiceDocumentType(): void {
    const search = this.editSettingsSignal().invoiceDocumentTypeFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.invoiceDocumentTypeFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterClientRequestFileType(): void {
    const search = this.editSettingsSignal().clientRequestFileTypeFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.clientRequestFileTypeFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterInvoiceTemplateType(): void {
    const search = this.editSettingsSignal().invoiceTemplateTypeFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.invoiceTemplateTypeFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterQuotationDocumentType(): void {
    const search = this.editSettingsSignal().quotationDocumentTypeFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.quotationDocumentTypeFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterQuotationTemplateType(): void {
    const search = this.editSettingsSignal().quotationTemplateTypeFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.quotationTemplateTypeFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterSelectedKycOrgDocument(): void {
    const search = this.editSettingsSignal().selectedKycOrgDocumentFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.selectedKycOrgDocumentFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterSelectedIndDocument(): void {
    const search = this.editSettingsSignal().selectedIndDocumentFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.selectedIndDocumentFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  filterSelectedKycIndDocument(): void {
    const search = this.editSettingsSignal().selectedKycIndDocumentFilter?.toLowerCase() || '';
    this.loading.set(true);
    this.loaderMessage.set(`Searching document types.`);
    this.documentTypeApi.search(search).subscribe(
      (data) => {
        this.selectedKycIndDocumentFilteredList.set(data || []);
        this.loading.set(false);
      },
      (error) => {
        this.toastr.error(error.error?.message ? error.error.message : error.message);
        this.loading.set(false);
      }
    );
  }

  onAddToIndDocumentsClick() {
    let val: any = this.editSettingsSignal();
    let settings = this.getSettings(val);
    let found = settings.individualDocuments?.find(
      (d: DocumentTypeDTO) => d.id === this.editSettingsSignal().selectedIndDocument.id
    );
    if (!found) {
      settings.individualDocuments?.push(this.editSettingsSignal().selectedIndDocument);
    }
    this.settingsApiStore.attachDocumentType({
      documentTypeId: this.editSettingsSignal().selectedIndDocument.id,
      purpose: DocumentTypePurpose.INDIVIDUAL,
    });
  }

  onAddToKycOrgDocumentsClick() {

    this.settingsApiStore.attachDocumentType({
      documentTypeId: this.editSettingsSignal().selectedKycOrgDocument.id,
      purpose: DocumentTypePurpose.ORGANISATION_KYC,
    });

  }

  onAddToOrgDocumentsClick() {

    this.settingsApiStore.attachDocumentType({
      documentTypeId: this.editSettingsSignal().selectedOrgDocument.id,
      purpose: DocumentTypePurpose.ORGANISATION,
    });
  }

  onAddToKycIndDocumentsClick() {
    // console.log(this.editSettingsSignal().selectedKycIndDocument);
    this.settingsApiStore.attachDocumentType({
      documentTypeId: this.editSettingsSignal().selectedKycIndDocument.id,
      purpose: DocumentTypePurpose.INDIVIDUAL_KYC,
    });
  }

  attachInvoiceTemplate(): void {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept =
      'application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document';
    input.onchange = (event: any) => {
      const file = event.target.files[0];
      if (file) {
        this.handleInvoiceTemplateUpload(file);
      }
    };
    input.click();
  }

  attachQuotationTemplate(): void {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept =
      'application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document';
    input.onchange = (event: any) => {
      const file = event.target.files[0];
      if (file) {
        this.handleQuotationTemplateUpload(file);
      }
    };
    input.click();
  }

  private handleInvoiceTemplateUpload(file: File): void {
    // TODO: Implement invoice template upload logic
    this.settingsApiStore.uploadTemplate({ template: file, target: TargetEntity.INVOICE });
  }

  private handleQuotationTemplateUpload(file: File): void {
    // TODO: Implement quotation template upload logic
    console.log('Quotation template upload:', file.name);

    this.settingsApiStore.uploadTemplate({ template: file, target: TargetEntity.QUOTATION });
  }

  downloadTemplate(target: TargetEntity): void {
    if (target === TargetEntity.INVOICE) {
    } else if (target === TargetEntity.QUOTATION) {
    }

    const docId =
      target === TargetEntity.INVOICE
        ? this.editSettingsSignal().invoiceTemplate?.id
        : this.editSettingsSignal().quotationTemplate?.id;

    if (!docId) {
      this.toastr.error('No template document available for download', 'Download Error');
      return;
    }

    let doc =
      target === TargetEntity.INVOICE
        ? this.settings.invoiceTemplate
        : this.settings.quotationTemplate;

    this.documentApi.downloadFile(doc?.id).subscribe({
      next: (res: any) => {
        const url = window.URL.createObjectURL(res);
        const a = document.createElement('a');
        a.href = url;

        // Optional: set a meaningful filename
        a.download = `${doc?.fileName}.pdf`; // or dynamically get filename from backend
        a.click();

        window.URL.revokeObjectURL(url); // clean up
      },
      error: (err: any) => {
        this.toastr.error(err.message || 'Failed to download file', 'Download Error');
      },
      complete: () => {
        // this.isDownloading.set('');
      },
    });
  }

  organisationDocumentsTableActionClicked(event: any): void {

    console.log(event)

    switch (event.action) {
      case 'settings-detach-org-documents':
        Swal.fire({
          title: 'Are you sure?',
          text: 'You are about to detach this document type from organisation documents. This action cannot be undone.',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
        }).then((result) => {
          if (result.isConfirmed) {
            this.settingsApiStore.detachDocumentType({
              documentTypeId: event.row.id,
              purpose: DocumentTypePurpose.ORGANISATION,
            });
            Swal.fire('Detached!', 'The document type has been detached from organisation documents.', 'success');
          }
        });

        break;
    }
  }

  individualDocumentsTableActionClicked(event: any): void {
    switch (event.action) {
      case 'settings-detach-individual-documents':
        // TODO: Implement the action

        Swal.fire({
          title: 'Are you sure?',
          text: 'You are about to detach this document type from individual documents. This action cannot be undone.',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
        }).then((result) => {
          if (result.isConfirmed) {

            this.settingsApiStore.detachDocumentType({
              documentTypeId: event.row.id,
              purpose: DocumentTypePurpose.INDIVIDUAL,
            });
            Swal.fire('Detached!', 'The document type has been detached from individual documents.', 'success');
          }
        });

        break;
    }
  }

  orgKycDocumentsTableActionClicked(event: any): void {
    switch (event.action) {
      case 'settings-detach-org-kyc-documents':
        // TODO: Implement the action
        Swal.fire({
          title: 'Are you sure?',
          text: 'You are about to detach this document type from organisation KYC documents. This action cannot be undone.',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
        }).then((result) => {
          if (result.isConfirmed) {

            this.settingsApiStore.detachDocumentType({
              documentTypeId: event.row.id,
              purpose: DocumentTypePurpose.ORGANISATION_KYC,
            });
            Swal.fire('Detached!', 'The document type has been detached from organisation KYC documents.', 'success');
          }
        });
        break;
    }
  }

  indKycDocumentsTableActionClicked(event: any): void {
    let form: any = {};
    let queryParams: any = {};
    let params: any = {};

    switch (event.action) {
      case 'settings-detach-ind-kyc-documents':
        // TODO: Implement the action

        Swal.fire({
          title: 'Are you sure?',
          text: 'You are about to detach this document type from individual KYC documents. This action cannot be undone.',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
        }).then((result) => {
          if (result.isConfirmed) {

            this.settingsApiStore.detachDocumentType({
              documentTypeId: event.row.id,
              purpose: DocumentTypePurpose.INDIVIDUAL_KYC,
            });
            Swal.fire('Detached!', 'The document type has been detached from individual KYC documents.', 'success');
          }
        });

        break;
    }
  }

  documentCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  openAddFieldGroupDialog(): void {
    const indKycDocuments = this.editSettingsSignal().indKycDocuments || [];
    
    this.dialog.open(AddKycFieldGroupDialog, {
      width: '600px',
      data: {
        kycDocuments: indKycDocuments,
        existingGroups: this.editSettingsSignal().individualKycFieldGroups || [],
        groupTypeLabel: 'Individual KYC Field Group',
        suiteLabel: 'Ind. KYC Suite',
        suiteDescription: 'individual KYC suite documents',
        targetType: TargetEntity.INDIVIDUAL,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        this.editSettingsSignal.update((value) => ({
          ...value,
          individualKycFieldGroups: [...(value.individualKycFieldGroups || []), result]
        }));
      }
    });
  }

  editFieldGroup(group: KycFieldGroupDTO): void {
    const indKycDocuments = this.editSettingsSignal().indKycDocuments || [];
    
    this.dialog.open(AddKycFieldGroupDialog, {
      width: '600px',
      data: {
        kycDocuments: indKycDocuments,
        existingGroups: this.editSettingsSignal().individualKycFieldGroups || [],
        editingGroup: group,
        groupTypeLabel: 'Individual KYC Field Group',
        suiteLabel: 'Ind. KYC Suite',
        suiteDescription: 'individual KYC suite documents',
        targetType: TargetEntity.INDIVIDUAL,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        this.editSettingsSignal.update((value) => {
          const groups = [...(value.individualKycFieldGroups || [])];
          const index = groups.findIndex((g) => g.id === group.id || g === group);
          if (index >= 0) {
            groups[index] = result;
          }
          return { ...value, individualKycFieldGroups: groups };
        });
      }
    });
  }

  deleteFieldGroup(group: KycFieldGroupDTO): void {
    Swal.fire({
      title: 'Delete field group?',
      text: `This will remove the field group "${group.label || 'Individual KYC Group'}" and all its fields.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editSettingsSignal.update((value) => ({
          ...value,
          individualKycFieldGroups: (value.individualKycFieldGroups || []).filter((g) => g !== group)
        }));
      }
    });
  }

  openAddOrganisationFieldGroupDialog(): void {
    const orgKycDocuments = this.editSettingsSignal().orgKycDocuments || [];

    this.dialog.open(AddKycFieldGroupDialog, {
      width: '600px',
      data: {
        kycDocuments: orgKycDocuments,
        existingGroups: this.editSettingsSignal().organisationKycFieldGroups || [],
        groupTypeLabel: 'Organisation KYC Field Group',
        suiteLabel: 'Org. KYC Suite',
        suiteDescription: 'organisation KYC suite documents',
        targetType: TargetEntity.ORGANISATION,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        this.editSettingsSignal.update((value) => ({
          ...value,
          organisationKycFieldGroups: [...(value.organisationKycFieldGroups || []), result]
        }));
      }
    });
  }

  editOrganisationFieldGroup(group: KycFieldGroupDTO): void {
    const orgKycDocuments = this.editSettingsSignal().orgKycDocuments || [];

    this.dialog.open(AddKycFieldGroupDialog, {
      width: '600px',
      data: {
        kycDocuments: orgKycDocuments,
        existingGroups: this.editSettingsSignal().organisationKycFieldGroups || [],
        editingGroup: group,
        groupTypeLabel: 'Organisation KYC Field Group',
        suiteLabel: 'Org. KYC Suite',
        suiteDescription: 'organisation KYC suite documents',
        targetType: TargetEntity.ORGANISATION,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        this.editSettingsSignal.update((value) => {
          const groups = [...(value.organisationKycFieldGroups || [])];
          const index = groups.findIndex((g) => g.id === group.id || g === group);
          if (index >= 0) {
            groups[index] = result;
          }
          return { ...value, organisationKycFieldGroups: groups };
        });
      }
    });
  }

  deleteOrganisationFieldGroup(group: KycFieldGroupDTO): void {
    Swal.fire({
      title: 'Delete field group?',
      text: `This will remove the field group "${group.label || 'Organisation KYC Group'}" and all its fields.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editSettingsSignal.update((value) => ({
          ...value,
          organisationKycFieldGroups: (value.organisationKycFieldGroups || []).filter((g) => g !== group)
        }));
      }
    });
  }
}

@Component({
  selector: 'app-add-kyc-field-group-dialog',
  standalone: true,
  template: `
    <div class="dialog-container">
      <h2 mat-dialog-title>{{ data.editingGroup ? 'Edit' : 'Add' }} {{ data.groupTypeLabel }}</h2>
      
      <mat-dialog-content>
        <div class="field-group-form">
          <mat-form-field class="full-width">
            <mat-label>Group Label</mat-label>
            <input matInput [(ngModel)]="groupLabel" placeholder="e.g., Basic Personal Information" />
          </mat-form-field>

          <mat-form-field class="full-width">
            <mat-label>Group Description</mat-label>
            <textarea matInput [(ngModel)]="groupDescription" placeholder="Optional description" rows="3"></textarea>
          </mat-form-field>

          <div class="fields-section">
            <h3>Expected Fields from {{ data.suiteLabel }}</h3>
            <p class="helper-text">Select expected fields from the {{ data.suiteDescription }}</p>
            
            <div class="fields-list">
              @for (field of availableFields; track field.id || $index) {
                <div class="field-checkbox-item">
                  <mat-checkbox 
                    [(ngModel)]="selectedFieldIds[field.id || field.field]"
                    (change)="onFieldSelected(field)">
                    <span class="field-name">{{ field.fieldLabel || field.field }}</span>
                    <span class="field-doc" *ngIf="field.documentType">({{ field.documentType }})</span>
                  </mat-checkbox>
                </div>
              }
              @empty {
                <p class="no-fields-message">No fields available from {{ data.suiteLabel }}</p>
              }
            </div>
          </div>

          <div class="selected-fields-summary">
            <h4>Selected Fields ({{ selectedFields.length }})</h4>
            @for (field of selectedFields; track field.id || $index) {
              <div class="selected-field-tag">
                <span>{{ field.fieldLabel || field.field }}</span>
              </div>
            }
          </div>
        </div>
      </mat-dialog-content>

      <mat-dialog-actions align="end">
        <button mat-button (click)="dialogRef.close()">Cancel</button>
        <button mat-raised-button color="primary" (click)="save()" [disabled]="!isValid()">
          {{ data.editingGroup ? 'Update' : 'Add' }} Group
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-container {
      padding: 10px;
    }
    
    .full-width {
      width: 100%;
    }
    
    .fields-section {
      margin-top: 24px;
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      padding: 16px;
      background-color: #fafafa;
    }
    
    .fields-section h3 {
      margin-top: 0;
      margin-bottom: 8px;
      font-size: 14px;
      font-weight: 500;
    }
    
    .helper-text {
      margin: 0 0 12px 0;
      color: #666;
      font-size: 12px;
    }
    
    .fields-list {
      max-height: 250px;
      overflow-y: auto;
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      padding: 8px;
      background: white;
    }
    
    .field-checkbox-item {
      padding: 8px;
      border-bottom: 1px solid #f0f0f0;
    }
    
    .field-checkbox-item:last-child {
      border-bottom: none;
    }
    
    .field-name {
      font-size: 13px;
    }
    
    .field-doc {
      color: #999;
      font-size: 11px;
      margin-left: 4px;
    }
    
    .no-fields-message {
      text-align: center;
      color: #999;
      padding: 16px 8px;
      font-size: 12px;
    }
    
    .selected-fields-summary {
      margin-top: 16px;
      padding: 12px;
      background: #f5f5f5;
      border-radius: 4px;
    }
    
    .selected-fields-summary h4 {
      margin-top: 0;
      margin-bottom: 8px;
      font-size: 13px;
    }
    
    .selected-field-tag {
      display: inline-block;
      background: #e3f2fd;
      color: #1976d2;
      padding: 6px 12px;
      border-radius: 4px;
      margin-right: 8px;
      margin-bottom: 4px;
      font-size: 12px;
    }
  `],
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    FormsModule
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddKycFieldGroupDialog {
  readonly dialogRef = inject(MatDialogRef<KycFieldGroupDTO>);
  readonly data = inject(MAT_DIALOG_DATA);
  
  groupLabel = '';
  groupDescription = '';
  availableFields: ExpectedFieldDTO[] = [];
  selectedFields: ExpectedFieldDTO[] = [];
  selectedFieldIds: { [key: string]: boolean } = {};
  private expectedFieldApi = inject(ExpectedFieldApi);

  constructor() {
    this.loadAvailableFields();
    if (this.data.editingGroup) {
      this.groupLabel = this.data.editingGroup.label || '';
      this.groupDescription = this.data.editingGroup.description || '';
      this.selectedFields = [...(this.data.editingGroup.expectedFields || [])];
      this.selectedFields.forEach((field) => {
        this.selectedFieldIds[field.id || field.field] = true;
      });
    }
  }

  private loadAvailableFields(): void {
    const documentIds = (this.data.kycDocuments || [])
      .map((doc: DocumentTypeDTO) => doc.id)
      .filter(Boolean);

    if (documentIds.length === 0) {
      this.availableFields = [];
      return;
    }

    this.expectedFieldApi.findByDocumentType(documentIds).subscribe({
      next: (fields) => {
        this.availableFields = this.uniqueFields(fields || []);
      },
      error: () => {
        this.availableFields = [];
      }
    });
  }

  onFieldSelected(field: ExpectedFieldDTO): void {
    const key = field.id || field.field;
    const isSelected = this.selectedFieldIds[key];

    if (isSelected) {
      this.selectedFields.push(field);
    } else {
      this.selectedFields = this.selectedFields.filter((f) => (f.id || f.field) !== key);
    }
  }

  save(): void {
    if (!this.isValid()) {
      return;
    }

    const group = this.data.editingGroup || new KycFieldGroupDTO();
    group.label = this.groupLabel;
    group.description = this.groupDescription;
    group.expectedFields = this.selectedFields;
    group.targetType = this.data.targetType;

    this.dialogRef.close(group);
  }

  isValid(): boolean {
    return !!this.groupLabel && this.selectedFields.length > 0;
  }

  private uniqueFields(fields: ExpectedFieldDTO[]): ExpectedFieldDTO[] {
    const seen = new Set<string>();
    return (fields || []).filter((field) => {
      const key = field.id || field.field;
      if (seen.has(key)) {
        return false;
      }
      seen.add(key);
      return true;
    });
  }
}
