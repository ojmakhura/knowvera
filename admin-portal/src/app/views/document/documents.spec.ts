import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { DocumentVerificationStatus } from '@app/models/bw/co/kyvera/document/document-verification-status';
import { DocumentApi } from '@app/services/bw/co/kyvera/document/document-api';
import { DocumentApiStore } from '@app/store/bw/co/kyvera/document/document-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/kyvera/document/type/document-type-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import { Documents, SearchDocumentsVarsForm } from './documents';

describe('SearchDocumentsVarsForm', () => {
  it('should initialize defaults', () => {
    const form = new SearchDocumentsVarsForm();

    expect(form.fileName).toBe('');
    expect(form.documentType).toBe('');
    expect(form.target).toBe('');
    expect(form.targetId).toBe('');
    expect(form.verificationStatus).toBe('');
    expect(form.documents).toEqual([]);
  });
});

describe('Documents', () => {
  let fixture: ComponentFixture<Documents>;
  let component: Documents;

  const dataPageSignal = signal<any>(null);
  const loadingSignal = signal(false);
  const loaderMessageSignal = signal('');
  const messagesSignal = signal<string[]>([]);
  const successSignal = signal(false);
  const errorSignal = signal(false);

  const documentApiStoreMock = {
    dataPage: dataPageSignal,
    loading: loadingSignal,
    loaderMessage: loaderMessageSignal,
    messages: messagesSignal,
    success: successSignal,
    error: errorSignal,
    searchPaged: vi.fn(),
  };

  const documentTypeApiStoreMock = {
    dataList: signal([]),
    getAll: vi.fn(),
  };

  const documentApiMock = {
    downloadFile: vi.fn(() => of(new Blob(['x']))),
    downloadFileByUrl: vi.fn(() => of(new Blob(['y']))),
  };

  const routerMock = {
    navigate: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    error: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Documents, TranslateModule.forRoot()],
      providers: [
        { provide: DocumentApiStore, useValue: documentApiStoreMock },
        { provide: DocumentTypeApiStore, useValue: documentTypeApiStoreMock },
        { provide: DocumentApi, useValue: documentApiMock },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Documents);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and trigger initial search load', () => {
    expect(component).toBeTruthy();
    expect(documentTypeApiStoreMock.getAll).toHaveBeenCalled();
    expect(documentApiStoreMock.searchPaged).toHaveBeenCalled();
  });

  it('should reset search and trigger search', () => {
    component.searchDocumentsSignal.update((value) => ({
      ...value,
      fileName: 'passport.pdf',
    }));

    component.resetSearch();

    expect(component.searchDocumentsSignal().fileName).toBe('');
    expect(documentApiStoreMock.searchPaged).toHaveBeenCalled();
  });

  it('should map status labels and classes', () => {
    expect(component.statusLabel(DocumentVerificationStatus.VERIFIED)).toBe('Verified');
    expect(component.statusLabel(DocumentVerificationStatus.MANUAL_REVIEW)).toBe('Pending Review');
    expect(component.statusClass(DocumentVerificationStatus.REJECTED)).toBe('status-flagged');
    expect(component.statusClass('UNKNOWN')).toBe('status-unverified');
  });

  it('should navigate to details and edit routes', () => {
    component.openDetails('doc-11');
    component.openEdit('doc-12');

    expect(routerMock.navigate).toHaveBeenCalledWith(['/', 'documents', 'details', 'doc-11']);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/', 'documents', 'edit', 'doc-12']);
  });
});
