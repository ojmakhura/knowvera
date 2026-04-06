import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { EditKycRecord } from './edit-kyc-record';

describe('EditKycRecord', () => {
  let component: EditKycRecord;
  let fixture: ComponentFixture<EditKycRecord>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditKycRecord, NoopAnimationsModule],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(EditKycRecord);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
