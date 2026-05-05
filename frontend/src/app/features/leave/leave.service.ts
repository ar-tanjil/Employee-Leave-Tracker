import { inject, Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { LeaveApproverTable, LeaveReq, LeaveTable, LeaveTypeRes } from '../../models/leave.models';
import { delay } from 'rxjs';
import { PaginationParams } from '../../models/api-response.model';
import { IDName } from '../../models/common.model';

@Injectable()
export class LeaveService {
  // inject
  private readonly api = inject(ApiService);

  saveLeave(leave: LeaveReq) {
    return this.api.post<{ message: string }, LeaveReq>('v1/leaves', leave);
  }

  getLeaves(params: PaginationParams) {
    return this.api.getPaged<LeaveTable>('v1/leaves', params);
  }

  getLeaveTypes() {
    return this.api.get<LeaveTypeRes[]>('v1/leaves/types');
  }

  getPendingLeaveReq(params: PaginationParams) {
    return this.api.getPaged<LeaveApproverTable>('v1/leaves/pending', params);
  }

  approveLeave(payload: { approvalInstanceId: number, comments: string }) {
    return this.api.post<{ message: string }, { approvalInstanceId: number, comments: string }>('v1/leaves/approve', payload);
  }

  rejectLeave(payload: { approvalInstanceId: number, comments: string }) {
    return this.api.post<{ message: string }, { approvalInstanceId: number, comments: string }>('v1/leaves/reject', payload);
  }
}
