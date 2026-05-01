package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.service.AuthService;
import com.employee_leave_tracker.backend.service.UserProvisioningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserProvisioningService userProvisioningService;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse> login(@RequestBody @Valid LoginRequest request) {
        var response = authService.login(request);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PostMapping("/provision-user-from-employee")
    public ResponseEntity<CustomResponse> provisionUserFromEmployee(@RequestParam Long employeeId,
                                                                    @RequestParam String rawPassword) {
        userProvisioningService.provisionUserFromEmployee(employeeId, rawPassword);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("User provisioned successfully"));
    }
}