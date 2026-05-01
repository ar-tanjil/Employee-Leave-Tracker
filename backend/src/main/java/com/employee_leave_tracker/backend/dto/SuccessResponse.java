package com.employee_leave_tracker.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> extends CustomResponse {

    private final T data;

    public SuccessResponse(@Nullable T data, String message) {
        super(message, HttpStatus.OK.value(), HttpStatus.OK.name());
        this.data = data;
    }

    public SuccessResponse(@Nullable T data) {
        String message = data != null ? "Data Found" : "No Data Found";
        super(message, HttpStatus.OK.value(), HttpStatus.OK.name());
        this.data = data;
    }

    public SuccessResponse(String message) {
        super(message, HttpStatus.OK.value(), HttpStatus.OK.name());
        this.data = null;
    }

    public SuccessResponse(String message, HttpStatus status) {
        super(message, status.value(), status.name());
        this.data = null;
    }
}
