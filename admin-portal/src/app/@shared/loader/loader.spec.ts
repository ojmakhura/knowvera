import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateModule } from '@ngx-translate/core';
import { Loader } from './loader';
import { LoaderState } from './loader.state';

describe('Loader', () => {
  let component: Loader;
  let fixture: ComponentFixture<Loader>;
  let state: LoaderState;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Loader, TranslateModule.forRoot()],
    }).compileComponents();

    fixture = TestBed.createComponent(Loader);
    component = fixture.componentInstance;
    // The loader shows the shared LoaderState, which the app's stores set while they load
    state = TestBed.inject(LoaderState);
  });

  function overlay(): Element | null {
    return (fixture.nativeElement as HTMLElement).querySelector('.loader-overlay');
  }

  function message(): Element | null {
    return (fixture.nativeElement as HTMLElement).querySelector('.loader-message');
  }

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should not render overlay when not loading', () => {
    fixture.detectChanges();
    expect(overlay()).toBeNull();
  });

  it('should render overlay when loading', () => {
    state.isLoading.set(true);
    fixture.detectChanges();
    expect(overlay()).toBeTruthy();
  });

  it('should not display a message by default', () => {
    state.isLoading.set(true);
    fixture.detectChanges();
    expect(message()).toBeNull();
  });

  it('should display specified message', () => {
    state.isLoading.set(true);
    state.message.set('testing');
    fixture.detectChanges();
    expect(message()?.textContent?.trim()).toBe('testing');
  });

  it('should hide again after a reset', () => {
    state.isLoading.set(true);
    state.message.set('testing');
    fixture.detectChanges();

    component.loaderReset();
    fixture.detectChanges();

    expect(overlay()).toBeNull();
    expect(state.message()).toBeUndefined();
  });
});
