import { CommonModule } from "@angular/common";
import { AfterViewInit, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from "@angular/core";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { Loader } from "@app/@shared/loader/loader";
import { DocumentDTO } from "@app/models/bw/co/centralkyc/document/document-dto";
import { KycComplianceStatus } from "@app/models/bw/co/centralkyc/kyc/kyc-compliance-status";
import { TargetEntity } from "@app/models/bw/co/centralkyc/target-entity";
import { KycRecordApiStore } from "@app/store/bw/co/centralkyc/kyc/kyc-record-api.store";
import { TranslateModule } from "@ngx-translate/core";
import { ToastrService } from "ngx-toastr";
import { IndividualApi } from "@app/services/bw/co/centralkyc/individual/individual-api";
import { OrganisationApi } from "@app/services/bw/co/centralkyc/organisation/organisation-api";
import { IndividualListDTO } from "@app/models/bw/co/centralkyc/individual/individual-list-dto";
import { OrganisationListDTO } from "@app/models/bw/co/centralkyc/organisation/organisation-list-dto";
import { DeclarationDTO } from "@app/models/bw/co/centralkyc/kyc/declaration-dto";
import { SourceOfFunds } from "@app/models/bw/co/centralkyc/source-of-funds";
import { FormControl } from "@angular/forms";
import { applyEach, form, FormField, minLength, required } from "@angular/forms/signals";
import { MaterialModule } from "@app/material.module";
import { NgxMatSelectSearchModule } from "ngx-mat-select-search";
import { PepStatus } from "@app/models/bw/co/centralkyc/individual/pep-status";
import { KycRecordDTO } from "@app/models/bw/co/centralkyc/kyc/kyc-record-dto";
import { KycRecordApi } from "@app/services/bw/co/centralkyc/kyc/kyc-record-api";

class KycRecordForm {

  id: string | any = null; // not displayed
  createdBy: string | any = null; // not displayed
  createdAt: Date | any = null; // not displayed
  modifiedBy: string | any = null; // not displayed
  modifiedAt: Date | any = null; // not displayed
  expiryDate: Date | any = null;
  uploadDate: Date | any = null;
  documents: Array<DocumentDTO> | any = null;
  kycStatus: KycComplianceStatus = KycComplianceStatus.INCOMPLETE;
  owner: OrganisationListDTO | IndividualListDTO | any = null;
  ownerType: TargetEntity | any = null;
  declaration: DeclarationDTO | any = {
    pepStatus: null,
    pepDetails: null,
    sanctionsMatch: null,
    sanctionsDetails: null
  };
  sourceOfFunds: Array<SourceOfFunds> | any = [];
  sourceOfFundsDetails: string | any = null;
  ownerFilter = '';
}

@Component({
  selector: 'app-edit-kyc-record',
  templateUrl: './edit-kyc-record.html',
  styleUrls: ['./edit-kyc-record.scss'],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    FormField,
    Loader,
    RouterModule,
    NgxMatSelectSearchModule
  ]
})
export class EditKycRecord implements OnInit, OnDestroy, AfterViewInit {

  kycRecordApi = inject(KycRecordApi);
  kycRecordApiStore = inject(KycRecordApiStore);
  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());
  error = linkedSignal(() => this.kycRecordApiStore.error());
  kycRecord = linkedSignal(() => this.kycRecordApiStore.data());

  KycComplianceStatusT: any = KycComplianceStatus;
  KycComplianceStatusOptions = Object.keys(this.KycComplianceStatusT);
  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = [TargetEntity.ORGANISATION, TargetEntity.INDIVIDUAL];

  PepStatusT: any = PepStatus;
  PepStatusOptions = Object.keys(this.PepStatusT);

  SourceOfFundsT: any = SourceOfFunds;
  SourceOfFundsOptions = Object.keys(this.SourceOfFundsT);

  // Individual and Organisation APIs
  individualApi = inject(IndividualApi);
  organisationApi = inject(OrganisationApi);

  toaster: ToastrService = inject(ToastrService);
  protected router: Router = inject(Router);
  protected route: ActivatedRoute = inject(ActivatedRoute);

  ownerFilterControl = new FormControl('');

  kycRecordSignal = signal(new KycRecordForm());
  kycRecordSignalForm = form(this.kycRecordSignal, (path) => {
    required(path.owner, { message: 'Select KYC Record owner' });
    required(path.kycStatus, { message: 'Select KYC Record status' });
    required(path.ownerType, { message: 'Select KYC Record owner type' });
    minLength(path.sourceOfFunds, 1, { message: 'Add at least one source of funds' });
  });

  ownerList = signal<Array<OrganisationListDTO | IndividualListDTO>>([]);

  constructor() {

  }

  ngAfterViewInit(): void {

    this.route.queryParams.subscribe(params => {
      const kycRecordId = params['id'];
      if (kycRecordId) {
        this.kycRecordApiStore.findById({ id: kycRecordId });
      }
    });

    // Load settings
    // this.settingsApiStore.getAll();
  }

  // Local editable copy of the record to bind the form to
  // Sync store data into local formData
  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  save(): void {

    this.kycRecordSignalForm().valid

    let form: KycRecordForm = this.kycRecordSignal();

    let kycRecord = new KycRecordDTO();
    kycRecord.id = form.id;
    kycRecord.createdBy = form.createdBy;
    kycRecord.createdAt = form.createdAt;
    kycRecord.modifiedBy = form.modifiedBy;
    kycRecord.modifiedAt = form.modifiedAt;
    kycRecord.expiryDate = form.expiryDate;
    kycRecord.uploadDate = form.uploadDate;
    kycRecord.documents = form.documents;
    kycRecord.kycStatus = form.kycStatus;
    kycRecord.target = form.ownerType;
    kycRecord.declaration = form.declaration;
    kycRecord.sourceOfFunds = form.sourceOfFunds;
    kycRecord.sourceOfFundsDetails = form.sourceOfFundsDetails;

    kycRecord.targetId = form.owner?.id;

    this.kycRecordApi.save(kycRecord).subscribe({
      next: (response) => {
        this.toaster.success('KYC Record saved successfully');
        this.router.navigate(['/kyc', 'details', response.id]);
      },
      error: (error) => {
        this.toaster.error('Failed to save KYC Record');
      }
    });

  }

  cancel(): void {
    this.router.navigate(['/kyc']);
  }

  ownerCompare(o1: OrganisationListDTO | IndividualListDTO, o2: OrganisationListDTO | IndividualListDTO): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  filterOwner() {

    const filterValue = this.ownerFilterControl.value || '';

    this.loading.set(true);

    if (this.kycRecordSignal().ownerType === TargetEntity.ORGANISATION) {

      this.organisationApi.search({ criteria: { name: filterValue } }).subscribe((response: OrganisationListDTO[]) => {
        this.ownerList.set(response);
        this.loading.set(false);
      }, error => {
        this.toaster.error('Failed to load organisations');
        this.ownerList.set([]);
        this.loading.set(false);
      });

    } else if (this.kycRecordSignal().ownerType === TargetEntity.INDIVIDUAL) {

      this.individualApi.search({ criteria: { name: filterValue } }).subscribe((response: IndividualListDTO[]) => {
        this.ownerList.set(response);
        this.loading.set(false);
      }, error => {
        this.toaster.error('Failed to load individuals');
        this.ownerList.set([]);
        this.loading.set(false);
      });

    } else {

      this.ownerList.set([]);
      this.loading.set(false);
    }
  }

  kycRecordFormReset() {

    this.kycRecordSignal.set(new KycRecordForm());
  }

  sourceOfFundsAdd() {

    this.kycRecordSignal.update((value) => ({
      ...value,
      sourceOfFunds: [
        ...value.sourceOfFunds,
        ''
      ]
    }))
  }

  sourceOfFundsRemove(i: number, item: SourceOfFunds) {
    this.kycRecordSignal.update((value) => {
      const sourceOfFunds = value.sourceOfFunds.filter((_: any, index: number) => index !== i);

      return {
        ...value,
        sourceOfFunds: sourceOfFunds
      }
    });
  }
}
