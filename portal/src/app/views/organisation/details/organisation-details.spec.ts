import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

import { OrganisationDetails } from './organisation-details';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { BranchApiStore } from '@app/store/bw/co/centralkyc/organisation/branch/branch-api.store';
import { BranchApi } from '@app/services/bw/co/centralkyc/organisation/branch/branch-api';
import { KycInvoiceApiStore } from '@app/store/bw/co/centralkyc/invoice/kyc-invoice-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/centralkyc/subscription/kyc-subscription-api.store';
import { ClientRequestApiStore } from '@app/store/bw/co/centralkyc/organisation/client/client-request-api.store';
import { ClientRequestApi } from '@app/services/bw/co/centralkyc/organisation/client/client-request-api';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';
import { AppEnvStore } from '@app/store/app-env.state';
import { ToastrService } from 'ngx-toastr';

describe('OrganisationDetails', () => {
  let component: OrganisationDetails;
  let fixture: ComponentFixture<OrganisationDetails>;

  const organisationApiStoreMock = {
    data: () => ({ branches: [] }),
    error: () => null,
    messages: () => [],
    success: () => false,
    loading: () => false,
    findById: () => undefined,
    reset: () => undefined,
  };

  const settingsApiStoreMock = { data: () => ({}) };

  const branchApiStoreMock = {
    dataList: () => [],
    loading: () => false,
    data: () => ({}),
    save: () => undefined,
    findByOrganisation: () => undefined,
    reset: () => undefined,
  };

  const branchApiMock = {};
  const kycInvoiceApiStoreMock = {
    dataList: () => [],
    reset: () => undefined,
  };
  const kycSubscriptionApiStoreMock = {
    dataList: () => [],
    reset: () => undefined,
  };
  const clientRequestApiStoreMock = {
    loading: () => false,
    dataPage: () => ({ content: [], page: { number: 0, size: 10, totalElements: 0 }, totalElements: 0 }),
    individualsRequestsPage: () => ({ content: [] }),
    organisationsRequestsPage: () => ({ content: [] }),
    reset: () => undefined,
  };
  const clientRequestApiMock = {};
  const documentApiMock = {
    upload: () => ({ pipe: () => ({ subscribe: () => ({}) }) }),
  };
  const documentApiStoreMock = {
    reset: () => undefined,
  };
  const appEnvStoreMock = {
    userOrganisation: () => ({ id: null }),
  };
  const toastrMock = {
    success: () => undefined,
    error: () => undefined,
  };
  const dialogMock = {
    open: () => ({ afterClosed: () => ({ subscribe: () => ({}) }) }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganisationDetails],
      providers: [
        provideRouter([]),
        { provide: OrganisationApiStore, useValue: organisationApiStoreMock },
        { provide: SettingsApiStore, useValue: settingsApiStoreMock },
        { provide: BranchApiStore, useValue: branchApiStoreMock },
        { provide: BranchApi, useValue: branchApiMock },
        { provide: MatDialog, useValue: dialogMock },
        { provide: KycInvoiceApiStore, useValue: kycInvoiceApiStoreMock },
        { provide: KycSubscriptionApiStore, useValue: kycSubscriptionApiStoreMock },
        { provide: ClientRequestApiStore, useValue: clientRequestApiStoreMock },
        { provide: ClientRequestApi, useValue: clientRequestApiMock },
        { provide: DocumentApi, useValue: documentApiMock },
        { provide: DocumentApiStore, useValue: documentApiStoreMock },
        { provide: AppEnvStore, useValue: appEnvStoreMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(OrganisationDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});