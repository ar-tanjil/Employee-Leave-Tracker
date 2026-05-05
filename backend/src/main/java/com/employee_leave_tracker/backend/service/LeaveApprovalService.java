package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.leave.LeaveApproverInstanceResDTO;
import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;
import com.employee_leave_tracker.backend.model.employee.Department;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;

import java.util.List;

public interface LeaveApprovalService {

    void initializeLeaveApprovalSteps(LeaveRequest request, Department department);


    void approveLeave(ApprovalActionDTO approvalActionDTO);

    void rejectLeave(ApprovalActionDTO approvalActionDTO);

    List<LeaveApproverInstanceResDTO> getAllPendingLeaveRequests(Long approverId);
}
