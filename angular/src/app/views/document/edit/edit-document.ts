import { CommonModule } from '@angular/common';
import { Component, effect, inject, linkedSignal, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { ActivatedRoute, Router } from '@angular/router';
import { MaterialModule } from '@app/material.module';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { DocumentVerificationStatus } from '@app/models/bw/co/knowvera/document/document-verification-status';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { DocumentApiStore } from '@app/store/bw/co/knowvera/document/document-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/knowvera/document/type/document-type-api.store';
import { Loader } from '@shared/loader/loader';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

class EditDocumentForm {
  id: string | any = null;
  target: TargetEntity | any = null;
  targetId: string | any = null;
  documentType: DocumentTypeDTO | any = null;
  fileName: string | any = null;
  verificationStatus: DocumentVerificationStatus | any = DocumentVerificationStatus.UNVERIFIED;
  url: string | any = null;
}

@Component({
  selector: 'app-edit-document',
  templateUrl: './edit-document.html',
  styleUrls: ['./edit-document.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MaterialModule,
    Loader,
    FormField,
  ],
})
export class EditDocumentComponent {
  readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  readonly toaster = inject(ToastrService);
  readonly documentApiStore = inject(DocumentApiStore);
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);

  editDocumentSignal = signal(new EditDocumentForm());
  editDocumentSignalForm = form(this.editDocumentSignal, () => {});
  isSaving = signal(false);
  lastLoadedId: string | null = null;

  document = linkedSignal<DocumentDTO | any>(() => this.documentApiStore.data());
  loading = linkedSignal(() => this.documentApiStore.loading());
  loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  success = linkedSignal(() => this.documentApiStore.success());
  error = linkedSignal(() => this.documentApiStore.error());
  messages = linkedSignal(() => this.documentApiStore.messages());

  documentTypeOptions = linkedSignal<DocumentTypeDTO[]>(() => this.documentTypeApiStore.dataList());

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = Object.keys(this.TargetEntityT);

  DocumentVerificationStatusT: any = DocumentVerificationStatus;
  DocumentVerificationStatusOptions = Object.keys(this.DocumentVerificationStatusT);

  constructor() {
    effect(() => {
      const current = this.document();

      if (!current?.id) {
        return;
      }

      this.populateForm(current);
    });

    effect(() => {
      if (this.isSaving() && this.success() && !this.loading()) {
        this.isSaving.set(false);
        this.toaster.success(this.messages()?.[0] || 'Document saved');
        this.router.navigate(['/documents/details'], {
          queryParams: { id: this.editDocumentSignal().id },
        });
      }

      if (this.isSaving() && this.error() && !this.loading()) {
        this.isSaving.set(false);
        this.toaster.error(this.messages()?.[0] || 'Failed to save document');
      }
    });

    this.documentApiStore.reset();
    this.documentTypeApiStore.getAll();
    this.loadDocumentFromRoute();

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id && id !== this.lastLoadedId) {
        this.lastLoadedId = id;
        this.documentApiStore.findById({ id });
      }
    });

    this.route.queryParamMap.subscribe((params) => {
      const id = params.get('id');
      if (id && id !== this.lastLoadedId) {
        this.lastLoadedId = id;
        this.documentApiStore.findById({ id });
      }
    });
  }

  documentTypeCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  editDocumentSave(): void {
    const current = this.document();
    const value = this.editDocumentSignal();

    if (!current?.id) {
      return;
    }

    const payload = new DocumentDTO();
    Object.assign(payload, current);

    payload.id = value.id || current.id;
    payload.target = value.target || null;
    payload.targetId = value.targetId || null;
    payload.documentTypeId = value.documentType?.id || null;
    payload.documentType = value.documentType?.name || null;
    payload.fileName = value.fileName || null;
    payload.url = value.url || null;
    payload.verificationStatus = value.verificationStatus || DocumentVerificationStatus.UNVERIFIED;

    this.isSaving.set(true);
    this.documentApiStore.save({ document: payload });
  }

  editDocumentReset(): void {
    const current = this.document();

    if (!current?.id) {
      this.editDocumentSignal.set(new EditDocumentForm());
      return;
    }

    this.populateForm(current);
  }

  cancelEdit(): void {
    const id = this.editDocumentSignal().id;

    if (id) {
      this.router.navigate(['/documents/details'], { queryParams: { id } });
      return;
    }

    this.router.navigate(['/documents']);
  }

  private loadDocumentFromRoute(): void {
    const id = this.route.snapshot.paramMap.get('id') || this.route.snapshot.queryParamMap.get('id');

    if (!id) {
      return;
    }

    this.lastLoadedId = id;
    this.documentApiStore.findById({ id });
  }

  private populateForm(document: DocumentDTO): void {
    const selectedType = this.documentTypeOptions().find((type) => type.id === document.documentTypeId)
      || ({ id: document.documentTypeId, name: document.documentType } as DocumentTypeDTO);

    this.editDocumentSignal.set({
      id: document.id,
      target: document.target,
      targetId: document.targetId,
      documentType: selectedType,
      fileName: document.fileName,
      verificationStatus: document.verificationStatus || DocumentVerificationStatus.UNVERIFIED,
      url: document.url,
    });
  }
}
