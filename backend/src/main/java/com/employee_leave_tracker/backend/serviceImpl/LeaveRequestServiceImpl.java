package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.LeaveStatus;
import com.employee_leave_tracker.backend.dto.leave.LeaveReqDTO;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;
import com.employee_leave_tracker.backend.repository.employee.EmployeeRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveRequestRepository;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import com.employee_leave_tracker.backend.service.LeaveCalculationService;
import com.employee_leave_tracker.backend.service.LeaveRequestService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final AuthUtils authUtils;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository requestRepository;
    private final LeaveCalculationService calculationService;
    private final LeaveBalanceService balanceService;

    @Override
    @Transactional
    public String applyLeave(LeaveReqDTO dto) {
        Long loggedEmployeeId = authUtils.getCurrentUserEmployeeId();
        Employee loggedEmployee = employeeRepository.findById(loggedEmployeeId)
                .orElseThrow(() -> new NoDataFoundException("Employee not found"));

        calculationService.validateLeaveDateRange(dto.startDate(), dto.endDate());

        double requestedDays = calculationService.calculateLeaveDays(
                dto.startDate(), dto.endDate(), dto.duration());

        balanceService.validateLeaveBalance(loggedEmployee.getId(), dto.leaveTypeId(), requestedDays);

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(loggedEmployee);
        request.setStartDate(dto.startDate());
        request.setEndDate(dto.endDate());
        request.setTotalDays(requestedDays);
        request.setStatus(LeaveStatus.PENDING);

        requestRepository.save(request);

        // TODO: Initialize Workflow

        return "Leave request created successfully";
    }

    @Transactional
    public void cancelLeave(Long requestId) {
        LeaveRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoDataFoundException("Request not found"));

        boolean isAdmin = authUtils.hasRole("ROLE_ADMIN");

        if (request.getStartDate().isBefore(LocalDate.now()) && !isAdmin) {
            throw new ArgumentNotValidException("Only admin can cancel leave after it has started");
        }

        if (request.getEmployee().getId().equals(authUtils.getCurrentUserEmployeeId())) {
            throw new ArgumentNotValidException("Only owner can cancel leave");
        }

        if (request.getStatus() == LeaveStatus.APPROVED) {
            balanceService.restoreLeaveBalance(
                    request.getEmployee().getId(),
                    request.getLeaveType().getId(),
                    request.getTotalDays()
            );
        }

        request.setStatus(LeaveStatus.CANCELLED);
    }
}