import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { EditOrganisation } from './edit-organisation';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
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


  const appEnvStoreMock = {
    userOrganisation: () => ({ id: null }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditOrganisation],
      providers: [
        provideRouter([]),
        { provide: OrganisationApiStore, useValue: organisationApiStoreMock },
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
