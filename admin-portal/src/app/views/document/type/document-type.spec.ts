import { signal, WritableSignal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { DocumentTypeApiStore } from '@app/store/bw/co/knowvera/document/type/document-type-api.store';
import { toast } from 'ngx-sonner';
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

  let toastSuccessSpy: ReturnType<typeof vi.spyOn>;
  let toastErrorSpy: ReturnType<typeof vi.spyOn>;
  let navigateSpy: ReturnType<typeof vi.spyOn>;

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

    toastSuccessSpy = vi.spyOn(toast, 'success').mockReturnValue(0);
    toastErrorSpy = vi.spyOn(toast, 'error').mockReturnValue(0);

    await TestBed.configureTestingModule({
      imports: [DocumentTypeComponent],
      providers: [
        { provide: DocumentTypeApiStore, useValue: mockDocumentTypeApiStore },
        provideRouter([]),
      ],
    }).compileComponents();

    navigateSpy = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);

    fixture = TestBed.createComponent(DocumentTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    vi.restoreAllMocks();
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

  it('should update criteria through the search form', () => {
    component.searchDocumentTypesForm.criteria().value.set('passport');

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

  it('should update page size and request pages from the pager', () => {
    component.changePageSize('50');

    expect(component.pageSize()).toBe(50);
    expect(mockDocumentTypeApiStore.pagedSearch.calls).toContainEqual([
      {
        criteria: '',
        pageNumber: 0,
        pageSize: 50,
      },
    ]);

    component.totalPages.set(5);
    component.goToPage(3);

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

  it('should not toast for the initial search', () => {
    expect(toastSuccessSpy).not.toHaveBeenCalled();
    expect(toastErrorSpy).not.toHaveBeenCalled();
  });

  it('should show success toast when a notifying search completes', () => {
    component.doSearch(0, 10, true);
    loadingSignal.set(true);
    fixture.detectChanges();

    dataPageSignal.set({
      content: [],
      page: { number: 0, size: 10, totalElements: 3, totalPages: 1 },
    });
    successSignal.set(true);
    loadingSignal.set(false);
    fixture.detectChanges();

    expect(toastSuccessSpy).toHaveBeenCalledWith('3 document types found');
    expect(toastErrorSpy).not.toHaveBeenCalled();
  });

  it('should show error toast when a search fails', () => {
    component.doSearch();
    loadingSignal.set(true);
    fixture.detectChanges();

    messagesSignal.set(['Failed to search']);
    errorSignal.set(true);
    loadingSignal.set(false);
    fixture.detectChanges();

    expect(toastErrorSpy).toHaveBeenCalledWith('Failed to search');
    expect(toastSuccessSpy).not.toHaveBeenCalled();
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

    expect(navigateSpy).toHaveBeenCalledWith(['/', 'document', 'type', 'edit']);
    expect(navigateSpy).toHaveBeenCalledWith(['/', 'document', 'type', 'doc-99', 'edit']);
  });
});
