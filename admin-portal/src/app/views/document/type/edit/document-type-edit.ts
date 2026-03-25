import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { form, required, FormField } from '@angular/forms/signals';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { ExpectedField } from '@app/models/bw/co/centralkyc/document/type/expected-field';
import { KeyField } from '@app/models/bw/co/centralkyc/key-field';
import { CompletionRequestMessage } from '@app/models/bw/co/centralkyc/lmstudio/completion-request-message';
import { DocumentTypeApiStore } from '@app/store/bw/co/centralkyc/document/type/document-type-api.store';
import Swal from 'sweetalert2';

export class EditDocumentTypeVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  code: string | any = null;
  name: string | any = null;
  description: string | any = null;
  expectedFields: Array<ExpectedField> = [];
  validationPrompts: Array<CompletionRequestMessage> = [];
  textExtractionPrompts: Array<CompletionRequestMessage> = [];
}

@Component({
  selector: 'app-document-type-edit',
  templateUrl: './document-type-edit.html',
  styleUrls: ['./document-type-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormField],
})
export class DocumentTypeEdit implements OnInit, AfterViewInit, OnDestroy {
  @Input() id: string = '';
  protected readonly keyFieldOptions = Object.values(KeyField);

  editDocumentTypeVarsForm: EditDocumentTypeVarsForm = new EditDocumentTypeVarsForm();
  editDocumentTypeSignal = signal(this.editDocumentTypeVarsForm);
  editDocumentTypeSignalForm = form(this.editDocumentTypeSignal, (path) => {
    required(path.code, { message: 'code.required' });
    required(path.name, { message: 'name.required' });
  });

  documentTypeApiStore = inject(DocumentTypeApiStore);

  loading = linkedSignal(() => false);

  constructor() {

    effect(() => {

      let docType = this.documentTypeApiStore.data();
      
      if(docType) {
        this.editDocumentTypeSignal.set(this.updateDocumentTypeSignal(docType));
      }
    });
  }

  ngOnInit(): void {

    if(this.id && this.id !== '') {

      this.documentTypeApiStore.findById({ id: this.id });
    }

  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}


  createNewExpectedFields(): ExpectedField {
    return new ExpectedField();
  }

  expectedFieldsAdd() {

    this.editDocumentTypeSignal.update((value) => ({
      ...value,
      expectedFields: [
        ...value.expectedFields,
        this.createNewExpectedFields()
      ]
    }))
  }

  expectedFieldsRemove(i: number, selected: ExpectedField) {
    
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

  onExpectedFieldKeyFieldChange(index: number, event: Event): void {
    const value = (event.target as HTMLSelectElement).value;

    this.editDocumentTypeSignal.update((state) => ({
      ...state,
      expectedFields: state.expectedFields.map((item, itemIndex) =>
        itemIndex === index ? { ...item, keyField: value } : item
      ),
    }));
  }

  createNewValidationPrompts(): CompletionRequestMessage {
    return new CompletionRequestMessage();
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

  validationPromptsRemove(i: number, selected: CompletionRequestMessage) {
    this.editDocumentTypeSignal.update((value) => {
      const validationPrompts = value.validationPrompts.filter((_: any, index: number) => index !== i);

      return {
        ...value,
        validationPrompts: validationPrompts
      }
    });
  }

  createNewTextExtractionPrompts(): CompletionRequestMessage {
    return new CompletionRequestMessage();
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

  textExtractionPromptsRemove(i: number, selected: CompletionRequestMessage) {
    this.editDocumentTypeSignal.update((value) => {
      const textExtractionPrompts = value.textExtractionPrompts.filter((_: any, index: number) => index !== i);

      return {
        ...value,
        textExtractionPrompts: textExtractionPrompts
      }
    });
  }

  updateDocumentTypeSignal(documentType: DocumentTypeDTO): EditDocumentTypeVarsForm {
    return {
      code: documentType.code,
      createdAt: documentType.createdAt,
      createdBy: documentType.createdBy,
      description: documentType.description,
      id: documentType.id,
      modifiedAt: documentType.modifiedAt,
      modifiedBy: documentType.modifiedBy,
      name: documentType.name,
      expectedFields: documentType.expectedFields || [],
      textExtractionPrompts: documentType.textExtractionPrompts || [],
      validationPrompts: documentType.validationPrompts || [],
    };
  }

  saveDocumentType(): void {
    this.loading.set(true);

    let formData: EditDocumentTypeVarsForm = this.editDocumentTypeSignal();
    let docType = new DocumentTypeDTO();
    docType.code = formData.code;
    docType.createdAt = formData.createdAt;
    docType.createdBy = formData.createdBy;
    docType.description = formData.description;
    docType.id = formData.id;
    docType.modifiedAt = formData.modifiedAt;
    docType.modifiedBy = formData.modifiedBy;
    docType.name = formData.name;
    docType.expectedFields = formData.expectedFields || [];
    docType.textExtractionPrompts = formData.textExtractionPrompts || [];
    docType.validationPrompts = formData.validationPrompts || [];

    // this.documentTypeApi.save(docType).subscribe({
    //   next: (documentType: DocumentTypeDTO) => {
    //     this.editDocumentTypeSignal.set(this.updateDocumentTypeSignal(documentType));
    //     this.loading.set(false);
    //   },
    //   error: (error) => {
    //     console.log(error);
    //     this.toaster.error(
    //       error.error?.message ? error.error.message : error.message
    //     )
    //     this.loading.set(false);
    //   }
    // });
  }
}
