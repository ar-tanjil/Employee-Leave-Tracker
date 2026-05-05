package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.leave.LeaveTypeDTO;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.model.leave.LeaveBalance;
import com.employee_leave_tracker.backend.model.leave.LeavePolicy;
import com.employee_leave_tracker.backend.model.leave.LeaveType;
import com.employee_leave_tracker.backend.repository.leave.LeaveBalanceRepository;
import com.employee_leave_tracker.backend.repository.leave.LeavePolicyRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveTypeRepository;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;


    @Override
    public LeaveBalance getOrCreateBalance(Long employeeId, Long leaveTypeId, Integer year) {
        return leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, year)
                .orElseThrow(() -> new NoDataFoundException("Leave balance not found"));
    }


    @Transactional
    public void createEmployeeLeaveBalances(Employee employee, Integer year) {


        // Active leave types
        List<LeaveType> leaveTypes = leaveTypeRepository.findByIsActive(true);
        if (leaveTypes.isEmpty()) {
            return; // noting to create
        }

        //Existing balances (avoid duplicates)
        List<LeaveBalance> existingBalances =
                leaveBalanceRepository.findByEmployeeIdAndYear(employee.getId(), year);

        Set<Long> existingLeaveTypeIds = existingBalances.stream()
                .map(lb -> lb.getLeaveType().getId())
                .collect(Collectors.toSet());

        //Fetch policies in bulk
        List<LeavePolicy> policies =
                leavePolicyRepository.findActiveByEmploymentType(employee.getEmploymentType());

        Map<Long, LeavePolicy> policyMap = policies.stream()
                .collect(Collectors.toMap(lp -> lp.getLeaveType().getId(), Function.identity()));

        List<LeaveBalance> newBalances = new ArrayList<>();

        for (LeaveType leaveType : leaveTypes) {

            //Skip if already exists
            if (existingLeaveTypeIds.contains(leaveType.getId())) {
                continue;
            }

            LeavePolicy policy = policyMap.get(leaveType.getId());
            // Skip if policy does not exist
            if (policy == null) {
                continue;
            }

            LeaveBalance balance = buildLeaveBalance(employee, leaveType, policy, year);
            newBalances.add(balance);
        }

        if (!newBalances.isEmpty()) {
            leaveBalanceRepository.saveAll(newBalances);
        }
    }

    @Override
    public List<LeaveTypeDTO> geEmployeeLeaveTypesWithBalance(Long employeeId, Integer year) {

        return leaveBalanceRepository.findAllByEmployeeIdAndYear(employeeId, year)
                .stream()
                .map(this::buildLeaveTypeDTO)
                .toList();
    }

    private LeaveTypeDTO buildLeaveTypeDTO(LeaveBalance balance) {
        return new LeaveTypeDTO(
                balance.getLeaveType().getId(),
                balance.getLeaveType().getName(),
                balance.getAllocatedDays(),
                balance.getUsedDays()
        );
    }

    private LeaveBalance buildLeaveBalance(Employee employee, LeaveType leaveType,
                                           LeavePolicy policy, Integer year) {
        Double allocatedDays = calculateAllocatedDays(employee, policy, year);

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setLeavePolicy(policy);
        balance.setYear(year);
        balance.setAllocatedDays(allocatedDays);
        balance.setUsedDays(0.0);
        balance.setAvailableDays(allocatedDays);
        balance.setPendingDays(0.0);
        return balance;
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
    public void setPendingLeaveBalance(Long empId, Long typeId, double days, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(empId, typeId, year)
                .orElseThrow(() -> new NoDataFoundException("Balance not found"));
        balance.setPendingDays(balance.getPendingDays() + days);
        leaveBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public void deductLeaveBalance(Long empId, Long typeId, double days, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(empId, typeId, year)
                .orElseThrow(() -> new NoDataFoundException("Balance not found"));
        balance.setAvailableDays(balance.getAvailableDays() - days);
        balance.setUsedDays(balance.getUsedDays() + days);
        balance.setPendingDays(balance.getPendingDays() - days);
        leaveBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public void restoreLeaveBalance(Long empId, Long typeId, double days, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(empId, typeId, year)
                .orElseThrow(() -> new NoDataFoundException("Balance not found"));
        balance.setAvailableDays(balance.getAvailableDays() + days);
        balance.setUsedDays(balance.getUsedDays() - days);

        balance.setPendingDays(balance.getPendingDays() - days);
        leaveBalanceRepository.save(balance);
    }

}
