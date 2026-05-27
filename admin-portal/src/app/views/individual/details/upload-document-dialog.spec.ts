import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { IndividualUploadDocumentDialogComponent } from './upload-document-dialog';

describe('IndividualUploadDocumentDialogComponent', () => {
  let fixture: ComponentFixture<IndividualUploadDocumentDialogComponent>;
  let component: IndividualUploadDocumentDialogComponent;

  const dialogRefMock = {
    close: vi.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndividualUploadDocumentDialogComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            documentTypes: [{ id: 'dt-1', name: 'Passport' }],
          },
        },
        { provide: MatDialogRef, useValue: dialogRefMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(IndividualUploadDocumentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update selected file from input event', () => {
    const file = new File(['abc'], 'passport.pdf', { type: 'application/pdf' });
    const event = {
      target: {
        files: [file],
      },
    } as unknown as Event;

    component.onFileSelected(event);

    expect(component.selectedFile()?.name).toBe('passport.pdf');
  });

  it('should validate upload availability', () => {
    expect(component.canUpload()).toBe(false);

    component.selectedDocumentTypeId = 'dt-1';
    component.selectedFile.set(new File(['x'], 'doc.txt'));

    expect(component.canUpload()).toBe(true);
  });

  it('should close with result on upload', () => {
    const file = new File(['abc'], 'passport.pdf', { type: 'application/pdf' });
    component.selectedDocumentTypeId = 'dt-1';
    component.selectedFile.set(file);

    component.onUpload();

    expect(dialogRefMock.close).toHaveBeenCalledWith({
      documentTypeId: 'dt-1',
      file,
    });
  });

  it('should close without payload on cancel', () => {
    component.onCancel();

    expect(dialogRefMock.close).toHaveBeenCalledWith();
  });
});
