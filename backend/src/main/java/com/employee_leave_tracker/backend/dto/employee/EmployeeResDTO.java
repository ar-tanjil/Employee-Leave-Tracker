package com.employee_leave_tracker.backend.dto.employee;

import java.time.LocalDate;

public record EmployeeResDTO(
        Long id,
        String employeeCode,
        String firstName,
        String lastName,
        String email,
        Long departmentId,
        String departmentName,
        Long designationId,
        String designationName,
        Long managerId,
        String managerName,
        LocalDate hireDate,
        String employmentType
) {


}
