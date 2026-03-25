import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  linkedSignal,
  OnDestroy,
  OnInit,
  Signal,
  signal,
  ViewChild,
} from '@angular/core';
import { form, required, FormField, min } from '@angular/forms/signals';
import { TableComponent } from '@app/components/table/table';
import { ActionTemplate } from '@app/models/action-template';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { DocumentTypePurpose } from '@app/models/bw/co/centralkyc/settings/document-type-purpose';
import { SalaryRangeDTO } from '@app/models/bw/co/centralkyc/settings/salary-range-dto';
import { SettingsDTO } from '@app/models/bw/co/centralkyc/settings/settings-dto';
import { ColumnModel } from '@app/models/column.model';
import { Page } from '@app/models/page.model';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { DocumentTypeApi } from '@app/services/bw/co/centralkyc/document/type/document-type-api';
import { SettingsApi } from '@app/services/bw/co/centralkyc/settings/settings-api';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

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
}

@Component({
  selector: 'app-system-settings',
  standalone: true,
  templateUrl: './system-settings.html',
  styleUrls: ['./system-settings.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormField],
})
export class SystemSettings implements OnInit, AfterViewInit, OnDestroy {
  editSettingsVarsForm: EditSettingsVarsForm = new EditSettingsVarsForm();
  editSettingsSignal = signal(this.editSettingsVarsForm);
  editSettingsSignalForm = form(this.editSettingsSignal, (path) => {
    required(path.kycDuration, { message: 'kyc.duration.required' });
    required(path.timeToAccountCreation, { message: 'time.to.account.creation.required' });
    min(path.kycDuration, 0, { message: 'kyc.duration.min' });
    min(path.timeToAccountCreation, 0, { message: 'time.to.account.creation.min' });
    required(path.platformName, { message: 'platform.name.required' });
    required(path.platformUrl, { message: 'platform.url.required' });
    required(path.kycPortalLink, { message: 'kyc.portal.link.required' });
    required(path.supportContact, { message: 'support.contact.required' });
    required(path.organisationAdminRole, { message: 'organisation.admin.role.required' });
    required(path.normalUserRole, { message: 'normal.user.role.required' });
    required(path.invoiceDocumentType, { message: 'invoice.document.type.required' });
    required(path.invoiceTemplateType, { message: 'invoice.template.type.required' });
    required(path.quotationDocumentType, { message: 'quotation.document.type.required' });
    required(path.quotationTemplateType, { message: 'quotation.template.type.required' });
  });

  toaster: ToastrService = inject(ToastrService);
  readonly settingsApiStore = inject(SettingsApiStore);
  @ViewChild('individualDocumentsTable') individualDocumentsTable!: TableComponent<
    Array<DocumentTypeDTO>
  >;
  individualDocumentsTablePaged: boolean = false;

  individualDocumentsTableColumns: ColumnModel[] = [
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  individualDocumentsTableColumnsActions: ActionTemplate[] = [
    {
      id: 'settings-detach-individual-documents',
      label: 'detach.individual.documents',
      icon: 'delete',
      tooltip: 'detach.individual.documents',
    },
  ];

  showIndividualDocumentsActions = true;

  individualDocumentsTableLoadEmitter(event: any): void {}
  // Should be overriden to handle the actions
  doEditSettingsDetachIndividualDocuments(form: any): any {}

  individualDocumentsTableActionClicked(event: any): void {
    let form: any = {};
    let queryParams: any = {};
    let params: any = {};

    switch (event.action) {
      case 'settings-detach-individual-documents':
        // TODO: Implement the action
        form = {
          id: event.row.id,
        };
        queryParams = {
          id: event.row.id,
        };
        params = this.doEditSettingsDetachIndividualDocuments(event);
        break;
    }
  }

  selectedOrgDocumentCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return false;
  }

  filterSelectedOrgDocument() {}

  selectedOrgDocumentBackingList: DocumentTypeDTO[] = [];
  selectedOrgDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);

  selectedOrgDocumentDisplays: string[] = ['name'];
  @ViewChild('organisationDocumentsTable') organisationDocumentsTable!: TableComponent<
    Array<DocumentTypeDTO>
  >;
  organisationDocumentsTablePaged: boolean = true;

  organisationDocumentsTableColumns: ColumnModel[] = [
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  organisationDocumentsTableColumnsActions: ActionTemplate[] = [
    {
      id: 'settings-detach-org-documents',
      label: 'detach.org.documents',
      icon: 'delete',
      tooltip: 'detach.org.documents',
    },
  ];

  showOrganisationDocumentsActions = true;

  organisationDocumentsTableLoadEmitter(event: any): void {}
  // Should be overriden to handle the actions
  doEditSettingsDetachOrgDocuments(form: any): any {}

  selectedKycOrgDocumentCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return false;
  }

  filterSelectedKycOrgDocument() {}

  selectedKycOrgDocumentBackingList: DocumentTypeDTO[] = [];
  selectedKycOrgDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);

  selectedKycOrgDocumentDisplays: string[] = ['name'];
  @ViewChild('orgKycDocumentsTable') orgKycDocumentsTable!: TableComponent<Array<DocumentTypeDTO>>;
  orgKycDocumentsTablePaged: boolean = false;

  orgKycDocumentsTableColumns: ColumnModel[] = [
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  orgKycDocumentsTableColumnsActions: ActionTemplate[] = [
    {
      id: 'settings-detach-org-kyc-documents',
      label: 'detach.org.kyc.documents',
      icon: 'delete',
      tooltip: 'detach.org.kyc.documents',
    },
  ];

  showOrgKycDocumentsActions = true;

  orgKycDocumentsTableLoadEmitter(event: any): void {}
  // Should be overriden to handle the actions
  doEditSettingsDetachOrgKycDocuments(form: any): any {}

  orgKycDocumentsTableActionClicked(event: any): void {
    let form: any = {};
    let queryParams: any = {};
    let params: any = {};

    switch (event.action) {
      case 'settings-detach-org-kyc-documents':
        // TODO: Implement the action
        form = {
          id: event.row.id,
        };
        queryParams = {
          id: event.row.id,
        };
        break;
    }
  }

  selectedIndDocumentCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return false;
  }

  filterSelectedIndDocument() {}

  selectedIndDocumentBackingList: DocumentTypeDTO[] = [];
  selectedIndDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);

  selectedIndDocumentDisplays: string[] = ['name'];

  selectedKycIndDocumentCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return false;
  }

  filterSelectedKycIndDocument() {}

  selectedKycIndDocumentBackingList: DocumentTypeDTO[] = [];
  selectedKycIndDocumentFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);

  selectedKycIndDocumentDisplays: string[] = ['name'];
  @ViewChild('indKycDocumentsTable') indKycDocumentsTable!: TableComponent<Array<DocumentTypeDTO>>;
  indKycDocumentsTablePaged: boolean = false;

  indKycDocumentsTableColumns: ColumnModel[] = [
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  indKycDocumentsTableColumnsActions: ActionTemplate[] = [
    {
      id: 'settings-detach-ind-kyc-documents',
      label: 'detach.ind.kyc.documents',
      icon: 'delete',
      tooltip: 'detach.ind.kyc.documents',
    },
  ];

  showIndKycDocumentsActions = true;

  indKycDocumentsTableLoadEmitter(event: any): void {}

  settingApiStore = inject(SettingsApiStore);
  settingApi = inject(SettingsApi);
  documentTypeApi = inject(DocumentTypeApi);
  documentApi = inject(DocumentApi);

  organisationDocumentsTableSignal = computed<DocumentTypeDTO[]>(
    () => this.editSettingsSignal().organisationDocuments || [],
  );
  individualDocumentsTableSignal = computed<DocumentTypeDTO[]>(
    () => this.editSettingsSignal().individualDocuments || [],
  );
  orgKycDocumentsTableSignal = computed<DocumentTypeDTO[]>(
    () => this.editSettingsSignal().orgKycDocuments || [],
  );
  indKycDocumentsTableSignal = computed<DocumentTypeDTO[]>(
    () => this.editSettingsSignal().indKycDocuments || [],
  );

  loading = linkedSignal(() => false);
  loaderMessage = linkedSignal(() => 'Loading...');
  error = linkedSignal(() => false);
  messages = linkedSignal(() => false);
  success = linkedSignal(() => false);

  settings: SettingsDTO = new SettingsDTO();

  documentsTableColumns: ColumnModel[] = [
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  availableRoles = signal<string[]>([]);

  invoiceDocumentTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  invoiceTemplateTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  quotationDocumentTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  quotationTemplateTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);
  clientRequestFileTypeFilteredList = linkedSignal<DocumentTypeDTO[]>(() => []);

  constructor() {
    effect(() => {
      this.settings = this.settingApiStore.data();
      // this.editSettingsForm.patchValue(this.settings);
      this.updateSettingForm(this.settings);

      if (this.settings.invoiceDocumentType && this.settings.invoiceDocumentType.id) {
        this.invoiceDocumentTypeFilteredList.set([this.settings.invoiceDocumentType || null]);
      }

      if (this.settings.invoiceTemplateType && this.settings.invoiceTemplateType.id) {
        this.invoiceTemplateTypeFilteredList.set([this.settings.invoiceTemplateType]);
      }

      if (this.settings.quotationDocumentType && this.settings.quotationDocumentType.id) {
        this.quotationDocumentTypeFilteredList.set([this.settings.quotationDocumentType]);
      }

      if (this.settings.quotationTemplateType && this.settings.quotationTemplateType.id) {
        this.quotationTemplateTypeFilteredList.set([this.settings.quotationTemplateType]);
      }

      if (this.settings.clientRequestFileType && this.settings.clientRequestFileType.id) {
        this.clientRequestFileTypeFilteredList.set([this.settings.clientRequestFileType]);
      }
    });
  }

  ngOnInit(): void {
    this.settingApiStore.reset();
    this.settingApiStore.getAll();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  updateSettingForm(settings: SettingsDTO) {
    console.log(settings);

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
    });
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

    return settings;
  }

  organisationDocumentsRemove(documentType: DocumentTypeDTO): void {
    console.log(documentType);

    Swal.fire({
      title: 'Are you sure?',
      text: 'You are about to detach this document type from organisation documents. This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.isConfirmed) {
        this.settingApiStore.detachDocumentType({
          documentTypeId: documentType.id,
          purpose: DocumentTypePurpose.ORGANISATION,
        });
        Swal.fire(
          'Detached!',
          'The document type has been detached from organisation documents.',
          'success',
        );
      }
    });
  }

  individualDocumentsRemove(documentType: DocumentTypeDTO): void {
    console.log(documentType);

    Swal.fire({
      title: 'Are you sure?',
      text: 'You are about to detach this document type from individual documents. This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.isConfirmed) {
        this.settingApiStore.detachDocumentType({
          documentTypeId: documentType.id,
          purpose: DocumentTypePurpose.INDIVIDUAL,
        });
        Swal.fire(
          'Detached!',
          'The document type has been detached from individual documents.',
          'success',
        );
      }
    });
  }

  orgKycDocumentsRemove(documentType: DocumentTypeDTO): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You are about to detach this document type from organisation KYC documents. This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.isConfirmed) {
        this.settingApiStore.detachDocumentType({
          documentTypeId: documentType.id,
          purpose: DocumentTypePurpose.ORGANISATION_KYC,
        });
        Swal.fire('Detached!', 'The document type has been detached from organisation KYC documents.', 'success');
      }
    });
  }

  indKycDocumentsRemove(documentType: DocumentTypeDTO): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You are about to detach this document type from individual KYC documents. This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.isConfirmed) {
        this.settingApiStore.detachDocumentType({
          documentTypeId: documentType.id,
          purpose: DocumentTypePurpose.INDIVIDUAL_KYC,
        });
        Swal.fire('Detached!', 'The document type has been detached from individual KYC documents.', 'success');
      }
    });
  }

  documentCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any) {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }
}
