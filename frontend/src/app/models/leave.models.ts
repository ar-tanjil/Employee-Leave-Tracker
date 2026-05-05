export type LeaveReq = {
  id: number;
  startDate: string;
  endDate: string;
  duration: LeaveDuration;
  reason: string;
};

export enum LeaveDuration {
  FULL_DAY = 'FULL_DAY',
  FIRST_HALF = 'FIRST_HALF',
  SECOND_HALF = 'SECOND_HALF',
}

export type LeaveTable = {
  id: number;
  startDate: string;
  endDate: string;
  leaveTypeName: string;
  totalDays: number;
  status: string;
};

export type LeaveApproverTable = {
  referenceId?: number;
  leaveId?: number;
  employeeName?: string;
  leaveTypeName?: string;
  startDate?: Date;
  endDate?: Date;
  leaveDuration?: string;
  totalDays?: number;
  status?: string;
  reason?: string;
};

export type LeaveTypeRes = {
  id?: number;
  name?: string;
  allocatedDays?: number;
  usedDays?: number;
}




