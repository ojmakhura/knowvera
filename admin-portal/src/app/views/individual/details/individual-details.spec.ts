import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { provideRouter, Router } from '@angular/router';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { IndividualApiStore } from '@app/store/bw/co/knowvera/individual/individual-api.store';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { of } from 'rxjs';
import { IndividualDetails } from './individual-details';

describe('IndividualDetails', () => {
  let fixture: ComponentFixture<IndividualDetails>;
  let component: IndividualDetails;

  const individualSignal = signal<any>({
    id: 'ind-1',
    identityNo: 'A1234567',
    firstName: 'Jane',
    middleName: 'Q',
    surname: 'Doe',
    documents: [],
    kycStatus: 'CURRENT',
    latestKyc: { documents: [] },
    phoneNumbers: [],
    employmentRecords: [],
  });
  const settingsSignal = signal<any>({ individualDocuments: [] });
  const loadingSignal = signal(false);
  const loaderMessageSignal = signal('');
  const messagesSignal = signal<string[]>([]);
  const successSignal = signal(false);
  const errorSignal = signal(false);

  const individualApiStoreMock = {
    data: individualSignal,
    loading: loadingSignal,
    loaderMessage: loaderMessageSignal,
    messages: messagesSignal,
    success: successSignal,
    error: errorSignal,
    findById: vi.fn(),
  };

  const settingsApiStoreMock = {
    data: settingsSignal,
    getAll: vi.fn(),
  };

  const documentApiMock = {
    remove: vi.fn(() => of(true)),
    upload: vi.fn(() => of(true)),
  };

  const dialogMock = {
    open: vi.fn(() => ({ afterClosed: () => of(undefined) })),
  };

  let navigateSpy: ReturnType<typeof vi.spyOn>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndividualDetails],
      providers: [
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
        { provide: SettingsApiStore, useValue: settingsApiStoreMock },
        { provide: DocumentApi, useValue: documentApiMock },
        { provide: MatDialog, useValue: dialogMock },
        provideRouter([]),
      ],
    }).compileComponents();

    navigateSpy = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
    dialogMock.open.mockClear();
    documentApiMock.remove.mockClear();

    fixture = TestBed.createComponent(IndividualDetails);
    component = fixture.componentInstance;
    component.id = 'ind-1';
    fixture.detectChanges();
  });

  it('should create and load settings + individual', () => {
    expect(component).toBeTruthy();
    expect(settingsApiStoreMock.getAll).toHaveBeenCalled();
    expect(individualApiStoreMock.findById).toHaveBeenCalledWith({ id: 'ind-1' });
  });

  it('should build display helpers', () => {
    expect(component.fullName()).toBe('Jane Q Doe');
    expect(component.statusLabel('CURRENT')).toBe('Verified Profile');
    expect(component.statusClass('EXPIRED')).toBe('expired');
    expect(component.recordHash()).toContain('ind-');
  });

  it('should navigate to edit and document routes', () => {
    component.openEdit();
    component.openDocumentDetails({ id: 'doc-1' } as any);
    component.openDocumentEdit({ id: 'doc-2' } as any);

    expect(navigateSpy).toHaveBeenCalledWith(['/individual', 'ind-1', 'edit']);
    expect(navigateSpy).toHaveBeenCalledWith(['/documents', 'doc-1']);
    expect(navigateSpy).toHaveBeenCalledWith(['/documents', 'doc-2', 'edit']);
  });

  it('should do nothing when document id is missing', () => {
    component.openDocumentDetails({ id: null } as any);
    component.openDocumentEdit({ id: null } as any);
    component.deleteDocument({ id: null } as any);

    expect(navigateSpy).not.toHaveBeenCalled();
    expect(documentApiMock.remove).not.toHaveBeenCalled();
  });

  it('should not open the upload dialog before target id exists', () => {
    individualSignal.set({ id: null, documents: [], latestKyc: { documents: [] } });
    fixture.detectChanges();

    component.id = null;
    component.openDocumentAdd();

    expect(dialogMock.open).not.toHaveBeenCalled();
  });
});
