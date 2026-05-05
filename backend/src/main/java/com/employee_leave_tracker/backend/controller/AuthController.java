package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.dto.auth.PasswordChangeDto;
import com.employee_leave_tracker.backend.service.AuthService;
import com.employee_leave_tracker.backend.service.UserProvisioningService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtils authUtils;
    private final AuthService authService;
    private final UserProvisioningService userProvisioningService;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse> login(@RequestBody @Valid LoginRequest request) {
        var response = authService.login(request);
        return ResponseEntity.ok(new SuccessResponse<>(response, "Login successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<CustomResponse> changePassword(@RequestBody @Valid PasswordChangeDto request) {
        authService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Password changed successfully"));
    }

}