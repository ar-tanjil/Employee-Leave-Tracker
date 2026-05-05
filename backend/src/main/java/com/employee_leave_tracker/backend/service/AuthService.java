package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.dto.auth.LoginResponse;
import com.employee_leave_tracker.backend.dto.auth.PasswordChangeDto;
import com.employee_leave_tracker.backend.dto.auth.RoleDTO;

import java.util.List;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void changePassword(PasswordChangeDto request);
}
