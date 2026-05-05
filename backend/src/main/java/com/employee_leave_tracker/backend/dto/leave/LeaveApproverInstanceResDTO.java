package com.employee_leave_tracker.backend.dto.leave;

import com.employee_leave_tracker.backend.constant.LeaveDuration;
import com.employee_leave_tracker.backend.constant.LeaveStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaveApproverInstanceResDTO(
        Long referenceId,
        Long leaveId,
        String employeeName,
        String leaveTypeName,
        LocalDate startDate,
        LocalDate endDate,
        LeaveDuration leaveDuration,
        Double totalDays,
        LeaveStatus status,
        String reason
) {}
