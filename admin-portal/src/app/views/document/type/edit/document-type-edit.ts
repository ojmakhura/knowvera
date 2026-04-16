
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTabsModule } from '@angular/material/tabs';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  computed,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { form, required, applyEach, FormField } from '@angular/forms/signals';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { KeyField } from '@app/models/bw/co/centralkyc/key-field';
import { DocumentTypeApiStore } from '@app/store/bw/co/centralkyc/document/type/document-type-api.store';
import Swal from 'sweetalert2';
import { Loader } from '@app/@shared/loader/loader';
import { TranslateModule } from '@ngx-translate/core';
import { ExpectedFieldDTO } from '@app/models/bw/co/centralkyc/document/type/field/expected-field-dto';
import { VerificationDataConfigDTO } from '@app/models/bw/co/centralkyc/document/type/verification/verification-data-config-dto';
import { ToastrService } from 'ngx-toastr';
import { PromptMessage } from '@app/models/bw/co/centralkyc/llm/prompt-message';
import { ExpectedFieldType } from '@app/models/bw/co/centralkyc/document/type/field/expected-field-type';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { IndividualDTO } from '@app/models/bw/co/centralkyc/individual/individual-dto';
import { OrganisationDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-dto';
import { KycInvoiceDTO } from '@app/models/bw/co/centralkyc/invoice/kyc-invoice-dto';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { ContactDTO } from '@app/models/bw/co/centralkyc/contact/contact-dto';
import { SettingsDTO } from '@app/models/bw/co/centralkyc/settings/settings-dto';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';

export class EditDocumentTypeVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  code: string | any = null;
  name: string | any = null;
  description: string | any = null;
  expires: boolean | any = false;
  expiryField: KeyField | any = null;
  expectedFields: Array<ExpectedFieldDTO> = [];
  validationPrompts: Array<PromptMessage> = [];
  textExtractionPrompts: Array<PromptMessage> = [];
  verificationDataConfigs: VerificationDataConfigDTO[] = [];
}

@Component({
  selector: 'app-document-type-edit',
  templateUrl: './document-type-edit.html',
  styleUrls: ['./document-type-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatCheckboxModule,
    MatTabsModule,
    Loader,
    FormField,
    TranslateModule
  ],
})
export class DocumentTypeEdit implements OnInit, AfterViewInit, OnDestroy {
  @Input() id: string = '';
  protected readonly keyFieldOptions = Object.values(KeyField);
  protected readonly promptRoleOptions = ['system', 'user', 'assistant'];
  // protected readonly verificationTagOptions = Object.values(VerificationTag);
  protected readonly verificationDataConfigIndex = signal(0);

  editDocumentTypeVarsForm: EditDocumentTypeVarsForm = new EditDocumentTypeVarsForm();
  editDocumentTypeSignal = signal(this.editDocumentTypeVarsForm);
  editDocumentTypeSignalForm = form(this.editDocumentTypeSignal, (path) => {
    required(path.code, { message: 'code.required' });
    required(path.name, { message: 'name.required' });

    if (this.editDocumentTypeSignal().expectedFields.length > 1) {
      applyEach(path.expectedFields, (fieldPath) => {
        // required(fieldPath.keyField, { message: 'keyField.required' });
        required(fieldPath.mandatory, { message: 'mandatory.required' });
        required(fieldPath.field, { message: 'field.required' });
      });
    }
  });

  documentTypeApiStore = inject(DocumentTypeApiStore);

  loading = linkedSignal(() => this.documentTypeApiStore.loading());
  loaderMessage = linkedSignal(() => this.documentTypeApiStore.loaderMessage());
  success = linkedSignal(() => this.documentTypeApiStore.success());
  error = linkedSignal(() => this.documentTypeApiStore.error());
  messages = linkedSignal(() => this.documentTypeApiStore.messages());

  toastr = inject(ToastrService);
  dialog = inject(MatDialog);

  // protected readonly documentTypeKeyFields = linkedSignal(() => this.editDocumentTypeSignal().expectedFields.map((field) => field.field).filter((keyField): keyField is KeyField => !!keyField));

  constructor() {

    effect(() => {

      let docType = this.documentTypeApiStore.data();

      if (docType) {
        this.editDocumentTypeSignal.set(this.updateDocumentTypeSignal(docType));
      }
    });

    effect(() => {

      const error = this.error();
      console.log('Error state changed:', error);

      if (error) {
        console.log('Error messages:', this.messages());
        this.toastr.error(this.messages()[0] || 'An error occurred while saving the document type.');
      }

    });

    effect(() => {

      const success = this.success();

      if (success) {
        this.toastr.success(this.messages()[0] || 'Document type saved successfully.');
      }

    });
  }

  ngOnInit(): void {

    this.documentTypeApiStore.reset();

    if (this.id && this.id !== '') {

      this.documentTypeApiStore.findById({ id: this.id });
    }

  }

  ngAfterViewInit(): void { }

  ngOnDestroy(): void { }

  discardChanges(): void {
    const current = this.documentTypeApiStore.data();

    if (current?.id) {
      this.editDocumentTypeSignal.set(this.updateDocumentTypeSignal(current));
      return;
    }

    this.editDocumentTypeSignal.set(new EditDocumentTypeVarsForm());
  }

  updateField<K extends keyof EditDocumentTypeVarsForm>(field: K, value: EditDocumentTypeVarsForm[K]): void {
    this.editDocumentTypeSignal.update((state) => ({
      ...state,
      [field]: value,
    }));
  }

  createNewExpectedFields(): ExpectedFieldDTO {
    let field = new ExpectedFieldDTO();
    field.documentTypeId = this.editDocumentTypeSignal().id;
    field.documentType = this.editDocumentTypeSignal().name;
    return field;
  }

  expectedFieldsAdd() {
    const dialogRef = this.dialog.open(ExpectedFieldDialogComponent, {
      width: '760px',
      maxWidth: '95vw',
      data: {
        title: 'Add Expected Field',
        field: this.createNewExpectedFields(),
      },
    });

    dialogRef.afterClosed().subscribe((result?: ExpectedFieldDTO) => {
      if (!result) {
        return;
      }

      this.editDocumentTypeSignal.update((value) => ({
        ...value,
        expectedFields: [result, ...value.expectedFields],
      }));
    });
  }

  expectedFieldsEdit(index: number, selected: ExpectedFieldDTO): void {
    const dialogRef = this.dialog.open(ExpectedFieldDialogComponent, {
      width: '760px',
      maxWidth: '95vw',
      data: {
        title: 'Edit Expected Field',
        field: {
          ...selected,
        },
      },
    });

    dialogRef.afterClosed().subscribe((result?: ExpectedFieldDTO) => {
      if (!result) {
        return;
      }

      this.editDocumentTypeSignal.update((value) => ({
        ...value,
        expectedFields: value.expectedFields.map((item, itemIndex) => itemIndex === index ? result : item),
      }));
    });
  }

  expectedFieldsRemove(i: number, selected: ExpectedFieldDTO) {

    Swal.fire({
      title: 'Are you sure?',
      text: `This will remove the field "${selected.field}" from the expected fields list.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editDocumentTypeSignal.update((value) => {
          const expectedFields = value.expectedFields.filter((_: any, index: number) => index !== i);

          return {
            ...value,
            expectedFields: expectedFields
          }
        });
      }
    });
  }

  updateExpectedField(index: number, field: keyof ExpectedFieldDTO, value: any): void {
    this.editDocumentTypeSignal.update((state) => ({
      ...state,
      expectedFields: state.expectedFields.map((item, itemIndex) => {
        if (itemIndex !== index) {
          return item;
        }

        return {
          ...item,
          [field]: value,
        };
      }),
    }));
  }

  onExpectedFieldKeyFieldChange(index: number, event: Event): void {
    const value = (event.target as HTMLSelectElement).value;

    // this.updateExpectedField(index, 'keyField', value);
  }

  createNewValidationPrompts(): PromptMessage {
    return new PromptMessage();
  }

  validationPromptsAdd() {

    this.editDocumentTypeSignal.update((value) => ({
      ...value,
      validationPrompts: [
        ...value.validationPrompts,
        this.createNewValidationPrompts()
      ]
    }))
  }

  validationPromptsRemove(i: number, selected: PromptMessage) {

    Swal.fire({
      title: 'Are you sure?',
      text: `This will remove the prompt with role "${selected.role}" from the validation prompts list.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editDocumentTypeSignal.update((value) => {
          const validationPrompts = value.validationPrompts.filter((_: any, index: number) => index !== i);

          return {
            ...value,
            validationPrompts: validationPrompts
          }
        });
      }
    });
  }

  createNewTextExtractionPrompts(): PromptMessage {
    return new PromptMessage();
  }

  textExtractionPromptsAdd() {

    this.editDocumentTypeSignal.update((value) => ({
      ...value,
      textExtractionPrompts: [
        ...value.textExtractionPrompts,
        this.createNewTextExtractionPrompts()
      ]
    }))
  }

  createNewVerificationDataConfig(): VerificationDataConfigDTO {
    const config = new VerificationDataConfigDTO();
    config.documentTypeId = this.editDocumentTypeSignal().id;
    config.documentType = this.editDocumentTypeSignal().name;
    return config;
  }

  verificationDataConfigsAdd(): void {
    this.editDocumentTypeSignal.update((value) => ({
      ...value,
      verificationDataConfigs: [
        this.createNewVerificationDataConfig(),
        ...value.verificationDataConfigs,
      ],
    }));

    this.verificationDataConfigIndex.set(0);
  }

  verificationDataConfigsRemove(i: number, selected: VerificationDataConfigDTO): void {
    Swal.fire({
      title: 'Are you sure?',
      text: `This will remove the verification data config "${selected.name || 'Untitled'}".`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editDocumentTypeSignal.update((value) => {
          const verificationDataConfigs = value.verificationDataConfigs.filter((_: any, index: number) => index !== i);

          const nextIndex = Math.max(0, Math.min(this.verificationDataConfigIndex(), verificationDataConfigs.length - 1));
          this.verificationDataConfigIndex.set(nextIndex);

          return {
            ...value,
            verificationDataConfigs,
          };
        });
      }
    });
  }

  // updateVerificationDataConfig(index: number, field: keyof VerificationDataConfigDTO, value: any): void {
  //   this.editDocumentTypeSignal.update((state) => ({
  //     ...state,
  //     verificationDataConfigs: state.verificationDataConfigs.map((item, itemIndex) => {
  //       if (itemIndex !== index) {
  //         return item;
  //       }

  //       return {
  //         ...item,
  //         [field]: value,
  //       };
  //     }),
  //   }));
  // }

  isVerificationDataConfigKeySelected(configIndex: number, keyField: KeyField): boolean {
    return this.editDocumentTypeSignal().verificationDataConfigs[configIndex]?.keyFields?.includes(keyField) ?? false;
  }

  verificationDataConfigCount(): number {
    return this.editDocumentTypeSignal().verificationDataConfigs.length;
  }

  currentVerificationDataConfigIndex(): number {
    const count = this.verificationDataConfigCount();

    if (count === 0) {
      return 0;
    }

    return Math.max(0, Math.min(this.verificationDataConfigIndex(), count - 1));
  }

  canGoToPreviousVerificationDataConfig(): boolean {
    return this.currentVerificationDataConfigIndex() > 0;
  }

  canGoToNextVerificationDataConfig(): boolean {
    return this.currentVerificationDataConfigIndex() < this.verificationDataConfigCount() - 1;
  }

  goToPreviousVerificationDataConfig(): void {
    if (!this.canGoToPreviousVerificationDataConfig()) {
      return;
    }

    this.verificationDataConfigIndex.update((index) => Math.max(0, index - 1));
  }

  goToNextVerificationDataConfig(): void {
    if (!this.canGoToNextVerificationDataConfig()) {
      return;
    }

    this.verificationDataConfigIndex.update((index) => Math.min(this.verificationDataConfigCount() - 1, index + 1));
  }

  toggleVerificationDataConfigKey(configIndex: number, keyField: KeyField, checked: boolean): void {
    this.editDocumentTypeSignal.update((state) => ({
      ...state,
      verificationDataConfigs: state.verificationDataConfigs.map((config, index) => {
        if (index !== configIndex) {
          return config;
        }

        const selectedKeyFields = Array.isArray(config.keyFields) ? config.keyFields : [];
        const keyFields = checked
          ? selectedKeyFields.includes(keyField)
            ? selectedKeyFields
            : [...selectedKeyFields, keyField]
          : selectedKeyFields.filter((item: KeyField) => item !== keyField);

        return {
          ...config,
          keyFields,
        };
      }),
    }));
  }

  // isVerificationTagSelected(tag: VerificationTag): boolean {
  //   return this.editDocumentTypeSignal().verificationTags.includes(tag);
  // }

  // toggleVerificationTag(tag: VerificationTag, checked: boolean): void {
  //   this.editDocumentTypeSignal.update((state) => {
  //     const verificationTags = checked
  //       ? state.verificationTags.includes(tag)
  //         ? state.verificationTags
  //         : [...state.verificationTags, tag]
  //       : state.verificationTags.filter((item) => item !== tag);

  //     return {
  //       ...state,
  //       verificationTags,
  //     };
  //   });
  // }

  textExtractionPromptsRemove(i: number, selected: PromptMessage) {
    Swal.fire({
      title: 'Are you sure?',
      text: `This will remove the prompt with role "${selected.role}" from the text extraction prompts list.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editDocumentTypeSignal.update((value) => {
          const textExtractionPrompts = value.textExtractionPrompts.filter((_: any, index: number) => index !== i);

          return {
            ...value,
            textExtractionPrompts: textExtractionPrompts
          }
        });
      }
    });
  }

  updateTextExtractionPrompt(index: number, field: keyof PromptMessage, value: any): void {
    this.editDocumentTypeSignal.update((state) => ({
      ...state,
      textExtractionPrompts: state.textExtractionPrompts.map((item, itemIndex) => {
        if (itemIndex !== index) {
          return item;
        }

        return {
          ...item,
          [field]: value,
        };
      }),
    }));
  }

  updateDocumentTypeSignal(documentType: DocumentTypeDTO): EditDocumentTypeVarsForm {
    return {
      code: documentType.code,
      createdAt: documentType.createdAt,
      createdBy: documentType.createdBy,
      description: documentType.description,
      expires: documentType.expires,
      expiryField: documentType.expiryField,
      id: documentType.id,
      modifiedAt: documentType.modifiedAt,
      modifiedBy: documentType.modifiedBy,
      name: documentType.name,
      expectedFields: documentType.expectedFields || [],
      textExtractionPrompts: documentType.textExtractionPrompts || [],
      validationPrompts: documentType.validationPrompts || [],
      verificationDataConfigs: documentType.verificationDataConfigs || [],
    };
  }

  saveDocumentType(): void {

    let formData: EditDocumentTypeVarsForm = this.editDocumentTypeSignal();
    let docType = new DocumentTypeDTO();
    docType.code = formData.code;
    docType.createdAt = formData.createdAt;
    docType.createdBy = formData.createdBy;
    docType.description = formData.description;
    docType.expires = formData.expires;
    docType.expiryField = formData.expiryField;
    docType.id = formData.id;
    docType.modifiedAt = formData.modifiedAt;
    docType.modifiedBy = formData.modifiedBy;
    docType.name = formData.name;
    docType.expectedFields = formData.expectedFields || [];
    docType.textExtractionPrompts = formData.textExtractionPrompts || [];
    docType.validationPrompts = formData.validationPrompts || [];
    docType.verificationDataConfigs = formData.verificationDataConfigs || [];

    this.documentTypeApiStore.save({
      documentType: docType
    });
  }

  trackByIndex(index: number): number {
    return index;
  }

  displayExpectedFieldType(value: ExpectedFieldType | null | undefined): string {
    return value ? String(value) : '-';
  }

  displayTargetType(value: TargetEntity | null | undefined): string {
    return value ? String(value) : '-';
  }
}

interface ExpectedFieldDialogData {
  title?: string;
  field: ExpectedFieldDTO;
}

const MATCH_TO_EXCLUDED_FIELDS = new Set([
  'id',
  'createdBy',
  'createdAt',
  'modifiedBy',
  'modifiedAt',
]);

const EXCLUDED_TARGET_TYPES = new Set<TargetEntity>([
  TargetEntity.BRANCH,
  TargetEntity.SUBSCRIPTION,
  TargetEntity.CLIENT_REQUEST,
  TargetEntity.DOCUMENT,
  TargetEntity.SETTINGS
]);

const getDtoMatchToFields = (dto: object): string[] =>
  Object.keys(dto).filter((field) => !MATCH_TO_EXCLUDED_FIELDS.has(field));

const TARGET_ENTITY_MATCH_TO_FACTORIES: Partial<Record<TargetEntity, () => string[]>> = {
  [TargetEntity.INDIVIDUAL]: () => getDtoMatchToFields(new IndividualDTO()),
  [TargetEntity.ORGANISATION]: () => getDtoMatchToFields(new OrganisationDTO()),
  [TargetEntity.INVOICE]: () => getDtoMatchToFields(new KycInvoiceDTO()),
  [TargetEntity.KYC_RECORD]: () => getDtoMatchToFields(new KycRecordDTO()),
  [TargetEntity.CONTACT]: () => getDtoMatchToFields(new ContactDTO()),
  [TargetEntity.SETTINGS]: () => getDtoMatchToFields(new SettingsDTO()),
};

@Component({
  selector: 'app-expected-field-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    FormField,
    TranslateModule
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2 mat-dialog-title>{{ data.title || 'Expected Field' }}</h2>

    <mat-dialog-content>
      <div class="dialog-form-grid">
        <mat-form-field appearance="outline">
          <mat-label>Field Name</mat-label>
          <input matInput [formField]="expectedFieldForm.field" placeholder="e.g. documentNumber" />
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Field Type</mat-label>
          <mat-select [formField]="expectedFieldForm.fieldType">
            @for (option of fieldTypeOptions; track option) {
            <mat-option [value]="option">
              <span translate>expected.field.type.{{ option }}</span>
            </mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Match To</mat-label>
          <mat-select [formField]="expectedFieldForm.matchTo">
            @if (matchToOptions().length === 0) {
            <mat-option [value]="null" disabled>Select target type first</mat-option>
            } @else {
            @for (option of matchToOptions(); track option) {
            <mat-option [value]="option">{{ option }}</mat-option>
            }
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Target Type</mat-label>
          <mat-select [formField]="expectedFieldForm.targetType">
            @for (option of targetTypeOptions; track option) {
            <mat-option [value]="option">
              <span translate>target.entity.{{ option }}</span>
            </mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Target Field</mat-label>
          <input matInput [formField]="expectedFieldForm.targetField" placeholder="e.g. identityNumber" />
        </mat-form-field>

        <mat-form-field appearance="outline" class="format-field">
          <mat-label>Format</mat-label>
          <textarea matInput [formField]="expectedFieldForm.format" placeholder="e.g. dd/MM/yyyy"></textarea>
        </mat-form-field>

        <div class="checkbox-row">
          <mat-checkbox [formField]="expectedFieldForm.mandatory">Mandatory</mat-checkbox>
          <mat-checkbox [formField]="expectedFieldForm.many">Many</mat-checkbox>
        </div>
      </div>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-stroked-button type="button" (click)="onCancel()">Cancel</button>
      <button mat-flat-button color="primary" type="button" [disabled]="!expectedFieldForm().valid()" (click)="onSave()">
        Save
      </button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      mat-dialog-content {
        min-width: 680px;
      }

      .dialog-form-grid {
        display: grid;
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 12px;
        padding-top: 8px;
      }

      mat-form-field {
        width: 100%;
      }

      .format-field {
        grid-column: 1 / -1;
      }

      .format-field textarea {
        min-height: 88px;
        resize: vertical;
      }

      .checkbox-row {
        grid-column: 1 / -1;
        display: flex;
        gap: 18px;
        padding: 4px 2px;
      }

      @media (max-width: 760px) {
        mat-dialog-content {
          min-width: 100%;
        }

        .dialog-form-grid {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class ExpectedFieldDialogComponent {
  private dialogRef = inject(MatDialogRef<ExpectedFieldDialogComponent>);
  data: ExpectedFieldDialogData = inject(MAT_DIALOG_DATA);

  protected readonly fieldTypeOptions = Object.values(ExpectedFieldType);
  protected readonly targetTypeOptions = Object.values(TargetEntity).filter((targetType) => !EXCLUDED_TARGET_TYPES.has(targetType));
  protected readonly matchToOptions = computed(() => {
    const targetType = this.expectedFieldSignal().targetType as TargetEntity | null | undefined;

    if (!targetType) {
      return [];
    }

    return TARGET_ENTITY_MATCH_TO_FACTORIES[targetType]?.() ?? [];
  });

  expectedFieldSignal = signal<ExpectedFieldDTO>(this.data.field);
  expectedFieldForm = form(this.expectedFieldSignal, (path) => {
    required(path.field);
  });

  constructor() {
    effect(() => {
      const matchTo = this.expectedFieldSignal().matchTo;
      const options = this.matchToOptions();

      if (!matchTo || options.includes(matchTo)) {
        return;
      }

      this.expectedFieldSignal.update((field) => ({
        ...field,
        matchTo: null,
      }));
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (!this.expectedFieldForm().valid()) {
      return;
    }

    this.dialogRef.close(this.expectedFieldSignal());
  }
}
