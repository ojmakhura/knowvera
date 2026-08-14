import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { OrganisationApiStore } from './../../../store/bw/co/knowvera/organisation/organisation-api.store';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { applyEach, email, form, FormField, required } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { Loader } from '@app/@shared/loader/loader';
import { GeneralStatus } from '@app/models/bw/co/knowvera/general-status';
import { KycComplianceStatus } from '@app/models/bw/co/knowvera/kyc/kyc-compliance-status';
import { OrganisationDomain } from '@app/models/bw/co/knowvera/organisation/organisation-domain';
import { OrganisationDTO } from '@app/models/bw/co/knowvera/organisation/organisation-dto';
import { PhoneNumber } from '@app/models/bw/co/knowvera/phone-number';
import { PhoneType } from '@app/models/bw/co/knowvera/phone-type';
import { TranslateModule } from '@ngx-translate/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { swalFire } from '@app/@shared/swal';
import { ToastrService } from 'ngx-toastr';

export class EditOrganisationVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  isClient: boolean | any = null;
  status: GeneralStatus | any = null;
  kycStatus: KycComplianceStatus | any = null;
  registrationNo: string | any = null;
  code: string | any = null;
  name: string | any = null;
  countryOfRegistration: string | any = null;
  description: string | any = null;
  phoneNumbers: Array<PhoneNumber> = [];
  contactEmailAddress: string | any = null;
  postalAddress: string | any = null;
  physicalAddress: string | any = null;
  domains: Array<OrganisationDomain> = [];
}

@Component({
  selector: 'app-organisation-edit',
  templateUrl: './organisation-edit.html',
  styleUrl: './organisation-edit.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    MatFormFieldModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatListModule,
    MatDividerModule,
    MatSlideToggleModule,
    MatCheckboxModule,
    Loader,
    FormField
],
})
export class OrganisationEdit implements OnInit, AfterViewInit, OnDestroy {
  @Input() id: string | any = null;

  editOrganisationFormSignal = signal(new EditOrganisationVarsForm());
  editOrganisationFormSignalForm = form(this.editOrganisationFormSignal, (path) => {
    required(path.status, { message: 'status.required' });
    required(path.kycStatus, { message: 'kyc.status.required' });
    required(path.registrationNo, { message: 'registration.no.required' });
    required(path.code, { message: 'code.required' });
    required(path.name, { message: 'name.required' });
    required(path.countryOfRegistration, { message: 'country.of.registration.required' });
    required(path.postalAddress, { message: 'postal.address.required' });
    required(path.physicalAddress, { message: 'physical.address.required' });
    email(path.contactEmailAddress, { message: 'contact.email.invalid' });
    applyEach(path.phoneNumbers, (phonePath) => {
      required(phonePath.type, { message: 'phone.type.required' });
      required(phonePath.phoneNumber, { message: 'phone.number.required' });
    });
    applyEach(path.domains, (domainPath) => {
      required(domainPath.name, { message: 'domain.name.required' });
    });
  });


  countries: string[] = [
    'Unknown', 'Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Antigua and Barbuda', 'Argentina', 'Armenia', 'Australia', 'Austria',
    'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados', 'Belarus', 'Belgium', 'Belize', 'Benin', 'Bhutan',
    'Bolivia', 'Bosnia and Herzegovina', 'Botswana', 'Brazil', 'Brunei', 'Bulgaria', 'Burkina Faso', 'Burundi', 'Cabo Verde', 'Cambodia',
    'Cameroon', 'Canada', 'Knowvera African Republic', 'Chad', 'Chile', 'China', 'Colombia', 'Comoros', 'Congo', 'Costa Rica',
    'Croatia', 'Cuba', 'Cyprus', 'Czech Republic', 'Denmark', 'Djibouti', 'Dominica', 'Dominican Republic', 'Ecuador', 'Egypt',
    'El Salvador', 'Equatorial Guinea', 'Eritrea', 'Estonia', 'Eswatini', 'Ethiopia', 'Fiji', 'Finland', 'France', 'Gabon',
    'Gambia', 'Georgia', 'Germany', 'Ghana', 'Greece', 'Grenada', 'Guatemala', 'Guinea', 'Guinea-Bissau', 'Guyana',
    'Haiti', 'Honduras', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran', 'Iraq', 'Ireland', 'Israel',
    'Italy', 'Jamaica', 'Japan', 'Jordan', 'Kazakhstan', 'Kenya', 'Kiribati', 'Korea, North', 'Korea, South', 'Kosovo',
    'Kuwait', 'Kyrgyzstan', 'Laos', 'Latvia', 'Lebanon', 'Lesotho', 'Liberia', 'Libya', 'Liechtenstein', 'Lithuania',
    'Luxembourg', 'Madagascar', 'Malawi', 'Malaysia', 'Maldives', 'Mali', 'Malta', 'Marshall Islands', 'Mauritania', 'Mauritius',
    'Mexico', 'Micronesia', 'Moldova', 'Monaco', 'Mongolia', 'Montenegro', 'Morocco', 'Mozambique', 'Myanmar', 'Namibia',
    'Nauru', 'Nepal', 'Netherlands', 'New Zealand', 'Nicaragua', 'Niger', 'Nigeria', 'North Macedonia', 'Norway', 'Oman',
    'Pakistan', 'Palau', 'Palestine', 'Panama', 'Papua New Guinea', 'Paraguay', 'Peru', 'Philippines', 'Poland', 'Portugal',
    'Qatar', 'Romania', 'Russia', 'Rwanda', 'Saint Kitts and Nevis', 'Saint Lucia', 'Saint Vincent and the Grenadines', 'Samoa', 'San Marino', 'Sao Tome and Principe',
    'Saudi Arabia', 'Senegal', 'Serbia', 'Seychelles', 'Sierra Leone', 'Singapore', 'Slovakia', 'Slovenia', 'Solomon Islands', 'Somalia',
    'South Africa', 'South Sudan', 'Spain', 'Sri Lanka', 'Sudan', 'Suriname', 'Sweden', 'Switzerland', 'Syria', 'Taiwan',
    'Tajikistan', 'Tanzania', 'Thailand', 'Timor-Leste', 'Togo', 'Tonga', 'Trinidad and Tobago', 'Tunisia', 'Turkey', 'Turkmenistan',
    'Tuvalu', 'Uganda', 'Ukraine', 'United Arab Emirates', 'United Kingdom', 'United States', 'Uruguay', 'Uzbekistan', 'Vanuatu', 'Vatican City',
    'Venezuela', 'Vietnam', 'Yemen', 'Zambia', 'Zimbabwe'
  ];

  organisationApiStore = inject(OrganisationApiStore);
  router = inject(Router);
  toaster = inject(ToastrService);

  loaderMessage = linkedSignal(() => 'Loading...');
  messages = linkedSignal(() => this.organisationApiStore.messages());
  success = linkedSignal(() => this.organisationApiStore.success());
  loading = linkedSignal(() => this.organisationApiStore.loading());
  error = linkedSignal(() => this.organisationApiStore.error());

  generalStatuses = Object.values(GeneralStatus);
  kycComplianceStatuses = Object.values(KycComplianceStatus);
  phoneTypes = Object.values(PhoneType);
  readonly breadcrumbLabel = 'Organisations';

  constructor() {
    effect(() => {
      const item = this.organisationApiStore.data();
      if (item && item.id) {
        this.editOrganisationFormSignal.set(item as any);
      }
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
  }

  ngOnInit(): void {
    this.organisationApiStore.reset();
    if (this.id) {
      this.organisationApiStore.findById({ id: this.id });
    }
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  saveOrganisation(): void {
    const formData = this.editOrganisationFormSignal();
    this.organisationApiStore.save({ organisation: formData as any });
  }

  cancel(): void {
    this.router.navigate(['/organisation']);
  }

  toggleIsClient(event: any): void {
    this.editOrganisationFormSignal.update((val) => {
      return {
        ...val,
        isClient: event.target.checked,
      };
    });
  }

  addPhoneNumber(): void {
    this.editOrganisationFormSignal.update((val) => {
      return {
        ...val,
        phoneNumbers: [...(val.phoneNumbers || []), { type: null, phoneNumber: '' } as any],
      };
    });
  }

  updatePhoneType(index: number, type: string): void {
    this.editOrganisationFormSignal.update((val) => {
      let clone = [...(val.phoneNumbers || [])];
      (clone[index] as any).type = type;
      return {
        ...val,
        phoneNumbers: clone,
      };
    });
  }

  updatePhoneNumber(index: number, numberStr: string): void {
    this.editOrganisationFormSignal.update((val) => {
      let clone = [...(val.phoneNumbers || [])];
      (clone[index] as any).phoneNumber = numberStr;
      return {
        ...val,
        phoneNumbers: clone,
      };
    });
  }

  removePhoneNumber(index: number): void {

    swalFire({
      title: 'Are you sure?',
      text: 'This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!',
    }).then((result) => {
      if (result.isConfirmed) {
        this.editOrganisationFormSignal.update((val) => {
          let clone = [...(val.phoneNumbers || [])];
          clone.splice(index, 1);
          return {
            ...val,
            phoneNumbers: clone,
          };
        });
        swalFire('Deleted!', 'The phone number has been deleted.', 'success');
      }
    });
  }

  addDomain(): void {
    this.editOrganisationFormSignal.update((val) => ({
      ...val,
      domains: [...(val.domains || []), { name: '', verified: false } as any],
    }));
  }

  updateDomainName(index: number, name: string): void {
    this.editOrganisationFormSignal.update((val) => {
      const clone = [...(val.domains || [])];
      clone[index] = {
        ...clone[index],
        name,
      } as any;
      return {
        ...val,
        domains: clone,
      };
    });
  }

  toggleDomainUnverified(index: number, isUnverified: boolean): void {
    this.editOrganisationFormSignal.update((val) => {
      const clone = [...(val.domains || [])];
      clone[index] = {
        ...clone[index],
        verified: !isUnverified,
      } as any;
      return {
        ...val,
        domains: clone,
      };
    });
  }

  removeDomain(index: number): void {
    this.editOrganisationFormSignal.update((val) => {
      const clone = [...(val.domains || [])];
      clone.splice(index, 1);
      return {
        ...val,
        domains: clone,
      };
    });
  }

  domainVerified(domain: OrganisationDomain | null | undefined): boolean {
    return Boolean((domain as any)?.verified ?? (domain as any)?.isVerified ?? (domain as any)?.status === 'VERIFIED');
  }

  formatAuditDate(value: Date | string | null | undefined): string {
    if (!value) {
      return 'Not available';
    }

    const date = value instanceof Date ? value : new Date(value);
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
}
