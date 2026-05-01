package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;

public interface LeaveApprovalService {

    void approveLeave(ApprovalActionDTO action);

    void rejectLeave(ApprovalActionDTO action);

}
