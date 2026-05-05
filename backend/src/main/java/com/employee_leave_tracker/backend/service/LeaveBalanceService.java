package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.leave.LeaveTypeDTO;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.model.leave.LeaveBalance;

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalance getOrCreateBalance(Long employeeId, Long leaveTypeId, Integer year);

    void createEmployeeLeaveBalances(Employee employee, Integer Year);

    List<LeaveTypeDTO> geEmployeeLeaveTypesWithBalance(Long employeeId, Integer year);

    void deductLeaveBalance(Long empId, Long typeId, double days, int year);

    void restoreLeaveBalance(Long empId, Long typeId, double days, int year);

    void setPendingLeaveBalance(Long empId, Long typeId, double days, int year);
}
