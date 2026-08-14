import { CommonModule } from "@angular/common";
import { AfterViewInit, ChangeDetectionStrategy, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from "@angular/core";
import { applyEach, email, form, FormField, required } from "@angular/forms/signals";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { Loader } from "@app/@shared/loader/loader";
import { EmploymentStatus } from "@app/models/bw/co/knowvera/individual/employment-status";
import { IndividualIdentityType } from "@app/models/bw/co/knowvera/individual/individual-identity-type";
import { MaritalStatus } from "@app/models/bw/co/knowvera/individual/marital-status";
import { Sex } from "@app/models/bw/co/knowvera/individual/sex";
import { KycComplianceStatus } from "@app/models/bw/co/knowvera/kyc/kyc-compliance-status";
import { BranchDTO } from "@app/models/bw/co/knowvera/organisation/branch/branch-dto";
import { OrganisationListDTO } from "@app/models/bw/co/knowvera/organisation/organisation-list-dto";
import { PhoneNumber } from "@app/models/bw/co/knowvera/phone-number";
import { TranslateModule } from "@ngx-translate/core";
import { NgxMatSelectSearchModule } from "ngx-mat-select-search";
import { swalFire } from '@app/@shared/swal-loader';
import { AppEnvStore } from "@app/store/app-env.state";
import { OrganisationApiStore } from "@app/store/bw/co/knowvera/organisation/organisation-api.store";
import { BranchApiStore } from "@app/store/bw/co/knowvera/organisation/branch/branch-api.store";
import { IndividualApiStore } from "@app/store/bw/co/knowvera/individual/individual-api.store";
import { Router } from "@angular/router";
import { PhoneType } from "@app/models/bw/co/knowvera/phone-type";
import { GeneralStatus } from "@app/models/bw/co/knowvera/general-status";
import { IndividualDTO } from "@app/models/bw/co/knowvera/individual/individual-dto";
import { ToastrService } from "ngx-toastr";

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
    selector: 'app-edit-individual',
    templateUrl: './edit-individual.html',
    styleUrls: ['./edit-individual.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [
        CommonModule,
        FormsModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,
        MatSlideToggleModule,
        MatFormFieldModule,
        FormField,
        NgxMatSelectSearchModule,
        TranslateModule,
        Loader
    ]
})
export class EditIndividual implements OnInit, AfterViewInit, OnDestroy {
    
    appEnvStore = inject(AppEnvStore);
    organisationApiStore = inject(OrganisationApiStore);
    branchApiStore = inject(BranchApiStore);
    readonly individualApiStore = inject(IndividualApiStore);
    readonly router = inject(Router);
    readonly toaster = inject(ToastrService);
    loading = computed(
        () => this.individualApiStore.loading() || this.organisationApiStore.loading() || this.branchApiStore.loading(),
    );
    isSaving = signal(false);
    submitted = signal(false);

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
        required(path.kycStatus, { message: 'kyc.status.required' });
        required(path.identityType, { message: 'identity.type.required' });
        required(path.identityNo, { message: 'identity.no.required' });
        required(path.sex, { message: 'sex.required' });
        required(path.firstName, { message: 'first.name.required' });
        required(path.surname, { message: 'surname.required' });
        required(path.nationality, { message: 'nationality.required' });
        required(path.emailAddress, { message: 'email.address.required' });
        email(path.emailAddress, { message: 'email.address.invalid' });
        required(path.maritalStatus, { message: 'marital.status.required' });
        required(path.employmentStatus, { message: 'employment.status.required' });
        required(path.hasUser, { message: 'has.user.required' });
        applyEach(path.phoneNumbers, (phonePath) => {
            required(phonePath.type, { message: 'phone.type.required' });
            required(phonePath.phoneNumber, { message: 'phone.number.required' });
        });
    });

    IndividualIdentityTypeT: typeof IndividualIdentityType = IndividualIdentityType;
    IndividualIdentityTypeOptions = Object.values(this.IndividualIdentityTypeT);
    KycComplianceStatusT: typeof KycComplianceStatus = KycComplianceStatus;
    KycComplianceStatusOptions = Object.values(this.KycComplianceStatusT);
    EmploymentStatusT: typeof EmploymentStatus = EmploymentStatus;
    EmploymentStatusOptions = Object.values(this.EmploymentStatusT);
    SexT: typeof Sex = Sex;
    SexOptions = Object.values(this.SexT);
    PhoneTypeT: typeof PhoneType = PhoneType;
    PhoneTypeOptions = Object.values(this.PhoneTypeT);
    GeneralStatusT: typeof GeneralStatus = GeneralStatus;
    GeneralStatusOptions = Object.values(this.GeneralStatusT);
    MaritalStatusT: typeof MaritalStatus = MaritalStatus;
    MaritalStatusOptions = Object.values(this.MaritalStatusT);


    loaderMessage = computed(() => {
        if (this.individualApiStore.loading()) {
            return this.individualApiStore.loaderMessage();
        }

        if (this.organisationApiStore.loading()) {
            return this.organisationApiStore.loaderMessage();
        }

        if (this.branchApiStore.loading()) {
            return this.branchApiStore.loaderMessage();
        }

        return '';
    });

    readonly selectedOrganisationId = computed(() => this.editIndividualSignal().organisation?.id ?? '');
    readonly selectedBranchId = computed(() => this.editIndividualSignal().branch?.id ?? '');
    readonly auditIdentifier = computed(() => this.editIndividualSignal().id || this.appEnvStore.individual()?.id || 'Pending');

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

    constructor() {

        effect(() => {

            const id = this.appEnvStore.individual()?.id;

            if (id) {
                this.individualApiStore.findById({ id });
            }

            const userOrganisationId = this.appEnvStore.userOrganisation()?.id;
            if (userOrganisationId) {
                this.organisationApiStore.getAll();
            }

            if (!userOrganisationId) {
                this.organisationApiStore.getAll();
            }

        });

        effect(() => {
            const individual = this.individual();

            if (!individual?.id) {
                return;
            }

            this.editIndividualSignal.set(this.toFormValue(individual));
        });

        effect(() => {
            const organisationId = this.selectedOrganisationId();

            if (organisationId) {
                this.branchApiStore.findByOrganisation({ organisationId });
                return;
            }

            this.filteredBranchList.set([]);
        });

        effect(() => {
            this.filteredOrganisationList.set(this.organisationList());
        });

        effect(() => {
            this.filteredBranchList.set(this.branchList());
        });

        effect(() => {
            if (!this.success() || this.loading()) {
                return;
            }

            const message = this.messages()[0] || 'Individual saved successfully';
            this.toaster.success(message, 'Success');
            this.isSaving.set(false);
        });

        effect(() => {
            if (!this.error() || this.loading()) {
                return;
            }

            const message = this.messages()[0] || 'Unable to save individual';
            this.toaster.error(message, 'Error');
            this.isSaving.set(false);

        });
    }

    ngOnInit(): void {
        // Initialization logic here
    }

    ngAfterViewInit(): void {
        // Logic after the view has been initialized
    }

    ngOnDestroy(): void {
        // Cleanup logic here
    }

    saveIndividual(): void {
        this.submitted.set(true);

        if (this.loading()) {
            return;
        }

        this.isSaving.set(true);
        this.individualApiStore.save({ individual: this.toIndividualDto(this.editIndividualSignal()) });
    }

    cancel(): void {
        this.router.navigate(['/individual']);
    }

    toggleHasUser(checked: boolean): void {
        this.editIndividualSignal.update((value) => ({
            ...value,
            hasUser: checked,
        }));
    }

    addPhoneNumber(): void {
        this.editIndividualSignal.update((value) => ({
            ...value,
            phoneNumbers: [...(value.phoneNumbers || []), { type: PhoneType.MOBILE, phoneNumber: '' }],
        }));
    }

    updatePhoneType(index: number, type: PhoneType): void {
        this.editIndividualSignal.update((value) => {
            const phoneNumbers = [...(value.phoneNumbers || [])];
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

    onPhoneTypeSelectionChange(index: number, nextType: string): void {
        const resolvedType = this.PhoneTypeOptions.find((type) => type === nextType);
        if (!resolvedType) {
            return;
        }

        this.updatePhoneType(index, resolvedType);
    }

    updatePhoneNumber(index: number, phoneNumber: string): void {
        this.editIndividualSignal.update((value) => {
            const phoneNumbers = [...(value.phoneNumbers || [])];
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

    removePhoneNumber(index: number): void {
        swalFire({
            title: 'Remove phone number?',
            text: 'This number will be removed from the record.',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Remove',
            cancelButtonText: 'Keep it',
        }).then((result) => {
            if (!result.isConfirmed) {
                return;
            }

            this.editIndividualSignal.update((value) => {
                const phoneNumbers = [...(value.phoneNumbers || [])];
                phoneNumbers.splice(index, 1);

                return {
                    ...value,
                    phoneNumbers,
                };
            });
        });
    }

    filterOrganisations(search: string): void {
        const query = search.trim().toLowerCase();
        const organisations = this.organisationList();

        if (!query) {
            this.filteredOrganisationList.set(organisations);
            return;
        }

        this.filteredOrganisationList.set(
            organisations.filter((organisation) =>
                `${organisation.name ?? ''} ${organisation.code ?? ''} ${organisation.registrationNo ?? ''}`
                    .toLowerCase()
                    .includes(query),
            ),
        );
    }

    filterBranches(search: string): void {
        const query = search.trim().toLowerCase();
        const branches = this.branchList();

        if (!query) {
            this.filteredBranchList.set(branches);
            return;
        }

        this.filteredBranchList.set(
            branches.filter((branch) =>
                `${branch.name ?? ''} ${branch.code ?? ''} ${branch.description ?? ''}`
                    .toLowerCase()
                    .includes(query),
            ),
        );
    }

    selectOrganisation(organisation: OrganisationListDTO | null): void {
        this.editIndividualSignal.update((value) => ({
            ...value,
            organisation,
            branch: organisation ? value.branch : null,
        }));
    }

    onOrganisationSelectionChange(organisationId: string): void {
        if (!organisationId) {
            this.selectOrganisation(null);
            return;
        }

        const selectedOrganisation =
            this.organisationList().find((organisation) => String(organisation.id) === organisationId) ?? null;
        this.selectOrganisation(selectedOrganisation);
    }

    selectBranch(branch: BranchDTO | null): void {
        this.editIndividualSignal.update((value) => ({
            ...value,
            branch,
        }));
    }

    onBranchSelectionChange(branchId: string): void {
        if (!branchId) {
            this.selectBranch(null);
            return;
        }

        const selectedBranch = this.branchList().find((branch) => String(branch.id) === branchId) ?? null;
        this.selectBranch(selectedBranch);
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

    private toFormValue(individual: IndividualDTO): EditIndividualVarsForm {
        return {
            id: individual.id,
            createdAt: individual.createdAt,
            createdBy: individual.createdBy,
            modifiedAt: individual.modifiedAt,
            modifiedBy: individual.modifiedBy,
            kycStatus: individual.kycStatus,
            kycStatusFilter: individual.kycStatus,
            identityType: individual.identityType,
            identityTypeFilter: individual.identityType,
            identityNo: individual.identityNo,
            sex: individual.sex,
            firstName: individual.firstName,
            middleName: individual.middleName,
            surname: individual.surname,
            phoneNumbers: [...(individual.phoneNumbers || [])],
            nationality: individual.nationality,
            postalAddress: individual.postalAddress,
            physicalAddress: individual.physicalAddress,
            emailAddress: individual.emailAddress,
            maritalStatus: individual.maritalStatus,
            dateOfBirth: individual.dateOfBirth,
            employmentStatus: individual.employmentStatus,
            hasUser: Boolean(individual.hasUser),
            organisation: individual.organisation,
            organisationFilter: individual.organisation,
            branch: individual.branch,
            branchFilter: individual.branch,
        };
    }

    private toIndividualDto(value: EditIndividualVarsForm): IndividualDTO {
        return {
            ...new IndividualDTO(),
            ...value,
            phoneNumbers: [...(value.phoneNumbers || [])],
            hasUser: Boolean(value.hasUser),
        };
    }
}