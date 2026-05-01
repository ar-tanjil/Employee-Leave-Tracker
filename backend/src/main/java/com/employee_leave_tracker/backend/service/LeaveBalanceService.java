package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.model.leave.LeaveBalance;

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalance getOrCreateBalance(Long employeeId, Long leaveTypeId);

    List<LeaveBalance> getEmployeeBalances(Long employeeId);

    void deductLeaveBalance(Long empId, Long typeId, double days, int year);

    void restoreLeaveBalance(Long empId, Long typeId, double days, int year);
}
