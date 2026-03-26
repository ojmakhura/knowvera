import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnInit,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { Loader } from '@app/@shared/loader/loader';
import { TimePeriod } from '@app/models/bw/co/centralkyc/time-period';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { form, required } from '@angular/forms/signals';
import { KycInvoiceDTO } from '@app/models/bw/co/centralkyc/invoice/kyc-invoice-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';

export class EditInvoiceVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  organisation: OrganisationListDTO | any = null;
  organisationFilter: OrganisationListDTO | any = null;
  ref: string | any = null;
  subscriptionPeriod: TimePeriod | any = null;
  startDate: Date | any = null;
  endDate: Date | any = null;
  amount: number | any = null;
  paid: boolean | any = null;
  paymentDate: Date | any = null;
  paymentReference: string | any = null;
}

@Component({
  selector: 'app-invoice-edit',
  templateUrl: './invoice-edit.html',
  styleUrls: ['./invoice-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatButtonToggleModule,
    TranslateModule,
    Loader,
  ],
})
export class InvoiceEdit implements OnInit {
  editInvoiceVarsForm: EditInvoiceVarsForm = new EditInvoiceVarsForm();
  editInvoiceSignal = signal(this.editInvoiceVarsForm);
  editInvoiceSignalForm = form(this.editInvoiceSignal, (path) => {
    required(path.ref, { message: 'ref.required' });
    required(path.organisation, { message: 'organisation.required' });
    required(path.subscriptionPeriod, { message: 'subscription.period.required' });
    required(path.amount, { message: 'amount.required' });
  });

  protected route: ActivatedRoute = inject(ActivatedRoute);
  protected router: Router = inject(Router);
  toaster: ToastrService = inject(ToastrService);
  readonly kycInvoiceApiStore = inject(KycInvoiceApiStore);

  readonly timePeriodOptions = Object.values(TimePeriod);
  loaderMessage = signal('');
  messages = linkedSignal(() => this.kycInvoiceApiStore.messages());
  loading = linkedSignal(() => this.kycInvoiceApiStore.loading());
  organisationLoading = linkedSignal(() => this.organisationApiStore.loading());
  private readonly saveRequested = signal(false);

  organisationApiStore = inject(OrganisationApiStore);
  protected readonly organisationFilteredList = signal<OrganisationListDTO[]>([]);
  private readonly allOrganisations = signal<OrganisationListDTO[]>([]);

  @Input() id: string | null = null;

  constructor() {
    effect(() => {
      const organisations = this.organisationApiStore.dataList();

      this.allOrganisations.set(organisations || []);

      if (!this.editInvoiceSignal().organisationFilter) {
        this.organisationFilteredList.set(organisations || []);
        return;
      }

      this.applyOrganisationFilter(String(this.editInvoiceSignal().organisationFilter || ''));
    });

    this.kycInvoiceApiStore.reset();
    this.organisationApiStore.reset();

    effect(() => {
      const invoice = this.kycInvoiceApiStore.data();

      if (!invoice) {
        return;
      }

      this.editInvoiceSignal.update((form) => ({
        ...form,
        id: invoice.id,
        createdAt: invoice.createdAt,
        createdBy: invoice.createdBy,
        modifiedAt: invoice.modifiedAt,
        modifiedBy: invoice.modifiedBy,
        organisation: invoice.organisationId ? {
          id: invoice.organisationId,
          name: invoice.organisationName,
          code: invoice.organisationCode || (invoice as any).organisatonCode,
          registrationNo: invoice.organisationRegistrationNo,
          status: '',
          contactEmailAddress: ''
        } : null,
        organisationFilter: invoice.organisationName || '',
        ref: invoice.ref,
        subscriptionPeriod: invoice.subscriptionPeriod,
        startDate: invoice.startDate,
        endDate: invoice.endDate,
        amount: invoice.amount,
        paid: Boolean(invoice.paid),
        paymentDate: invoice.paymentDate,
        paymentReference: invoice.paymentReference,
      }));

      if (invoice.organisationId) {
        this.organisationFilteredList.set([{
          id: invoice.organisationId,
          name: invoice.organisationName,
          code: invoice.organisationCode || (invoice as any).organisatonCode,
          registrationNo: invoice.organisationRegistrationNo,
          status: '',
          contactEmailAddress: '',
          isClient: false,
          kycStatus: '',
          keycloakId: ''
        }]);
      }
    });

    effect(() => {
      const org = this.organisationApiStore.data();

      if (!(org?.id)) {
        return;
      }

      this.editInvoiceSignal.update((form) => ({
        ...form,
        organisation: {
          id: org.id,
          name: org.name,
          code: org.code,
          registrationNo: org.registrationNo,
          status: org.status,
          contactEmailAddress: org.contactEmailAddress
        },
        organisationFilter: org.name,
      }));

      this.organisationFilteredList.set([{
        id: org.id,
        name: org.name,
        code: org.code,
        registrationNo: org.registrationNo,
        status: org.status,
        contactEmailAddress: org.contactEmailAddress,
        isClient: org.isClient,
        kycStatus: org.kycStatus,
        keycloakId: org.keycloakId
      }]);
    });

    effect(() => {
      if (!this.saveRequested() || this.loading()) {
        return;
      }

      if (this.kycInvoiceApiStore.error()) {
        this.toaster.error(this.messages()[0] || 'Failed to save invoice.');
        this.saveRequested.set(false);
        return;
      }

      const savedInvoice = this.kycInvoiceApiStore.data();

      if (savedInvoice?.id) {
        this.toaster.success(this.messages()[0] || 'Invoice saved successfully.');
        this.saveRequested.set(false);

        if (!this.id && !this.route.snapshot.queryParamMap.get('id')) {
          this.router.navigate(['/', 'invoice', 'edit', savedInvoice.id]);
        }
      }
    });
  }

  ngOnInit(): void {
    this.organisationApiStore.getAll();

    if (this.id) {
      this.kycInvoiceApiStore.findById({ id: this.id });
    }

    const routeId = this.route.snapshot.queryParamMap.get('id');
    const organisationId = this.route.snapshot.queryParamMap.get('organisationId');

    if (routeId) {
      this.kycInvoiceApiStore.findById({ id: routeId });
    }

    if (organisationId) {
      this.organisationApiStore.findById({ id: organisationId });
    }
  }

  updateField<K extends keyof EditInvoiceVarsForm>(field: K, value: EditInvoiceVarsForm[K]): void {
    this.editInvoiceSignal.update((form) => ({
      ...form,
      [field]: value,
    }));
  }

  organisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any) {
    return o1 && o2 && o1.id === o2.id;
  }

  filterOrganisation() {
    this.applyOrganisationFilter(String(this.editInvoiceSignal().organisationFilter || ''));
  }

  selectOrganisation(organisationId: string): void {
    const organisation = this.allOrganisations().find((item) => item.id === organisationId)
      || this.organisationFilteredList().find((item) => item.id === organisationId)
      || null;

    this.editInvoiceSignal.update((form) => ({
      ...form,
      organisation,
      organisationFilter: organisation?.name || '',
    }));
  }

  saveInvoice(): void {
    if (this.editInvoiceSignalForm().invalid()) {
      this.toaster.error('Complete the required invoice fields before saving.');
      return;
    }

    this.saveRequested.set(true);
    this.kycInvoiceApiStore.save({ invoice: this.buildInvoicePayload() });
  }

  cancel(): void {
    this.router.navigate(['/', 'invoice']);
  }

  periodLabel(value: TimePeriod): string {
    return value.charAt(0) + value.slice(1).toLowerCase();
  }

  organisationOptionLabel(organisation: OrganisationListDTO): string {
    return `${organisation.name || 'Unnamed'}${organisation.code ? ' • ' + organisation.code : ''}`;
  }

  organisationSupportLabel(): string {
    const organisation = this.editInvoiceSignal().organisation;

    if (!organisation) {
      return 'Select the legal entity that owns this billing record.';
    }

    return organisation.registrationNo || organisation.contactEmailAddress || organisation.code || '';
  }

  formatAuditDate(value: Date | string | null | undefined): string {
    if (!value) {
      return 'Not available';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return String(value);
    }

    return new Intl.DateTimeFormat('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  }

  asDateInput(value: Date | string | null | undefined): string {
    if (!value) {
      return '';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return '';
    }

    return date.toISOString().slice(0, 10);
  }

  private applyOrganisationFilter(rawValue: string): void {
    const value = rawValue.trim().toLowerCase();
    const organisations = this.allOrganisations();

    if (!value) {
      this.organisationFilteredList.set(organisations);
      return;
    }

    this.organisationFilteredList.set(
      organisations.filter((organisation) =>
        [organisation.name, organisation.code, organisation.registrationNo]
          .filter(Boolean)
          .some((item) => String(item).toLowerCase().includes(value)),
      ),
    );
  }

  private buildInvoicePayload(): KycInvoiceDTO {
    const current = this.editInvoiceSignal();
    const invoice = new KycInvoiceDTO();

    invoice.id = current.id;
    invoice.createdAt = current.createdAt;
    invoice.createdBy = current.createdBy;
    invoice.modifiedAt = current.modifiedAt;
    invoice.modifiedBy = current.modifiedBy;
    invoice.ref = current.ref;
    invoice.organisationId = current.organisation?.id || null;
    invoice.organisationName = current.organisation?.name || null;
    invoice.organisationCode = current.organisation?.code || null;
    (invoice as any).organisatonCode = current.organisation?.code || null;
    invoice.organisationRegistrationNo = current.organisation?.registrationNo || null;
    invoice.subscriptionPeriod = current.subscriptionPeriod || null;
    invoice.startDate = current.startDate || null;
    invoice.endDate = current.endDate || null;
    invoice.amount = current.amount === '' || current.amount === null ? null : Number(current.amount);
    invoice.paid = Boolean(current.paid);
    invoice.paymentDate = current.paid ? current.paymentDate || null : null;
    invoice.paymentReference = current.paid ? current.paymentReference || null : null;

    return invoice;
  }
}
