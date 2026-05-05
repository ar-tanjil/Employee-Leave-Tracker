package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.leave.LeaveReqDTO;
import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;
import com.employee_leave_tracker.backend.service.LeaveApprovalService;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import com.employee_leave_tracker.backend.service.LeaveRequestService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/v1/leaves")
@RequiredArgsConstructor
public class LeaveController {


    private final AuthUtils authUtils;
    private final LeaveRequestService leaveRequestService;
    private final LeaveApprovalService leaveApprovalService;
    private final LeaveBalanceService leaveBalanceService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<CustomResponse> applyLeave(@RequestBody @Valid LeaveReqDTO dto) {
        var response = leaveRequestService.applyLeave(dto);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<CustomResponse> getLeaveRequestsByEmployee() {

        Long employeeId = authUtils.getCurrentUserEmployeeId();

        var response = leaveRequestService.getLeaveRequestsByEmployee(employeeId);
        return ResponseEntity.ok(new ListResponse<>(response));
    }


    @GetMapping("/cancel/{id}")
    public ResponseEntity<CustomResponse> cancelLeave(@PathVariable("id") Long leaveId) {
        leaveRequestService.cancelLeave(leaveId);
        return ResponseEntity.ok(new SuccessResponse<>("Leave request cancelled successfully"));
    }

    @GetMapping("/types")
    public ResponseEntity<CustomResponse> getLeaveTypes() {
        Long employeeId = authUtils.getCurrentUserEmployeeId();
        Integer year = Year.now().getValue();
        var response = leaveBalanceService.geEmployeeLeaveTypesWithBalance(employeeId, year);
        return ResponseEntity.ok(new ListResponse<>(response));
    }

    @GetMapping("/pending")
    public ResponseEntity<CustomResponse> getAllPendingLeaveRequests() {

        Long userId = authUtils.getCurrentUserId();
        var response = leaveApprovalService.getAllPendingLeaveRequests(userId);
        return ResponseEntity.ok(new ListResponse<>(response));
    }

    @PostMapping("/approve")
    public ResponseEntity<CustomResponse> approveLeave(@RequestBody @Valid ApprovalActionDTO dto) {
        leaveApprovalService.approveLeave(dto);
        return ResponseEntity.ok(new SuccessResponse<>("Leave request approved successfully"));
    }

    @PostMapping("/reject")
    public ResponseEntity<CustomResponse> rejectLeave(@RequestBody @Valid ApprovalActionDTO dto) {
        leaveApprovalService.rejectLeave(dto);
        return ResponseEntity.ok(new SuccessResponse<>("Leave request rejected successfully"));
    }
}
