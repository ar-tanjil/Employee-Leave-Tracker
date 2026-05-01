package com.employee_leave_tracker.backend.dto.leave;

import com.employee_leave_tracker.backend.constant.LeaveDuration;

import java.time.LocalDate;

public record LeaveResDTO(
        Long employeeId,
        Long leaveTypeId,
        LocalDate startDate,
        LocalDate endDate,
        LeaveDuration duration,
        String reason
) {}
