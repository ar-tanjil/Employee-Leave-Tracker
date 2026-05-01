package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;

public interface LeaveApprovalService {

    void initializeLeaveApprovalSteps(LeaveRequest request);


    void approveLeave(Long approvalInstanceId);

    void rejectLeave(Long approvalInstanceId);

}
