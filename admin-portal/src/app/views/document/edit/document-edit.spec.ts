import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { DocumentVerificationStatus } from '@app/models/bw/co/knowvera/document/document-verification-status';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { DocumentApiStore } from '@app/store/bw/co/knowvera/document/document-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/knowvera/document/type/document-type-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import { DocumentEdit } from './document-edit';

describe('DocumentEdit', () => {
  let fixture: ComponentFixture<DocumentEdit>;
  let component: DocumentEdit;

  const dataSignal = signal<any>({});
  const loadingSignal = signal(false);
  const loaderMessageSignal = signal('');
  const successSignal = signal(false);
  const errorSignal = signal(false);
  const messagesSignal = signal<string[]>([]);

  const documentApiStoreMock = {
    data: dataSignal,
    loading: loadingSignal,
    loaderMessage: loaderMessageSignal,
    success: successSignal,
    error: errorSignal,
    messages: messagesSignal,
    reset: vi.fn(),
    findById: vi.fn(),
    save: vi.fn(),
  };

  const documentTypeApiStoreMock = {
    dataList: signal([]),
    getAll: vi.fn(),
  };

  const documentApiMock = {
    downloadFileByUrl: vi.fn(() => of(new Blob(['file']))),
  };

  const routerMock = {
    navigate: vi.fn(),
  };

  const routeMock = {
    snapshot: {
      queryParamMap: {
        get: vi.fn(() => null),
      },
    },
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentEdit, TranslateModule.forRoot()],
      providers: [
        { provide: DocumentApiStore, useValue: documentApiStoreMock },
        { provide: DocumentTypeApiStore, useValue: documentTypeApiStoreMock },
        { provide: DocumentApi, useValue: documentApiMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: routeMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and initialize stores', () => {
    expect(component).toBeTruthy();
    expect(documentApiStoreMock.reset).toHaveBeenCalled();
    expect(documentTypeApiStoreMock.getAll).toHaveBeenCalled();
  });

  it('should save edited document payload', () => {
    component.editDocumentSignal.update((value) => ({
      ...value,
      id: 'doc-1',
      fileName: 'passport.png',
      verificationStatus: DocumentVerificationStatus.VERIFIED,
    }));

    component.editDocumentSave();

    expect(documentApiStoreMock.save).toHaveBeenCalled();
    const payload = documentApiStoreMock.save.mock.calls.at(-1)?.[0]?.document;
    expect(payload.id).toBe('doc-1');
    expect(payload.fileName).toBe('passport.png');
    expect(payload.verificationStatus).toBe(DocumentVerificationStatus.VERIFIED);
  });

  it('should derive labels and reference', () => {
    expect(component.targetEntityLabel('CLIENT_REQUEST')).toBe('CLIENT REQUEST');
    expect(component.verificationStatusLabel(DocumentVerificationStatus.MANUAL_REVIEW)).toBe('Pending Review');
    expect(component.internalReference()).toBe('DOC-KYC-NEW');
  });

  it('should navigate on cancel according to edit state', () => {
    component.cancelEdit();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/documents']);

    component.editDocumentSignal.update((value) => ({
      ...value,
      id: 'doc-7',
    }));
    component.cancelEdit();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/documents/details', 'doc-7']);
  });
});
