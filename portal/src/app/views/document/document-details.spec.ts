import { ComponentFixture, TestBed } from '@angular/core/testing';
import Keycloak from 'keycloak-js';

import { DocumentDetails } from './document-details';
import { DocumentApiStore } from '@app/store/bw/co/knowvera/document/document-api.store';
import { ToastrService } from 'ngx-toastr';

describe('DocumentDetails', () => {
  let component: DocumentDetails;
  let fixture: ComponentFixture<DocumentDetails>;

  const documentApiStoreMock = {
    loaderMessage: () => '',
    messages: () => [],
    success: () => false,
    loading: () => false,
    error: () => null,
    data: () => ({ dataVerifications: [], validationResults: null }),
    findById: () => undefined,
  };

  const toastrMock = {
    success: () => undefined,
    error: () => undefined,
  };

  const keycloakMock = {
    hasRealmRole: () => false,
    hasResourceRole: () => false,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentDetails],
      providers: [
        { provide: DocumentApiStore, useValue: documentApiStoreMock },
        { provide: ToastrService, useValue: toastrMock },
        { provide: Keycloak, useValue: keycloakMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
