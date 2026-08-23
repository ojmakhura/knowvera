// views/settings/template-mappings/template-mappings.ts
import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject, linkedSignal, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/knowvera/document/type/document-type-api.store';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { TemplateMappings as TemplateMappingsDTO } from '@app/models/bw/co/knowvera/settings/template-mappings';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { LoaderState } from '@app/@shared/loader/loader.state';

class TemplateMappingsModel {
  user: string | any = null;
  invoiceDocumentType: DocumentTypeDTO | any = null;
  invoiceTemplateType: DocumentTypeDTO | any = null;
  invoiceTemplate: DocumentDTO | any = null;
  quotationDocumentType: DocumentTypeDTO | any = null;
  quotationTemplateType: DocumentTypeDTO | any = null;
  quotationTemplate: DocumentDTO | any = null;
}

interface TemplateCard {
  key: 'invoice' | 'quotation';
  target: TargetEntity;
  title: string;
  description: string;
  icon: string;
  documentTypeField: 'invoiceDocumentType' | 'quotationDocumentType';
  templateTypeField: 'invoiceTemplateType' | 'quotationTemplateType';
  templateField: 'invoiceTemplate' | 'quotationTemplate';
  acceptedFormats: string;
}

@Component({
  selector: 'app-template-mappings',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './template-mappings.html',
  styleUrls: ['./template-mappings.scss'],
})
export class TemplateMappings implements OnInit {
  readonly TargetEntity = TargetEntity;

  settingsApiStore = inject(SettingsApiStore);
  documentTypeApiStore = inject(DocumentTypeApiStore);
  private readonly documentApi = inject(DocumentApi);
  private readonly toastr = inject(ToastrService);

  loading = linkedSignal(() => this.documentTypeApiStore.loading() || this.settingsApiStore.loading());
  loaderMessage = linkedSignal(() => this.documentTypeApiStore.loaderMessage() || this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.documentTypeApiStore.success() || this.settingsApiStore.success());
  error = linkedSignal(() => this.documentTypeApiStore.error() || this.settingsApiStore.error());
  messages = linkedSignal(() => this.documentTypeApiStore.messages() || this.settingsApiStore.messages());

  documentTypeOptions = computed<DocumentTypeDTO[]>(() => this.documentTypeApiStore.dataList());

  templateMappingsSignal = linkedSignal(() => {
    const store = this.settingsApiStore.templateMappings();
    const model = new TemplateMappingsModel();
    model.user = store?.user ?? null;
    model.invoiceDocumentType = store?.invoiceDocumentType ?? null;
    model.invoiceTemplateType = store?.invoiceTemplateType ?? null;
    model.invoiceTemplate = store?.invoiceTemplate ?? null;
    model.quotationDocumentType = store?.quotationDocumentType ?? null;
    model.quotationTemplateType = store?.quotationTemplateType ?? null;
    model.quotationTemplate = store?.quotationTemplate ?? null;
    return model;
  });

  cards: TemplateCard[] = [
    {
      key: 'invoice',
      target: TargetEntity.INVOICE,
      title: 'Invoice Configuration',
      description: 'Map and manage invoice document structures.',
      icon: 'receipt_long',
      documentTypeField: 'invoiceDocumentType',
      templateTypeField: 'invoiceTemplateType',
      templateField: 'invoiceTemplate',
      acceptedFormats: '.pdf, .doc, .docx (Max 5MB)',
    },
    {
      key: 'quotation',
      target: TargetEntity.QUOTATION,
      title: 'Quotation Configuration',
      description: 'Define mapping structures for quote extraction.',
      icon: 'request_quote',
      documentTypeField: 'quotationDocumentType',
      templateTypeField: 'quotationTemplateType',
      templateField: 'quotationTemplate',
      acceptedFormats: '.pdf, .doc, .docx (Max 5MB)',
    },
  ];

  loaderState = inject(LoaderState);
  constructor() {

    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getTemplateMappings();
    this.documentTypeApiStore.getAll();
  }

  updateDocumentType(field: 'invoiceDocumentType' | 'quotationDocumentType' | 'invoiceTemplateType' | 'quotationTemplateType', id: string): void {
    const option = this.documentTypeOptions().find((d) => d.id === id) ?? null;
    this.templateMappingsSignal.update((value) => ({ ...value, [field]: option }));
  }

  triggerUpload(card: TemplateCard): void {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept =
      'application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document';
    input.onchange = (event: any) => {
      const file = event.target.files?.[0];
      if (file) {
        this.settingsApiStore.uploadTemplate({ template: file, target: card.target });
      }
    };
    input.click();
  }

  downloadTemplate(card: TemplateCard): void {
    const doc = this.templateMappingsSignal()[card.templateField];

    if (!doc?.id) {
      this.toastr.error('No template document available for download', 'Download Error');
      return;
    }

    this.documentApi.downloadFile(doc.id).subscribe({
      next: (res: any) => {
        const url = window.URL.createObjectURL(res);
        const a = document.createElement('a');
        a.href = url;
        a.download = doc.fileName || 'template';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err: any) => {
        this.toastr.error(err?.error?.message || err?.message || 'Failed to download file', 'Download Error');
      },
    });
  }

  discard(): void {
    this.settingsApiStore.getTemplateMappings();
  }

  save(): void {
    const value = this.templateMappingsSignal();
    const templateMappings = new TemplateMappingsDTO();
    templateMappings.user = value.user;
    templateMappings.invoiceDocumentType = value.invoiceDocumentType;
    templateMappings.invoiceTemplateType = value.invoiceTemplateType;
    templateMappings.quotationDocumentType = value.quotationDocumentType;
    templateMappings.quotationTemplateType = value.quotationTemplateType;

    this.settingsApiStore.saveTemplateMappings({ templateMappings });
  }
}
