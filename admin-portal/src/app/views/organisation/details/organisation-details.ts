import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { CurrencyPipe, DatePipe, CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, inject, Input, linkedSignal, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TableComponent } from '@app/components/table/table';
import { BranchDTO } from '@app/models/bw/co/centralkyc/organisation/branch/branch-dto';
import { ClientRequestDTO } from '@app/models/bw/co/centralkyc/organisation/client/client-request-dto';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/centralkyc/subscription/kyc-subscription-dto';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { Page } from '@app/models/page.model';
import { BranchApi } from '@app/services/bw/co/centralkyc/organisation/branch/branch-api';
import { ClientRequestApi } from '@app/services/bw/co/centralkyc/organisation/client/client-request-api';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';
import { BranchApiStore } from '@app/store/bw/co/centralkyc/organisation/branch/branch-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/centralkyc/organisation/client/client-request-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';

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
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatChipsModule
  ],
  templateUrl: './organisation-details.html',
  styleUrl: './organisation-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DatePipe, CurrencyPipe],
})
export class OrganisationDetails implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string = '';
  organisationApiStore = inject(OrganisationApiStore);

  organisation = linkedSignal(() => this.organisationApiStore.data());

  branchApiStore = inject(BranchApiStore);
  branchApi = inject(BranchApi);
  kycInvoiceApiStore = inject(KycInvoiceApiStore);
  kycSubscriptionApiStore = inject(KycSubscriptionApiStore);
  clientRequestApiStore = inject(ClientRequestApiStore);
  clientRequestApi = inject(ClientRequestApi);
  datePipe = inject(DatePipe);
  currencyPipe = inject(CurrencyPipe);

  error = linkedSignal(() => false);
  loaderMessage = linkedSignal(() => 'Loading...');
  messages = linkedSignal(() => false);
  success = linkedSignal(() => false);
  loading = linkedSignal(() => this.organisationApiStore.loading() || this.clientRequestApiStore.loading());

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

  // requestsColumns = [...this.clientRequestsTableColumns.map(column => column.id), 'actions'];

  constructor() { }

  ngOnInit(): void {
    console.log('OrganisationDetails ngOnInit', this.id);

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
  }

  ngOnDestroy(): void { }

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
