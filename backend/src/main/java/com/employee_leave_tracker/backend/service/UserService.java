package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.auth.RoleAssignReqDTO;
import com.employee_leave_tracker.backend.dto.auth.RoleDTO;

import java.util.List;

public interface UserService {
    List<RoleDTO> getAllActiveRoles();

    List<RoleDTO> getRolesByEmployeeId(Long employeeId);

    String assignRoles(RoleAssignReqDTO request);
}
