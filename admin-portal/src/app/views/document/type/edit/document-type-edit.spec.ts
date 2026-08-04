import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ExpectedFieldDTO } from '@app/models/bw/co/kyvera/document/type/field/expected-field-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/kyvera/document/type/document-type-dto';
import { VerificationDataConfigDTO } from '@app/models/bw/co/kyvera/document/type/verification/verification-data-config-dto';
import { TargetEntity } from '@app/models/bw/co/kyvera/target-entity';
import { TimePeriod } from '@app/models/bw/co/kyvera/time-period';
import { VerificationDataConfigApi } from '@app/services/bw/co/kyvera/document/type/verification/verification-data-config-api';
import { DocumentTypeApiStore } from '@app/store/bw/co/kyvera/document/type/document-type-api.store';
import { ExpectedFieldApiStore } from '@app/store/bw/co/kyvera/document/type/field/expected-field-api.store';
import { VerificationDataConfigApiStore } from '@app/store/bw/co/kyvera/document/type/verification/verification-data-config-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import {
  DocumentTypeEdit,
  EditDocumentTypeVarsForm,
  ExpectedFieldDialogComponent,
} from './document-type-edit';

describe('EditDocumentTypeVarsForm', () => {
  it('should initialize defaults', () => {
    const form = new EditDocumentTypeVarsForm();

    expect(form.code).toBeNull();
    expect(form.name).toBeNull();
    expect(form.expires).toBe(false);
    expect(form.expectedFields).toEqual([]);
    expect(form.validationPrompts).toEqual([]);
    expect(form.textExtractionPrompts).toEqual([]);
    expect(form.verificationDataConfigs).toEqual([]);
  });
});

describe('DocumentTypeEdit', () => {
  let fixture: ComponentFixture<DocumentTypeEdit>;
  let component: DocumentTypeEdit;

  const dataSignal = signal(new DocumentTypeDTO());
  const loadingSignal = signal(false);
  const loaderMessageSignal = signal('');
  const successSignal = signal(false);
  const errorSignal = signal(false);
  const messagesSignal = signal<string[]>([]);

  const mockDocumentTypeApiStore = {
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

  const mockDialog = {
    open: vi.fn(() => ({
      afterClosed: () => of(undefined),
    })),
  };

  const mockToastr = {
    success: vi.fn(),
    error: vi.fn(),
  };

  const mockVerificationConfigApi = {
    remove: vi.fn(() => of(true)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentTypeEdit, NoopAnimationsModule, TranslateModule.forRoot()],
      providers: [
        { provide: DocumentTypeApiStore, useValue: mockDocumentTypeApiStore },
        { provide: MatDialog, useValue: mockDialog },
        { provide: ToastrService, useValue: mockToastr },
        { provide: ExpectedFieldApiStore, useValue: {} },
        { provide: VerificationDataConfigApiStore, useValue: {} },
        { provide: VerificationDataConfigApi, useValue: mockVerificationConfigApi },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentTypeEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and reset store on init', () => {
    expect(component).toBeTruthy();
    expect(mockDocumentTypeApiStore.reset).toHaveBeenCalled();
  });

  it('should call findById when id is provided', () => {
    component.id = 'doc-1';

    component.ngOnInit();

    expect(mockDocumentTypeApiStore.findById).toHaveBeenCalledWith({ id: 'doc-1' });
  });

  it('should update fields in signal state', () => {
    component.updateField('name', 'Passport');

    expect(component.editDocumentTypeSignal().name).toBe('Passport');
  });

  it('should support expected field navigation', () => {
    component.editDocumentTypeSignal.update((state) => ({
      ...state,
      expectedFields: [{ id: 'a' } as any, { id: 'b' } as any],
    }));

    component.goToNextExpectedField();
    expect(component.currentExpectedFieldIndex()).toBe(1);
    expect(component.canGoToNextExpectedField()).toBe(false);

    component.goToPreviousExpectedField();
    expect(component.currentExpectedFieldIndex()).toBe(0);
    expect(component.canGoToPreviousExpectedField()).toBe(false);
  });

  it('should toggle verification config key selection', () => {
    const field = { id: 'f-1', field: 'accountNumber' } as ExpectedFieldDTO;
    const config = {
      id: 'cfg-1',
      expectedFields: [],
    } as VerificationDataConfigDTO;

    component.editDocumentTypeSignal.update((state) => ({
      ...state,
      verificationDataConfigs: [config],
    }));

    component.toggleVerificationDataConfigKey(0, field, true);
    expect(component.editDocumentTypeSignal().verificationDataConfigs[0].expectedFields).toEqual([field]);

    component.toggleVerificationDataConfigKey(0, field, false);
    expect(component.editDocumentTypeSignal().verificationDataConfigs[0].expectedFields).toEqual([]);
  });

  it('should discard to persisted data when store has current document type', () => {
    const persisted = new DocumentTypeDTO();
    persisted.id = 'doc-42';
    persisted.name = 'Persisted Name';
    persisted.code = 'PERSISTED';
    dataSignal.set(persisted);

    component.updateField('name', 'Unsaved');
    component.discardChanges();

    expect(component.editDocumentTypeSignal().name).toBe('Persisted Name');
    expect(component.editDocumentTypeSignal().code).toBe('PERSISTED');
  });

  it('should discard to new form when store has no persisted id', () => {
    const empty = new DocumentTypeDTO();
    empty.id = null;
    dataSignal.set(empty);

    component.updateField('name', 'Unsaved');
    component.discardChanges();

    expect(component.editDocumentTypeSignal().name).toBeNull();
  });

  it('should map and save document type', () => {
    component.editDocumentTypeSignal.update((state) => ({
      ...state,
      id: 'doc-7',
      code: 'PASSPORT',
      name: 'Passport',
      expires: true,
      expiryPeriod: TimePeriod.WEEK,
      expiresIn: 90,
    }));

    component.saveDocumentType();

    expect(mockDocumentTypeApiStore.save).toHaveBeenCalled();
    const payload = mockDocumentTypeApiStore.save.mock.calls.at(-1)?.[0];
    expect(payload.documentType.id).toBe('doc-7');
    expect(payload.documentType.code).toBe('PASSPORT');
    expect(payload.documentType.name).toBe('Passport');
    expect(payload.documentType.expires).toBe(true);
  });

  it('should compare expiry field values by id', () => {
    expect(component.expiryFieldCompare({ id: 'x' } as any, { id: 'x' } as any)).toBe(true);
    expect(component.expiryFieldCompare({ id: 'x' } as any, { id: 'y' } as any)).toBe(false);
  });

  it('should track by index', () => {
    expect(component.trackByIndex(3)).toBe(3);
  });
});

describe('ExpectedFieldDialogComponent', () => {
  let fixture: ComponentFixture<ExpectedFieldDialogComponent>;
  let component: ExpectedFieldDialogComponent;

  const mockDialogRef = {
    close: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpectedFieldDialogComponent, NoopAnimationsModule, TranslateModule.forRoot()],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            title: 'Expected Field',
            field: new ExpectedFieldDTO(),
          },
        },
        { provide: MatDialogRef, useValue: mockDialogRef },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ExpectedFieldDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close without payload on cancel', () => {
    component.onCancel();

    expect(mockDialogRef.close).toHaveBeenCalledWith();
  });

  it('should not save invalid expected field form', () => {
    component.onSave();

    expect(mockDialogRef.close).not.toHaveBeenCalledWith(expect.objectContaining({ field: expect.anything() }));
  });

  it('should save valid expected field form', () => {
    component.expectedFieldSignal.update((field) => ({
      ...field,
      field: 'documentNumber',
    }));

    component.onSave();

    expect(mockDialogRef.close).toHaveBeenCalledWith(
      expect.objectContaining({
        field: 'documentNumber',
      }),
    );
  });

  it('should reset matchTo when not valid for selected target type', () => {
    component.expectedFieldSignal.update((field) => ({
      ...field,
      targetType: TargetEntity.INDIVIDUAL,
      matchTo: 'not-a-real-field',
    }));
    fixture.detectChanges();

    expect(component.expectedFieldSignal().matchTo).toBeNull();
  });
});
