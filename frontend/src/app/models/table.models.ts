export type SortDir = 'asc' | 'desc';

export interface SortState {
  key: string;
  dir: SortDir;
}

// export interface PageState {
//   page: number; // 1-based
//   pageSize: number;
// }

// /** Emitted on every sort / page change */
// export interface TableQueryEvent {
//   sort: SortState | null;
//   page: PageState;
// }

export interface TableQueryEvent {
  sort: { key: string; dir: 'asc' | 'desc' } | null;
  page: { page: number; pageSize: number };
}

/** Column definition supplied by the parent */
export interface ColumnDef<T = unknown> {
  key: keyof T & string;
  label: string;
  sortable?: boolean;
  class?: string; // td class
  headerClass?: string; // th class
}

/** The parent passes this back after each API call */
export interface TableData<T> {
  data: T[];
  total: number; // total records (for pagination maths)
}
