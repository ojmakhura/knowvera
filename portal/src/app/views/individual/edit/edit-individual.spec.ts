import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { EditIndividual } from './edit-individual';
import { AppEnvStore } from '@app/store/app-env.state';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { BranchApiStore } from '@app/store/bw/co/knowvera/organisation/branch/branch-api.store';
import { IndividualApiStore } from '@app/store/bw/co/knowvera/individual/individual-api.store';
import { ToastrService } from 'ngx-toastr';

describe('EditIndividual', () => {
  let component: EditIndividual;
  let fixture: ComponentFixture<EditIndividual>;

  const appEnvStoreMock = {
    individual: () => ({ id: null }),
    userOrganisation: () => ({ id: null }),
  };

  const organisationApiStoreMock = {
    loading: () => false,
    dataList: () => [],
    getAll: () => undefined,
    loaderMessage: () => '',
  };

  const branchApiStoreMock = {
    loading: () => false,
    dataList: () => [],
    findByOrganisation: () => undefined,
    loaderMessage: () => '',
  };

  const individualApiStoreMock = {
    loading: () => false,
    data: () => ({}),
    error: () => null,
    messages: () => [],
    success: () => false,
    save: () => undefined,
    findById: () => undefined,
    loaderMessage: () => '',
  };

  const toastrMock = {
    success: () => undefined,
    error: () => undefined,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditIndividual],
      providers: [
        provideRouter([]),
        { provide: AppEnvStore, useValue: appEnvStoreMock },
        { provide: OrganisationApiStore, useValue: organisationApiStoreMock },
        { provide: BranchApiStore, useValue: branchApiStoreMock },
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(EditIndividual);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});