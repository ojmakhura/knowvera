// views/settings/document-requirements/document-requirements.ts
import { Component, OnInit, effect, inject, linkedSignal, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { swalFire } from '@app/@shared/swal';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { DocumentTypePurpose } from '@app/models/bw/co/knowvera/settings/document-type-purpose';
import { DocumentTypeApi } from '@app/services/bw/co/knowvera/document/type/document-type-api';
import { SettingsApi } from '@app/services/bw/co/knowvera/settings/settings-api';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { LoaderState } from '@app/@shared/loader/loader.state';

class DocumentRequirementsModel {
  individualDocuments: DocumentTypeDTO[] = [];
  organisationDocuments: DocumentTypeDTO[] = [];
  indKycDocuments: DocumentTypeDTO[] = [];
  orgKycDocuments: DocumentTypeDTO[] = [];
}

type DocumentCategoryKey =
  | 'individualDocuments'
  | 'organisationDocuments'
  | 'indKycDocuments'
  | 'orgKycDocuments';

interface DocumentCategory {
  key: DocumentCategoryKey;
  purpose: DocumentTypePurpose;
  label: string;
  icon: string;
  highlighted?: boolean;
}

@Component({
  selector: 'app-document-requirements',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './document-requirements.html',
  styleUrls: ['./document-requirements.scss'],
})
export class DocumentRequirements implements OnInit {
  settingsApiStore = inject(SettingsApiStore);
  loaderState = inject(LoaderState);
  private settingsApi = inject(SettingsApi);
  private documentTypeApi = inject(DocumentTypeApi);
  private toastr = inject(ToastrService);

  loading = linkedSignal(() => this.settingsApiStore.loading());
  loaderMessage = linkedSignal(() => this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.settingsApiStore.success());
  error = linkedSignal(() => this.settingsApiStore.error());
  messages = linkedSignal(() => this.settingsApiStore.messages());

  documentRequirementsSignal = linkedSignal(() => {
    const storeData = this.settingsApiStore.documentRequirements();
    const model = new DocumentRequirementsModel();
    model.individualDocuments = storeData?.individualDocuments ?? [];
    model.organisationDocuments = storeData?.organisationDocuments ?? [];
    model.indKycDocuments = storeData?.indKycDocuments ?? [];
    model.orgKycDocuments = storeData?.orgKycDocuments ?? [];
    return model;
  });

  categories: DocumentCategory[] = [
    {
      key: 'individualDocuments',
      purpose: DocumentTypePurpose.INDIVIDUAL,
      label: 'Individual Documents',
      icon: 'person',
    },
    {
      key: 'organisationDocuments',
      purpose: DocumentTypePurpose.ORGANISATION,
      label: 'Organisation Documents',
      icon: 'domain',
    },
    {
      key: 'indKycDocuments',
      purpose: DocumentTypePurpose.INDIVIDUAL_KYC,
      label: 'Ind. KYC Suite',
      icon: 'verified_user',
      highlighted: true,
    },
    {
      key: 'orgKycDocuments',
      purpose: DocumentTypePurpose.ORGANISATION_KYC,
      label: 'Org KYC Suite',
      icon: 'corporate_fare',
      highlighted: true,
    },
  ];

  searchTerm = signal('');

  private availableDocumentTypes = signal<DocumentTypeDTO[]>([]);

  constructor() {
    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getDocumentRequirements();
    this.documentTypeApi.getAll().subscribe({
      next: (types) => this.availableDocumentTypes.set((types as DocumentTypeDTO[]) || []),
      error: () => this.availableDocumentTypes.set([]),
    });
  }

  categoryDocuments(category: DocumentCategory): DocumentTypeDTO[] {
    const term = this.searchTerm().trim().toLowerCase();
    const docs = this.documentRequirementsSignal()[category.key] ?? [];

    if (!term) {
      return docs;
    }

    return docs.filter((doc) => (doc.name || '').toLowerCase().includes(term));
  }

  addDocument(category: DocumentCategory): void {
    const existingIds = new Set(
      (this.documentRequirementsSignal()[category.key] ?? []).map((doc) => doc.id),
    );
    const options = this.availableDocumentTypes().filter((doc) => !existingIds.has(doc.id));

    if (!options.length) {
      this.toastr.info('All available document types are already configured here');
      return;
    }

    const inputOptions: Record<string, string> = {};
    options.forEach((doc) => {
      if (doc.id) {
        inputOptions[doc.id] = doc.name;
      }
    });

    swalFire({
      title: `Add to ${category.label}`,
      input: 'select',
      inputOptions,
      inputPlaceholder: 'Select a document type',
      showCancelButton: true,
      confirmButtonText: 'Add',
    }).then((result) => {
      if (!result.isConfirmed || !result.value) {
        return;
      }

      const selectedDoc = options.find((doc) => doc.id === result.value);

      this.settingsApi.attachDocumentType(result.value as string, category.purpose).subscribe({
        next: () => {
          this.toastr.success('Document requirement added successfully');
          if (selectedDoc) {
            this.documentRequirementsSignal.update((value) => ({
              ...value,
              [category.key]: [...(value[category.key] ?? []), selectedDoc],
            }));
          }
        },
        error: (error) => {
          this.toastr.error(
            error?.error?.message || error?.message || 'Unable to add document requirement',
          );
        },
      });
    });
  }

  removeDocument(category: DocumentCategory, doc: DocumentTypeDTO): void {
    swalFire({
      title: 'Remove document requirement?',
      text: `Remove "${doc.name}" from ${category.label}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Remove',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (!result.isConfirmed) {
        return;
      }

      this.settingsApi.detachDocumentType(doc.id || '', category.purpose).subscribe({
        next: () => {
          this.toastr.success('Document requirement removed successfully');
          this.documentRequirementsSignal.update((value) => ({
            ...value,
            [category.key]: (value[category.key] ?? []).filter((d) => d.id !== doc.id),
          }));
        },
        error: (error) => {
          this.toastr.error(
            error?.error?.message || error?.message || 'Unable to remove document requirement',
          );
        },
      });
    });
  }
}
