package com.employee_leave_tracker.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResponse<T> extends CustomResponse {

    private final Collection<T> data;

    public ListResponse(@Nullable Collection<T> data) {
        super(data != null && data.isEmpty() ? "Data Not Found" : "Data Found", HttpStatus.OK.value(), HttpStatus.OK.name());
        this.data = data;
    }

    public ListResponse(@Nullable Collection<T> data, String message) {
        super(message, HttpStatus.OK.value(), HttpStatus.OK.name());
        this.data = data;
    }

    public ListResponse(String message) {
        super(message, HttpStatus.OK.value(), HttpStatus.OK.name());
        this.data = null;
    }

    public ListResponse(String message, HttpStatus status) {
        super(message, status.value(), status.name());
        this.data = null;
    }
}
