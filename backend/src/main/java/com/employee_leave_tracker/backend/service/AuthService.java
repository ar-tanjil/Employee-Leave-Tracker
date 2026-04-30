package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.auth.LoginRequest;
import com.employee_leave_tracker.backend.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
