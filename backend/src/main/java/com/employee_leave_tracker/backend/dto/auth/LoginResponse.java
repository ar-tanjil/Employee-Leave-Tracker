package com.employee_leave_tracker.backend.dto.auth;

import java.util.List;

public record LoginResponse(String token, String username, List<String> roles) {}
