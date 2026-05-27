import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { Loader } from './loader';

describe('Loader', () => {
  let component: Loader;
  let fixture: ComponentFixture<Loader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        Loader,
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Loader);
    component = fixture.componentInstance;
  });

  it('should initialize with loading disabled', () => {
    expect(component.isLoading).toBe(false);
  });

  it('should allow enabling loading state', () => {
    component.isLoading = true;
    expect(component.isLoading).toBe(true);
  });

  it('should have no message by default', () => {
    expect(component.message).toBeUndefined();
  });

  it('should update message value', () => {
    component.message = 'testing';
    expect(component.message).toBe('testing');
  });
});
