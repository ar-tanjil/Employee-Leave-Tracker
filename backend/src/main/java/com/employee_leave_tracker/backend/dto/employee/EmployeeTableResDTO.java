package com.employee_leave_tracker.backend.dto.employee;

public record EmployeeTableResDTO(
        Long id,
        String firstName,
        String lastName,
        String designation,
        String department
) {
}
