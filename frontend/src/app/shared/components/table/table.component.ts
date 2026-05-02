import { CommonModule, NgTemplateOutlet } from '@angular/common';
import {
  Component,
  computed,
  contentChild,
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
  SortState,
  TableData, TableQueryEvent,
} from '../../../models/table.models';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-table',
  imports: [CommonModule, NgTemplateOutlet, FormsModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css',
})
export class TableComponent<T extends object>
  implements OnInit, OnChanges {
  // input
  columns = input.required<ColumnDef<T>[]>();
  page = input.required<() => TableData<T>>();
  loading = input(false);
  initialPageSize = input(10);
  pageSizes = input([5, 10, 25, 50]);

  actionsTemplate = contentChild<TemplateRef<{ $implicit: T }>>('actions');

  // output
  queryChange = output<TableQueryEvent>();

  // state
  query = signal<TableQueryEvent>({
    sort: null,
    page: { page: 1, pageSize: 10 },
  });

  totalPages = computed(() => {
    const { total } = this.page()();
    const { pageSize } = this.query().page;
    return Math.max(1, Math.ceil(total / pageSize));
  });

  rangeStart = computed(() => {
    const { page, pageSize } = this.query().page;
    return (page - 1) * pageSize + 1;
  });

  rangeEnd = computed(() => {
    const { page, pageSize } = this.query().page;
    return Math.min(page * pageSize, this.page()().total);
  });

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
    for (
      let i = Math.max(2, current - 1);
      i <= Math.min(total - 1, current + 1);
      i++
    ) {
      pages.push(i);
    }
    if (current < total - 2) pages.push(-1);
    pages.push(total);
    return pages;
  });

  ngOnInit(): void {
    this.updateQuery({
      sort: null,
      page: { page: 1, pageSize: this.initialPageSize() },
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['page'] && !changes['page'].firstChange) {
    }
  }

  // sort
  onSort(key: string): void {
    const current = this.query();
    let newSort: SortState;

    if (current.sort?.key === key) {
      newSort = {
        key,
        dir: current.sort.dir === 'asc' ? 'desc' : 'asc',
      };
    } else {
      newSort = { key, dir: 'asc' };
    }
    this.updateQuery({
      sort: newSort,
      page: { ...current.page, page: 1 },
    });
  }

  isSortActive(key: string, dir: SortDir): boolean {
    const s = this.query().sort;
    return s?.key === key && s.dir == dir;
  }

  goToPage(p: number): void {
    const current = this.query();
    if (p < 1 || p > this.totalPages() || p === current.page.page) {
      return;
    }
    this.updateQuery({
      ...current,
      page: { ...current.page, page: p },
    });
  }

  onPageSizeChange(size: number): void {
    const current = this.query();
    this.updateQuery({
      ...current,
      page: { page: 1, pageSize: Number(size) },
    });
  }

  // helper
  updateQuery(newValues: TableQueryEvent) {
    this.query.set(newValues);
    this.queryChange.emit(this.query());
  }
}
