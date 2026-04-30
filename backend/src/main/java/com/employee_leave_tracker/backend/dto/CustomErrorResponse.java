package com.employee_leave_tracker.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse extends CustomResponse {
    private final String errors;
    private final String path;

    public CustomErrorResponse(String message, int code,
                            String status, String errors,
                            String path) {
        super(message, code, status);
        this.errors = errors;
        this.path = path;
    }

    public CustomErrorResponse(String message, int code,
                            String status, String path) {
        super(message, code, status);
        this.errors = null;
        this.path = path;
    }
}
