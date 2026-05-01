package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.dto.auth.LoginResponse;
import com.employee_leave_tracker.backend.service.AuthService;
import com.employee_leave_tracker.backend.service.UserProvisioningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserProvisioningService userProvisioningService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/provision-user-from-employee")
    public ResponseEntity<?> provisionUserFromEmployee(@RequestParam Long employeeId,
                                                       @RequestParam String rawPassword) {
        userProvisioningService.provisionUserFromEmployee(employeeId, rawPassword);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}