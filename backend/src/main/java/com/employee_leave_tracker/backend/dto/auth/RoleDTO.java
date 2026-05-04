package com.employee_leave_tracker.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleDTO(Long id, String name,
                      String employeeName,
                      String employeeCode,
                      String description,
                      List<PermissionDTO> permissions) {
}
