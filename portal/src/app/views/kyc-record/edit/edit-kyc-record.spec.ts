import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditKycRecord } from './edit-kyc-record';

describe('EditKycRecord', () => {
  let component: EditKycRecord;
  let fixture: ComponentFixture<EditKycRecord>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditKycRecord]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditKycRecord);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
