package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.model.leave.LeaveBalance;
import com.employee_leave_tracker.backend.model.leave.LeavePolicy;
import com.employee_leave_tracker.backend.model.leave.LeaveType;
import com.employee_leave_tracker.backend.repository.employee.EmployeeRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveBalanceRepository;
import com.employee_leave_tracker.backend.repository.leave.LeavePolicyRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveTypeRepository;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;


    @Override
    @Transactional
    public LeaveBalance getOrCreateBalance(Long employeeId, Long leaveTypeId) {
        Integer currentYear = Year.now().getValue();
        return leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, currentYear)
                .orElseGet(() -> initializeBalance(employeeId, leaveTypeId, currentYear));
    }

    @Override
    @Transactional
    public List<LeaveBalance> getEmployeeBalances(Long employeeId) {
        Integer currentYear = Year.now().getValue();

        List<LeaveType> activeLeaveTypes = leaveTypeRepository.findByIsActive(true);

        List<LeaveBalance> balances = new ArrayList<>();
        for (LeaveType leaveType : activeLeaveTypes) {
            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveType.getId(), currentYear)
                    .orElseGet(() -> initializeBalance(employeeId, leaveType.getId(), currentYear));

            if (balance != null) {
                balances.add(balance);
            }
        }

        return balances;
    }


    private LeaveBalance initializeBalance(Long employeeId, Long leaveTypeId,
                                           Integer year) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoDataFoundException("Employee not found"));

        // Find active policy for this leave type and employment type
        LeavePolicy policy = leavePolicyRepository
                .findActiveByLeaveTypeIdAndEmploymentType(leaveTypeId, employee.getEmploymentType())
                .orElseThrow(() -> new NoDataFoundException("No policy found for this leave type and employment type"));


        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new NoDataFoundException("Leave type not found"));

        // Calculate allocated days based on joining date if in current year
        Double allocatedDays = calculateAllocatedDays(employee, policy, year);

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setLeavePolicy(policy);
        balance.setYear(year);
        balance.setAllocatedDays(allocatedDays);
        balance.setUsedDays(0.0);
        balance.setPendingDays(0.0);
        balance.setAvailableDays(allocatedDays);

        return leaveBalanceRepository.save(balance);
    }

    private Double calculateAllocatedDays(Employee employee, LeavePolicy policy, Integer year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);
        LocalDate joiningDate = employee.getHireDate();

        // If joined before this year, give full allocation
        if (joiningDate.getYear() < year) {
            return policy.getMaxDaysPerYear().doubleValue();
        }

        // If joined in this year, pro-rate based on remaining months
        if (joiningDate.getYear() == year) {
            long totalDaysInYear = ChronoUnit.DAYS.between(yearStart, yearEnd) + 1;
            long remainingDays = ChronoUnit.DAYS.between(joiningDate, yearEnd) + 1;

            double proRatedDays = (policy.getMaxDaysPerYear().doubleValue() * remainingDays) / totalDaysInYear;
            return Math.round(proRatedDays * 2) / 2.0; // Round to nearest 0.5
        }

        // Joined after this year
        return 0.0;
    }


    @Override
    @Transactional
    public void deductLeaveBalance(Long empId, Long typeId, double days, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(empId, typeId, year)
                .orElseThrow(() -> new NoDataFoundException("Balance not found"));
        balance.setAvailableDays(balance.getAvailableDays() - days);
        leaveBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public void restoreLeaveBalance(Long empId, Long typeId, double days, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(empId, typeId, year)
                .orElseThrow(() -> new NoDataFoundException("Balance not found"));
        balance.setAvailableDays(balance.getAvailableDays() + days);
        leaveBalanceRepository.save(balance);
    }

}
