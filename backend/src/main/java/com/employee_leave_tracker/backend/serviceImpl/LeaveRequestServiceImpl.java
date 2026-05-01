package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.LeaveDuration;
import com.employee_leave_tracker.backend.constant.LeaveStatus;
import com.employee_leave_tracker.backend.dto.leave.LeaveReqDTO;
import com.employee_leave_tracker.backend.dto.leave.LeaveResDTO;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.model.leave.LeaveBalance;
import com.employee_leave_tracker.backend.model.leave.LeavePolicy;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;
import com.employee_leave_tracker.backend.model.leave.LeaveType;
import com.employee_leave_tracker.backend.repository.employee.EmployeeRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveRequestRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveTypeRepository;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import com.employee_leave_tracker.backend.service.LeaveCalculationService;
import com.employee_leave_tracker.backend.service.LeaveRequestService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final AuthUtils authUtils;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository requestRepository;
    private final LeaveCalculationService calculationService;
    private final LeaveBalanceService balanceService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceService leaveBalanceService;

    @Override
    @Transactional
    public String applyLeave(LeaveReqDTO dto) {

        Long loggedEmployeeId = authUtils.getCurrentUserEmployeeId();

        calculationService.validateLeaveDateRange(dto.startDate(), dto.endDate());
        if (requestRepository.existsOverlappingRequest(
                loggedEmployeeId, dto.startDate(), dto.endDate())) {
            throw new ArgumentNotValidException("Leave request overlaps with existing request");
        }

        double requestedDays = calculationService.calculateLeaveDays(
                dto.startDate(), dto.endDate(), dto.duration());

        LeaveBalance balance = leaveBalanceService.getOrCreateBalance(loggedEmployeeId, dto.leaveTypeId());
        LeavePolicy policy = balance.getLeavePolicy();
        LeaveType leaveType = balance.getLeaveType();

        validateLeaveBalanceAndPolicy(dto, balance, policy, requestedDays);

        Employee loggedEmployee = employeeRepository.findById(loggedEmployeeId)
                .orElseThrow(() -> new NoDataFoundException("Employee not found"));


        LeaveRequest request = new LeaveRequest();
        request.setEmployee(loggedEmployee);
        request.setLeaveType(leaveType);
        request.setLeavePolicy(policy);
        request.setStartDate(dto.startDate());
        request.setEndDate(dto.endDate());
        request.setTotalDays(requestedDays);
        request.setStatus(LeaveStatus.PENDING);
        request.setLeaveDuration(dto.duration());

        requestRepository.save(request);

        // TODO: Initialize Workflow

        return "Leave request created successfully";
    }


    public void validateLeaveBalanceAndPolicy(LeaveReqDTO dto, LeaveBalance balance,
                                              LeavePolicy policy, double requestedDays) {
        // 3. Validate available balance
        if (balance.getAvailableDays() < requestedDays) {
            throw new ArgumentNotValidException("Insufficient leave balance");
        }
        // 4. Validate against policy
        if (requestedDays > policy.getMaxDaysPerRequest()) {
            throw new ArgumentNotValidException("Requested days exceed policy limit");
        }

        if (!LeaveDuration.FULL_DAY.equals(dto.duration()) && !policy.isAllowHalfDay()) {
            throw new ArgumentNotValidException("Half-day leave not allowed for this leave type");
        }

        long daysNotice = ChronoUnit.DAYS.between(LocalDate.now(), dto.startDate());
        if (daysNotice < policy.getMinDaysNotice()) {
            throw new ArgumentNotValidException("Minimum notice period is " + policy.getMinDaysNotice() + " days");
        }
    }


    public void validateLeaveRequest(Long employeeId, double requestedDays, LeaveReqDTO request) {
        calculationService.validateLeaveDateRange(request.startDate(), request.endDate());
        LeaveBalance balance = leaveBalanceService.getOrCreateBalance(employeeId, request.leaveTypeId());

        LeavePolicy policy = balance.getLeavePolicy();

        // 3. Validate available balance
        if (balance.getAvailableDays() < requestedDays) {
            throw new ArgumentNotValidException("Insufficient leave balance");
        }
        // 4. Validate against policy
        if (requestedDays > policy.getMaxDaysPerRequest()) {
            throw new ArgumentNotValidException("Requested days exceed policy limit");
        }

        if (!LeaveDuration.FULL_DAY.equals(request.duration()) && !policy.isAllowHalfDay()) {
            throw new ArgumentNotValidException("Half-day leave not allowed for this leave type");
        }

        long daysNotice = ChronoUnit.DAYS.between(LocalDate.now(), request.startDate());
        if (daysNotice < policy.getMinDaysNotice()) {
            throw new ArgumentNotValidException("Minimum notice period is " + policy.getMinDaysNotice() + " days");
        }
    }


    @Override
    public List<LeaveResDTO> getLeaveRequestsByEmployee() {
        Long loggedEmployeeId = authUtils.getCurrentUserEmployeeId();
        return requestRepository.findByEmployeeId(loggedEmployeeId);
    }

    @Transactional
    public void cancelLeave(Long requestId) {
        LeaveRequest leaveRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoDataFoundException("Request not found"));

        boolean isAdmin = authUtils.hasRole("ROLE_ADMIN");

        if (leaveRequest.getStartDate().isBefore(LocalDate.now()) && !isAdmin) {
            throw new ArgumentNotValidException("Only admin can cancel leave after it has started");
        }

        if (!leaveRequest.getEmployee().getId().equals(authUtils.getCurrentUserEmployeeId())) {
            throw new ArgumentNotValidException("Only owner can cancel leave");
        }

        if (leaveRequest.getStatus() == LeaveStatus.APPROVED) {
            balanceService.restoreLeaveBalance(
                    leaveRequest.getEmployee().getId(),
                    leaveRequest.getLeaveType().getId(),
                    leaveRequest.getTotalDays(),
                    leaveRequest.getStartDate().getYear()
            );
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);
    }
}