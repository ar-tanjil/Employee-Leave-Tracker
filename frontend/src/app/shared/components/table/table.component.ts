import { CommonModule, NgTemplateOutlet } from '@angular/common';
import {
  Component,
  computed,
  contentChild,
  contentChildren,
  input,
  OnChanges,
  OnInit,
  output,
  signal,
  SimpleChanges,
  TemplateRef,
} from '@angular/core';
import {
  ColumnDef,
  SortDir,
  TableQueryEvent,
} from '../../../models/table.models';
import { FormsModule } from '@angular/forms';
import { TableCellDirective } from '../../directives/table-cell.directive';

@Component({
  selector: 'app-table',
  imports: [CommonModule, NgTemplateOutlet, FormsModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css',
})
export class TableComponent<T extends object>
  implements OnInit {
  // Inputs
  columns = input.required<ColumnDef<T>[]>();
  pageData = input.required<() => { data: T[]; total: number }>();
  loading = input(false);
  initialPageSize = input(10);
  pageSizes = input([5, 10, 25, 50]);

  // Captures all <ng-template appCell="..."> from parent
  private customCells = contentChildren(TableCellDirective);

  // Captures <ng-template #actions let-row>
  actionsTemplate = contentChild<TemplateRef<{ $implicit: T }>>('actions');

  // Output
  queryChange = output<TableQueryEvent>();

  // State Signal
  query = signal<TableQueryEvent>({
    sort: null,
    page: { page: 1, pageSize: 10 },
  });

  // Computed Values
  totalPages = computed(() => {
    const total = this.pageData()().total;
    const pageSize = this.query().page.pageSize;
    return Math.max(1, Math.ceil(total / pageSize));
  });

  rangeStart = computed(() =>
    (this.query().page.page - 1) * this.query().page.pageSize + 1);

  rangeEnd = computed(() =>
    Math.min(this.query().page.page * this.query().page.pageSize, this.pageData()().total));


  skeletonRows = computed(() => {
    return Array(this.query().page.pageSize)
      .fill(0)
      .map((_, i) => i);
  });

  pageNumbers = computed(() => {
    const total = this.totalPages();
    const current = this.query().page.page;
    if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1);
    const pages: number[] = [1];
    if (current > 3) pages.push(-1);
    for (let i = Math.max(2, current - 1); i <= Math.min(total - 1, current + 1); i++) pages.push(i);
    if (current < total - 2) pages.push(-1);
    pages.push(total);
    return pages;
  });

  // OnInit
  ngOnInit(): void {
    this.query.update(q => ({ ...q, page: { ...q.page, pageSize: this.initialPageSize() } }));
    console.log('TableComponent initialized with query:', this.pageData()());
    this.queryChange.emit(this.query());
  }

  ngViewInit(): void {
    console.log('Custom cell templates:', this.pageData()());
  }

  // Helper to find custom template for a column
  getCustomTemplate(key: string): TemplateRef<any> | null {
    return this.customCells().find(c => c.columnName === key)?.template || null;
  }

  // sort
  onSort(key: string): void {
    const current = this.query();
    const dir = (current.sort?.key === key && current.sort.dir === 'asc') ? 'desc' : 'asc';
    this.updateQuery({ sort: { key, dir }, page: { ...current.page, page: 1 } });
  }

  isSortActive(key: string, dir: SortDir): boolean {
    const s = this.query().sort;
    return s?.key === key && s.dir == dir;
  }

  goToPage(p: number): void {
    if (p < 1 || p > this.totalPages() || p === this.query().page.page) return;
    this.updateQuery({ ...this.query(), page: { ...this.query().page, page: p } });
  }

  onPageSizeChange(size: any): void {
    this.updateQuery({ sort: this.query().sort, page: { page: 1, pageSize: Number(size) } });
  }

  // helper
  private updateQuery(newValues: TableQueryEvent) {
    this.query.set(newValues);
    this.queryChange.emit(this.query());
  }
}
