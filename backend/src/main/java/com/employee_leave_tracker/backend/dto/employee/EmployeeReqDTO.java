package com.employee_leave_tracker.backend.dto.employee;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record EmployeeReqDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long departmentId,
        Long designationId,
        LocalDate hireDate,
        String employmentType,
        String address,
        MultipartFile image
) {
}
