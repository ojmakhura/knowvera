import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { IndividualDetails } from './individual-details';
import { AppEnvStore } from '@app/store/app-env.state';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';

describe('IndividualDetails', () => {
  let component: IndividualDetails;
  let fixture: ComponentFixture<IndividualDetails>;

  const appEnvStoreMock = {
    individual: () => ({ id: null }),
  };

  const individualApiStoreMock = {
    data: () => ({ phoneNumbers: [], employmentRecords: [], documents: [] }),
    loaderMessage: () => '',
    messages: () => [],
    success: () => false,
    loading: () => false,
    error: () => null,
    findById: () => undefined,
  };

  const settingsApiStoreMock = {
    data: () => ({}),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndividualDetails],
      providers: [
        provideRouter([]),
        { provide: AppEnvStore, useValue: appEnvStoreMock },
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
        { provide: SettingsApiStore, useValue: settingsApiStoreMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(IndividualDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
