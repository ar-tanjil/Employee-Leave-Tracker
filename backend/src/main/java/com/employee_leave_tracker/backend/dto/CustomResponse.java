package com.employee_leave_tracker.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CustomResponse {

    private final int code;
    private final String status;
    private final String message;
    private final LocalDateTime timestamp;

    public CustomResponse(String message, int code, String status) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}
