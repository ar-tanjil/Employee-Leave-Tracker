package com.employee_leave_tracker.backend.exception.handler;

import com.employee_leave_tracker.backend.dto.CustomErrorResponse;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleArgumentNotValidException(ArgumentNotValidException ex,
                                                                               HttpServletRequest request) {
        log.error(ex.getMessage(), ex);

        CustomErrorResponse response = new CustomErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                ex.getStatus().name(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNoDataFoundException(NoDataFoundException ex,
                                                                          HttpServletRequest request) {
        log.error(ex.getMessage(), ex);

        CustomErrorResponse response = new CustomErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                ex.getStatus().name(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                         HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        CustomErrorResponse response = new CustomErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                errors,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleUsernameNotFoundException(BadCredentialsException ex,
                                                                               HttpServletRequest request) {
        log.error(ex.getMessage(), ex);

        CustomErrorResponse response = new CustomErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex,
                                                               HttpServletRequest request) {
        log.error(ex.getMessage(), ex);

        CustomErrorResponse response = new CustomErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
