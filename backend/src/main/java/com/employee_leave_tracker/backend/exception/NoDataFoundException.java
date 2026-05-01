package com.employee_leave_tracker.backend.exception;

import org.springframework.http.HttpStatus;


public class NoDataFoundException extends CustomException {
    public NoDataFoundException(String message) {
        super(message, 800, HttpStatus.NOT_FOUND);
    }
}
