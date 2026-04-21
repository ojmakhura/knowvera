import { CommonModule } from '@angular/common';
import { AfterViewChecked, AfterViewInit, Component, computed, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { Loader } from '@app/@shared/loader/loader';
import { form, FormField, minLength, required } from '@angular/forms/signals';
import { TranslateModule } from '@ngx-translate/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatStepperModule, MatStepper } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { ToastrService } from 'ngx-toastr';
import { MatRadioModule } from '@angular/material/radio';
import Swal from 'sweetalert2';
import { IndividualDTO } from '@app/models/bw/co/centralkyc/individual/individual-dto';
import { OrganisationDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-dto';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/centralkyc/organisation/client/client-request-api.store';
import { ClientRequestApi } from '@app/services/bw/co/centralkyc/organisation/client/client-request-api';

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
    MatCardModule,
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

  toastr = inject(ToastrService);

  // ClientRequestStatus enum for template
  ClientRequestStatus = ClientRequestStatus;

  individualApiStore = inject(IndividualApiStore);
  individual = linkedSignal(() => this.individualApiStore.data());

  organisationApiStore = inject(OrganisationApiStore);

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

        console.log('IDENTITY CONFIRMATION TOKEN:', this.identityConfirmationToken());

      }
    });

    effect(() => {
      if (this.individual()) {
        console.log('LOADED INDIVIDUAL:', this.individual());
        this.registerSignal.update((data) => ({
          ...data,
          individual: this.individual()
        }));

        if (this.individual()?.id) {
          this.toastr.success('Individual details loaded successfully', 'Success');
        }
      }
    });

    effect(() => {
      if (this.organisationApiStore.data()) {
        console.log('LOADED ORGANISATION:', this.organisationApiStore.data());

        this.registerSignal.update((data) => ({
          ...data,
          organisation: this.organisationApiStore.data()
        }));

        if (this.organisationApiStore.data()?.id) {
          this.toastr.success('Organisation details loaded successfully', 'Success');
        }
      }
    });

    effect(() => {
      let clientRequest = this.clientRequestApiStore.data();
      if (clientRequest.id && !this.confirmingRegistration) {
        this.toastr.success(`Client request has been ${clientRequest.status.toLowerCase()} successfully`, 'Success');
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
    });
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
    if (this.requestId && this.token) {

      console.log('CONFIRMING TOKEN WITH REQUEST ID:', this.requestId, ' AND TOKEN:', this.token);

      this.clientRequestApiStore.confirmToken({ requestId: this.requestId, token: this.token });
    }
  }

  finish() { }

  confirmIdentification(stepper: MatStepper) {

    this.individualApiStore.loadRequestIndividual({
      requestId: this.requestId,
      identityConfirmationToken: this.identityConfirmationToken(),
      identityNo: this.registerSignal().identificationNumber || ''
    });

    stepper.next();
  }

  detailsConfirmed(stepper: MatStepper) {
    this.individualDetailsConfirmed.set(true);
    stepper.next();
  }

  submitRegistration() {

    Swal.fire({
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
          next: (res) => {
            this.toastr.success('Registration status confirmed successfully', 'Success');
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.toastr.error('An error occurred while confirming registration status', 'Error');
          }
        });

      }
    })
  }
}
