import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { ToastrService } from 'ngx-toastr';
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

  const routerMock = {
    navigate: vi.fn(),
  };

  const toastrMock = {
    success: vi.fn(),
    warning: vi.fn(),
    error: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndividualDetails],
      providers: [
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
        { provide: SettingsApiStore, useValue: settingsApiStoreMock },
        { provide: DocumentApi, useValue: documentApiMock },
        { provide: MatDialog, useValue: dialogMock },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrMock },
      ],
    }).compileComponents();

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

    expect(routerMock.navigate).toHaveBeenCalledWith(['/individual', 'edit', 'ind-1']);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/documents/details', 'doc-1']);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/documents/edit', 'doc-2']);
  });

  it('should warn when document id is missing', () => {
    component.openDocumentDetails({ id: null } as any);
    component.openDocumentEdit({ id: null } as any);
    component.deleteDocument({ id: null } as any);

    expect(toastrMock.warning).toHaveBeenCalledTimes(3);
  });

  it('should warn when adding document before target id exists', () => {
    individualSignal.set({ id: null, latestKyc: { documents: [] } });
    fixture.detectChanges();

    component.id = null;
    component.openDocumentAdd();

    expect(toastrMock.warning).toHaveBeenCalled();
  });
});
