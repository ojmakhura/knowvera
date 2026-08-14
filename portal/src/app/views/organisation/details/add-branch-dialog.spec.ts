import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { BranchFormDialogComponent } from './add-branch-dialog';

describe('BranchFormDialogComponent', () => {
  let component: BranchFormDialogComponent;
  let fixture: ComponentFixture<BranchFormDialogComponent>;

  const dialogRefMock = {
    close: () => undefined,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchFormDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: dialogRefMock },
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            code: 'BR-001',
            name: 'Branch One',
            physicalAddress: '123 Main St',
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BranchFormDialogComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
