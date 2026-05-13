import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AfterViewInit, ChangeDetectionStrategy, Component, computed, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { IndividualListDTO } from '@app/models/bw/co/centralkyc/individual/individual-list-dto';
import { form, FormField } from '@angular/forms/signals';
import { required } from '@angular/forms/signals';
import { ClientRequestApiStore } from '@app/store/bw/co/centralkyc/organisation/client/client-request-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { IndividualIdentityType } from '@app/models/bw/co/centralkyc/individual/individual-identity-type';
import { ClientRequestDTO } from '@app/models/bw/co/centralkyc/organisation/client/client-request-dto';
import { ToastrService } from 'ngx-toastr';
import { SearchObject } from '@app/models/search-object';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { IndividualSearchCriteria } from '@app/models/bw/co/centralkyc/individual/individual-search-criteria';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { TranslateModule } from '@ngx-translate/core';
import { RouterLink } from '@angular/router';
import Swal from 'sweetalert2';

export class EditClientRequestForm {
  id: string = '';
  createdAt: Date | null = null;
  createdBy: string = '';
  modifiedAt: Date | null = null;
  modifiedBy: string = '';
  targetKycStatus: string | null = null;
  status: ClientRequestStatus = ClientRequestStatus.PENDING;
  organisation: OrganisationListDTO | null = null;
  organisationFilter: string = '';
  documentId: string = '';
  fileName: string = '';
  fileUrl: string = '';
  target: TargetEntity | null = null;
  targetObject: OrganisationListDTO | IndividualListDTO | null = null;
  targetObjectFilter: string = '';
}

@Component({
  selector: 'app-client-request-edit',
  templateUrl: './client-request-edit.html',
  styleUrls: ['./client-request-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatDividerModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    FormField,
    NgxMatSelectSearchModule,
    TranslateModule,
    RouterLink
]
})
export class ClientRequestEdit implements OnInit, AfterViewInit, OnDestroy {
  

  @Input() id?: string;

  readonly clientRequestApiStore = inject(ClientRequestApiStore);
  readonly organisationApiStore = inject(OrganisationApiStore);
  readonly individualApiStore = inject(IndividualApiStore);
  protected route: ActivatedRoute = inject(ActivatedRoute);

  readonly targetOptions = [TargetEntity.ORGANISATION, TargetEntity.INDIVIDUAL];
  readonly requestStatuses = [
    ClientRequestStatus.CONTACTED,
    ClientRequestStatus.PENDING,
    ClientRequestStatus.ACCEPTED,
    ClientRequestStatus.REJECTED,
  ];
  readonly kycStatuses = [
    KycComplianceStatus.CURRENT,
    KycComplianceStatus.INCOMPLETE,
    KycComplianceStatus.ABSENT,
    KycComplianceStatus.EXPIRED,
  ];

  editClientRequestSignal = signal<EditClientRequestForm>(new EditClientRequestForm());

  editClientRequestForm = form(this.editClientRequestSignal, (path) => {
    required(path.status, { message: 'status.required' })
    required(path.target, { message: 'target.entity.required' })
  });

  loading = computed(
    () => this.clientRequestApiStore.loading() ||
      this.organisationApiStore.loading() ||
      this.individualApiStore.loading(),
  );

  messages =  linkedSignal(() => this.clientRequestApiStore.messages().concat(this.organisationApiStore.messages(), this.individualApiStore.messages()));
  success = linkedSignal(() => this.clientRequestApiStore.success() || this.organisationApiStore.success() || this.individualApiStore.success());
  error = linkedSignal(() => this.clientRequestApiStore.error() || this.organisationApiStore.error() || this.individualApiStore.error());

  readonly targetFilter = signal('Alexander');
  readonly organisationFilter = signal('Veritas');
  readonly selectedTargetType = signal<TargetEntity>(TargetEntity.INDIVIDUAL);
  
  organisationList = linkedSignal(() => this.organisationApiStore.dataList());
  targetOrganisationList = linkedSignal(() => this.organisationApiStore.dataList());
  targetIndividualList = linkedSignal(() => this.individualApiStore.dataList());

  clientRequest = this.clientRequestApiStore.data;

  // Enum options
  ClientRequestStatusT: any = ClientRequestStatus;
  ClientRequestStatusOptions = Object.keys(this.ClientRequestStatusT);

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = [TargetEntity.ORGANISATION, TargetEntity.INDIVIDUAL];

  IndividualIdentityTypeT: any = IndividualIdentityType;
  IndividualIdentityTypeOptions = Object.keys(this.IndividualIdentityTypeT);

  toastr = inject(ToastrService);

  constructor() {
    effect(() => {
      let clientRequest = this.clientRequestApiStore.data();

      this.handleClientRequestSignalUpdate(clientRequest!);
    });

    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toastr.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toastr.error(messages[0]);
      }
    })

    effect(() => {

      let individual = this.individualApiStore.data();

      if (!individual) {
        return;
      }

      let target: IndividualListDTO = {
        id: individual.id,
        name: individual.firstName + ' ' + individual.surname,
        identityType: individual.identityType,
        identityNo: individual.identityNo,
        emailAddress: individual.emailAddress,
        kycStatus: individual.kycStatus,
        sex: individual.sex,
        pepStatus: individual.pepStatus,
        userCreated: individual.userCreated,
        physicalAddress: individual.physicalAddress,
        postalAddress: individual.postalAddress,
      }

      this.targetIndividualList.set([{
        ...target
      }]);

      this.editClientRequestSignal.update((value) => ({
        ...value,
        targetObject: target
      }));
    });

    effect(() => {

      let organisation = this.organisationApiStore.data();

      if (!organisation) {
        return;
      }

      let target: OrganisationListDTO = {
        id: organisation.id,
        code: organisation.code,
        name: organisation.name,
        registrationNo: organisation.registrationNo,
        contactEmailAddress: organisation.contactEmailAddress,
        status: organisation.status,
        isClient: organisation.isClient,
        kycStatus: organisation.kycStatus,
        keycloakId: organisation.keycloakId,
        postalAddress: organisation.postalAddress,
        physicalAddress: organisation.physicalAddress,
      }

      this.targetOrganisationList.set([{
        ...target
      }]);

      this.editClientRequestSignal.update((value) => ({
        ...value,
        targetObject: target
      }));
    });

  }

  ngOnInit(): void {
    const query = this.route.snapshot.queryParamMap;
    this.prefillFromQuery(query);

    if(this.id) {

      this.clientRequestApiStore.findById({ id: this.id });
    }
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  saveChanges(): void {
    if (this.editClientRequestForm().invalid()) {
      Swal.fire({
        icon: 'error',
        title: 'Form Invalid',
        text: 'Please fill in all required fields before saving.',
      });
      return;
    }

    const formData = this.editClientRequestSignal();
    const clientRequest = new ClientRequestDTO();

    clientRequest.id = formData.id;
    clientRequest.createdAt = formData.createdAt;
    clientRequest.createdBy = formData.createdBy;
    clientRequest.modifiedAt = formData.modifiedAt;
    clientRequest.modifiedBy = formData.modifiedBy;
    clientRequest.status = formData.status;
    clientRequest.organisationId = formData.organisation?.id;
    clientRequest.organisationCode = formData.organisation?.code;
    clientRequest.organisation = formData.organisation ? formData.organisation.name : '';
    clientRequest.organisationRegistrationNo = formData.organisation ? formData.organisation.registrationNo : '';
    clientRequest.target = formData.target;
    clientRequest.targetId = formData.targetObject?.id;
    clientRequest.documentId = formData.documentId;
    clientRequest.fileName = formData.fileName;
    clientRequest.fileUrl = formData.fileUrl;

    if (formData.target === TargetEntity.ORGANISATION) {

      clientRequest.name = (formData.targetObject as OrganisationListDTO)?.name;
      clientRequest.registration = (formData.targetObject as OrganisationListDTO)?.registrationNo;

    } else if (formData.target === TargetEntity.INDIVIDUAL) {

      clientRequest.name = ((formData.targetObject as IndividualListDTO)?.name) ;
      clientRequest.registration = (formData.targetObject as IndividualListDTO)?.identityNo;
      clientRequest.identityType = (formData.targetObject as IndividualListDTO)?.identityType || null;

    }

    this.clientRequestApiStore.save({ clientRequest: clientRequest });
  }

  discardChanges(): void {

    this.editClientRequestSignal.set(new EditClientRequestForm());
  }

  handleClientRequestSignalUpdate(clientRequest: ClientRequestDTO) {
    if (!clientRequest) {
        return;
      }

      let org: OrganisationListDTO = new OrganisationListDTO();
      org.id = clientRequest.organisationId;
      org.name = clientRequest.organisation;
      org.registrationNo = clientRequest.organisationRegistrationNo;
      org.contactEmailAddress = '';
      org.status = '';
      org.postalAddress = '';
      org.physicalAddress = '';


      let target: OrganisationListDTO | IndividualListDTO;

      if(clientRequest.target === TargetEntity.ORGANISATION) {

        this.organisationApiStore.findById({ id: clientRequest.targetId });

      } else {

        this.individualApiStore.findById({ id: clientRequest.targetId });
        
      }

      this.editClientRequestSignal.update((value) => ({
        ...value,
        id: clientRequest.id,
        createdAt: clientRequest.createdAt,
        createdBy: clientRequest.createdBy,
        modifiedAt: clientRequest.modifiedAt,
        modifiedBy: clientRequest.modifiedBy,
        status: clientRequest.status,
        organisation: clientRequest.organisationId ? org : null,
        documentId: clientRequest.documentId,
        fileName: clientRequest.fileName,
        fileUrl: clientRequest.fileUrl,
        target: clientRequest.target || null,
        targetObject: null,
        organisationFilter: '',
        targetOrganisationFilter: '',
        targetIndividualFilter: '',
        targetKycStatus: clientRequest.targetKycStatus || null,
      }));

      if (clientRequest.organisationId) {
        this.organisationList.set([{
          ...org
        }]);
      }
  }

  // selectTarget(record: TargetRecord): void {
  //   this.selectedTarget.set(record);
  // }

  // selectOrganisation(record: OrganisationRecord): void {
  //   this.selectedOrganisationId.set(record.id);
  // }

  setTargetType(value: TargetEntity): void {
    this.selectedTargetType.set(value);
    this.editClientRequestSignal.update(v => ({ ...v, target: value, targetObject: null }));
  }

  setRequestStatus(value: ClientRequestStatus): void {
    this.editClientRequestSignal.update(v => ({ ...v, status: value }));

    this.clientRequestApiStore.updateStatus({
      id: this.editClientRequestSignal().id,
      status: value
    });
  }

  setKycStatus(value: KycComplianceStatus): void {
    this.editClientRequestSignal.update(v => ({ ...v, targetKycStatus: value }));
  }

  isCurrentRequestStatus(value: ClientRequestStatus): boolean {
    return this.editClientRequestSignal().status === value;
  }

  isCurrentKycStatus(value: KycComplianceStatus): boolean {
    return this.editClientRequestSignal().targetKycStatus === value;
  }

  updateTargetFilter(value: string): void {
    this.targetFilter.set(value);
  }

  updateOrganisationFilter(value: string): void {
    this.organisationFilter.set(value);
  }


  targetTypeLabel(value: TargetEntity): string {
    return value === TargetEntity.INDIVIDUAL ? 'Individual' : 'Organisation';
  }

  requestStatusLabel(value: ClientRequestStatus): string {
    switch (value) {
      case ClientRequestStatus.CONTACTED:
        return 'In Review';
      case ClientRequestStatus.PENDING:
        return 'Pending';
      case ClientRequestStatus.ACCEPTED:
        return 'Completed';
      case ClientRequestStatus.REJECTED:
        return 'Rejected';
    }
  }

  kycStatusLabel(value: KycComplianceStatus): string {
    switch (value) {
      case KycComplianceStatus.CURRENT:
        return 'Verified';
      case KycComplianceStatus.INCOMPLETE:
        return 'Unverified';
      case KycComplianceStatus.ABSENT:
        return 'Flagged';
      case KycComplianceStatus.EXPIRED:
        return 'Expired';
      default:
        return 'Unverified';
    }
  }

  // isOrganisationSelected(record: OrganisationRecord): boolean {
  //   return this.selectedOrganisationId() === record.id;
  // }

  kycToneClass(value: KycComplianceStatus): string {
    switch (value) {
      case KycComplianceStatus.CURRENT:
        return 'verified';
      case KycComplianceStatus.INCOMPLETE:
        return 'unverified';
      case KycComplianceStatus.ABSENT:
        return 'flagged';
      case KycComplianceStatus.EXPIRED:
        return 'expired';
      default:
        return 'underified';
    }
  }

  requestToneClass(value: ClientRequestStatus): string {
    switch (value) {
      case ClientRequestStatus.PENDING:
        return 'active';
      case ClientRequestStatus.ACCEPTED:
        return 'success';
      case ClientRequestStatus.REJECTED:
        return 'danger';
      case ClientRequestStatus.CONTACTED:
      default:
        return 'neutral';
    }
  }


  // Organisation filter methods
  filterOrganisation(): void {
    let criteria = new SearchObject<OrganisationSearchCriteria>();
    criteria.criteria = {
      registrationNo: this.editClientRequestSignal().organisationFilter,
      name: this.editClientRequestSignal().organisationFilter,
      isClient: true
    }
    this.organisationApiStore.search({ criteria: criteria });
  }

  organisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any) {
    return o1 && o2 ? o1.id === o2.id : false;
  }

  // Target organisation filter methods
  filterTargetOrganisation(): void {
    let criteria = new SearchObject<OrganisationSearchCriteria>();
    criteria.criteria = {
      registrationNo: this.editClientRequestSignal().targetObjectFilter,
      name: this.editClientRequestSignal().targetObjectFilter
    }
    this.organisationApiStore.search({ criteria: criteria });
  }

  targetOrganisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any) {
    return o1 && o2 ? o1.id === o2.id : false;
  }

  // Target individual filter methods
  filterTargetIndividual(): void {
    let criteria = new SearchObject<IndividualSearchCriteria>();
    criteria.criteria = {
      identityNo: this.editClientRequestSignal().targetObjectFilter,
      firstName: this.editClientRequestSignal().targetObjectFilter,
      surname: this.editClientRequestSignal().targetObjectFilter
    }
    this.individualApiStore.search({ criteria: criteria });
  }

  individualCompare(o1: IndividualListDTO | any, o2: IndividualListDTO | any) {
    return o1 && o2 ? o1.id === o2.id : false;
  }

  private prefillFromQuery(query: ParamMap): void {
    const target = query.get('target');
    const organisationId = query.get('organisationId');
    const organisationName = query.get('organisationName') || '';
    const organisationRegistrationNo = query.get('organisationRegistrationNo') || '';

    if (target === TargetEntity.INDIVIDUAL || target === TargetEntity.ORGANISATION) {
      this.setTargetType(target);
    }

    if (!organisationId) {
      return;
    }

    const organisation = new OrganisationListDTO();
    organisation.id = organisationId;
    organisation.name = organisationName;
    organisation.registrationNo = organisationRegistrationNo;
    organisation.contactEmailAddress = '';
    organisation.status = '';
    organisation.postalAddress = '';
    organisation.physicalAddress = '';

    this.organisationList.set([{ ...organisation }]);
    this.editClientRequestSignal.update((value) => ({
      ...value,
      organisation,
      organisationFilter: '',
    }));

    // Prefill target entity if provided
    const targetId = query.get('targetId');
    const targetName = query.get('targetName');

    if (target === TargetEntity.INDIVIDUAL && targetId && targetName) {
      const individual = new IndividualListDTO();
      individual.id = targetId;
      individual.name = targetName;
      individual.identityNo = query.get('targetIdentityNo') || '';
      individual.identityType = query.get('targetIdentityType') || null;
      individual.emailAddress = '';

      this.targetIndividualList.set([{ ...individual }]);
      this.editClientRequestSignal.update((value) => ({
        ...value,
        targetObject: individual,
        targetObjectFilter: '',
      }));
    } else if (target === TargetEntity.ORGANISATION && targetId && targetName) {
      const targetOrg = new OrganisationListDTO();
      targetOrg.id = targetId;
      targetOrg.name = targetName;
      targetOrg.registrationNo = query.get('targetRegistrationNo') || '';
      targetOrg.contactEmailAddress = '';
      targetOrg.status = '';
      targetOrg.postalAddress = '';
      targetOrg.physicalAddress = '';

      this.targetOrganisationList.set([{ ...targetOrg }]);
      this.editClientRequestSignal.update((value) => ({
        ...value,
        targetObject: targetOrg,
        targetObjectFilter: '',
      }));
    }
  }
}
