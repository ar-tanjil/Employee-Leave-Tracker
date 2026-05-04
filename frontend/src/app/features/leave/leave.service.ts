import { inject, Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { LeaveReq, LeaveTable } from '../../models/leave.models';
import { delay } from 'rxjs';
import { PaginationParams } from '../../models/api-response.model';

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
}
