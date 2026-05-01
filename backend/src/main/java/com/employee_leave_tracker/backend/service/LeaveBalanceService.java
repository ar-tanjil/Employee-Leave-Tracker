package com.employee_leave_tracker.backend.service;

public interface LeaveBalanceService {

    double getAvailableLeaveBalance(Long empId, Long typeId);

    void validateLeaveBalance(Long empId, Long typeId, double requestedDays);

    void deductLeaveBalance(Long empId, Long typeId, double days);

    void restoreLeaveBalance(Long empId, Long typeId, double days);
}
