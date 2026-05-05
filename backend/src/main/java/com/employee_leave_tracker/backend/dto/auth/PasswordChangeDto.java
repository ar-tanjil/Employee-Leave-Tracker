package com.employee_leave_tracker.backend.dto.auth;

public record PasswordChangeDto(
        String oldPassword,
        String newPassword
) {
}
