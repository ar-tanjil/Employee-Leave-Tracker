package com.employee_leave_tracker.backend.dto.employee;

import lombok.Builder;

import java.time.LocalDate;

@Builder
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
        LocalDate hireDate,
        String employmentType,
        String address,
        String image
) {


}
