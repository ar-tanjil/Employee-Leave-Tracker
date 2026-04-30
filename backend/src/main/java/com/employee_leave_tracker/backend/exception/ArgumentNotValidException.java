package com.employee_leave_tracker.backend.exception;


import org.springframework.http.HttpStatus;

public class ArgumentNotValidException extends CustomException {


    public ArgumentNotValidException(String message) {
        super(message, 999, HttpStatus.BAD_REQUEST);
    }

    public ArgumentNotValidException(String message, String error) {
        super(message, 999, HttpStatus.BAD_REQUEST, error);
    }


}
