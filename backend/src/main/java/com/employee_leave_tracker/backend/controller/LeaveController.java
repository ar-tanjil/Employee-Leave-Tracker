package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.leave.LeaveReqDTO;
import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;
import com.employee_leave_tracker.backend.service.LeaveApprovalService;
import com.employee_leave_tracker.backend.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/leaves")
@RequiredArgsConstructor
public class LeaveController {


    private final LeaveRequestService leaveRequestService;
    private final LeaveApprovalService leaveApprovalService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<CustomResponse> applyLeave(@RequestBody @Valid LeaveReqDTO dto) {
        var response = leaveRequestService.applyLeave(dto);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<CustomResponse> cancelLeave(@PathVariable Long id) {
        leaveRequestService.cancelLeave(id);
        return ResponseEntity.ok(new SuccessResponse<>("Leave request cancelled successfully"));
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
