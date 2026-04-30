package com.employee_leave_tracker.backend.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException {

    private final Integer code;
    private final HttpStatus status;
    private final String error;
    private final LocalDateTime timestamp;

    public CustomException(String message, Integer code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
        this.error = null;
        this.timestamp = LocalDateTime.now();
    }

    public CustomException(String message, Integer code, HttpStatus status, String error) {
        super(message);
        this.code = code;
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }


    public CustomException(String message, Throwable cause,
                           Integer code, HttpStatus status, String error) {
        super(message, cause);
        this.code = code;
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }


    public CustomException(String message, Throwable cause,
                           boolean enableSuppression, boolean writableStackTrace,
                           Integer code, HttpStatus status, String error) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
        ;
    }
}
