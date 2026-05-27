import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { EditOrganisation } from './edit-organisation';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { ToastrService } from 'ngx-toastr';
import { AppEnvStore } from '@app/store/app-env.state';

describe('EditOrganisation', () => {
  let component: EditOrganisation;
  let fixture: ComponentFixture<EditOrganisation>;

  const organisationApiStoreMock = {
    messages: () => [],
    success: () => false,
    loading: () => false,
    error: () => null,
    data: () => ({}),
    findById: () => undefined,
    save: () => undefined,
  };

  const toastrMock = {
    success: () => undefined,
    error: () => undefined,
  };

  const appEnvStoreMock = {
    userOrganisation: () => ({ id: null }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditOrganisation],
      providers: [
        provideRouter([]),
        { provide: OrganisationApiStore, useValue: organisationApiStoreMock },
        { provide: ToastrService, useValue: toastrMock },
        { provide: AppEnvStore, useValue: appEnvStoreMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(EditOrganisation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
