import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
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
import { Router, RouterLink } from '@angular/router';
import { TableComponent } from '@app/components/table/table';
import { BranchDTO } from '@app/models/bw/co/centralkyc/organisation/branch/branch-dto';
import { ClientRequestDTO } from '@app/models/bw/co/centralkyc/organisation/client/client-request-dto';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/centralkyc/subscription/kyc-subscription-dto';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { Page } from '@app/models/page.model';
import { BranchApi } from '@app/services/bw/co/centralkyc/organisation/branch/branch-api';
import { ClientRequestApi } from '@app/services/bw/co/centralkyc/organisation/client/client-request-api';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';
import { BranchApiStore } from '@app/store/bw/co/centralkyc/organisation/branch/branch-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/centralkyc/organisation/client/client-request-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { BranchFormDialogComponent } from './add-branch-dialog';
import Swal from 'sweetalert2';
import { Loader } from '@app/@shared/loader/loader';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';

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
    Loader
  ],
  templateUrl: './organisation-details.html',
  styleUrl: './organisation-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DatePipe, CurrencyPipe],
})
export class OrganisationDetails implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string = '';
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
  subscriptionsLoading = linkedSignal(() => false);

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
  selectedDocumentFile = signal<File | null>(null);
  isUploadingDocument = signal(false);

  toaster: ToastrService = inject(ToastrService);

  // requestsColumns = [...this.clientRequestsTableColumns.map(column => column.id), 'actions'];

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
      let branch = this.branchApiStore.data();

      if (branch?.id) {
        this.loadBranches();
      }
    });  
  }

  ngOnInit(): void {

    if (this.id && this.id !== '') {

      this.organisationApiStore.findById({
        id: this.id
      })

      this.branchApiStore.findByOrganisation({
        organisationId: this.id
      });

      this.loadBranches();
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

  }

  ngOnDestroy(): void { }

  private openBranchDialog(data: BranchDTO): void {
    const ref = this.dialog.open(BranchFormDialogComponent, { data, width: '480px' });
    ref.afterClosed().subscribe((branch: BranchDTO | undefined) => {
      if (branch) {
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
        this.branchApiStore.findByOrganisation({ organisationId: this.id });
      }
    });

  }

  openDocumentDetails(document: DocumentDTO): void {
    if (!document?.id) {
      this.toaster.warning('Document details are unavailable for unsaved records.');
      return;
    }

    this.router.navigate(['/document/details', document.id]);
  }

  openDocumentEdit(document: DocumentDTO): void {
    if (!document?.id) {
      this.toaster.warning('Cannot edit a document without an id.');
      return;
    }

    this.router.navigate(['/document/edit', document.id]);
  }

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
          this.organisationApiStore.findById({ id: this.id });
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
      organisationId: this.id,
      organisation: this.organisation().name
    } as BranchDTO;

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
    let org = this.organisation();
    if (org?.id) {
      this.clientRequestApiStore.findByTargetPaged({
        target: TargetEntity.ORGANISATION,
        targetId: org.id,
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
    const org = this.organisation();
    if (org?.id) {
      this.branchApiStore.findByOrganisation({ organisationId: org.id });
    }
  }

  refreshBranches(): void {
    this.loadBranches();
  }

  // Invoices Management Methods
  loadInvoices(): void {
    const org = this.organisation();
    if (org?.id) {
      this.kycInvoiceApiStore.findByOrganisation({ organisationId: org.id });
    }
  }

  refreshInvoices(): void {
    this.loadInvoices();
  }

  // Subscriptions Management Methods
  loadSubscriptions(): void {
    const org = this.organisation();
    if (org?.id) {
      this.kycSubscriptionApiStore.findByOrganisation({ organisationId: org.id });
    }
  }

  refreshSubscriptions(): void {
    this.loadSubscriptions();
  }

  // Client Requests Management Methods
  loadIndividualClientRequests(pageNumber: number = 0, pageSize: number = 10): void {

    const org = this.organisation();
    if (org?.id) {
      this.clientRequestApiStore.findIndividualsByOrganisationPaged({
        organisationId: org.id,
        pageNumber,
        pageSize
      });
    }
  }

  loadOrganisationClientRequests(pageNumber: number = 0, pageSize: number = 10): void {
    const org = this.organisation();
    if (org?.id) {
      this.clientRequestApiStore.findOrganisationsByOrganisationPaged({
        organisationId: org.id,
        pageNumber,
        pageSize
      });
    }
  }
}
