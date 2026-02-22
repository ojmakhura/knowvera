import { CommonModule, JsonPipe } from '@angular/common';
import { AfterViewInit, Component, inject, linkedSignal, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycRecordApi } from '@app/services/bw/co/centralkyc/kyc/kyc-record-api';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-kyc-record',
  imports: [CommonModule],
  templateUrl: './kyc-record.html',
  styleUrl: './kyc-record.scss',
  providers: [
    JsonPipe
  ]
})
export class KycRecord implements OnInit, OnDestroy, AfterViewInit {

  settingsApiStore = inject(SettingsApiStore);
  kycRecordApiStore = inject(KycRecordApiStore);
  kycRecordApi = inject(KycRecordApi);

  indKycDocuments = linkedSignal(() => this.settingsApiStore.data().indKycDocuments);
  orgKycDocuments = linkedSignal(() => this.settingsApiStore.data().orgKycDocuments);

  currentIndividualRecord = linkedSignal(() => this.kycRecordApiStore.currentIndividualRecord());
  currentOrganisationRecord = linkedSignal(() => this.kycRecordApiStore.currentOrganisationRecord());
  myRecords = linkedSignal(() => this.kycRecordApiStore.data());

  private keycloak = inject(Keycloak);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  constructor() {}

  ngOnInit(): void {
    this.settingsApiStore.getAll();
    // this.kycRecordApiStore.findMyCurrentIndividualRecord();
    // this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    this.kycRecordApiStore.findMyRecords();

    // this.kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).subscribe({
    //   next: (record) => {
    //     console.log(record)
    //   }
    // })

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
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
  }

}
