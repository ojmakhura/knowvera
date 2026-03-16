import { CommonModule, JsonPipe } from '@angular/common';
import { AfterViewInit, Component, effect, inject, linkedSignal, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycRecordApi } from '@app/services/bw/co/centralkyc/kyc/kyc-record-api';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import Keycloak from 'keycloak-js';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { Loader } from '@app/@shared/loader/loader';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-kyc-record',
  imports: [CommonModule, Loader],
  templateUrl: './kyc-record.html',
  styleUrl: './kyc-record.scss',
  providers: [
    CommonModule,
    Loader
  ]
})
export class KycRecord implements OnInit, OnDestroy, AfterViewInit {

  settingsApiStore = inject(SettingsApiStore);
  kycRecordApiStore = inject(KycRecordApiStore);

  indKycDocuments = linkedSignal(() => this.settingsApiStore.data().indKycDocuments);
  orgKycDocuments = linkedSignal(() => this.settingsApiStore.data().orgKycDocuments);

  myRecords = linkedSignal(() => this.kycRecordApiStore.data());

  record = linkedSignal(() => this.kycRecordApiStore.data());

  selectedFile: File | null = null;
  selectedDocumentType: string = '';

  private keycloak = inject(Keycloak);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  toaster: ToastrService = inject(ToastrService);

  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  error = linkedSignal(() => this.kycRecordApiStore.error());
  messages = linkedSignal(() => this.kycRecordApiStore.messages());
  loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());
  success = linkedSignal(() => this.kycRecordApiStore.success());

  constructor() {

    effect(() => {

      let success = this.success();
      if(success) {
        this.toaster.success(this.messages()[0], "Success");
        
      }
    });

    effect(() => {
      let error = this.error();
      if(error) {
        this.toaster.error("An error occurred: " + JSON.stringify(error), "Error");
        
      }
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getAll();

    this.keycloak.loadUserInfo().then((userInfo) => {
      console.log(userInfo);
    });

    this.route.queryParams.subscribe(params => {
      const target = params['target'] as TargetEntity;
      if (target) {
        // this.navigateToRecordCreation(target);
        console.log('Navigate to record creation for target:', target);
      }
    });

    this.route.params.subscribe(params => {
      console.log('Route params:', params);
      const recordId = params['id'];
      if (recordId) {
        this.kycRecordApiStore.findById({id: recordId});
      }
    });
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onDocumentTypeSelected(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedDocumentType = select.value;
  }

  uploadDocument(): void {
    const record = this.record();
    if (!record || !this.selectedFile || !this.selectedDocumentType) {
      alert('Please select a file and document type');
      return;
    }

    let doc = new DocumentDTO();
    doc.target = TargetEntity.KYC_RECORD;
    doc.targetId = record.id;
    doc.documentTypeId = this.selectedDocumentType;
    doc.fileName = this.selectedFile.name;

    this.kycRecordApiStore.updateRecordFiles({
      id: record.id,
      documents: [doc],
      files: [this.selectedFile]
    });


  }
}
