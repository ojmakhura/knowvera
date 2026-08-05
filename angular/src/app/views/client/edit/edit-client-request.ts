import { CommonModule } from "@angular/common";
import { AfterViewInit, Component, computed, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from "@angular/core";
import { email, FormField, form, required } from "@angular/forms/signals";
import { FormsModule } from "@angular/forms";
import { Loader } from "@app/@shared/loader/loader";
import { MaterialModule } from "@app/material.module";
import { ClientRequestApiStore } from "@app/store/bw/co/knowvera/organisation/client/client-request-api.store";
import { TranslateModule } from "@ngx-translate/core";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { ClientRequestDTO } from "@app/models/bw/co/knowvera/organisation/client/client-request-dto";
import { ClientRequestStatus } from "@app/models/bw/co/knowvera/organisation/client/client-request-status";
import { TargetEntity } from "@app/models/bw/co/knowvera/target-entity";
import { IndividualIdentityType } from "@app/models/bw/co/knowvera/individual/individual-identity-type";
import { OrganisationApiStore } from "@app/store/bw/co/knowvera/organisation/organisation-api.store";
import { IndividualApiStore } from "@app/store/bw/co/knowvera/individual/individual-api.store";
import { OrganisationListDTO } from "@app/models/bw/co/knowvera/organisation/organisation-list-dto";
import { IndividualListDTO } from "@app/models/bw/co/knowvera/individual/individual-list-dto";
import { SearchObject } from "@app/models/search-object";
import { OrganisationSearchCriteria } from "@app/models/bw/co/knowvera/organisation/organisation-search-criteria";
import { IndividualSearchCriteria } from "@app/models/bw/co/knowvera/individual/individual-search-criteria";
import { MatDialog } from "@angular/material/dialog";
import { ToastrService } from "ngx-toastr";
import { MatSelectChange, MatSelectModule } from "@angular/material/select";
import { NgxMatSelectSearchModule } from "ngx-mat-select-search";
import { Sex } from "@app/models/bw/co/knowvera/individual/sex";

export class EditClientRequestVarsForm {
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
  selector: 'app-edit-client-request',
  templateUrl: './edit-client-request.html',
  styleUrls: ['./edit-client-request.scss'],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    Loader,
    FormField,
    MatSelectModule,
    NgxMatSelectSearchModule
  ],
})
export class EditClientRequestComponent implements OnInit, AfterViewInit, OnDestroy {

  @Input() id?: string;

  readonly clientRequestApiStore = inject(ClientRequestApiStore);
  readonly organisationApiStore = inject(OrganisationApiStore);
  readonly individualApiStore = inject(IndividualApiStore);
  protected route: ActivatedRoute = inject(ActivatedRoute);
  // protected router: Router = inject(Router);
  dialog: MatDialog = inject(MatDialog);
  toaster: ToastrService = inject(ToastrService);

  editClientRequestSignal = signal<EditClientRequestVarsForm>(new EditClientRequestVarsForm());

  editClientRequestForm = form(this.editClientRequestSignal, (path) => {
    required(path.status, { message: 'status.required' })
    required(path.target, { message: 'target.entity.required' })
  });

  loading = computed(
    () => this.clientRequestApiStore.loading() ||
      this.organisationApiStore.loading() ||
      this.individualApiStore.loading(),
  );

  organisationList = linkedSignal(() => this.organisationApiStore.dataList());
  targetOrganisationList = linkedSignal(() => this.organisationApiStore.dataList());
  targetIndividualList = linkedSignal(() => this.individualApiStore.dataList());

  error = linkedSignal(() => this.clientRequestApiStore.error());
  messages = linkedSignal(() => this.clientRequestApiStore.messages());
  success = linkedSignal(() => this.clientRequestApiStore.success());

  clientRequest = this.clientRequestApiStore.data;

  // Enum options
  ClientRequestStatusT: any = ClientRequestStatus;
  ClientRequestStatusOptions = Object.keys(this.ClientRequestStatusT);

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = [TargetEntity.ORGANISATION, TargetEntity.INDIVIDUAL];

  IndividualIdentityTypeT: any = IndividualIdentityType;
  IndividualIdentityTypeOptions = Object.keys(this.IndividualIdentityTypeT);

  constructor() {
    effect(() => {
      let clientRequest = this.clientRequestApiStore.data();

      this.handleClientRequestUpdate(clientRequest!);
    });

    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
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
        userCreated: individual.userCreated
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
        keycloakId: organisation.keycloakId
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
    this.clientRequestApiStore.reset();
  }

  handleClientRequestUpdate(clientRequest: ClientRequestDTO) {
    if (!clientRequest) {
        return;
      }

      let org: OrganisationListDTO = new OrganisationListDTO();
      org.id = clientRequest.organisationId;
      org.name = clientRequest.organisation;
      org.registrationNo = clientRequest.organisationRegistrationNo;
      org.contactEmailAddress = '';
      org.status = '';

      let target: OrganisationListDTO | IndividualListDTO;

      if(clientRequest.target === TargetEntity.ORGANISATION) {

        target = new OrganisationListDTO();
        (target as OrganisationListDTO).id = clientRequest.targetId;
        (target as OrganisationListDTO).name = clientRequest.name;
        (target as OrganisationListDTO).registrationNo = clientRequest.registration;
        (target as OrganisationListDTO).contactEmailAddress = '';
        (target as OrganisationListDTO).status = '';

        this.targetOrganisationList.set([{
          ...(target as OrganisationListDTO)
        }]);

      } else {

        target = new IndividualListDTO();
        (target as IndividualListDTO).id = clientRequest.targetId;
        (target as IndividualListDTO).name = clientRequest.name;
        (target as IndividualListDTO).identityType = clientRequest.identityType || null;
        (target as IndividualListDTO).identityNo = clientRequest.registration;
        (target as IndividualListDTO).emailAddress = '';

        this.targetIndividualList.set([{
          ...(target as IndividualListDTO)
        }]);
      }

      this.editClientRequestSignal.update((value) => ({
        ...value,
        id: clientRequest.id,
        createdAt: clientRequest.createdAt,
        createdBy: clientRequest.createdBy,
        modifiedAt: clientRequest.modifiedAt,
        modifiedBy: clientRequest.modifiedBy,
        status: clientRequest.status,
        organisationId: clientRequest.organisationId,
        organisation: clientRequest.organisationId ? org : null,
        documentId: clientRequest.documentId,
        fileName: clientRequest.fileName,
        fileUrl: clientRequest.fileUrl,
        target: clientRequest.target || null,
        targetObject: target || null,
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

  ngAfterViewInit(): void {

    this.route.queryParams.subscribe((params: any) => {
      if (params.id) {
        this.clientRequestApiStore.findById(params);
      } else if (params.organisationId) {
        // Pre-fill organisation if coming from organisation details
        this.editClientRequestSignal.update((value) => ({
          ...value,
          organisationId: params.organisationId,

        }));
      }

      if( params.targetIndividual) {
        // Pre-fill organisation if coming from individual details

        this.individualApiStore.findById({ id: params.targetIndividual });

        let targetObject = new IndividualListDTO();
        targetObject.id = params.targetIndividual;
        this.editClientRequestSignal.update((value) => ({
          ...value,
          target: TargetEntity.INDIVIDUAL,
          targetObject: targetObject
        }));

      }

      if( params.targetOrganisation) {
        // Pre-fill organisation if coming from individual details

        this.organisationApiStore.findById({ id: params.targetOrganisation });

        let targetObject = new OrganisationListDTO();
        targetObject.id = params.targetOrganisation;
        this.editClientRequestSignal.update((value) => ({
          ...value,
          target: TargetEntity.ORGANISATION,
          targetObject: targetObject
        }));

      }
    });

    this.route.params.subscribe((params: any) => {
      if (params.id) {
        this.clientRequestApiStore.findById(params);
      }
    });
  }

  ngOnDestroy(): void {
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

  // Form actions
  editClientRequestFormReset() {
    this.editClientRequestSignal.set(new EditClientRequestVarsForm());
  }

  editClientRequestFormSave() {
    if (this.editClientRequestForm().invalid()) {
      this.toaster.error('Please fill all required fields');
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

  editClientRequestFormDelete() {
    if (!this.editClientRequestSignal().id) {
      return;
    }

    if (confirm('Are you sure you want to delete this client request?')) {
      // this.clientRequestApiStore.delete({ id: this.editClientRequestSignal().id });
    }
  }

  targetEntitySelectionChange(event: MatSelectChange<TargetEntity>) {

    console.log('Target entity changed:', event.value);

    if(event.value === TargetEntity.ORGANISATION) {
      this.editClientRequestSignal.update((value) => ({
        ...value,
        targetObject: null,
        targetOrganisationFilter: '',
        targetIndividualFilter: ''
      }));
    } else if(event.value === TargetEntity.INDIVIDUAL) {
      this.editClientRequestSignal.update((value) => ({
        ...value,
        targetObject: null,
        targetOrganisationFilter: '',
        targetIndividualFilter: ''
      }));
    }
  }
}
