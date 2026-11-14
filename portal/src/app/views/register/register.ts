import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { Loader } from '@app/@shared/loader/loader';
import { form, FormField, minLength, required } from '@angular/forms/signals';
import { TranslateModule } from '@ngx-translate/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatStepperModule, MatStepper } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { swalFire } from '@app/@shared/swal-loader';
import { IndividualDTO } from '@app/models/bw/co/knowvera/individual/individual-dto';
import { OrganisationDTO } from '@app/models/bw/co/knowvera/organisation/organisation-dto';
import { ClientRequestStatus } from '@app/models/bw/co/knowvera/organisation/client/client-request-status';
import { IndividualApiStore } from '@app/store/bw/co/knowvera/individual/individual-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/knowvera/organisation/client/client-request-api.store';
import { ClientRequestApi } from '@app/services/bw/co/knowvera/organisation/client/client-request-api';
import { OrganisationApi } from '@app/services/bw/co/knowvera/organisation/organisation-api';
import { toast } from '@app/@shared/toast';

class RegisterParams {
  // identificationType: string = '';
  identificationNumber: string = '';
  individual: IndividualDTO | null = null;
  organisation: OrganisationDTO | null = null;
  registrationStatus: ClientRequestStatus | null = null;
}

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    TranslateModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    Loader,
    FormField
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register implements OnInit, OnDestroy, AfterViewInit {

  requestId!: string;
  token!: string;

  toastr = toast;

  // ClientRequestStatus enum for template
  ClientRequestStatus = ClientRequestStatus;

  individualApiStore = inject(IndividualApiStore);
  individual = linkedSignal(() => this.individualApiStore.data());

  organisationApiStore = inject(OrganisationApiStore);
  private readonly organisationApi = inject(OrganisationApi);

  /** Who the request is for, from the link: an organisation proves its registration number. */
  isOrganisation = signal(false);
  /** The client organisation of the request, once its registration number is confirmed. */
  requestOrganisation = signal<OrganisationDTO | null>(null);
  organisationLoadFailed = signal(false);
  /** The request's client is known: its details can be confirmed. */
  clientLoaded = computed(() => this.isOrganisation() ? !!this.requestOrganisation()?.id : !!this.individual()?.id);

  clientRequestApi = inject(ClientRequestApi);
  clientRequestApiStore = inject(ClientRequestApiStore);
  route = inject(ActivatedRoute)

  tokenConfirmed = computed(() => this.clientRequestApiStore.tokenConfirmed());
  identityConfirmationToken = computed(() => this.clientRequestApiStore.identityConfirmationToken());
  registrationToken = computed(() => this.clientRequestApiStore.registrationToken());

  confirmingRegistration = false;

  requestEntityLoaded = computed(() => {
    const individualLoaded = this.individualApiStore.registrationIndividualLoaded();
    const organisationLoaded = this.organisationApiStore.registrationOrganisationLoaded();
    return individualLoaded || organisationLoaded;
  });

  registerSignal = signal<RegisterParams>(new RegisterParams());
  individualDetailsConfirmed = signal(false);

  registerForm = form(this.registerSignal, (path) => {
    // required(path.identificationType, { message: 'Please select organisation or individual' });
    required(path.identificationNumber, { message: 'Identification number is required' });
    minLength(path.identificationNumber, 3, { message: 'Identification number must be at least 3 characters long' });
    required(path.registrationStatus, { message: 'Please select registration status' });
  });

  loading = computed(() => this.clientRequestApiStore.loading() || this.individualApiStore.loading() || this.organisationApiStore.loading());
  loaderMessage = computed(() => {
    if (this.clientRequestApiStore.loading()) {
      return this.clientRequestApiStore.loaderMessage();
    } else if (this.individualApiStore.loading()) {
      return this.individualApiStore.loaderMessage();
    } else if (this.organisationApiStore.loading()) {
      return this.organisationApiStore.loaderMessage();
    } else {
      return '';
    }
  });

  @ViewChild('stepper') stepper!: MatStepper;

  protected router: Router = inject(Router);

  constructor() {

    effect(() => {
      if (this.identityConfirmationToken().length > 0) {


      }
    });

    effect(() => {
      if (this.individual()) {
        this.registerSignal.update((data) => ({
          ...data,
          individual: this.individual()
        }));

        if (this.individual()?.id) {
          this.toastr.success('Individual details loaded successfully');
        }
      }
    });

    effect(() => {
      if (this.organisationApiStore.data()) {

        this.registerSignal.update((data) => ({
          ...data,
          organisation: this.organisationApiStore.data()
        }));

        if (this.organisationApiStore.data()?.id) {
          this.toastr.success('Organisation details loaded successfully');
        }
      }
    });

    effect(() => {
      let clientRequest = this.clientRequestApiStore.data();
      if (clientRequest.id && !this.confirmingRegistration) {
        this.toastr.success(`Client request has been ${clientRequest.status.toLowerCase()} successfully`);
      }

      this.confirmingRegistration = false;

    });

  }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.requestId = params['requestId'];
    });

    this.route.queryParams.subscribe(params => {

      this.token = params['token'];
      this.isOrganisation.set(params['target'] === 'ORGANISATION');
    });
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
    if (this.requestId && this.token) {


      this.clientRequestApiStore.confirmToken({ requestId: this.requestId, token: this.token });
    }
  }

  finish() { }

  confirmIdentification(stepper: MatStepper) {

    // The link was not accepted (expired, already used, or the server failed): there is no
    // confirmation token, so loading the individual can only fail
    if (!this.identityConfirmationToken()) {
      this.toastr.error('This registration link could not be confirmed. Please open the link from your email again, or ask for a new one.');
      return;
    }

    const identificationNumber = (this.registerSignal().identificationNumber || '').trim();

    if (this.isOrganisation()) {
      this.organisationLoadFailed.set(false);
      this.organisationApi
        .loadRequestOrganisation(this.requestId, this.identityConfirmationToken(), encodeURIComponent(identificationNumber))
        .subscribe({
          next: (organisation) => {
            this.requestOrganisation.set(organisation);
            this.toastr.success('Organisation details loaded successfully');
          },
          error: () => {
            this.requestOrganisation.set(null);
            this.organisationLoadFailed.set(true);
            this.toastr.error('The registration number does not match this request.');
          },
        });
    } else {
      this.individualApiStore.loadRequestIndividual({
        requestId: this.requestId,
        identityConfirmationToken: this.identityConfirmationToken(),
        identityNo: identificationNumber
      });
    }

    stepper.next();
  }

  detailsConfirmed(stepper: MatStepper) {
    this.individualDetailsConfirmed.set(true);
    stepper.next();
  }

  submitRegistration() {

    swalFire({
      title: 'Are you sure?',
      text: `You have selected to ${this.registerSignal().registrationStatus == ClientRequestStatus.ACCEPTED ? 'accept' : 'reject'}. This action cannot be undone.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.isConfirmed) {
        this.confirmingRegistration = true;
        this.clientRequestApi.confirmRegistration(
          this.requestId,
          this.registerSignal().registrationStatus == ClientRequestStatus.ACCEPTED,
          this.registrationToken()
        ).subscribe({
          next: (accepted) => {
            this.toastr.success(
              accepted
                ? (this.isOrganisation()
                  ? 'Request accepted. The organisation\'s contact will get an email to set a password, then can sign in.'
                  : 'Request accepted. Check your email for a link to set your password, then sign in.')
                : 'Your response has been recorded.',
            );
            this.router.navigate(['/']);
          },
          error: () => {
            this.toastr.error('An error occurred while confirming registration status');
          }
        });

      }
    })
  }
}
