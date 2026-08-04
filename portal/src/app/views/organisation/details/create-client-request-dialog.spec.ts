import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { CreateClientRequestDialogComponent } from './create-client-request-dialog';
import { OrganisationApiStore } from '@app/store/bw/co/kyvera/organisation/organisation-api.store';
import { IndividualApiStore } from '@app/store/bw/co/kyvera/individual/individual-api.store';

describe('CreateClientRequestDialogComponent', () => {
  let component: CreateClientRequestDialogComponent;
  let fixture: ComponentFixture<CreateClientRequestDialogComponent>;

  const dialogRefMock = {
    close: () => undefined,
  };

  const organisationApiStoreMock = {
    dataList: () => [],
    search: () => undefined,
  };

  const individualApiStoreMock = {
    dataList: () => [],
    search: () => undefined,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateClientRequestDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: dialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: OrganisationApiStore, useValue: organisationApiStoreMock },
        { provide: IndividualApiStore, useValue: individualApiStoreMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateClientRequestDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});