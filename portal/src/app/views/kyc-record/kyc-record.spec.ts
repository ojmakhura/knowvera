import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KycRecord } from './kyc-record';

describe('KycRecord', () => {
  let component: KycRecord;
  let fixture: ComponentFixture<KycRecord>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KycRecord]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KycRecord);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
