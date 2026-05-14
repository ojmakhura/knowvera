import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, computed, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { applyEach, email, form, required, FormField } from '@angular/forms/signals';
import { GeneralStatus } from '@app/models/bw/co/centralkyc/general-status';
import { EmploymentStatus } from '@app/models/bw/co/centralkyc/individual/employment-status';
import { IndividualIdentityType } from '@app/models/bw/co/centralkyc/individual/individual-identity-type';
import { MaritalStatus } from '@app/models/bw/co/centralkyc/individual/marital-status';
import { PepStatus } from '@app/models/bw/co/centralkyc/individual/pep-status';
import { Sex } from '@app/models/bw/co/centralkyc/individual/sex';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { BranchDTO } from '@app/models/bw/co/centralkyc/organisation/branch/branch-dto';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { PhoneNumber } from '@app/models/bw/co/centralkyc/phone-number';
import { PhoneType } from '@app/models/bw/co/centralkyc/phone-type';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { BranchApiStore } from '@app/store/bw/co/centralkyc/organisation/branch/branch-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { SearchObject } from '@app/models/search-object';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { Loader } from '@app/@shared/loader/loader';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { IndividualDTO } from '@app/models/bw/co/centralkyc/individual/individual-dto';

export class EditIndividualVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  kycStatus: KycComplianceStatus | any = null;
  kycStatusFilter: KycComplianceStatus | any = null;
  identityType: IndividualIdentityType | any = null;
  identityTypeFilter: IndividualIdentityType | any = null;
  identityNo: string | any = null;
  sex: Sex | any = null;
  firstName: string | any = null;
  middleName: string | any = null;
  surname: string | any = null;
  phoneNumbers: Array<PhoneNumber> = [];
  nationality: string | any = null;
  postalAddress: string | any = null;
  physicalAddress: string | any = null;
  emailAddress: string | any = null;
  maritalStatus: MaritalStatus | any = null;
  dateOfBirth: Date | any = null;
  employmentStatus: EmploymentStatus | any = null;
  hasUser: boolean | any = null;
  organisation: OrganisationListDTO | any = null;
  organisationFilter: OrganisationListDTO | any = null;
  branch: BranchDTO | any = null;
  branchFilter: BranchDTO | any = null;
}

@Component({
  selector: 'app-individual-edit',
  templateUrl: './individual-edit.html',
  styleUrls: ['./individual-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatDividerModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatDatepickerModule,
    FormField,
    NgxMatSelectSearchModule,
    TranslateModule,
    Loader
  ]
})
export class IndividualEdit implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string | any;

  organisationApiStore = inject(OrganisationApiStore);
  branchApiStore = inject(BranchApiStore);
  readonly individualApiStore = inject(IndividualApiStore);
  readonly router = inject(Router);
  loading = computed(
    () => this.individualApiStore.loading() || this.organisationApiStore.loading() || this.branchApiStore.loading(),
  );
  isSaving = signal(false);

  organisationList = linkedSignal(() => this.organisationApiStore.dataList());
  branchList = linkedSignal(() => this.branchApiStore.dataList());

  filteredOrganisationList = linkedSignal(() => this.organisationApiStore.dataList());

  filteredBranchList = linkedSignal(() => this.branchApiStore.dataList());

  error = linkedSignal(() => this.individualApiStore.error());
  messages = linkedSignal(() => this.individualApiStore.messages());
  success = linkedSignal(() => this.individualApiStore.success());

  individual = this.individualApiStore.data;

  editIndividualVarsForm: EditIndividualVarsForm = new EditIndividualVarsForm();
  editIndividualSignal = signal(this.editIndividualVarsForm);
  editIndividualSignalForm = form(this.editIndividualSignal, (path) => {
    required(path.kycStatus, { message: 'kyc.status.required' })
    required(path.identityType, { message: 'identity.type.required' })
    required(path.identityNo, { message: 'identity.no.required' })
    required(path.sex, { message: 'sex.required' })
    required(path.firstName, { message: 'first.name.required' })
    required(path.surname, { message: 'surname.required' })
    required(path.nationality, { message: 'nationality.required' })
    required(path.emailAddress, { message: 'email.address.required' })
    email(path.emailAddress, { message: 'email.address.invalid' });
    required(path.maritalStatus, { message: 'marital.status.required' })
    required(path.employmentStatus, { message: 'employment.status.required' })
    required(path.hasUser, { message: 'has.user.required' });
    email(path.emailAddress, { message: 'email.address.invalid' });
    applyEach(path.phoneNumbers, (phonePath) => {
      required(phonePath.type, { message: 'phone.type.required' });
      required(phonePath.phoneNumber, { message: 'phone.number.required' });
    });
  });

  IndividualIdentityTypeT: any = IndividualIdentityType;
  IndividualIdentityTypeOptions = Object.keys(this.IndividualIdentityTypeT);
  KycComplianceStatusT: any = KycComplianceStatus;
  KycComplianceStatusOptions = Object.keys(this.KycComplianceStatusT);
  PepStatusT: any = PepStatus;
  PepStatusOptions = Object.keys(this.PepStatusT);
  EmploymentStatusT: any = EmploymentStatus;
  EmploymentStatusOptions = Object.keys(this.EmploymentStatusT);
  SexT: any = Sex;
  SexOptions = Object.keys(this.SexT);
  PhoneTypeT: any = PhoneType;
  PhoneTypeOptions = Object.keys(this.PhoneTypeT);
  newPhoneType = signal<PhoneType>(PhoneType.MOBILE);
  newPhoneNumber = signal('');
  GeneralStatusT: any = GeneralStatus;
  GeneralStatusOptions = Object.keys(this.GeneralStatusT);
  MaritalStatusT: any = MaritalStatus;
  MaritalStatusOptions = Object.keys(this.MaritalStatusT);


  loaderMessage = signal('');
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

      const individual = this.individualApiStore.data();

      if (individual) {
        this.editIndividualSignal.update((value) => ({
          ...value,
          ...individual,
        }));

        if (individual.organisation) {
          // this.organisationSearch.set(this.organisationOptionLabel(individual.organisation));
          this.branchApiStore.findByOrganisation({ organisationId: individual.organisation.id });
        }

        if (individual.branch) {
          // this.branchSearch.set(this.branchOptionLabel(individual.branch));
        }
      }
    });

    effect(() => {
      if (!this.isSaving()) {
        return;
      }

      if (this.success() && !this.loading()) {
        this.isSaving.set(false);
        this.router.navigate(['/individual', 'details', this.individual().id || this.editIndividualSignal().id]);
      }

      if (this.error() && !this.loading()) {
        this.isSaving.set(false);
      }
    });
  }

  ngOnInit(): void {

    this.individualApiStore.reset();
    this.organisationApiStore.reset();
    this.branchApiStore.reset();
    this.organisationApiStore.getAll();

    if (this.id && this.id !== '') {
      this.individualApiStore.findById({ id: this.id });
    }

  }

  ngAfterViewInit(): void { }

  ngOnDestroy(): void { }

  cancel(): void {
    const targetId = this.editIndividualSignal().id || this.id;

    if (targetId) {
      this.router.navigate(['/individual', 'details', targetId]);
      return;
    }

    this.router.navigate(['/individual']);
  }

  addPhoneNumber(): void {
    this.editIndividualSignal.update((value) => ({
      ...value,
      phoneNumbers: [
        ...(value.phoneNumbers ?? []),
        { type: PhoneType.MOBILE, phoneNumber: '' } as PhoneNumber,
      ],
    }));
  }

  updatePhoneType(index: number, type: PhoneType): void {
    this.editIndividualSignal.update((value) => {
      const phoneNumbers = [...(value.phoneNumbers ?? [])];
      phoneNumbers[index] = {
        ...phoneNumbers[index],
        type,
      } as PhoneNumber;

      return {
        ...value,
        phoneNumbers,
      };
    });
  }

  updatePhoneNumber(index: number, phoneNumber: string): void {
    this.editIndividualSignal.update((value) => {
      const phoneNumbers = [...(value.phoneNumbers ?? [])];
      phoneNumbers[index] = {
        ...phoneNumbers[index],
        phoneNumber,
      } as PhoneNumber;

      return {
        ...value,
        phoneNumbers,
      };
    });
  }

  phoneNumbersRemove(i: number) {
    this.editIndividualSignal.update((value) => {
      const phoneNumbers = value.phoneNumbers.filter((_: any, index: number) => index !== i);

      return {
        ...value,
        phoneNumbers: phoneNumbers
      }
    });
  }

  organisationCompare(o1: OrganisationListDTO | any, o2: OrganisationListDTO | any) {
    return o1 && o2 && o1.id === o2.id;
  }

  branchCompare(b1: BranchDTO | any, b2: BranchDTO | any) {
    return b1 && b2 && b1.id === b2.id;
  }


  save(): void {
    this.isSaving.set(true);

    let individual = new IndividualDTO();
    individual = {
      ...individual,
      ...this.editIndividualSignal(),
    };

    if (individual.dateOfBirth instanceof Date) {
      const d = individual.dateOfBirth as Date;
      individual.dateOfBirth = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    }

    this.individualApiStore.save({ individual: individual });
  }

  filterOrganisations() {
    let criteria = new SearchObject<OrganisationSearchCriteria>();
    criteria.criteria = {
      name: this.editIndividualSignal().organisationFilter,
    };
    this.organisationApiStore.search({ criteria });
  }

  organisationSelected() {

    console.log(this.editIndividualSignal().organisation);
    this.branchApiStore.findByOrganisation({ organisationId: this.editIndividualSignal().organisation?.id });
  }

}
