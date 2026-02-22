import { SettingsApiStore } from './../../store/bw/co/centralkyc/settings/settings-api.store';
import { AfterViewInit, Component, OnDestroy, OnInit, signal, inject, linkedSignal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { ClientRequestDTO } from '@app/models/bw/co/centralkyc/organisation/client/client-request-dto';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycRecordApi } from '@app/services/bw/co/centralkyc/kyc/kyc-record-api';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';

class DashboardForm {

  clientRequests: ClientRequestDTO[] = [];
  currentKycRecord: KycRecordDTO | null = null;
  organisations: OrganisationListDTO[] = [];
}

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit, OnDestroy, AfterViewInit {

  settingsApiStore = inject(SettingsApiStore);
  kycRecordApiStore = inject(KycRecordApiStore);
  kycRecordApi = inject(KycRecordApi);

  dashbboardSignal = signal(new DashboardForm());

  currentIndividualRecord = linkedSignal(() => this.kycRecordApiStore.currentIndividualRecord());
  currentOrganisationRecord = linkedSignal(() => this.kycRecordApiStore.currentOrganisationRecord());
  myRecords = linkedSignal(() => this.kycRecordApiStore.dataList());

  route = inject(ActivatedRoute);
  router = inject(Router);

  constructor() { }

  ngOnInit(): void {
    this.settingsApiStore.getAll();

    // this.kycRecordApiStore.findMyCurrentIndividualRecord();
    // setTimeout(() => {
    //     this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    // }, 1000);
    // this.kycRecordApiStore.findMyCurrentRecord({ ownerType: TargetEntity.ORGANISATION });
    // this.kycRecordApiStore.findMyRecords();

    this.kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).subscribe({
      next: record => {
        console.log(record);
      }
    })
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
  }

  formatDate(date: Date | any): string {
    if (!date) return 'N/A';

    try {
      const dateObj = date instanceof Date ? date : new Date(date);
      return dateObj.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch {
      return 'N/A';
    }
  }

  getStatusClass(status: string): string {
    if (!status) return 'unknown';

    const statusLower = status.toLowerCase();
    if (statusLower.includes('approved') || statusLower.includes('verified') || statusLower.includes('active')) {
      return 'success';
    }
    if (statusLower.includes('pending') || statusLower.includes('review')) {
      return 'warning';
    }
    if (statusLower.includes('rejected') || statusLower.includes('expired') || statusLower.includes('failed')) {
      return 'danger';
    }
    return 'info';
  }

  viewRecord(record: KycRecordDTO): void {
    console.log('Viewing record:', record);
    // Navigate to record details page
  }

  editRecord(record: KycRecordDTO): void {
    console.log('Editing record:', record);
    // Navigate to record edit page
  }

  createRecord(type: string): void {
    console.log('Creating record of type:', type);
    // Navigate to record creation page
    this.router.navigate(['/kyc-record', 'edit'], {
      queryParams: {
        target: type
      }
    })
  }

  refreshRecords(): void {
    // this.kycRecordApiStore.findMyCurrentIndividualRecord();
    // this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    // this.kycRecordApiStore.findMyRecords();

    // this.kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).subscribe({
    //   next: record => {
    //     console.log(record);
    //   }
    // })
  }

}
