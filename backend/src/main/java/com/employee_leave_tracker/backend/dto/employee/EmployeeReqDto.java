package com.employee_leave_tracker.backend.dto.employee;

import java.time.LocalDate;

public record EmployeeReqDto(
        Long id,
        String employeeCode,
        String firstName,
        String lastName,
        String email,
        Long departmentId,
        Long designationId,
        Long managerId,
        LocalDate hireDate,
        String employmentType
) {
}
