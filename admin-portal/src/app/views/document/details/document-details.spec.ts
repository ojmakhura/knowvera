import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DocumentVerificationStatus } from '@app/models/bw/co/knowvera/document/document-verification-status';
import { DocumentApiStore } from '@app/store/bw/co/knowvera/document/document-api.store';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import { TranslateModule } from '@ngx-translate/core';
import Keycloak from 'keycloak-js';
import { DocumentDetails } from './document-details';

describe('DocumentDetails', () => {
  let fixture: ComponentFixture<DocumentDetails>;
  let component: DocumentDetails;

  const dataSignal = signal<any>({
    id: 'doc-1',
    verificationStatus: DocumentVerificationStatus.UNVERIFIED,
    validationResults: { score: 0.8, match: true, signalScores: {} },
    dataComparisons: [],
    dataVerifications: [],
    fileContent: '',
    metadata: { issuer: 'Gov' },
  });
  const loadingSignal = signal(false);
  const loaderMessageSignal = signal('');
  const messagesSignal = signal<string[]>([]);
  const successSignal = signal(false);
  const errorSignal = signal(false);

  const documentApiStoreMock = {
    data: dataSignal,
    loading: loadingSignal,
    loaderMessage: loaderMessageSignal,
    messages: messagesSignal,
    success: successSignal,
    error: errorSignal,
    findById: vi.fn(),
    save: vi.fn(),
    analyseDocument: vi.fn(),
    verifyData: vi.fn(),
    updateVerificationStatus: vi.fn(),
  };

  const keycloakMock = {
    hasRealmRole: vi.fn(() => true),
    hasResourceRole: vi.fn(() => false),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentDetails, TranslateModule.forRoot()],
      providers: [
        { provide: DocumentApiStore, useValue: documentApiStoreMock },
        { provide: Keycloak, useValue: keycloakMock },
        {
          provide: KEYCLOAK_EVENT_SIGNAL,
          useValue: signal({ type: KeycloakEventType.Ready, args: true }),
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentDetails);
    component = fixture.componentInstance;
    component.id = 'doc-1';
    fixture.detectChanges();
  });

  it('should create and load by id on init', () => {
    expect(component).toBeTruthy();
    expect(documentApiStoreMock.findById).toHaveBeenCalledWith({ id: 'doc-1' });
  });

  it('should expose computed values for confidence and match label', () => {
    expect(component.confidenceScore()).toBe(80);
    expect(component.matchLabel()).toBe('Match');
  });

  it('should save metadata changes', () => {
    component.openMetadataEdit();
    component.updateMetadataKey(0, 'issuer');
    component.updateMetadataValue(0, 'Knowvera Registry');

    component.saveMetadata();

    expect(documentApiStoreMock.save).toHaveBeenCalled();
    const payload = documentApiStoreMock.save.mock.calls.at(-1)?.[0]?.document;
    expect(payload.metadata.issuer).toBe('Knowvera Registry');
  });

  it('should trigger verify, analyse and status update', () => {
    component.analyseDocument();
    component.verifyData();
    component.updateStatus(DocumentVerificationStatus.VERIFIED);

    expect(documentApiStoreMock.analyseDocument).toHaveBeenCalledWith({ id: 'doc-1' });
    expect(documentApiStoreMock.verifyData).toHaveBeenCalledWith({ id: 'doc-1' });
    expect(documentApiStoreMock.updateVerificationStatus).toHaveBeenCalledWith({
      id: 'doc-1',
      status: DocumentVerificationStatus.VERIFIED,
    });
  });
});
