package com.employee_leave_tracker.backend.service;

public interface UserProvisioningService {

    void provisionUserFromEmployee(Long employeeId, String rawPassword);
}
