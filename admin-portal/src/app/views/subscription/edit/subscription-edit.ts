import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { disabled, form, FormField, required } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { OrganisationListDTO } from '@app/models/bw/co/knowvera/organisation/organisation-list-dto';
import { OrganisationSearchCriteria } from '@app/models/bw/co/knowvera/organisation/organisation-search-criteria';
import { KycSubsciptionStatus } from '@app/models/bw/co/knowvera/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/knowvera/subscription/kyc-subscription-dto';
import { TimePeriod } from '@app/models/bw/co/knowvera/time-period';
import { SearchObject } from '@app/models/search-object';
import { KycSubscriptionApi } from '@app/services/bw/co/knowvera/subscription/kyc-subscription-api';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/knowvera/subscription/kyc-subscription-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';

export class EditSubscriptionVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  status: KycSubsciptionStatus | any = null;
  organisation: OrganisationListDTO | any = null;
  organisationFilter: OrganisationListDTO | any = null;
  ref: string | any = null;
  period: TimePeriod | any = null;
  startDate: Date | any = null;
  endDate: Date | any = null;
  amount: number | any = null;
}
@Component({
  selector: 'app-subscription-edit',
  templateUrl: './subscription-edit.html',
  styleUrls: ['./subscription-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatCardModule,
    MatDividerModule,
    MatIconModule,
    MatTooltipModule,
    CommonModule,
    FormField,
    TranslateModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    Loader,
    NgxMatSelectSearchModule,
    MatDatepickerModule
  ]
})
export class SubscriptionEdit implements OnInit, AfterViewInit, OnDestroy {

  organisationApiStore = inject(OrganisationApiStore);
  toaster: ToastrService = inject(ToastrService);
  readonly kycSubscriptionApi = inject(KycSubscriptionApi);
  readonly kycSubscriptionApiStore = inject(KycSubscriptionApiStore);

  editSubscriptionVarsForm: EditSubscriptionVarsForm = new EditSubscriptionVarsForm();
  editSubscriptionSignal = signal(this.editSubscriptionVarsForm);
  editSubscriptionSignalForm = form(this.editSubscriptionSignal, (path) => {
    required(path.status, { message: 'status.required' })
    required(path.organisation, { message: 'organisation.required' })
    required(path.period, { message: 'period.required' })
    required(path.startDate, { message: 'start.date.required' })
    required(path.amount, { message: 'amount.required' });
    disabled(path.ref);
  });

  @Input() id: string | any;

  statuses = Object.values(KycSubsciptionStatus);
  periods = Object.values(TimePeriod);

  loading = linkedSignal(() => this.kycSubscriptionApiStore.loading());
  error = linkedSignal(() => this.kycSubscriptionApiStore.error());
  messages = linkedSignal(() => this.kycSubscriptionApiStore.messages());
  success = linkedSignal(() => this.kycSubscriptionApiStore.success());

  constructor() {

    this.kycSubscriptionApiStore.reset();
    

    effect(() => {
      const organisations = this.organisationApiStore.dataList();
      // this.organisationFilteredList.set(organisations);
    });

    effect(() => {
      let subscription = this.kycSubscriptionApiStore.data();
      if (subscription) {

        this.editSubscriptionSignal.update((form) => ({
          id: subscription.id,
          createdAt: subscription.createdAt,
          createdBy: subscription.createdBy,
          modifiedAt: subscription.modifiedAt,
          modifiedBy: subscription.modifiedBy,
          organisation: subscription.organisationId ? {
            id: subscription.organisationId,
            name: subscription.organisationName,
            code: subscription.organisationCode,
            registrationNo: subscription.organisationRegistrationNo,
            status: '',
            contactEmailAddress: ''
          } : null,
          ref: subscription.ref,
          startDate: subscription.startDate,
          endDate: subscription.endDate,
          amount: subscription.amount,
          status: subscription.status,
          period: subscription.period,
          organisationFilter: ''
        }));
      }
    });
  }

  ngOnInit(): void {

    this.organisationApiStore.getAll();
    if (this.id) {
      this.kycSubscriptionApiStore.findById({ id: this.id });
    }
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  organisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any) {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  filterOrganisation() {
    const filterValue = this.editSubscriptionSignal().organisationFilter || '';
    let search = new SearchObject<OrganisationSearchCriteria>();
    search.criteria = {
      name: filterValue,
      isClient: true
    };

    this.organisationApiStore.search({
      criteria: search,
    });
  }

  subscriptionSave(): void {
    let formValue = this.editSubscriptionSignal();
    console.log(formValue)

    let subscription = new KycSubscriptionDTO();
    subscription.id = formValue.id;
    subscription.ref = formValue.ref;
    subscription.organisationName = formValue.organisation?.name;
    subscription.organisationId = formValue.organisation?.id;
    subscription.organisationCode = formValue.organisation?.code;
    subscription.organisationRegistrationNo = formValue.organisation?.registrationNo;
    subscription.status = formValue.status;
    subscription.startDate = formValue.startDate;
    subscription.endDate = formValue.endDate;
    subscription.amount = formValue.amount;
    subscription.period = formValue.period;
    subscription.createdBy = formValue.createdBy;
    subscription.createdAt = formValue.createdAt;
    subscription.modifiedBy = formValue.modifiedBy;
    subscription.modifiedAt = formValue.modifiedAt;

    this.kycSubscriptionApiStore.save({ subscription });
  }

  selectionChanged(org: OrganisationListDTO): void {

    console.log(org);

    this.editSubscriptionSignal.update((form) => ({
      ...form,
      organisation: org,
      organisationFilter: org.code + ' - ' + org.name
    }));
  }
}
