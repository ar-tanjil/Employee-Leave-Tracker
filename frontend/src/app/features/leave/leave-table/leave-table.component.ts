import { Component, computed, inject, signal } from '@angular/core';
import { LeaveFormComponent } from '../leave-form/leave-form.component';

import { LeaveService } from '../leave.service';
import { LeaveTable } from '../../../models/leave.models';
import { ColumnDef, TableQueryEvent } from '../../../models/table.models';
import { PaginationParams } from '../../../models/api-response.model';
import { finalize } from 'rxjs';
import { TableComponent } from '../../../shared/components/table/table.component';
import { TableCellDirective } from "../../../shared/directives/table-cell.directive";
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-leave-table',
  imports: [LeaveFormComponent, TableComponent, TableCellDirective, DatePipe],
  templateUrl: './leave-table.component.html',
  styleUrl: './leave-table.component.css',
})
export class LeaveTableComponent {
  // inject
  private readonly leaveService = inject(LeaveService);

  // dialog state
  readonly showForm = signal(false);
  readonly showView = signal(false);

  // table state

  private readonly leaveList = signal<LeaveTable[]>([]);
  private readonly total = signal(0);

  readonly loading = signal(false);
  readonly pageData = computed(() => ({
    data: this.leaveList(),
    total: this.total(),
  }));

  // query change handler
  onQueryChange(event: TableQueryEvent): void {
    this.loading.set(true);
    // buid pagenation and sorting params
    let params: PaginationParams = {
      page: event.page.page - 1,
      pageSize: event.page.pageSize,
    };

    if (event.sort) {
      params.sortBy = event.sort.key;
      params.sortOrder = event.sort.dir;
    }

    this.leaveService
      .getLeaves(params)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe((response) => {
        this.leaveList.set(response.data);
        this.total.set(response.metaData.totalElements);
      });
  }

  openForm(id: number | null): void {
    if (!id) {
      return this.showForm.set(true);
    }
  }

  onSaved() {
    this.onQueryChange({ page: { page: 1, pageSize: 10 }, sort: null });
    this.showForm.set(false);
  }

  // presentation
  getStatusClass(status: string): string {
  switch (status) {
    case 'APPROVED': return 'bg-green-100 text-green-800';
    case 'REJECTED': return 'bg-red-800 text-red-100';
    default: return 'bg-gray-100 text-gray-800';
  }
}

  // columns definition
  columnsDef: ColumnDef<LeaveTable>[] = [
    {
      key: 'startDate',
      label: 'Date',
    },
    { key: 'totalDays', label: 'Total Day', sortable: false },
    { key: 'leaveTypeName', label: 'Leave Type', },
    { key: 'status', label: 'Status', },
  ];
}
