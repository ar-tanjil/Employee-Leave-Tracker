package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.auth.RoleAssignReqDTO;
import com.employee_leave_tracker.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PostMapping("/roles/assign")
    public ResponseEntity<CustomResponse> assignRoles(@RequestBody RoleAssignReqDTO request) {
        var response = userService.assignRoles(request);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

}
