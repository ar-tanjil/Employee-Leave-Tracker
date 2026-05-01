package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.ApprovalStatus;
import com.employee_leave_tracker.backend.constant.ApproverType;
import com.employee_leave_tracker.backend.constant.LeaveStatus;
import com.employee_leave_tracker.backend.constant.WorkflowType;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.UnauthorizedException;
import com.employee_leave_tracker.backend.model.leave.LeaveApprovalInstance;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;
import com.employee_leave_tracker.backend.model.workflow.Workflow;
import com.employee_leave_tracker.backend.model.workflow.WorkflowStep;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveApprovalInstanceRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveRequestRepository;
import com.employee_leave_tracker.backend.service.LeaveApprovalService;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import com.employee_leave_tracker.backend.service.WorkflowService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveApprovalServiceImpl implements LeaveApprovalService {

    private final AuthUtils authUtils;
    private final LeaveRequestRepository requestRepo;
    private final LeaveBalanceService balanceService;
    private final WorkflowService workflowService;
    private final UserAccountRepository userAccountRepository;
    private final LeaveApprovalInstanceRepository leaveApprovalInstanceRepo;


    @Transactional
    public void initializeLeaveApprovalSteps(LeaveRequest request) {
        Workflow workflow = workflowService.ensureWorkflowExists(WorkflowType.LEAVE);
        List<WorkflowStep> stepList = workflowService.findAllWorkflowSteps(workflow.getId());
        if (stepList.isEmpty()) {
            throw new ArgumentNotValidException("No workflow steps configured");
        }

        List<LeaveApprovalInstance> instancesList = new ArrayList<>();

        for (WorkflowStep step : stepList) {

            Long approverId = resolveApproverId(step.getApproverType(), request.getEmployee().getId());

            if (approverId == null) {
                continue;
            }

            LeaveApprovalInstance instance = new LeaveApprovalInstance();
            instance.setLeaveRequest(request);
            instance.setApproverId(approverId);
            instance.setStepOrder(step.getStepOrder());
            instance.setStatus(ApprovalStatus.PENDING);
            instance.setActive(step.getStepOrder() == 1);

            instancesList.add(instance);

        }


        leaveApprovalInstanceRepo.saveAll(instancesList);
    }

    private Long resolveApproverId(ApproverType type, Long employeeId) {

        return switch (type) {
            case MANAGER -> userAccountRepository
                    .findManagerUserIdByEmployeeId(employeeId)
                    .orElse(null);
            case HR_ADMIN -> userAccountRepository
                    .findFirstByRole("HR_ADMIN")
                    .or(() -> userAccountRepository.findFirstByRole("SYSTEM_ADMIN"))
                    .orElseThrow(() -> new RuntimeException("No HR/SYSTEM admin found"));
        };
    }


    @Override
    @Transactional
    public void approveLeave(Long approvalInstanceId) {

        Long loggedUserId = authUtils.getCurrentUserId();

        LeaveApprovalInstance current = getCurrentStep(approvalInstanceId, WorkflowType.LEAVE);

        if (!current.getApproverId().equals(loggedUserId)) {
            throw new UnauthorizedException("Unauthorized approver");
        }

        current.setStatus(ApprovalStatus.APPROVED);
        current.setActive(false);

        // Find next step
        List<LeaveApprovalInstance> allSteps =
                leaveApprovalInstanceRepo.findByIdOrderByStepOrderAsc(approvalInstanceId);

        Optional<LeaveApprovalInstance> nextStep = allSteps.stream()
                .filter(s -> s.getStepOrder() > current.getStepOrder())
                .findFirst();

        if (nextStep.isPresent()) {
            nextStep.get().setActive(true);
        } else {
            // final approval
            LeaveRequest request = current.getLeaveRequest();
            request.setStatus(LeaveStatus.APPROVED);

            // Deduct balance only on final approval
            balanceService.deductLeaveBalance(
                    request.getEmployee().getId(),
                    request.getLeaveType().getId(),
                    request.getTotalDays(),
                    request.getStartDate().getYear()
            );

            requestRepo.save(request);
        }
    }

    @Override
    @Transactional
    public void rejectLeave(Long approvalInstanceId) {

        Long loggedUserId = authUtils.getCurrentUserId();

        LeaveApprovalInstance current = getCurrentStep(approvalInstanceId, WorkflowType.LEAVE);

        if (!current.getApproverId().equals(loggedUserId)) {
            throw new UnauthorizedException("Unauthorized approver");
        }

        current.setStatus(ApprovalStatus.REJECTED);
        current.setActive(false);

        // Reject all remaining steps
        List<LeaveApprovalInstance> allSteps =
                leaveApprovalInstanceRepo.findByIdOrderByStepOrderAsc(approvalInstanceId);

        allSteps.stream()
                .filter(s -> s.getStepOrder() > current.getStepOrder())
                .forEach(s -> {
                    s.setStatus(ApprovalStatus.REJECTED);
                    s.setActive(false);
                });

        // update LeaveRequest status = REJECTED
        LeaveRequest request = current.getLeaveRequest();
        request.setStatus(LeaveStatus.REJECTED);
        requestRepo.save(request);
    }


    private LeaveApprovalInstance getCurrentStep(Long referenceId, WorkflowType type) {
        return leaveApprovalInstanceRepo
                .findByIdAndActiveTrue(referenceId)
                .orElseThrow(() -> new RuntimeException("No active step found"));
    }
}
