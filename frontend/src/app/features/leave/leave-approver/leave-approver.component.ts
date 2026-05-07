import { LeaveApproverTable } from './../../../models/leave.models';
import { LeaveService } from './../leave.service';
import { Component, computed, inject, signal } from '@angular/core';
import { TableComponent } from "../../../shared/components/table/table.component";
import { ColumnDef, TableQueryEvent } from '../../../models/table.models';
import { PaginationParams } from '../../../models/api-response.model';
import { finalize } from 'rxjs';
import { TableCellDirective } from "../../../shared/directives/table-cell.directive";
import { DatePipe } from '@angular/common';
import { IconComponent } from "../../../shared/components/icon/icon.component";
import { TooltipDirective } from "../../../shared/directives/tooltip.directive";
import Swal from 'sweetalert2';
import { LeaveViewComponent } from "../leave-view/leave-view.component";

@Component({
  selector: 'app-leave-approver',
  imports: [TableComponent, TableCellDirective, DatePipe, IconComponent, TooltipDirective, LeaveViewComponent],
  providers: [LeaveService],
  templateUrl: './leave-approver.component.html',
  styleUrl: './leave-approver.component.css',
})
export class LeaveApproverComponent {

  // inject
  private readonly leaveService = inject(LeaveService);


  // child component data
  readonly selectedLeave = signal<LeaveApproverTable | null>(null);

  // child component state
  readonly showLeaveDetail = signal(false);


  // table state
  private readonly leaveList = signal<LeaveApproverTable[]>([]);
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

    this.loading.set(false);

    this.leaveService
      .getPendingLeaveReq(params)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe((response) => {
        this.leaveList.set(response.data);
        this.total.set(response.data.length);
      });

  }



  async handleLeaveAction(leave: LeaveApproverTable): Promise<void> {
    const result = await Swal.fire({
      title: 'Process Leave Request',
      text: `Do you want to Approve or Reject the leave for ${leave.employeeName}?`,
      icon: 'question',
      showCancelButton: true,
      showDenyButton: true,
      confirmButtonText: 'Approve',
      denyButtonText: 'Reject',
      cancelButtonText: 'Wait, go back',
      confirmButtonColor: '#10b981', // Emerald/Green
      denyButtonColor: '#ef4444',    // Red
    });

    const payload = {
      approvalInstanceId: leave.referenceId!,
      comments: ""
    }

    // confirm
    if (result.isConfirmed) {
      this.leaveService.approveLeave(payload).subscribe({
        next: () => {
          Swal.fire('Approved!', 'The leave request has been accepted.', 'success');
          this.onQueryChange({ page: { page: 1, pageSize: 10 }, sort: null });
        },
        error: () => Swal.fire('Error', 'Approval failed.', 'error')
      });

      // deny
    } else if (result.isDenied) {
      this.leaveService.rejectLeave(payload).subscribe({
        next: () => {
          Swal.fire('Rejected', 'The leave request was denied.', 'info');
          this.onQueryChange({ page: { page: 1, pageSize: 10 }, sort: null });
        },
        error: () => Swal.fire('Error', 'Rejection failed.', 'error')
      });
    }

    // If result.isDismissed is true, nothing happens.
  }

  openLeaveDetails(data: LeaveApproverTable) {
    this.selectedLeave.set(data);
    this.showLeaveDetail.set(true);
  }

  closeLeaveDetails() {
    this.selectedLeave.set(null);
    this.showLeaveDetail.set(false);
  }



  // columns definition
  columnsDef: ColumnDef<LeaveApproverTable>[] = [
    {
      key: 'employeeName',
      label: 'Employee Name',
      class: 'font-medium text-slate-900'
    },
    {
      key: 'startDate',
      label: 'Date Range',
      sortable: false,
      class: 'font-medium text-slate-900',
    },
    { key: 'leaveTypeName', label: 'Leave Type', sortable: false },
    { key: 'status', label: 'Status', sortable: false },
  ];

}
