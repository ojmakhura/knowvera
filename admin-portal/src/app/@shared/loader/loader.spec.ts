import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';
import { Loader } from './loader';

describe('Loader', () => {
  let component: Loader;
  let fixture: ComponentFixture<Loader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Loader, NoopAnimationsModule, TranslateModule.forRoot()],
    }).compileComponents();

    fixture = TestBed.createComponent(Loader);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should not render overlay when not loading', () => {
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('.loader-overlay')).toBeNull();
  });

  it('should render overlay when loading', () => {
    component.isLoading = true;
    fixture.detectChanges();

    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('.loader-overlay')).toBeTruthy();
  });

  it('should not display a message by default', () => {
    component.isLoading = true;
    fixture.detectChanges();

    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('.loader-message')).toBeNull();
  });

  it('should display specified message', () => {
    component.isLoading = true;
    component.message = 'testing';
    fixture.detectChanges();

    const element = fixture.nativeElement as HTMLElement;
    const message = element.querySelector('.loader-message');

    expect(message?.textContent?.trim()).toBe('testing');
  });
});
