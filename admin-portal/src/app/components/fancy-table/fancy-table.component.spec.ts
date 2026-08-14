import { EventEmitter, signal, WritableSignal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatPaginator } from '@angular/material/paginator';
import { MatRadioChange } from '@angular/material/radio';
import { TranslateModule } from '@ngx-translate/core';
import { ActionTemplate } from '@app/models/action-template';
import { ColumnModel } from '@app/models/column.model';
import { Page } from '@app/models/page.model';
import { SelectionType } from '@app/models/selection-type.model';
import { FancyTableComponent } from './fancy-table.component';

type Row = {
  id: string;
  name: string;
  code?: string;
};

describe('FancyTableComponent', () => {
  let fixture: ComponentFixture<FancyTableComponent<Row>>;
  let component: FancyTableComponent<Row>;
  let fancyDataSignal: WritableSignal<any>;

  const mainColumns: ColumnModel[] = [new ColumnModel('name', 'Name')];
  const dataColumns: ColumnModel[] = [
    new ColumnModel('name', 'Name'),
    new ColumnModel('code', 'Code'),
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FancyTableComponent, NoopAnimationsModule, TranslateModule.forRoot()],
    }).compileComponents();

    fixture = TestBed.createComponent(FancyTableComponent<Row>);
    component = fixture.componentInstance;
    component.mainColumns = mainColumns;
    component.dataColumns = dataColumns;
    fancyDataSignal = component.dataSignal as WritableSignal<any>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should configure columns with actions by default', () => {
    component.ngOnInit();

    expect(component.allColumns).toEqual(['name', 'code', 'actions']);
  });

  it('should prepend selection column when selection is enabled', () => {
    component.showActions = false;
    component.selectionType = SelectionType.MULTIPLE;

    component.ngOnInit();

    expect(component.allColumns).toEqual(['selection', 'name', 'code']);
  });

  it('should fill items from non-paged data signal', () => {
    const rows = [
      { id: '1', name: 'Alice', code: 'A1' },
      { id: '2', name: 'Bob', code: 'B2' },
    ];

    component.paged = false;
    fancyDataSignal.set(rows);
    fixture.detectChanges();

    expect(component.dataItems().length).toBe(2);
    expect(component.totalElements).toBe(2);
  });

  it('should fill items from paged signal and keep total elements', () => {
    const page = new Page<Row>();
    page.content = [{ id: '3', name: 'Charlie', code: 'C3' }];
    page.totalElements = 40;

    component.paged = true;
    fancyDataSignal.set(page);
    fixture.detectChanges();

    expect(component.dataItems()).toEqual([{ id: '3', name: 'Charlie', code: 'C3' }]);
    expect(component.totalElements).toBe(40);
  });

  it('should emit action click payload', () => {
    const emitSpy = vi.spyOn(component.actionClicked, 'emit');

    component.onActionClicked('view', '99');

    expect(emitSpy).toHaveBeenCalledWith({ action: 'view', id: '99' });
  });

  it('should select one row for radio selection', () => {
    const row = { id: '1', name: 'Alice' };

    component.radioSelected({ value: row } as MatRadioChange, row);

    expect(component.selectedItems).toEqual([row]);
  });

  it('should add rows for checkbox selection', () => {
    const row = { id: '1', name: 'Alice' };

    component.checkboxSelected({ checked: true } as MatCheckboxChange, row);
    expect(component.selectedItems).toEqual([row]);
  });

  it('should remove using custom selectionFilter', () => {
    const selectedA = { id: '1', name: 'Alice' };
    const selectedB = { id: '2', name: 'Bob' };

    component.selectedItems = [selectedA, selectedB];
    component.selectionFilter = (a: Row, b: Row) => a.id === b.id;

    component.checkboxSelected({ checked: false } as MatCheckboxChange, { id: '2', name: 'ignored' });

    expect(component.selectedItems).toEqual([selectedA]);
  });

  it('should emit page changes when paginator emits', () => {
    const emitSpy = vi.spyOn(component.pageChanged, 'emit');
    const paginator = {
      page: new EventEmitter<any>(),
      pageIndex: 0,
      pageSize: 10,
    } as unknown as MatPaginator;

    component.paginator = paginator;

    (paginator as any).pageIndex = 3;
    (paginator as any).pageSize = 25;
    paginator.page.emit(paginator as any);

    expect(emitSpy).toHaveBeenCalledWith({ pageNumber: 3, pageSize: 25 });
  });

  it('should keep action templates input', () => {
    const actions: ActionTemplate[] = [new ActionTemplate('edit', 'Edit', 'edit')];

    component.actions = actions;

    expect(component.actions[0].label).toBe('Edit');
  });
});
