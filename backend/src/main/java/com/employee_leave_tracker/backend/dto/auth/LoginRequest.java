package com.employee_leave_tracker.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "admin", description = "The user's registered email")
        @NotBlank
        String username,

        @Schema(example = "admin@123#", description = "The user's secret password")
        @NotBlank
        String password
) {
}