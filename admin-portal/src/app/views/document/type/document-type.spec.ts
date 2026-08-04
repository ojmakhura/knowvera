import { signal, WritableSignal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { DocumentTypeDTO } from '@app/models/bw/co/kyvera/document/type/document-type-dto';
import { DocumentTypeApiStore } from '@app/store/bw/co/kyvera/document/type/document-type-api.store';
import { ToastrService } from 'ngx-toastr';
import { DocumentTypeComponent } from './document-type';

type DocumentTypePage = {
  content: DocumentTypeDTO[];
  page: {
    number: number;
    size: number;
    totalElements: number;
    totalPages: number;
  };
};

type SpyFn<TArgs extends unknown[] = unknown[]> = ((...args: TArgs) => void) & {
  calls: TArgs[];
};

const createSpyFn = <TArgs extends unknown[] = unknown[]>(): SpyFn<TArgs> => {
  const fn = ((...args: TArgs) => {
    fn.calls.push(args);
  }) as SpyFn<TArgs>;

  fn.calls = [];
  return fn;
};

describe('DocumentTypeComponent', () => {
  let fixture: ComponentFixture<DocumentTypeComponent>;
  let component: DocumentTypeComponent;

  let dataPageSignal: WritableSignal<DocumentTypePage | null>;
  let loaderMessageSignal: WritableSignal<string>;
  let messagesSignal: WritableSignal<string[]>;
  let successSignal: WritableSignal<boolean>;
  let loadingSignal: WritableSignal<boolean>;
  let errorSignal: WritableSignal<boolean>;

  let mockDocumentTypeApiStore: {
    pagedSearch: SpyFn<[{
      criteria: string;
      pageNumber: number;
      pageSize: number;
    }]>;
    dataPage: typeof dataPageSignal;
    loaderMessage: typeof loaderMessageSignal;
    messages: typeof messagesSignal;
    success: typeof successSignal;
    loading: typeof loadingSignal;
    error: typeof errorSignal;
  };

  let mockToastr: {
    success: SpyFn<[string]>;
    error: SpyFn<[string]>;
  };

  let mockRouter: {
    navigate: SpyFn<[string[]]>;
  };

  beforeEach(async () => {
    dataPageSignal = signal<DocumentTypePage | null>(null);
    loaderMessageSignal = signal('');
    messagesSignal = signal<string[]>([]);
    successSignal = signal(false);
    loadingSignal = signal(false);
    errorSignal = signal(false);

    mockDocumentTypeApiStore = {
      pagedSearch: createSpyFn(),
      dataPage: dataPageSignal,
      loaderMessage: loaderMessageSignal,
      messages: messagesSignal,
      success: successSignal,
      loading: loadingSignal,
      error: errorSignal,
    };

    mockToastr = {
      success: createSpyFn(),
      error: createSpyFn(),
    };

    mockRouter = {
      navigate: createSpyFn(),
    };

    await TestBed.configureTestingModule({
      imports: [DocumentTypeComponent, NoopAnimationsModule],
      providers: [
        { provide: DocumentTypeApiStore, useValue: mockDocumentTypeApiStore },
        { provide: ToastrService, useValue: mockToastr },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DocumentTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and run initial search', () => {
    expect(component).toBeTruthy();
    expect(mockDocumentTypeApiStore.pagedSearch.calls).toContainEqual([
      {
        criteria: '',
        pageNumber: 0,
        pageSize: 10,
      },
    ]);
  });

  it('should update criteria from input event', () => {
    const event = {
      target: {
        value: 'passport',
      },
    } as unknown as Event;

    component.onCriteriaInput(event);

    expect(component.searchDocumentTypesSignal().criteria).toBe('passport');
  });

  it('should run search using criteria and provided pagination', () => {
    component.searchDocumentTypesSignal.update((state) => ({
      ...state,
      criteria: 'id-card',
    }));

    component.doSearch(2, 25);

    expect(mockDocumentTypeApiStore.pagedSearch.calls).toContainEqual([
      {
        criteria: 'id-card',
        pageNumber: 2,
        pageSize: 25,
      },
    ]);
  });

  it('should clear criteria and trigger a fresh search', () => {
    component.searchDocumentTypesSignal.update((state) => ({
      ...state,
      criteria: 'something',
    }));

    component.clearCriteria();

    expect(component.searchDocumentTypesSignal().criteria).toBe('');
    expect(mockDocumentTypeApiStore.pagedSearch.calls).toContainEqual([
      {
        criteria: '',
        pageNumber: 0,
        pageSize: 10,
      },
    ]);
  });

  it('should update page size and request a new page on paginator event', () => {
    component.onPageChange({
      pageIndex: 3,
      pageSize: 50,
      length: 120,
      previousPageIndex: 2,
    });

    expect(component.pageSize()).toBe(50);
    expect(mockDocumentTypeApiStore.pagedSearch.calls).toContainEqual([
      {
        criteria: '',
        pageNumber: 3,
        pageSize: 50,
      },
    ]);
  });

  it('should sync data source and pagination signals when store page changes', () => {
    const first = new DocumentTypeDTO();
    first.id = 'dt-1';
    first.name = 'Passport';

    const second = new DocumentTypeDTO();
    second.id = 'dt-2';
    second.name = 'Driving License';

    dataPageSignal.set({
      content: [first, second],
      page: {
        number: 1,
        size: 25,
        totalElements: 52,
        totalPages: 3,
      },
    });
    fixture.detectChanges();

    expect(component.dataSource.data).toEqual([first, second]);
    expect(component.searchDocumentTypesSignal().documentTypes).toEqual([first, second]);
    expect(component.currentPage()).toBe(1);
    expect(component.pageSize()).toBe(25);
    expect(component.totalElements()).toBe(52);
    expect(component.totalPages()).toBe(3);
    expect(component.showingCount()).toBe(2);
  });

  it('should show success toast when store reports successful operation', () => {
    messagesSignal.set(['Saved successfully']);
    successSignal.set(true);
    loadingSignal.set(false);

    fixture.detectChanges();

    expect(mockToastr.success.calls).toContainEqual(['Saved successfully']);
    expect(mockToastr.error.calls.length).toBe(0);
  });

  it('should show error toast when store reports failed operation', () => {
    messagesSignal.set(['Failed to save']);
    successSignal.set(false);
    errorSignal.set(true);
    loadingSignal.set(false);

    fixture.detectChanges();

    expect(mockToastr.error.calls).toContainEqual(['Failed to save']);
    expect(mockToastr.success.calls.length).toBe(0);
  });

  it('should calculate field and prompt counts defensively', () => {
    const documentType = new DocumentTypeDTO();
    documentType.expectedFields = [{}, {}];
    documentType.validationPrompts = [{}];
    documentType.textExtractionPrompts = [{}, {}, {}];

    expect(component.fieldCount(documentType)).toBe(2);
    expect(component.promptCount(documentType)).toBe(4);

    documentType.expectedFields = null;
    documentType.validationPrompts = null;
    documentType.textExtractionPrompts = undefined;

    expect(component.fieldCount(documentType)).toBe(0);
    expect(component.promptCount(documentType)).toBe(0);
  });

  it('should navigate to create and edit pages', () => {
    component.openCreate();
    component.openEdit('doc-99');

    expect(mockRouter.navigate.calls).toContainEqual([['/', 'document', 'type', 'edit']]);
    expect(mockRouter.navigate.calls).toContainEqual([['/', 'document', 'type', 'edit', 'doc-99']]);
  });
});
