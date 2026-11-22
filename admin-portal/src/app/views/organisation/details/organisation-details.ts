import { toast } from 'ngx-sonner';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { PageEvent, MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatDialog } from '@angular/material/dialog';
import { CurrencyPipe, DatePipe, CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, computed, ElementRef, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal, ViewChild, untracked } from '@angular/core';
import { form, FormRoot, FormField } from '@angular/forms/signals';
import { Router, RouterLink } from '@angular/router';
import { TableComponent } from '@app/components/table/table';
import { BranchDTO } from '@app/models/bw/co/knowvera/organisation/branch/branch-dto';
import { ClientRequestDTO } from '@app/models/bw/co/knowvera/organisation/client/client-request-dto';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { GeneralStatus } from '@app/models/bw/co/knowvera/general-status';
import { KycComplianceStatus } from '@app/models/bw/co/knowvera/kyc/kyc-compliance-status';
import { ClientRequestStatus } from '@app/models/bw/co/knowvera/organisation/client/client-request-status';
import { KycSubsciptionStatus } from '@app/models/bw/co/knowvera/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/knowvera/subscription/kyc-subscription-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { Page } from '@app/models/page.model';
import { AppEnvStore } from '@app/store/app-env.state';
import { BranchApi } from '@app/services/bw/co/knowvera/organisation/branch/branch-api';
import { ClientRequestApi } from '@app/services/bw/co/knowvera/organisation/client/client-request-api';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { KycInvoiceApiStore } from '@app/store/bw/co/knowvera/invoice/kyc-invoice-api.store';
import { BranchApiStore } from '@app/store/bw/co/knowvera/organisation/branch/branch-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/knowvera/organisation/client/client-request-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/knowvera/subscription/kyc-subscription-api.store';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { BranchFormDialogComponent } from './add-branch-dialog';
import { swalFire } from '@app/@shared/swal';
import { finalize } from 'rxjs';
import { DocumentApiStore } from '@app/store/bw/co/knowvera/document/document-api.store';
import { CreateClientRequestDialogComponent } from './create-client-request-dialog';
import {
  ManageRequirementsDialogComponent,
  ManageRequirementsDialogData,
  ManageRequirementsDialogResult,
} from './manage-requirements-dialog';
import { DocumentTypeApi } from '@app/services/bw/co/knowvera/document/type/document-type-api';
import { FormApi } from '@app/services/bw/co/knowvera/form/form-api';
import { FormDTO } from '@app/models/bw/co/knowvera/form/form-dto';
import { LoaderState } from '@app/@shared/loader/loader.state';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import {
  AssignFieldGroupsDialogComponent,
  AssignFieldGroupsDialogData,
  AssignFieldGroupsDialogResult,
  FieldGroupFilter,
  FieldGroupScope,
} from './assign-field-groups-dialog';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-organisation-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatChipsModule,
    MatTabsModule,
    MatSelectModule,
    TranslateModule,
    FormRoot,
    FormField,
  ],
  templateUrl: './organisation-details.html',
  styleUrls: ['./organisation-details.scss'],
  providers: [DatePipe, CurrencyPipe],
})
export class OrganisationDetails implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string = '';
  targetEntity = TargetEntity;
  organisationApiStore = inject(OrganisationApiStore);
  settingsApiStore = inject(SettingsApiStore);
  settings = linkedSignal(() => this.settingsApiStore.data());
  loaderState = inject(LoaderState);

  organisation = linkedSignal(() => this.organisationApiStore.data());

  branchApiStore = inject(BranchApiStore);
  branchApi = inject(BranchApi);
  dialog = inject(MatDialog);
  kycInvoiceApiStore = inject(KycInvoiceApiStore);
  kycSubscriptionApiStore = inject(KycSubscriptionApiStore);
  clientRequestApiStore = inject(ClientRequestApiStore);
  clientRequestApi = inject(ClientRequestApi);
  documentApi = inject(DocumentApi);
  documentApiStore = inject(DocumentApiStore);
  router = inject(Router);
  datePipe = inject(DatePipe);
  currencyPipe = inject(CurrencyPipe);
  appEnvState = inject(AppEnvStore);
  private readonly documentTypeApi = inject(DocumentTypeApi);
  private readonly formApi = inject(FormApi);

  // Forms the organisation asks its clients to complete
  forms = signal<FormDTO[]>([]);
  formsError = signal(false);
  organisationClientForms = computed(() => this.forms().filter((f) => f.targetType === TargetEntity.ORGANISATION));
  individualClientForms = computed(() => this.forms().filter((f) => f.targetType !== TargetEntity.ORGANISATION));

  error = linkedSignal(() => this.organisationApiStore.error());
  messages = linkedSignal(() => this.organisationApiStore.messages());
  success = linkedSignal(() => this.organisationApiStore.success());
  loading = linkedSignal(() => this.organisationApiStore.loading() || this.clientRequestApiStore.loading() || this.branchApiStore.loading() || this.isUploadingDocument());

  // Branches related properties
  branches = linkedSignal<BranchDTO[]>(() => this.branchApiStore.dataList());
  branchesLoading = linkedSignal(() => false);
  currentPage = signal(0);
  pageSize = signal(10);
  totalBranches = signal(0);
  newBranch = signal({
    code: '',
    name: '',
    description: '',
    physicalAddress: ''
  });

  // Invoices related properties
  invoices = this.kycInvoiceApiStore.dataList;
  invoicesLoading = linkedSignal(() => false);

  // Subscriptions related properties
  subscriptions = linkedSignal<KycSubscriptionDTO[]>(() => this.kycSubscriptionApiStore.dataList());
  subscriptionsDataSource = new MatTableDataSource<KycSubscriptionDTO>([]);
  subscriptionsLoading = linkedSignal(() => false);
  @ViewChild('subscriptionsPaginator') subscriptionsPaginator?: MatPaginator;

  clientRequestsTableSignal = linkedSignal<Page<ClientRequestDTO>>(() => this.clientRequestApiStore.dataPage());
  clientRequests = linkedSignal<ClientRequestDTO[]>(() => this.clientRequestsTableSignal().content || []);
  clientRequestsDataSource = new MatTableDataSource<ClientRequestDTO>([]);
  clientRequestsCurrentPage = signal(0);
  clientRequestsPageSize = signal(10);
  clientRequestsTotalElements = signal(0);
  clientRequestStatuses = Object.values(ClientRequestStatus);
  clientRequestsColumns = [
    'createdAt', 'organisation', 'status'
  ]

  // Individual Client Requests related properties
  individualClientRequests = linkedSignal(() => this.clientRequestApiStore.individualsRequestsPage());
  @ViewChild('individualClientRequestsTable') individualClientRequestsTable!: TableComponent<ClientRequestDTO>;
  individualClientRequestsLoading = linkedSignal(() => false);
  uploadedIndividualClientRequestFiles = signal<any[]>([]);
  isUploadingIndividualClientRequestFile = signal(false);
  individualClientRequestFileUploadProgress = signal(0);

  // Organisation Client Requests related properties
  organisationClientRequests = this.clientRequestApiStore.organisationsRequestsPage;
  @ViewChild('organisationClientRequestsTable') organisationClientRequestsTable!: TableComponent<ClientRequestDTO>;
  organisationClientRequestsLoading = linkedSignal(() => false);
  uploadedOrganisationClientRequestFiles = signal<any[]>([]);
  isUploadingOrganisationClientRequestFile = signal(false);
  organisationClientRequestFileUploadProgress = signal(0);

  // Uploaded documents actions
  documentTypeIdForUpload = signal('');
  @ViewChild('documentUploadInput') documentUploadInput?: ElementRef<HTMLInputElement>;
  documentUploadForm = form(this.documentTypeIdForUpload, {
    submission: {
      action: async () => {
        if (this.documentUploadInput) this.uploadOrganisationDocument(this.documentUploadInput.nativeElement);
      },
    },
  });
  selectedDocumentFile = signal<File | null>(null);
  isUploadingDocument = signal(false);

  // KYC report sections and document requirements
  individualFieldGroups = computed<KycFieldGroupDTO[]>(() =>
    [...(this.organisation()?.individualFieldGroups || [])].sort((a: KycFieldGroupDTO, b: KycFieldGroupDTO) => (a.position ?? 0) - (b.position ?? 0))
  );
  organisationFieldGroups = computed<KycFieldGroupDTO[]>(() =>
    [...(this.organisation()?.organisationFieldGroups || [])].sort((a: KycFieldGroupDTO, b: KycFieldGroupDTO) => (a.position ?? 0) - (b.position ?? 0))
  );
  verifiedDomainCount = computed(() => (this.organisation()?.domains || []).filter((domain: any) => domain?.verified).length);
  individualKycDocuments = computed<DocumentTypeDTO[]>(() => this.organisation()?.individualKycDocuments || []);
  organisationKycDocuments = computed<DocumentTypeDTO[]>(() => this.organisation()?.organisationKycDocuments || []);
  /** Chooses the documents this organisation requires of itself and of each individual client. */
  openManageRequirements(): void {
    const organisation = this.organisation();
    if (!organisation?.id) {
      return;
    }
    this.documentTypeApi.getAll().subscribe({
      next: (documentTypes) => {
        const data: ManageRequirementsDialogData = {
          documentTypes: documentTypes || [],
          organisationKycDocuments: this.organisationKycDocuments(),
          individualKycDocuments: this.individualKycDocuments(),
        };
        this.dialog
          .open<ManageRequirementsDialogComponent, ManageRequirementsDialogData, ManageRequirementsDialogResult>(
            ManageRequirementsDialogComponent,
            { data, width: '52rem', maxWidth: '95vw', panelClass: 'dp-dialog' },
          )
          .afterClosed()
          .subscribe((result) => {
            if (!result) {
              return;
            }
            this.loaderState.message.set('Saving document requirements');
            this.organisationApiStore.save({
              organisation: {
                ...this.organisation(),
                organisationKycDocuments: result.organisationKycDocuments,
                individualKycDocuments: result.individualKycDocuments,
              },
            });
          });
      },
      error: () => toast.error('The document types could not be loaded.'),
    });
  }

  openAssignFieldGroups(filter: FieldGroupFilter = 'ALL', focusGroupId: string | null = null): void {
    const organisation = this.organisation();
    const data: AssignFieldGroupsDialogData = {
      organisationGroups: this.settings()?.organisationKycFieldGroups || [],
      individualGroups: this.settings()?.individualKycFieldGroups || [],
      assignedOrganisationGroups: this.organisationFieldGroups(),
      assignedIndividualGroups: this.individualFieldGroups(),
      filter,
      focusGroupId,
    };

    this.dialog
      .open<AssignFieldGroupsDialogComponent, AssignFieldGroupsDialogData, AssignFieldGroupsDialogResult>(
        AssignFieldGroupsDialogComponent,
        { data, width: '48rem', maxWidth: '95vw', panelClass: 'dp-dialog' },
      )
      .afterClosed()
      .subscribe((result) => {
        if (!result || !organisation?.id) {
          return;
        }

        this.loaderState.message.set('Saving organisation');
        this.organisationApiStore.save({
          organisation: {
            ...organisation,
            organisationFieldGroups: result.organisationReportGroups,
            individualFieldGroups: result.individualReportGroups,
          },
        });
      });
  }

  /** Number of fields the library defines for an assigned group (for the "n / m fields active" count). */
  libraryFieldCount(scope: FieldGroupScope, group: KycFieldGroupDTO): number {
    const library: KycFieldGroupDTO[] =
      (scope === 'ORG' ? this.settings()?.organisationKycFieldGroups : this.settings()?.individualKycFieldGroups) || [];
    const match = library.find((g) => (group.id && g.id === group.id) || g.label === group.label);
    return (match?.groupFields || group.groupFields || []).length;
  }

  /** Downloads the organisation record as JSON. */
  exportRecord(): void {
    const organisation = this.organisation();
    const blob = new Blob([JSON.stringify(organisation, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement('a');

    anchor.href = url;
    anchor.download = `${organisation?.code || organisation?.name || 'organisation'}.json`;
    anchor.click();
    URL.revokeObjectURL(url);
  }

  // toaster: ToastrService = inject(ToastrService);

  constructor() {
    effect(() => {
      this.loaderState.isLoading.set(this.loading());
    });

    effect(() => {
      const page = this.clientRequestsTableSignal();

      if (!page) {
        this.clientRequestsDataSource.data = [];
        this.clientRequestsCurrentPage.set(0);
        this.clientRequestsPageSize.set(10);
        this.clientRequestsTotalElements.set(0);
        return;
      }

      this.clientRequestsDataSource.data = page.content || [];
      this.clientRequestsCurrentPage.set(page.page?.number || 0);
      this.clientRequestsPageSize.set(page.page?.size || 10);
      this.clientRequestsTotalElements.set(page.page?.totalElements || page.totalElements || 0);
    });

    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading() && messages[0]) {
        untracked(() => toast.success(messages[0]));
      }

      if (this.error() && !this.loading()) {
        untracked(() => toast.error(messages[0]));
      }
    });

    effect(() => {
      this.subscriptionsDataSource.data = this.subscriptions();
    });

    effect(() => {
      let branch = this.branchApiStore.data();

      if (branch?.id) {
        this.loadBranches();
      }
    });

    effect(() => {

      this.branches.set(this.organisationApiStore.data()?.branches || []);
    });
  }

  /** The forms this organisation owns, for the Client Forms card. */
  loadForms(): void {
    this.formsError.set(false);
    this.formApi.findByOrganisation(this.id).subscribe({
      next: (forms) => this.forms.set([...(forms || [])].sort((a, b) => (a.name ?? '').localeCompare(b.name ?? ''))),
      error: () => this.formsError.set(true),
    });
  }

  ngOnInit(): void {

    if (this.id && this.id !== '') {
      this.loaderState.message.set('Loading organisation');
      this.organisationApiStore.findById({
        id: this.id
      })
      this.settingsApiStore.getAll();
      this.loadForms();

      // this.branchApiStore.findByOrganisation({
      //   organisationId: this.id
      // });

      // this.loadBranches();
      this.loadInvoices();
      this.loadSubscriptions();
      this.loadIndividualClientRequests();
      this.loadOrganisationClientRequests();
      this.doSearchRequests();

    }
  }

  ngAfterViewInit(): void {
    this.organisationApiStore.reset();
    this.branchApiStore.reset();
    this.kycInvoiceApiStore.reset();
    this.kycSubscriptionApiStore.reset();
    this.clientRequestApiStore.reset();
    this.documentApiStore.reset();

    if (this.subscriptionsPaginator) {
      this.subscriptionsDataSource.paginator = this.subscriptionsPaginator;
    }

  }

  ngOnDestroy(): void { }

  orgInitials(): string {
    const name = (this.organisation()?.name || '').trim();
    if (!name) {
      return 'OR';
    }

    const parts = name.split(/\s+/).filter(Boolean);
    return parts.length === 1
      ? parts[0].substring(0, 2).toUpperCase()
      : (parts[0][0] + parts[1][0]).toUpperCase();
  }

  isOrganisationVerified(): boolean {
    return this.organisation()?.status === GeneralStatus.ACTIVE;
  }

  kycStatusClass(): string {
    switch (this.organisation()?.kycStatus) {
      case KycComplianceStatus.CURRENT:
        return 'kyc-current';
      case KycComplianceStatus.INCOMPLETE:
        return 'kyc-incomplete';
      case KycComplianceStatus.EXPIRED:
      case KycComplianceStatus.ABSENT:
      case KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED:
        return 'kyc-failed';
      default:
        return 'kyc-unknown';
    }
  }

  private openBranchDialog(data: BranchDTO): void {
    const ref = this.dialog.open(BranchFormDialogComponent, { data, width: '480px' });
    ref.afterClosed().subscribe((branch: BranchDTO | undefined) => {
      if (branch) {
        this.loaderState.message.set('Saving branch');
        this.branchApiStore.save({ branch });
        this.branchApiStore.findByOrganisation({ organisationId: this.id });
      }
    });
  }

  openAddBranchDialog(): void {
    let branch = new BranchDTO();
    branch.organisationId = this.id;
    branch.organisation = this.organisation().name;
    this.openBranchDialog(branch);
  }

  openEditBranchDialog(branch: BranchDTO): void {
    this.openBranchDialog(branch);
  }

  deleteBranch(branch: BranchDTO): void {

    swalFire({
      title: 'Delete Branch',
      text: `Are you sure you want to delete "${branch.name || branch.code}"? This cannot be undone.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed && branch.id) {
        this.loaderState.message.set('Removing branch');
        this.branchApiStore.remove({ id: branch.id });
        this.branchApiStore.findByOrganisation({ organisationId: this.id });
      }
    });

  }

  openDocumentDetails(document: DocumentDTO): void {
    if (!document?.id) {
      toast.warning('Document details are unavailable for unsaved records.');
      return;
    }

    this.router.navigate(['/documents', document.id]);
  }

  openDocumentEdit(document: DocumentDTO): void {
    if (!document?.id) {
      toast.warning('Cannot edit a document without an id.');
      return;
    }

    this.router.navigate(['/documents', document.id, 'edit']);
  }

  onDocumentFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files.length > 0 ? input.files[0] : null;
    this.selectedDocumentFile.set(file);
  }

  uploadOrganisationDocument(fileInput: HTMLInputElement): void {
    const org = this.organisation();
    const documentTypeId = this.documentTypeIdForUpload().trim();
    const file = this.selectedDocumentFile();

    if (!org?.id) {
      toast.error('Organisation must be loaded before uploading documents.');
      return;
    }

    if (!documentTypeId) {
      toast.warning('Please provide a document type id.');
      return;
    }

    if (!file) {
      toast.warning('Please choose a file to upload.');
      return;
    }

    this.loaderState.message.set('Uploading document');
    this.isUploadingDocument.set(true);


    this.documentApi.upload(TargetEntity.ORGANISATION, org.id, documentTypeId, file)
      .pipe(finalize(() => this.isUploadingDocument.set(false)))
      .subscribe({
        next: () => {
          toast.success('Document uploaded successfully.');
          this.selectedDocumentFile.set(null);
          fileInput.value = '';
          this.organisationApiStore.findById({ id: this.id });
        },
        error: (error: any) => {
          const message = error?.error?.message || 'Failed to upload document.';
          toast.error(message);
        }
      });
  }

  addBranch(): void {

    const draft = this.newBranch();
    if (!draft.code.trim() || !draft.name.trim() || !draft.physicalAddress.trim()) {
      return;
    }

    const branch: BranchDTO = {
      code: draft.code.trim(),
      name: draft.name.trim(),
      description: draft.description.trim(),
      physicalAddress: draft.physicalAddress.trim(),
      organisationId: this.id,
      organisation: this.organisation().name
    } as BranchDTO;

    this.loaderState.message.set('Saving branch');
    this.branchApiStore.save({ branch });
    this.branchApiStore.findByOrganisation({ organisationId: this.id });

    this.newBranch.set({
      code: '',
      name: '',
      description: '',
      physicalAddress: ''
    });
  }

  private doSearchRequests(pageNumber: number = 0, pageSize: number = 10, target?: TargetEntity): void {
    console.log(this.id)
    if (this.id) {
      this.clientRequestApiStore.findByTargetPaged({
        target: TargetEntity.ORGANISATION,
        targetId: this.id,
        pageNumber,
        pageSize,
      });
    }
  }

  onClientRequestsPageChange(event: PageEvent): void {
    this.loaderState.message.set('Loading client requests');
    this.doSearchRequests(event.pageIndex, event.pageSize);
  }

  // Branches Management Methods
  loadBranches(): void {
    if (this.id) {
      this.loaderState.message.set('Loading branches');
      this.branchApiStore.findByOrganisation({ organisationId: this.id });
    }
  }

  refreshBranches(): void {
    this.loadBranches();
  }

  // Invoices Management Methods
  loadInvoices(): void {
    const org = this.organisation();
    if (this.id) {
      this.kycInvoiceApiStore.findByOrganisation({ organisationId: this.id });
    }
  }

  refreshInvoices(): void {
    this.loadInvoices();
  }

  // Subscriptions Management Methods
  loadSubscriptions(): void {
    if (this.id) {
      this.kycSubscriptionApiStore.findByOrganisation({ organisationId: this.id });
    }
  }

  refreshSubscriptions(): void {
    this.loadSubscriptions();
  }

  openSubscriptionDetails(subscription: KycSubscriptionDTO): void {
    if (!subscription?.id) {
      toast.warning('Subscription details are unavailable for unsaved records.');
      return;
    }

    this.router.navigate(['/subscriptions', subscription.id]);
  }

  subscriptionStatusClass(status: KycSubsciptionStatus | string | null | undefined): string {
    switch (status) {
      case KycSubsciptionStatus.ACTIVE:
        return 'status-active';
      case KycSubsciptionStatus.INACTIVE:
        return 'status-inactive';
      case KycSubsciptionStatus.CANCELLED:
        return 'status-cancelled';
      default:
        return 'status-inactive';
    }
  }

  formatSubscriptionAmount(amount: number | string | null | undefined): string {
    const value = typeof amount === 'string' ? Number(amount) : amount;

    if (value === null || value === undefined || Number.isNaN(value)) {
      return '-';
    }

    return this.currencyPipe.transform(value, this.appEnvState.currency() || 'BWP') || '-';
  }

  formatSubscriptionDate(value: Date | string | null | undefined): string {
    if (!value) {
      return '-';
    }

    return this.datePipe.transform(value, 'dd MMM yyyy') || '-';
  }

  // Client Requests Management Methods
  loadIndividualClientRequests(pageNumber: number = 0, pageSize: number = 10): void {

    if (this.id) {
      this.clientRequestApiStore.findIndividualsByOrganisationPaged({
        organisationId: this.id,
        pageNumber,
        pageSize
      });
    }
  }

  loadOrganisationClientRequests(pageNumber: number = 0, pageSize: number = 10): void {
    if (this.id) {
      this.clientRequestApiStore.findOrganisationsByOrganisationPaged({
        organisationId: this.id,
        pageNumber,
        pageSize
      });
    }
  }

  onIndividualClientRequestFileSelected(event: Event, fileInput: HTMLInputElement): void {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files.length > 0 ? input.files[0] : null;

    if (!file) {
      return;
    }

    if (!this.isSupportedClientRequestFile(file.name)) {
      toast.warning('Please select a valid Excel or CSV file (.xlsx, .xls, .csv).');
      fileInput.value = '';
      return;
    }

    this.isUploadingIndividualClientRequestFile.set(true);
    this.clientRequestApi.uploadRequests(file, this.id, TargetEntity.INDIVIDUAL)
      .pipe(finalize(() => {
        this.isUploadingIndividualClientRequestFile.set(false);
        fileInput.value = '';
      }))
      .subscribe({
        next: () => {
          toast.success('Individual client requests uploaded successfully.');
          const page = this.individualClientRequests().page;
          this.loadIndividualClientRequests(page.number || 0, page.size || 10);
        },
        error: (error: any) => {
          this.showClientRequestUploadError(error, 'Failed to upload individual client requests.');
        }
      });
  }

  onOrganisationClientRequestFileSelected(event: Event, fileInput: HTMLInputElement): void {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files.length > 0 ? input.files[0] : null;

    if (!file) {
      return;
    }

    if (!this.isSupportedClientRequestFile(file.name)) {
      toast.warning('Please select a valid Excel or CSV file (.xlsx, .xls, .csv).');
      fileInput.value = '';
      return;
    }

    this.isUploadingOrganisationClientRequestFile.set(true);
    this.clientRequestApi.uploadRequests(file, this.id, TargetEntity.ORGANISATION)
      .pipe(finalize(() => {
        this.isUploadingOrganisationClientRequestFile.set(false);
        fileInput.value = '';
      }))
      .subscribe({
        next: () => {
          toast.success('Organisation client requests uploaded successfully.');
          const page = this.organisationClientRequests().page;
          this.loadOrganisationClientRequests(page.number || 0, page.size || 10);
        },
        error: (error: any) => {
          this.showClientRequestUploadError(error, 'Failed to upload organisation client requests.');
        }
      });
  }

  /**
   * A refused import stores nothing and names each wrong row (problem detail `errors`): those
   * are listed in a dialog, since a toast cannot hold them. Anything else is a toast.
   */
  private showClientRequestUploadError(error: any, fallback: string): void {
    const problem = error?.error;
    const detail: string = problem?.detail || problem?.message || fallback;
    const rows: string[] = Array.isArray(problem?.errors) ? problem.errors : [];

    if (rows.length === 0) {
      toast.error(detail);
      return;
    }

    const escape = (text: string) => String(text).replace(/[&<>"']/g, (c) => `&#${c.charCodeAt(0)};`);
    const shown = rows.slice(0, 50);
    const more = rows.length > shown.length ? `<p>and ${rows.length - shown.length} more rows.</p>` : '';

    swalFire({
      icon: 'error',
      title: 'Nothing was imported',
      html: `<p>${escape(detail)}</p>`
        + `<ul style="text-align: left; max-height: 40vh; overflow-y: auto;">`
        + shown.map((row) => `<li>${escape(row)}</li>`).join('')
        + `</ul>${more}`,
      width: '48rem',
    });
  }

  private isSupportedClientRequestFile(fileName: string): boolean {
    return /\.(xlsx|xls|csv)$/i.test(fileName || '');
  }

  openCreateClientRequestDialog(defaultTarget: TargetEntity): void {
    const organisation = this.organisation();

    if (!this.id) {
      toast.warning('Organisation must be loaded before creating a client request.');
      return;
    }

    const ref = this.dialog.open(CreateClientRequestDialogComponent, {
      width: '520px',
      data: { defaultTarget },
    });

    ref.afterClosed().subscribe((result: any) => {
      console.log('Dialog result:', result);
      if (!result) {
        return;
      }

      const { target, individual, organisation: targetOrg } = result;
      // const queryParams: any = {
      //   organisationId: this.id,
      //   organisationName: organisation?.name || '',
      //   organisationRegistrationNo: organisation?.registrationNo || '',
      //   target,
      // };


      let targetId = null;
      let name = null;
      let clientRequest = new ClientRequestDTO();
      // Add target entity details based on selection
      if (target === TargetEntity.INDIVIDUAL && individual) {

        targetId = individual.id;
        // The dialog lists individuals (name only); a full individual has first and last names
        name = individual.name ?? [individual.firstName, individual.lastName].filter(Boolean).join(' ');
        clientRequest.identityType = individual.identityType;
        clientRequest.emailAddress = individual.emailAddress;
        clientRequest.registration = individual.identityNo;

      } else if (target === TargetEntity.ORGANISATION && targetOrg) {

        targetId = targetOrg.id;
        name = targetOrg.name;
        clientRequest.registration = targetOrg.registrationNo;
        clientRequest.emailAddress = targetOrg.contactEmailAddress;
      }

      clientRequest.organisationId = this.id;
      clientRequest.organisation = organisation?.name;
      clientRequest.organisationCode = organisation?.code;
      clientRequest.organisationRegistrationNo = organisation?.registrationNo;
      clientRequest.target = target;
      clientRequest.targetId = targetId;
      clientRequest.targetKycStatus = organisation?.kycStatus;
      clientRequest.status = ClientRequestStatus.PENDING;
      clientRequest.name = name;

      this.clientRequestApi.save(clientRequest).subscribe({
        next: (savedRequest) => {
          toast.success('Client request created successfully.');
          this.doSearchRequests(this.clientRequestsCurrentPage(), this.clientRequestsPageSize());

          if (target === TargetEntity.INDIVIDUAL && individual) {

            this.clientRequestApiStore.findIndividualsByOrganisationPaged({
              organisationId: organisation?.id,
              pageNumber: this.individualClientRequests().page?.number || 0,
              pageSize: this.individualClientRequests().page?.size || 10
            });

          } else if (target === TargetEntity.ORGANISATION && targetOrg) {
            this.clientRequestApiStore.findOrganisationsByOrganisationPaged({
              organisationId: organisation?.id,
              pageNumber: this.organisationClientRequests().page?.number || 0,
              pageSize: this.organisationClientRequests().page?.size || 10
            });
          }
        },
        error: (error: any) => {
          const message = error?.error?.message || 'Failed to create client request.';
          toast.error(message);
        }
      });

    });
  }
}
