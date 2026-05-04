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
  duration: LeaveDuration;
  status: string;
};

export type LeaveApproverTable = {
  leaveId: number;
  referenceId: number;
  employeeName: string;
  startDate: string;
  endDate: string;
  applyDate: string;
  duration: number;
  status: string;
};
