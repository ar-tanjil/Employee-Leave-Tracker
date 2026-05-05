package com.employee_leave_tracker.backend.dto.leave;

public record LeaveTypeDTO(
        Long id,
        String name,
        Double allocatedDays,
        Double usedDays
) {
}
