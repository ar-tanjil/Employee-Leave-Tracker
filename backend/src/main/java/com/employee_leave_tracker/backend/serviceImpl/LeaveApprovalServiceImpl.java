package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.ApprovalStatus;
import com.employee_leave_tracker.backend.constant.LeaveStatus;
import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;
import com.employee_leave_tracker.backend.model.leave.LeaveApprovalInstance;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;
import com.employee_leave_tracker.backend.repository.leave.LeaveApprovalInstanceRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveRequestRepository;
import com.employee_leave_tracker.backend.service.LeaveApprovalService;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveApprovalServiceImpl implements LeaveApprovalService {

    private final LeaveApprovalInstanceRepository approvalRepo;
    private final LeaveRequestRepository requestRepo;
    private final LeaveBalanceService balanceService;

    @Override
    @Transactional
    public void approveLeave(ApprovalActionDTO action) {
        LeaveApprovalInstance currentStep = approvalRepo.findById(action.approvalInstanceId())
                .orElseThrow();

        currentStep.setStatus(ApprovalStatus.APPROVED);
        currentStep.setComments(action.comments());
        currentStep.setActionDate(LocalDateTime.now());
        approvalRepo.save(currentStep);

        finalizeApproval(currentStep.getLeaveRequest().getId());
    }

    @Override
    @Transactional
    public void rejectLeave(ApprovalActionDTO action) {
        LeaveApprovalInstance currentStep = approvalRepo.findById(action.approvalInstanceId())
                .orElseThrow();

        currentStep.setStatus(ApprovalStatus.REJECTED);
        approvalRepo.save(currentStep);

        LeaveRequest request = currentStep.getLeaveRequest();
        request.setStatus(LeaveStatus.REJECTED);
        requestRepo.save(request);
    }

    private void finalizeApproval(Long requestId) {
        Optional<LeaveApprovalInstance> nextStep = approvalRepo.findTopByLeaveRequestIdOrderByStepOrderDesc(requestId);

        if (nextStep.isEmpty()) {
            // Final Approval
            LeaveRequest request = requestRepo.findById(requestId).orElseThrow();
            request.setStatus(LeaveStatus.APPROVED);

            // Deduct balance only on final approval
            balanceService.deductLeaveBalance(
                    request.getEmployee().getId(),
                    request.getLeaveType().getId(),
                    request.getTotalDays()
            );

            requestRepo.save(request);
        }
        // If nextStep present, workflow remains PENDING until that approver acts
    }
}
