package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.auth.PasswordChangeDto;
import com.employee_leave_tracker.backend.model.employee.Employee;

public interface UserProvisioningService {

    void provisionUserFromEmployee(Long employeeId, String rawPassword);

    void provisionUserFromEmployee(Employee employee);

}
