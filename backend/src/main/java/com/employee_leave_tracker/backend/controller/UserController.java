package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.auth.RoleAssignReqDTO;
import com.employee_leave_tracker.backend.dto.employee.EmployeeResDTO;
import com.employee_leave_tracker.backend.model.auth.UserAccount;
import com.employee_leave_tracker.backend.security.AppUserPrincipal;
import com.employee_leave_tracker.backend.service.EmployeeService;
import com.employee_leave_tracker.backend.service.UserService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUtils authUtils;
    private final UserService userService;
    private final EmployeeService employeeService;

    @GetMapping("/profile")
    public ResponseEntity<CustomResponse> getProfile() {
        EmployeeResDTO response;
        if (authUtils.hasRole("SYSTEM_ADMIN")) {

            AppUserPrincipal user = authUtils.getCurrentUser();

             response = EmployeeResDTO.builder()
                    .firstName("SYSTEM")
                     .employeeCode(user.getUsername())
                    .lastName("ADMIN")
                    .email("system@admin.com")
                    .build();
        } else {
            Long id = authUtils.getCurrentUserEmployeeId();
             response = employeeService.getEmployeeById(id);
        }

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("/roles")
    public ResponseEntity<CustomResponse> getRoles() {
        var response = userService.getAllActiveRoles();
        return ResponseEntity.ok(new ListResponse<>(response, "Roles fetched successfully"));
    }

    @GetMapping("/roles/{employeeId}")
    public ResponseEntity<CustomResponse> getRolesByEmployeeId(@PathVariable Long employeeId) {
        var response = userService.getRolesByEmployeeId(employeeId);
        return ResponseEntity.ok(new ListResponse<>(response, "Roles fetched successfully"));
    }

    @PostMapping("/assign-roles")
    public ResponseEntity<CustomResponse> assignRoles(@RequestBody RoleAssignReqDTO request) {
        var response = userService.assignRoles(request);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

}
