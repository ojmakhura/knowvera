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
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { BranchDTO } from '@app/models/bw/co/centralkyc/organisation/branch/branch-dto';
import { ClientRequestDTO } from '@app/models/bw/co/centralkyc/organisation/client/client-request-dto';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentVerificationStatus } from '@app/models/bw/co/centralkyc/document/document-verification-status';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { KycSubsciptionStatus } from '@app/models/bw/co/centralkyc/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/centralkyc/subscription/kyc-subscription-dto';
import { OrganisationDomain } from '@app/models/bw/co/centralkyc/organisation/organisation-domain';
import { PhoneNumber } from '@app/models/bw/co/centralkyc/phone-number';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { Page } from '@app/models/page.model';
import { AppEnvStore } from '@app/store/app-env.state';
import { BranchApi } from '@app/services/bw/co/centralkyc/organisation/branch/branch-api';
import { ClientRequestApi } from '@app/services/bw/co/centralkyc/organisation/client/client-request-api';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';
import { BranchApiStore } from '@app/store/bw/co/centralkyc/organisation/branch/branch-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/centralkyc/organisation/client/client-request-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
// import { BranchFormDialogComponent } from './add-branch-dialog';
import Swal from 'sweetalert2';
import { Loader } from '@app/@shared/loader/loader';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';
import { BranchFormDialogComponent } from './add-branch-dialog';
import { CreateClientRequestDialogComponent } from './create-client-request-dialog';
// import { CreateClientRequestDialogComponent } from './create-client-request-dialog';

@Component({
  selector: 'app-organisation-details',
  standalone: true,
  imports: [
    CommonModule,
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
    Loader
  ],
  templateUrl: './organisation-details.html',
  styleUrl: './organisation-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DatePipe, CurrencyPipe],
})
export class OrganisationDetails implements OnInit, AfterViewInit, OnDestroy {

  // @Input() id: string = '';
  targetEntity = TargetEntity;
  organisationApiStore = inject(OrganisationApiStore);
  settingsApiStore = inject(SettingsApiStore);
  settings = linkedSignal(() => this.settingsApiStore.data());

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

  error = linkedSignal(() => this.organisationApiStore.error());
  loaderMessage = linkedSignal(() => 'Loading...');
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
  // @ViewChild('individualClientRequestsTable') individualClientRequestsTable!: TableComponent<ClientRequestDTO>;
  individualClientRequestsLoading = linkedSignal(() => false);
  uploadedIndividualClientRequestFiles = signal<any[]>([]);
  isUploadingIndividualClientRequestFile = signal(false);
  individualClientRequestFileUploadProgress = signal(0);

  // Organisation Client Requests related properties
  organisationClientRequests = this.clientRequestApiStore.organisationsRequestsPage;
  // @ViewChild('organisationClientRequestsTable') organisationClientRequestsTable!: TableComponent<ClientRequestDTO>;
  organisationClientRequestsLoading = linkedSignal(() => false);
  uploadedOrganisationClientRequestFiles = signal<any[]>([]);
  isUploadingOrganisationClientRequestFile = signal(false);
  organisationClientRequestFileUploadProgress = signal(0);

  // Uploaded documents actions
  documentTypeIdForUpload = signal('');
  selectedDocumentFile = signal<File | null>(null);
  isUploadingDocument = signal(false);

  toaster: ToastrService = inject(ToastrService);

  constructor() {
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

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
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

    effect(() => {
      let userOrg = this.appEnvState.userOrganisation();

      if (userOrg && userOrg.id) {

        this.organisationApiStore.findById({ id: userOrg.id });
        this.loadInvoices();
        this.loadSubscriptions();
        this.loadIndividualClientRequests();
        this.loadOrganisationClientRequests();
        // this.doSearchRequests();
      }

    });
  }

  ngOnInit(): void {

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

  private openBranchDialog(data: BranchDTO): void {
    const ref = this.dialog.open(BranchFormDialogComponent, { data, width: '480px' });
    ref.afterClosed().subscribe((branch: BranchDTO | undefined) => {
      if (branch) {
        this.branchApiStore.save({ branch });
        this.branchApiStore.findByOrganisation({ organisationId: this.appEnvState.userOrganisation()?.id });
      }
    });
  }

  openAddBranchDialog(): void {
    let branch = new BranchDTO();
    branch.organisationId = this.appEnvState.userOrganisation()?.id;
    branch.organisation = this.organisation().name;
    this.openBranchDialog(branch);
  }

  openEditBranchDialog(branch: BranchDTO): void {
    this.openBranchDialog(branch);
  }

  deleteBranch(branch: BranchDTO): void {

    Swal.fire({
      title: 'Delete Branch',
      text: `Are you sure you want to delete "${branch.name || branch.code}"? This cannot be undone.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed && branch.id) {
        this.branchApiStore.remove({ id: branch.id });
        this.branchApiStore.findByOrganisation({ organisationId: this.appEnvState.userOrganisation()?.id });
      }
    });

  }

  openDocumentDetails(document: DocumentDTO): void {
    if (!document?.id) {
      this.toaster.warning('Document details are unavailable for unsaved records.');
      return;
    }

    this.router.navigate(['/document', document.id]);
  }

  // openDocumentEdit(document: DocumentDTO): void {
  //   if (!document?.id) {
  //     this.toaster.warning('Cannot edit a document without an id.');
  //     return;
  //   }

  //   this.router.navigate(['/document/edit', document.id]);
  // }

  selectUploadDocumentType(documentTypeId: string): void {
    this.documentTypeIdForUpload.set(documentTypeId || '');
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
      this.toaster.error('Organisation must be loaded before uploading documents.');
      return;
    }

    if (!documentTypeId) {
      this.toaster.warning('Please provide a document type id.');
      return;
    }

    if (!file) {
      this.toaster.warning('Please choose a file to upload.');
      return;
    }

    this.isUploadingDocument.set(true);


    this.documentApi.upload(TargetEntity.ORGANISATION, org.id, documentTypeId, file)
      .pipe(finalize(() => this.isUploadingDocument.set(false)))
      .subscribe({
        next: () => {
          this.toaster.success('Document uploaded successfully.');
          this.selectedDocumentFile.set(null);
          fileInput.value = '';
          this.organisationApiStore.findById({ id: this.appEnvState.userOrganisation()?.id });
        },
        error: (error: any) => {
          const message = error?.error?.message || 'Failed to upload document.';
          this.toaster.error(message);
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
      organisationId: this.appEnvState.userOrganisation()?.id,
      organisation: this.organisation().name
    } as BranchDTO;

    this.branchApiStore.save({ branch });
    this.branchApiStore.findByOrganisation({ organisationId: this.appEnvState.userOrganisation()?.id });

    this.newBranch.set({
      code: '',
      name: '',
      description: '',
      physicalAddress: ''
    });
  }

  private doSearchRequests(pageNumber: number = 0, pageSize: number = 10, target?: TargetEntity): void {
    const userOrg = this.appEnvState.userOrganisation();
    if (userOrg && userOrg.id) {
      this.clientRequestApiStore.findByTargetPaged({
        target: TargetEntity.ORGANISATION,
        targetId: userOrg.id,
        pageNumber,
        pageSize,
      });
    }
  }

  onClientRequestsPageChange(event: PageEvent): void {
    this.doSearchRequests(event.pageIndex, event.pageSize);
  }

  // Branches Management Methods
  loadBranches(): void {
    if (this.appEnvState.userOrganisation()?.id) {
      this.branchApiStore.findByOrganisation({ organisationId: this.appEnvState.userOrganisation()?.id });
    }
  }

  refreshBranches(): void {
    this.loadBranches();
  }

  // Invoices Management Methods
  loadInvoices(): void {
    // const org = this.organisation();
    if (this.appEnvState.userOrganisation()?.id) {
      this.kycInvoiceApiStore.findByOrganisation({ organisationId: this.appEnvState.userOrganisation()?.id });
    }
  }

  refreshInvoices(): void {
    this.loadInvoices();
  }

  // Subscriptions Management Methods
  loadSubscriptions(): void {
    if (this.appEnvState.userOrganisation()?.id) {
      this.kycSubscriptionApiStore.findByOrganisation({ organisationId: this.appEnvState.userOrganisation()?.id });
    }
  }

  refreshSubscriptions(): void {
    this.loadSubscriptions();
  }

  openSubscriptionDetails(subscription: KycSubscriptionDTO): void {
    if (!subscription?.id) {
      this.toaster.warning('Subscription details are unavailable for unsaved records.');
      return;
    }

    this.router.navigate(['/subscription/details', subscription.id]);
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

    const userOrg = this.appEnvState.userOrganisation();
    if (userOrg && userOrg.id) {
      this.clientRequestApiStore.findIndividualsByOrganisationPaged({
        organisationId: userOrg.id,
        pageNumber,
        pageSize
      });
    }
  }

  loadOrganisationClientRequests(pageNumber: number = 0, pageSize: number = 10): void {
    const userOrg = this.appEnvState.userOrganisation();
    if (userOrg && userOrg.id) {
      this.clientRequestApiStore.findOrganisationsByOrganisationPaged({
        organisationId: userOrg.id,
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
      this.toaster.warning('Please select a valid Excel or CSV file (.xlsx, .xls, .csv).');
      fileInput.value = '';
      return;
    }

    this.isUploadingIndividualClientRequestFile.set(true);
    this.clientRequestApi.uploadRequests(file, this.appEnvState.userOrganisation()?.id, TargetEntity.INDIVIDUAL)
      .pipe(finalize(() => {
        this.isUploadingIndividualClientRequestFile.set(false);
        fileInput.value = '';
      }))
      .subscribe({
        next: () => {
          this.toaster.success('Individual client requests uploaded successfully.');
          const page = this.individualClientRequests().page;
          this.loadIndividualClientRequests(page.number || 0, page.size || 10);
        },
        error: (error: any) => {
          const message = error?.error?.message || 'Failed to upload individual client requests.';
          this.toaster.error(message);
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
      this.toaster.warning('Please select a valid Excel or CSV file (.xlsx, .xls, .csv).');
      fileInput.value = '';
      return;
    }

    this.isUploadingOrganisationClientRequestFile.set(true);
    this.clientRequestApi.uploadRequests(file, this.appEnvState.userOrganisation()?.id, TargetEntity.ORGANISATION)
      .pipe(finalize(() => {
        this.isUploadingOrganisationClientRequestFile.set(false);
        fileInput.value = '';
      }))
      .subscribe({
        next: () => {
          this.toaster.success('Organisation client requests uploaded successfully.');
          const page = this.organisationClientRequests().page;
          this.loadOrganisationClientRequests(page.number || 0, page.size || 10);
        },
        error: (error: any) => {
          const message = error?.error?.message || 'Failed to upload organisation client requests.';
          this.toaster.error(message);
        }
      });
  }

  private isSupportedClientRequestFile(fileName: string): boolean {
    return /\.(xlsx|xls|csv)$/i.test(fileName || '');
  }

  openCreateClientRequestDialog(defaultTarget: TargetEntity): void {
    const organisation = this.organisation();

    if (!this.appEnvState.userOrganisation()?.id) {
      this.toaster.warning('Organisation must be loaded before creating a client request.');
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

      let targetId = null;
      let name = null;
      let clientRequest = new ClientRequestDTO();
      // Add target entity details based on selection
      if (target === TargetEntity.INDIVIDUAL && individual) {

        targetId = individual.id;
        name = individual.firstName + ' ' + individual.lastName;
        clientRequest.identityType = individual.identityType;
        clientRequest.emailAddress = individual.emailAddress;
        clientRequest.registration = individual.identityNo;

      } else if (target === TargetEntity.ORGANISATION && targetOrg) {

        targetId = targetOrg.id;
        name = targetOrg.name;
        clientRequest.registration = targetOrg.registrationNo;
        clientRequest.emailAddress = targetOrg.contactEmailAddress;
      }

      clientRequest.organisationId = this.appEnvState.userOrganisation()?.id;
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
          this.toaster.success('Client request created successfully.');
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
          this.toaster.error(message);
        }
      });

    });
  }

  openEditOrganisation(): void {
    this.router.navigate(['/organisation/edit']);
  }

  openNewAudit(): void {
    this.toaster.info('Audit workflow will be available soon.');
  }

  organisationDocuments(): DocumentDTO[] {
    return (this.organisation()?.documents || []) as DocumentDTO[];
  }

  organisationDomains(): OrganisationDomain[] {
    return (this.organisation()?.domains || []) as OrganisationDomain[];
  }

  organisationPhoneNumbers(): PhoneNumber[] {
    return (this.organisation()?.phoneNumbers || []) as PhoneNumber[];
  }

  individualRequestsList(): ClientRequestDTO[] {
    return this.individualClientRequests()?.content || [];
  }

  organisationRequestsList(): ClientRequestDTO[] {
    return this.organisationClientRequests()?.content || [];
  }

  selectedDocumentFileName(): string {
    return this.selectedDocumentFile()?.name || 'No file selected';
  }

  canUploadDocument(): boolean {
    return !!this.selectedDocumentFile() && !!this.documentTypeIdForUpload().trim() && !this.isUploadingDocument();
  }

  formatStatus(status: string | null | undefined): string {
    if (!status) {
      return 'Unknown';
    }

    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (letter) => letter.toUpperCase());
  }

  documentStatusClass(status: DocumentVerificationStatus | string | null | undefined): string {
    switch (status) {
      case DocumentVerificationStatus.VERIFIED:
        return 'status-active';
      case DocumentVerificationStatus.REJECTED:
        return 'status-rejected';
      case DocumentVerificationStatus.UNVERIFIED:
      case DocumentVerificationStatus.MANUAL_REVIEW:
      case DocumentVerificationStatus.IN_PROGRESS:
        return 'status-review';
      default:
        return 'status-review';
    }
  }

  clientRequestStatusClass(status: ClientRequestStatus | string | null | undefined): string {
    switch (status) {
      case ClientRequestStatus.ACCEPTED:
        return 'status-active';
      case ClientRequestStatus.REJECTED:
        return 'status-rejected';
      case ClientRequestStatus.CONTACTED:
        return 'status-processing';
      case ClientRequestStatus.PENDING:
      default:
        return 'status-review';
    }
  }

  domainVerificationClass(verified: boolean | null | undefined): string {
    return verified ? 'status-active' : 'status-review';
  }

  formatPhoneType(type: string | null | undefined): string {
    if (!type) {
      return 'Phone';
    }

    return this.formatStatus(type);
  }

  organisationInitials(): string {
    const name = this.organisation()?.name || '';
    const initials = name
      .split(' ')
      .filter((part: string) => !!part)
      .slice(0, 2)
      .map((part: string) => part[0].toUpperCase())
      .join('');

    return initials || 'ORG';
  }
}
