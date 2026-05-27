import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { Individuals, SearchIndividualsVarsForm } from './individuals';

describe('SearchIndividualsVarsForm', () => {
  it('should initialize defaults', () => {
    const form = new SearchIndividualsVarsForm();

    expect(form.identityNo).toBe('');
    expect(form.firstName).toBe('');
    expect(form.individuals).toEqual([]);
  });
});

describe('Individuals', () => {
  let fixture: ComponentFixture<Individuals>;
  let component: Individuals;

  const dataPageSignal = signal<any>(null);
  const loadingSignal = signal(false);
  const messagesSignal = signal<string[]>([]);
  const successSignal = signal(false);
  const errorSignal = signal(false);

  const individualApiStoreMock = {
    dataPage: dataPageSignal,
    loading: loadingSignal,
    messages: messagesSignal,
    success: successSignal,
    error: errorSignal,
    pagedSearch: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Individuals],
      providers: [
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Individuals);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and run initial search', () => {
    expect(component).toBeTruthy();
    expect(individualApiStoreMock.pagedSearch).toHaveBeenCalled();
  });

  it('should update search field and reset search', () => {
    component.updateField('firstName', 'Alice');
    expect(component.searchIndividualsSignal().firstName).toBe('Alice');

    component.resetSearch();
    expect(component.searchIndividualsSignal().firstName).toBe('');
  });

  it('should map statuses and initials correctly', () => {
    expect(component.statusLabel('CURRENT')).toBe('Verified');
    expect(component.statusClass('INCOMPLETE')).toBe('status-pending');
    expect(component.initialsOf('Alice Bob')).toBe('AB');
    expect(component.initialsOf(null)).toBe('NA');
  });

  it('should navigate to create, details and edit pages', () => {
    component.createNewIndividual();
    component.openDetails('ind-1');
    component.openEdit('ind-2');

    expect(routerMock.navigate).toHaveBeenCalledWith(['/individual', 'edit']);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/individual', 'details', 'ind-1']);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/individual', 'edit', 'ind-2']);
  });

  it('should apply page navigation boundaries', () => {
    const searchSpy = vi.spyOn(component, 'doSearch');

    (component as any).totalPages.set(3);
    (component as any).currentPage.set(1);
    (component as any).pageSize.set(10);

    component.previousPage();
    component.nextPage();
    component.goToPage(2);
    component.goToPage(2);

    expect(searchSpy).toHaveBeenCalledWith(0, 10);
    expect(searchSpy).toHaveBeenCalledWith(2, 10);
  });

  it('should update data source and metrics from store page', () => {
    dataPageSignal.set({
      content: [
        { id: '1', kycStatus: 'CURRENT' },
        { id: '2', kycStatus: 'FLAGGED' },
      ],
      page: {
        number: 0,
        size: 10,
        totalElements: 2,
        totalPages: 1,
      },
    });
    fixture.detectChanges();

    expect((component as any).dataSource.data.length).toBe(2);
    expect((component as any).verifiedCount()).toBe(1);
    expect((component as any).flaggedCount()).toBe(1);
  });
});
