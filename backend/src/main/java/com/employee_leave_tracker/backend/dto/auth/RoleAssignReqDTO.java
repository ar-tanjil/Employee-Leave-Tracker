package com.employee_leave_tracker.backend.dto.auth;


import java.util.List;

public record RoleAssignReqDTO(Long employeeId,
                              List<Long> roleIds) {

}
