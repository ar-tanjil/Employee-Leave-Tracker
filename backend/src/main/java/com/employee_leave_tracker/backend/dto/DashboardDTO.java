package com.employee_leave_tracker.backend.dto;

public record DashboardDTO(
        Long totalEmployees,
        Long employeesOnLeave,
        Long employeeAttendance
) {

    public DashboardDTO(Long totalEmployees, Long employeesOnLeave) {
        this(totalEmployees, employeesOnLeave, totalEmployees - employeesOnLeave);
    }
}
