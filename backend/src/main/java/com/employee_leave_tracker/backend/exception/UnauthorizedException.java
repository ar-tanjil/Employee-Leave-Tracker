package com.employee_leave_tracker.backend.exception;

import org.springframework.http.HttpStatus;


public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message) {
        super(message, 800, HttpStatus.UNAUTHORIZED);
    }
}
