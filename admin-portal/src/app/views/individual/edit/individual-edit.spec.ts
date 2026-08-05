import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNativeDateAdapter } from '@angular/material/core';
import { Router } from '@angular/router';
import { IndividualApiStore } from '@app/store/bw/co/knowvera/individual/individual-api.store';
import { BranchApiStore } from '@app/store/bw/co/knowvera/organisation/branch/branch-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { IndividualEdit } from './individual-edit';

describe('IndividualEdit', () => {
  let fixture: ComponentFixture<IndividualEdit>;
  let component: IndividualEdit;

  const individualSignal = signal<any>({ id: null, phoneNumbers: [] });
  const loadingSignal = signal(false);
  const successSignal = signal(false);
  const errorSignal = signal(false);
  const messagesSignal = signal<string[]>([]);
  const orgListSignal = signal<any[]>([]);
  const branchListSignal = signal<any[]>([]);

  const individualApiStoreMock = {
    data: individualSignal,
    loading: loadingSignal,
    success: successSignal,
    error: errorSignal,
    messages: messagesSignal,
    reset: vi.fn(),
    findById: vi.fn(),
    save: vi.fn(),
  };

  const organisationApiStoreMock = {
    dataList: orgListSignal,
    loading: signal(false),
    reset: vi.fn(),
    getAll: vi.fn(),
    search: vi.fn(),
  };

  const branchApiStoreMock = {
    dataList: branchListSignal,
    loading: signal(false),
    reset: vi.fn(),
    findByOrganisation: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndividualEdit, TranslateModule.forRoot()],
      providers: [
        provideNativeDateAdapter(),
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
        { provide: OrganisationApiStore, useValue: organisationApiStoreMock },
        { provide: BranchApiStore, useValue: branchApiStoreMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(IndividualEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and initialize stores', () => {
    expect(component).toBeTruthy();
    expect(individualApiStoreMock.reset).toHaveBeenCalled();
    expect(organisationApiStoreMock.reset).toHaveBeenCalled();
    expect(branchApiStoreMock.reset).toHaveBeenCalled();
    expect(organisationApiStoreMock.getAll).toHaveBeenCalled();
  });

  it('should load by id when provided', () => {
    component.id = 'ind-7';
    component.ngOnInit();

    expect(individualApiStoreMock.findById).toHaveBeenCalledWith({ id: 'ind-7' });
  });

  it('should manage phone numbers', () => {
    component.addPhoneNumber();
    expect(component.editIndividualSignal().phoneNumbers.length).toBe(1);

    component.updatePhoneNumber(0, '123456789');
    expect(component.editIndividualSignal().phoneNumbers[0].phoneNumber).toBe('123456789');

    component.phoneNumbersRemove(0);
    expect(component.editIndividualSignal().phoneNumbers.length).toBe(0);
  });

  it('should navigate correctly on cancel', () => {
    component.cancel();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/individual']);

    component.editIndividualSignal.update((value) => ({
      ...value,
      id: 'ind-9',
    }));
    component.cancel();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/individual', 'details', 'ind-9']);
  });

  it('should save and normalize date of birth', () => {
    component.editIndividualSignal.update((value) => ({
      ...value,
      id: 'ind-1',
      dateOfBirth: new Date('2000-05-03T00:00:00.000Z'),
    }));

    component.save();

    expect(individualApiStoreMock.save).toHaveBeenCalled();
    const payload = individualApiStoreMock.save.mock.calls.at(-1)?.[0]?.individual;
    expect(payload.id).toBe('ind-1');
    expect(payload.dateOfBirth).toBe('2000-05-03');
  });
});
