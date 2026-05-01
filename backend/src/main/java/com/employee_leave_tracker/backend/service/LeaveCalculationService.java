package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.constant.LeaveDuration;

import java.time.LocalDate;

public interface LeaveCalculationService {

    double calculateLeaveDays(LocalDate start, LocalDate end, LeaveDuration duration);

    void validateLeaveDateRange(LocalDate start, LocalDate end);

}
