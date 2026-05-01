package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.model.leave.LeaveBalance;
import com.employee_leave_tracker.backend.repository.leave.LeaveBalanceRepository;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository balanceRepository;

    @Override
    public double getAvailableLeaveBalance(Long empId, Long typeId) {
        return balanceRepository.findByEmployeeIdAndLeaveTypeId(empId, typeId)
                .map(LeaveBalance::getAvailableDays)
                .orElseThrow(() -> new NoDataFoundException("Balance not found"));
    }

    @Override
    public void validateLeaveBalance(Long empId, Long typeId, double requestedDays) {

        if (getAvailableLeaveBalance(empId, typeId) > requestedDays) {
            throw new ArgumentNotValidException("Insufficient leave balance");
        }
    }

    @Override
    @Transactional
    public void deductLeaveBalance(Long empId, Long typeId, double days) {
        LeaveBalance balance = balanceRepository.findByEmployeeIdAndLeaveTypeId(empId, typeId)
                .orElseThrow();
        balance.setAvailableDays(balance.getAvailableDays() - days);
        balanceRepository.save(balance);
    }

    @Override
    @Transactional
    public void restoreLeaveBalance(Long empId, Long typeId, double days) {
        LeaveBalance balance = balanceRepository.findByEmployeeIdAndLeaveTypeId(empId, typeId)
                .orElseThrow();
        balance.setAvailableDays(balance.getAvailableDays() + days);
        balanceRepository.save(balance);
    }
}
