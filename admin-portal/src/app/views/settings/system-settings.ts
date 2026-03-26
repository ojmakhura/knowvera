import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, signal } from '@angular/core';
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

type PendingAction = 'save' | 'attach' | 'detach' | null;
type SettingsDocumentListKey =
  | 'individualDocuments'
  | 'organisationDocuments'
  | 'orgKycDocuments'
  | 'indKycDocuments';

type AccountCreationOption = {
  value: number;
  label: string;
};

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
  ],
})
export class SystemSettings {
  private readonly destroyRef = inject(DestroyRef);
  private readonly toastr = inject(ToastrService);
  private readonly settingsApiStore = inject(SettingsApiStore);
  private readonly documentTypeApi = inject(DocumentTypeApi);
  private readonly documentApi = inject(DocumentApi);

  readonly resourceLoading = signal(false);
  readonly availableDocumentTypes = signal<DocumentTypeDTO[]>([]);
  readonly availableTemplates = signal<DocumentDTO[]>([]);
  readonly pendingAction = signal<PendingAction>(null);
  readonly documentTypePurpose = DocumentTypePurpose;
  readonly targetEntity = TargetEntity;

  readonly salaryRangeColumns = ['label', 'min', 'max', 'active', 'actions'];
  readonly accountCreationOptions: AccountCreationOption[] = [
    { value: 0, label: 'Instant (Automated)' },
    { value: 1, label: 'Within 24 Hours' },
    { value: 2, label: 'Manual Review Required' },
  ];

  settingsForm: SettingsDTO = new SettingsDTO();
  documentFilter = '';
  selectedIndividualDocumentId: string | null = null;
  selectedOrganisationDocumentId: string | null = null;
  selectedOrganisationKycDocumentId: string | null = null;
  selectedIndividualKycDocumentId: string | null = null;
  roleOptions: string[] = ['SUPER_ADMIN', 'ADMIN', 'COMPLIANCE_ADMIN', 'VERIFIED_USER', 'USER'];

  constructor() {
    effect(() => {
      const settings = this.settingsApiStore.data();

      if (!this.hasSettingsPayload(settings)) {
        return;
      }

      this.settingsForm = this.cloneSettings(settings);
      this.roleOptions = this.buildRoleOptions(settings);
    });

    effect(() => {
      const action = this.pendingAction();

      if (!action || this.settingsApiStore.loading()) {
        return;
      }

      const messages = this.settingsApiStore.messages();
      if (this.settingsApiStore.success()) {
        this.toastr.success(messages[0] || this.defaultSuccessMessage(action));
      } else if (this.settingsApiStore.error()) {
        this.toastr.error(messages[0] || 'Unable to update system settings.');
      }

      this.pendingAction.set(null);
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.reset();
    this.settingsApiStore.getAll();
    this.loadReferenceData();
  }

  get isLoading(): boolean {
    return this.settingsApiStore.loading() || this.resourceLoading();
  }

  get accountCreationLabel(): string {
    return (
      this.accountCreationOptions.find(
        (option) => option.value === (this.settingsForm.timeToAccountCreation ?? 0),
      )?.label || 'Instant (Automated)'
    );
  }

  get lastVerifiedAt(): string {
    return this.formatAuditDate(this.settingsForm.modifiedAt || this.settingsForm.createdAt);
  }

  compareDocumentType = (left: DocumentTypeDTO | null, right: DocumentTypeDTO | null): boolean =>
    (left?.id || null) === (right?.id || null);

  compareTemplate = (left: DocumentDTO | null, right: DocumentDTO | null): boolean =>
    (left?.id || null) === (right?.id || null);

  resetForm(): void {
    this.settingsForm = this.cloneSettings(this.settingsApiStore.data());
  }

  saveSettings(): void {
    if (!this.isFormValid()) {
      this.toastr.error('Complete the required settings fields before saving.');
      return;
    }

    this.pendingAction.set('save');
    this.settingsApiStore.save({ setttings: this.cloneSettings(this.settingsForm) });
  }

  exportAuditLog(): void {
    const payload = {
      exportedAt: new Date().toISOString(),
      audit: {
        createdAt: this.settingsForm.createdAt,
        createdBy: this.settingsForm.createdBy,
        modifiedAt: this.settingsForm.modifiedAt,
        modifiedBy: this.settingsForm.modifiedBy,
      },
      settings: this.cloneSettings(this.settingsForm),
    };

    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = 'system-settings-audit.json';
    anchor.click();
    window.URL.revokeObjectURL(url);
  }

  addSalaryRange(): void {
    const lastRange = this.settingsForm.salaryRanges[this.settingsForm.salaryRanges.length - 1];
    const nextMin = this.asNumber(lastRange?.max) ?? this.asNumber(lastRange?.min) ?? 0;

    const range = new SalaryRangeDTO();
    range.min = nextMin;
    range.max = nextMin + 50000;
    range.active = true;

    this.settingsForm.salaryRanges = [...this.settingsForm.salaryRanges, range];
  }

  removeSalaryRange(index: number): void {
    this.settingsForm.salaryRanges = this.settingsForm.salaryRanges.filter(
      (_range: SalaryRangeDTO, itemIndex: number) => itemIndex !== index,
    );
  }

  addDocumentRequirement(purpose: DocumentTypePurpose): void {
    const documentTypeId = this.selectedDocumentIdFor(purpose);

    if (!documentTypeId) {
      return;
    }

    this.pendingAction.set('attach');
    this.settingsApiStore.attachDocumentType({ documentTypeId, purpose });
    this.clearSelectedDocumentId(purpose);
  }

  async removeDocumentRequirement(
    purpose: DocumentTypePurpose,
    documentType: DocumentTypeDTO,
  ): Promise<void> {
    const result = await Swal.fire({
      title: 'Remove document requirement?',
      text: `This will detach ${this.documentTypeDisplay(documentType)} from the selected settings group.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Detach',
      cancelButtonText: 'Cancel',
    });

    if (!result.isConfirmed) {
      return;
    }

    this.pendingAction.set('detach');
    this.settingsApiStore.detachDocumentType({ documentTypeId: documentType.id, purpose });
  }

  filteredDocuments(key: SettingsDocumentListKey): DocumentTypeDTO[] {
    const filterValue = this.documentFilter.trim().toLowerCase();
    const items = this.documentList(key);

    if (!filterValue) {
      return items;
    }

    return items.filter((item) => this.documentTypeDisplay(item).toLowerCase().includes(filterValue));
  }

  availableDocumentOptions(purpose: DocumentTypePurpose): DocumentTypeDTO[] {
    const usedIds = new Set(this.documentList(this.keyForPurpose(purpose)).map((item) => item.id));
    return this.availableDocumentTypes().filter((item) => !usedIds.has(item.id));
  }

  templatesFor(target: TargetEntity): DocumentDTO[] {
    return this.availableTemplates().filter((template) => template.target === target);
  }

  documentTypeDisplay(documentType: DocumentTypeDTO | null | undefined): string {
    if (!documentType) {
      return 'Unknown document type';
    }

    return documentType.name || documentType.code || 'Unnamed document type';
  }

  templateDisplay(template: DocumentDTO | null | undefined): string {
    if (!template) {
      return 'No template selected';
    }

    return template.fileName || template.documentType || template.targetLabel || template.id || 'Template';
  }

  salaryRangeLabel(index: number): string {
    return `Band ${String(index + 1).padStart(2, '0')}`;
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

    return clone;
  }

  private buildRoleOptions(settings: SettingsDTO): string[] {
    const baseRoles = ['SUPER_ADMIN', 'ADMIN', 'COMPLIANCE_ADMIN', 'VERIFIED_USER', 'USER'];
    return Array.from(
      new Set([...baseRoles, settings.organisationAdminRole, settings.normalUserRole].filter(Boolean)),
    );
  }

  private isFormValid(): boolean {
    return Boolean(
      this.settingsForm.platformName &&
        this.settingsForm.platformUrl &&
        this.settingsForm.kycPortalLink &&
        this.settingsForm.supportContact &&
        this.settingsForm.organisationAdminRole &&
        this.settingsForm.normalUserRole,
    );
  }

  private defaultSuccessMessage(action: Exclude<PendingAction, null>): string {
    switch (action) {
      case 'attach':
        return 'Document requirement attached successfully.';
      case 'detach':
        return 'Document requirement detached successfully.';
      case 'save':
        return 'System settings saved successfully.';
    }
  }

  private documentList(key: SettingsDocumentListKey): DocumentTypeDTO[] {
    return this.settingsForm[key] || [];
  }

  private keyForPurpose(purpose: DocumentTypePurpose): SettingsDocumentListKey {
    switch (purpose) {
      case DocumentTypePurpose.INDIVIDUAL:
        return 'individualDocuments';
      case DocumentTypePurpose.ORGANISATION:
        return 'organisationDocuments';
      case DocumentTypePurpose.ORGANISATION_KYC:
        return 'orgKycDocuments';
      case DocumentTypePurpose.INDIVIDUAL_KYC:
        return 'indKycDocuments';
    }
  }

  private selectedDocumentIdFor(purpose: DocumentTypePurpose): string | null {
    switch (purpose) {
      case DocumentTypePurpose.INDIVIDUAL:
        return this.selectedIndividualDocumentId;
      case DocumentTypePurpose.ORGANISATION:
        return this.selectedOrganisationDocumentId;
      case DocumentTypePurpose.ORGANISATION_KYC:
        return this.selectedOrganisationKycDocumentId;
      case DocumentTypePurpose.INDIVIDUAL_KYC:
        return this.selectedIndividualKycDocumentId;
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
}
