import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, linkedSignal, signal } from '@angular/core';
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
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { forkJoin } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { swalFire } from '@app/@shared/swal';

import { Loader } from '@app/@shared/loader/loader';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { DocumentTypePurpose } from '@app/models/bw/co/knowvera/settings/document-type-purpose';
import { SalaryRangeDTO } from '@app/models/bw/co/knowvera/settings/salary-range-dto';
import { SettingsDTO } from '@app/models/bw/co/knowvera/settings/settings-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { DocumentTypeApi } from '@app/services/bw/co/knowvera/document/type/document-type-api';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { form, required, FormField, applyEach } from '@angular/forms/signals';
import { DocumentVerificationStatus } from '@app/models/bw/co/knowvera/document/document-verification-status';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { TranslateModule } from '@ngx-translate/core';
import { ExpectedFieldDTO } from '@app/models/bw/co/knowvera/document/type/field/expected-field-dto';
import { ExpectedFieldApi } from '@app/services/bw/co/knowvera/document/type/field/expected-field-api';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import { AddKycFieldGroupDialog } from './add-kyc-field-group-dialog/add-kyc-field-group-dialog';
import { KycFieldGroupApi } from '@app/services/bw/co/knowvera/settings/kyc/kyc-field-group-api';
import { GroupFieldDTO } from '@app/models/bw/co/knowvera/settings/kyc/group-field-dto';
import { ToolSelectorDTO } from '@app/models/bw/co/knowvera/settings/tool-selector-dto';
import { Tool } from '@app/models/bw/co/knowvera/settings/tool';
import { AddToolSelectorDialog } from './add-tool-selector-dialog/add-tool-selector-dialog';

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
  textExtractionTools: Array<ToolSelectorDTO> = [];
  documentConfirmationTools: Array<ToolSelectorDTO> = [];
  textProcessingTools: Array<ToolSelectorDTO> = [];
  textCleanupTools: Array<ToolSelectorDTO> = [];
}

type ToolSelectorTarget =
  | 'textExtractionTools'
  | 'documentConfirmationTools'
  | 'textProcessingTools'
  | 'textCleanupTools';

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
  private readonly documentTypeApi = inject(DocumentTypeApi);
  private readonly documentApi = inject(DocumentApi);
  private readonly expectedFieldApi = inject(ExpectedFieldApi);
  private readonly kycFieldGroupApi = inject(KycFieldGroupApi);
  private readonly dialog = inject(MatDialog);

  readonly resourceLoading = signal(false);
  readonly availableDocumentTypes = signal<DocumentTypeDTO[]>([]);
  readonly availableTemplates = signal<DocumentDTO[]>([]);
  readonly documentTypePurpose = DocumentTypePurpose;
  readonly targetEntity = TargetEntity;
  readonly toolOptions = Object.values(Tool);

  readonly salaryRangeColumns = ['label', 'min', 'max', 'active', 'actions'];
  documentFilter = '';
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

      this.refreshKycExpectedFieldOptions(
        settings.indKycDocuments || [],
        settings.orgKycDocuments || [],
      );
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

    swalFire({
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
    const documentTypeId = this.selectedDocumentIdFor(purpose);

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
    const result = await swalFire({
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
        settings.salaryRanges?.length ||
        settings.textExtractionTools?.length ||
        settings.documentConfirmationTools?.length ||
        settings.textProcessingTools?.length ||
        settings.textCleanupTools?.length),
    );
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
    this.editSettingsSignal.update((value) => {
      switch (purpose) {
        case DocumentTypePurpose.INDIVIDUAL:
          return { ...value, selectedIndDocument: null };
        case DocumentTypePurpose.ORGANISATION:
          return { ...value, selectedOrgDocument: null };
        case DocumentTypePurpose.ORGANISATION_KYC:
          return { ...value, selectedKycOrgDocument: null };
        case DocumentTypePurpose.INDIVIDUAL_KYC:
          return { ...value, selectedKycIndDocument: null };
      }
    });
  }

  private asNumber(value: number | string | null | undefined): number | null {
    if (value === null || value === undefined || value === '') {
      return null;
    }

    const result = Number(value);
    return Number.isNaN(result) ? null : result;
  }


  updateSettingForm(settings: SettingsDTO) {

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
      textExtractionTools: this.normalizeToolSelectors(settings.textExtractionTools),
      documentConfirmationTools: this.normalizeToolSelectors(settings.documentConfirmationTools),
      textProcessingTools: this.normalizeToolSelectors(settings.textProcessingTools),
      textCleanupTools: this.normalizeToolSelectors(settings.textCleanupTools),
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
      const groupFields = [...(targetGroup.groupFields || [])];

      if (!this.includesExpectedField(groupFields, selectedField)) {
        targetGroup.groupFields = [...groupFields, selectedField];
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

  removeGroupField(isOrganisation: boolean, groupField: GroupFieldDTO, index: number): void {

    const targetType = isOrganisation ? TargetEntity.ORGANISATION : TargetEntity.INDIVIDUAL;
    const targetKey = isOrganisation ? 'organisationKycFieldGroups' : 'individualKycFieldGroups';

    let groups = isOrganisation ? this.editSettingsSignal().organisationKycFieldGroups : this.editSettingsSignal().individualKycFieldGroups;

    swalFire({
      title: 'Remove field group?',
      text: `This will remove the field group ${groupField.field} from ${isOrganisation ? 'organisation' : 'individual'} KYC settings.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Remove',
    }).then((result) => {
      console.log('Swal result', result);
      if (result.isConfirmed) {
        let group = groups[index];
        this.kycFieldGroupApi.removeField(group.id, groupField.id).subscribe({
          next: (group: KycFieldGroupDTO) => {

            this.editSettingsSignal.update((value) => {
              const groups = [...(value.individualKycFieldGroups || [])];
              const index = groups.findIndex((g) => g.id === group?.id || g === group);
              if (index >= 0) {
                groups[index] = group;
              }
              return { ...value, individualKycFieldGroups: groups };
            });
            this.toastr.success('Field group updated successfully');
          },
          error: (error) => {
            this.toastr.error(error?.error?.message || error?.message || 'Unable to remove field from group');
          }
        });
      }
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
          groupFields: (group.groupFields || []).filter(
            (item: any) => !this.sameExpectedField(item, field),
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
    settings.textExtractionTools = this.normalizeToolSelectors(value.textExtractionTools);
    settings.documentConfirmationTools = this.normalizeToolSelectors(value.documentConfirmationTools);
    settings.textProcessingTools = this.normalizeToolSelectors(value.textProcessingTools);
    settings.textCleanupTools = this.normalizeToolSelectors(value.textCleanupTools);
    settings.individualKycFieldGroups = this.normalizeKycGroups(
      value.individualKycFieldGroups,
      this.settings.individualKycFieldGroups,
      TargetEntity.INDIVIDUAL,
      'Individual KYC Expected Fields',
    );
    settings.organisationKycFieldGroups = this.normalizeKycGroups(
      value.organisationKycFieldGroups,
      this.settings.organisationKycFieldGroups,
      TargetEntity.ORGANISATION,
      'Organisation KYC Expected Fields',
    );

    return settings;
  }

  openAddToolSelectorDialog(target: ToolSelectorTarget): void {
    this.dialog.open(AddToolSelectorDialog, {
      width: 'min(96vw, 520px)',
      maxWidth: '100vw',
      data: {
        title: `Add ${this.toolSelectorLabelFor(target)}`,
        toolOptions: this.toolOptions,
      },
    }).afterClosed().subscribe((result: ToolSelectorDTO | null) => {
      if (!result) {
        return;
      }

      this.applyToolSelectorChange(target, (items) => [...items, result]);
    });
  }

  removeToolSelector(target: ToolSelectorTarget, index: number): void {
    this.applyToolSelectorChange(
      target,
      (items) => items.filter((_: ToolSelectorDTO, i: number) => i !== index),
    );
  }

  moveToolSelectorUp(target: ToolSelectorTarget, index: number): void {
    if (index <= 0) {
      return;
    }

    this.applyToolSelectorChange(target, (items) => {
      const reordered = [...items];
      [reordered[index - 1], reordered[index]] = [reordered[index], reordered[index - 1]];
      return reordered;
    });
  }

  moveToolSelectorDown(target: ToolSelectorTarget, index: number): void {
    this.applyToolSelectorChange(target, (items) => {
      if (index >= items.length - 1) {
        return items;
      }

      const reordered = [...items];
      [reordered[index], reordered[index + 1]] = [reordered[index + 1], reordered[index]];
      return reordered;
    });
  }

  private normalizeToolSelectors(tools: ToolSelectorDTO[] | null | undefined): ToolSelectorDTO[] {
    return (tools || []).map((tool, index) => {
      const normalized = new ToolSelectorDTO();
      normalized.id = tool?.id ?? null;
      normalized.tool = tool?.tool ?? null;
      normalized.position = index + 1;
      return normalized;
    });
  }

  private applyToolSelectorChange(
    target: ToolSelectorTarget,
    update: (items: ToolSelectorDTO[]) => ToolSelectorDTO[],
  ): void {
    let nextItems: ToolSelectorDTO[] = [];

    this.editSettingsSignal.update((value) => {
      const current = this.normalizeToolSelectors(value[target] || []);
      nextItems = this.normalizeToolSelectors(update(current));
      return {
        ...value,
        [target]: nextItems,
      } as EditSettingsVarsForm;
    });

    this.persistToolSelectorChanges();
  }

  private persistToolSelectorChanges(): void {
    const settings = this.getSettings(this.editSettingsSignal());
    this.loading.set(true);
    this.loaderMessage.set('Saving settings');
    this.settingsApiStore.save({ settings });
  }

  private toolSelectorLabelFor(target: ToolSelectorTarget): string {
    switch (target) {
      case 'textExtractionTools':
        return 'Text Extraction Tool';
      case 'documentConfirmationTools':
        return 'Document Confirmation Tool';
      case 'textProcessingTools':
        return 'Text Processing Tool';
      case 'textCleanupTools':
        return 'Text Cleanup Tool';
    }
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
    group.groupFields = this.uniqueExpectedFields(fields || []) as any;

    return [group];
  }

  private createKycGroup(targetType: TargetEntity, label: string): KycFieldGroupDTO {
    const group = new KycFieldGroupDTO();
    group.targetType = targetType;
    group.label = label;
    group.description = null;
    group.groupFields = [];
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
      normalized.groupFields = this.uniqueExpectedFields(group?.groupFields || []) as any;
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

  openAddFieldGroupDialog(): void {

    this.dialog.open(AddKycFieldGroupDialog, {
      width: 'min(96vw, 920px)',
      maxWidth: '100vw',
      data: {
        expectedFieldOptions: this.individualKycExpectedFieldOptions() || [],
        existingGroups: this.editSettingsSignal().individualKycFieldGroups || [],
        groupTypeLabel: 'Individual KYC Field Group',
        suiteLabel: 'Ind. KYC Suite',
        suiteDescription: 'individual KYC suite documents',
        targetType: TargetEntity.INDIVIDUAL,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        result.settingsId = this.editSettingsSignal().id;

        this.kycFieldGroupApi.save(result).subscribe({
          next: (savedGroup) => {
            this.editSettingsSignal.update((value) => ({
              ...value,
              individualKycFieldGroups: [...(value.individualKycFieldGroups || []), savedGroup]
            }));
            this.toastr.success('Field group created successfully');
          },
          error: (error) => {
            this.toastr.error(error?.error?.message || error?.message || 'Failed to create field group');
          }
        });
      }
    });
  }

  editFieldGroup(group?: KycFieldGroupDTO): void {

    this.dialog.open(AddKycFieldGroupDialog, {
      width: 'min(96vw, 920px)',
      maxWidth: '100vw',
      data: {
        expectedFieldOptions: this.individualKycExpectedFieldOptions() || [],
        existingGroups: this.editSettingsSignal().individualKycFieldGroups || [],
        editingGroup: group,
        groupTypeLabel: 'Individual KYC Field Group',
        suiteLabel: 'Ind. KYC Suite',
        suiteDescription: 'individual KYC suite documents',
        targetType: TargetEntity.INDIVIDUAL,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        this.kycFieldGroupApi.save(result).subscribe({
          next: (savedGroup) => {
            this.editSettingsSignal.update((value) => {
              const groups = [...(value.individualKycFieldGroups || [])];
              const index = groups.findIndex((g) => g.id === group?.id || g === group);
              if (index >= 0) {
                groups[index] = savedGroup;
              }
              return { ...value, individualKycFieldGroups: groups };
            });
            this.toastr.success('Field group updated successfully');
          },
          error: (error) => {
            this.toastr.error(error?.error?.message || error?.message || 'Failed to update field group');
          }
        });

      }
    });
  }

  deleteFieldGroup(group: KycFieldGroupDTO): void {

    let isOrg = group.targetType === TargetEntity.ORGANISATION;

    swalFire({
      title: 'Delete field group?',
      text: `This will remove the field group "${group.label || 'Individual KYC Group'}" and all its fields.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
    }).then((result) => {

      console.log('Deleting group', group, result);
      if (result.isConfirmed) {

        this.kycFieldGroupApi.remove(group.id || '').subscribe({
          next: () => {
            this.toastr.success('Field group removed successfully');

            if (!isOrg) {
              this.editSettingsSignal.update((value) => ({
                ...value,
                individualKycFieldGroups: (value.individualKycFieldGroups || []).filter((g) => g !== group)
              }));
            } else {
              this.editSettingsSignal.update((value) => ({
                ...value,
                organisationKycFieldGroups: (value.organisationKycFieldGroups || []).filter((g) => g !== group)
              }));
            }
          },
          error: (error) => {
            this.toastr.error(error?.error?.message || error?.message || 'Failed to remove field group');
          }
        });
      }
    });
  }

  openAddOrganisationFieldGroupDialog(): void {
    this.dialog.open(AddKycFieldGroupDialog, {
      width: 'min(96vw, 920px)',
      maxWidth: '100vw',
      data: {
        expectedFieldOptions: this.organisationKycExpectedFieldOptions() || [],
        existingGroups: this.editSettingsSignal().organisationKycFieldGroups || [],
        groupTypeLabel: 'Organisation KYC Field Group',
        suiteLabel: 'Org. KYC Suite',
        suiteDescription: 'organisation KYC suite documents',
        targetType: TargetEntity.ORGANISATION,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        result.settingsId = this.editSettingsSignal().id;

        this.kycFieldGroupApi.save(result).subscribe({
          next: (savedGroup) => {
            this.editSettingsSignal.update((value) => ({
              ...value,
              organisationKycFieldGroups: [...(value.organisationKycFieldGroups || []), savedGroup]
            }));
            this.toastr.success('Field group created successfully');
          },
          error: (error) => {
            this.toastr.error(error?.error?.message || error?.message || 'Failed to create field group');
          }
        });
      }
    });
  }

  editOrganisationFieldGroup(group: KycFieldGroupDTO): void {
    this.dialog.open(AddKycFieldGroupDialog, {
      width: 'min(96vw, 920px)',
      maxWidth: '100vw',
      data: {
        expectedFieldOptions: this.organisationKycExpectedFieldOptions() || [],
        existingGroups: this.editSettingsSignal().organisationKycFieldGroups || [],
        editingGroup: group,
        groupTypeLabel: 'Organisation KYC Field Group',
        suiteLabel: 'Org. KYC Suite',
        suiteDescription: 'organisation KYC suite documents',
        targetType: TargetEntity.ORGANISATION,
      }
    }).afterClosed().subscribe((result) => {
      if (result) {
        this.kycFieldGroupApi.save(result).subscribe({
          next: (savedGroup) => {
            this.editSettingsSignal.update((value) => {
              const groups = [...(value.organisationKycFieldGroups || [])];
              const index = groups.findIndex((g) => g.id === group.id || g === group);
              if (index >= 0) {
                groups[index] = savedGroup;
              }
              return { ...value, organisationKycFieldGroups: groups };
            });
            this.toastr.success('Field group updated successfully');
          },
          error: (error) => {
            this.toastr.error(error?.error?.message || error?.message || 'Failed to update field group');
          }
        });
      }
    });
  }

}

