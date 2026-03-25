import { CommonModule } from '@angular/common';
import { OrganisationApiStore } from './../../../store/bw/co/centralkyc/organisation/organisation-api.store';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { applyEach, email, form, FormField, maxLength, minLength, required } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { Loader } from '@app/@shared/loader/loader';
import { GeneralStatus } from '@app/models/bw/co/centralkyc/general-status';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { OrganisationDomain } from '@app/models/bw/co/centralkyc/organisation/organisation-domain';
import { OrganisationDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-dto';
import { PhoneNumber } from '@app/models/bw/co/centralkyc/phone-number';
import { PhoneType } from '@app/models/bw/co/centralkyc/phone-type';
import { TranslateModule } from '@ngx-translate/core';
import { MatFormFieldModule } from '@angular/material/form-field';


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
    FormField,
    TranslateModule,
    RouterLink,
    Loader,
    MatFormFieldModule
  ]
})
export class OrganisationEdit implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string | any = null;

  organisationApiStore = inject(OrganisationApiStore);

  editOrganisationVarsForm: EditOrganisationVarsForm = new EditOrganisationVarsForm();
  editOrganisationSignal = signal(this.editOrganisationVarsForm);
  editOrganisationSignalForm = form(this.editOrganisationSignal, (path) => {
    required(path.isClient, { message: 'is.client.required' });
    required(path.status, { message: 'status.required' });
    required(path.kycStatus, { message: 'kyc.status.required' });
    required(path.registrationNo, { message: 'registration.no.required' });
    required(path.code, { message: 'code.required' });
    minLength(path.code, 3, { message: 'code.min.length' });
    maxLength(path.code, 6, { message: 'code.max.length' });
    required(path.name, { message: 'name.required' });
    required(path.countryOfRegistration, { message: 'country.of.registration.required' });
    required(path.description, { message: 'description.required' });
    required(path.postalAddress, { message: 'postal.address.required' });
    required(path.physicalAddress, { message: 'physical.address.required' });
    required(path.domains, { message: 'domains.required' });
    email(path.contactEmailAddress, { message: 'contact.email.address.invalid' });
    applyEach(path.phoneNumbers, (phonePath) => {
      required(phonePath.type, { message: 'phone.type.required' });
      required(phonePath.phoneNumber, { message: 'phone.number.required' });
    });
  });

  KycComplianceStatusT: any = KycComplianceStatus;
  KycComplianceStatusOptions = Object.keys(this.KycComplianceStatusT);
  GeneralStatusT: any = GeneralStatus;
  GeneralStatusOptions = Object.keys(this.GeneralStatusT);
  PhoneTypeT: any = PhoneType;
  PhoneTypeOptions = Object.keys(this.PhoneTypeT);
  newPhoneNumber = signal('');
  newPhoneType = signal<PhoneType>(PhoneType.MOBILE);
  newDomainName = signal('');

  loaderMessage = signal('');
  messages = linkedSignal(() => this.organisationApiStore.messages());
  success = linkedSignal(() => this.organisationApiStore.success());
  loading = linkedSignal(() => this.organisationApiStore.loading());
  error = linkedSignal(() => this.organisationApiStore.error());
  selected: any = null;

  countries: string[] = [
    'Unknown', 'Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Antigua and Barbuda', 'Argentina', 'Armenia', 'Australia', 'Austria',
    'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados', 'Belarus', 'Belgium', 'Belize', 'Benin', 'Bhutan',
    'Bolivia', 'Bosnia and Herzegovina', 'Botswana', 'Brazil', 'Brunei', 'Bulgaria', 'Burkina Faso', 'Burundi', 'Cabo Verde', 'Cambodia',
    'Cameroon', 'Canada', 'Central African Republic', 'Chad', 'Chile', 'China', 'Colombia', 'Comoros', 'Congo', 'Costa Rica',
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

  constructor() {

    effect(() => {

      const organisation = this.organisationApiStore.data();

      if (organisation) {
        this.editOrganisationSignal.update((value) => ({
          ...value,
          ...organisation,
        }));
      }
    });
  }

  ngOnInit(): void {

    this.organisationApiStore.reset();

    if (this.id && this.id !== '') {
      this.organisationApiStore.findById({ id: this.id });
    }

  }

  ngAfterViewInit(): void { }

  ngOnDestroy(): void { }

  addPhoneNumber(): void {

    const phoneNumber = this.newPhoneNumber().trim();
    if (!phoneNumber) {
      return;
    }

    const phone: PhoneNumber = {
      type: this.newPhoneType(),
      phoneNumber
    };

    this.editOrganisationSignal.update((value) => ({
      ...value,
      phoneNumbers: [...(value.phoneNumbers ?? []), phone]
    }));

    this.newPhoneNumber.set('');
  }

  removePhoneNumber(index: number): void {
    this.editOrganisationSignal.update((value) => ({
      ...value,
      phoneNumbers: (value.phoneNumbers ?? []).filter((_, i) => i !== index)
    }));
  }

  addDomain(): void {

    const name = this.newDomainName().trim();
    if (!name) {
      return;
    }

    const domain: OrganisationDomain = {
      name,
      verified: false
    };

    this.editOrganisationSignal.update((value) => ({
      ...value,
      domains: [...(value.domains ?? []), domain]
    }));

    this.newDomainName.set('');
  }

  removeDomain(index: number): void {
    this.editOrganisationSignal.update((value) => ({
      ...value,
      domains: (value.domains ?? []).filter((_, i) => i !== index)
    }));
  }

  saveOrganisation(): void {

    this.editOrganisationSignalForm().invalid()

    let value = this.editOrganisationSignal();
    let org = {
      code: value.code,
      description: value.description,
      domains: value.domains,
      phoneNumbers: value.phoneNumbers,
      status: value.status,
      postalAddress: value.postalAddress,
      physicalAddress: value.physicalAddress,
      contactEmailAddress: value.contactEmailAddress,
      name: value.name,
      registrationNo: value.registrationNo,
      createdAt: value.createdAt,
      createdBy: value.createdBy,
      modifiedAt: value.modifiedAt,
      modifiedBy: value.modifiedBy,
      isClient: value.isClient,
      kycStatus: value.kycStatus,
      countryOfRegistration: value.countryOfRegistration,
      id: value.id
    } as OrganisationDTO;

    this.organisationApiStore.save({ organisation: org });
  }
}
