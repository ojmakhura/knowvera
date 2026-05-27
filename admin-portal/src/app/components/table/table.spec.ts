import { signal, WritableSignal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatRadioChange } from '@angular/material/radio';
import { TranslateModule } from '@ngx-translate/core';
import { ActionTemplate } from '@app/models/action-template';
import { ColumnModel } from '@app/models/column.model';
import { Page } from '@app/models/page.model';
import { SelectionType } from '@app/models/selection-type.model';
import { TableComponent } from './table';

type Row = {
  id: string;
  name: string;
  code?: string;
};

describe('TableComponent', () => {
  let fixture: ComponentFixture<TableComponent<Row>>;
  let component: TableComponent<Row>;
  let tableDataSignal: WritableSignal<any>;

  const dataColumns: ColumnModel[] = [
    new ColumnModel('id', 'Id'),
    new ColumnModel('name', 'Name'),
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableComponent, NoopAnimationsModule, TranslateModule.forRoot()],
    }).compileComponents();

    fixture = TestBed.createComponent(TableComponent<Row>);
    component = fixture.componentInstance;
    component.dataColumns = dataColumns;
    tableDataSignal = component.dataSignal as WritableSignal<any>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should build columns with actions by default', () => {
    component.selectionType = SelectionType.NONE;

    component.ngOnInit();

    expect(component.allColumns).toEqual(['id', 'name', 'actions']);
  });

  it('should add selection column when selection is enabled', () => {
    component.showActions = false;
    component.selectionType = SelectionType.SINGLE;

    component.ngOnInit();

    expect(component.allColumns).toEqual(['selection', 'id', 'name']);
  });

  it('should populate non-paged data from signal', () => {
    const rows = [
      { id: '1', name: 'Alice' },
      { id: '2', name: 'Bob' },
    ];

    component.paged = false;
    tableDataSignal.set(rows);
    fixture.detectChanges();

    expect(component.dataSource.data.length).toBe(2);
    expect(component.totalElements).toBe(2);
  });

  it('should populate paged data from signal', () => {
    const page = new Page<Row>();
    page.content = [{ id: '3', name: 'Charlie' }];
    page.page.totalElements = 11;

    component.paged = true;
    tableDataSignal.set(page);
    fixture.detectChanges();

    expect(component.dataSource.data).toEqual([{ id: '3', name: 'Charlie' }]);
    expect(component.totalElements).toBe(11);
  });

  it('should emit action events', () => {
    const emitSpy = vi.spyOn(component.actionClicked, 'emit');

    component.onActionClicked('edit', { id: '1', name: 'Alice' });

    expect(emitSpy).toHaveBeenCalledWith({
      action: 'edit',
      row: { id: '1', name: 'Alice' },
    });
  });

  it('should select one item for radio selection', () => {
    component.radioSelected({ value: true } as MatRadioChange, { id: '2', name: 'Bob' });

    expect(component.selectedItems).toEqual([{ id: '2', name: 'Bob' }]);
  });

  it('should add items for checkbox selection', () => {
    const row = { id: '1', name: 'Alice' };

    component.checkboxSelected({ checked: true } as MatCheckboxChange, row);
    expect(component.selectedItems).toEqual([row]);
  });

  it('should use selectionFilter when removing selected items', () => {
    const selectedA = { id: '1', name: 'Alice' };
    const selectedB = { id: '2', name: 'Bob' };

    component.selectedItems = [selectedA, selectedB];
    component.selectionFilter = (a: Row, b: Row) => a.id === b.id;

    component.checkboxSelected({ checked: false } as MatCheckboxChange, { id: '2', name: 'Different label' });

    expect(component.selectedItems).toEqual([selectedA]);
  });

  it('should emit paginator change events', () => {
    const emitSpy = vi.spyOn(component.paginatorChange, 'emit');

    component.tablePaginator.page.emit({
      pageIndex: 2,
      pageSize: 50,
      length: 100,
      previousPageIndex: 1,
    });

    expect(emitSpy).toHaveBeenCalledWith({
      pageNumber: 2,
      pageSize: 50,
    });
  });

  it('should keep action templates input', () => {
    const actions: ActionTemplate[] = [new ActionTemplate('edit', 'Edit', 'edit')];

    component.actions = actions;

    expect(component.actions[0].id).toBe('edit');
  });
});
