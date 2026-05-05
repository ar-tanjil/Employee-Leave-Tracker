package com.employee_leave_tracker.backend.dto.workflow;

public record ApprovalActionDTO(
        Long approvalInstanceId,
        String comments
) { }
