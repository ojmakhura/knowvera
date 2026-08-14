import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { KycRecord } from './kyc-record';

describe('KycRecord', () => {
  let component: KycRecord;
  let fixture: ComponentFixture<KycRecord>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KycRecord],
      providers: [
        provideRouter([]),
        {
          provide: ToastrService,
          useValue: {
            success: () => undefined,
            error: () => undefined,
          },
        },
      ],
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
