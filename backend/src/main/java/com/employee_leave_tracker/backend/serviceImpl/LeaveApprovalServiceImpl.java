package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.ApprovalStatus;
import com.employee_leave_tracker.backend.constant.ApproverType;
import com.employee_leave_tracker.backend.constant.LeaveStatus;
import com.employee_leave_tracker.backend.constant.WorkflowType;
import com.employee_leave_tracker.backend.dto.leave.LeaveApproverInstanceResDTO;
import com.employee_leave_tracker.backend.dto.workflow.ApprovalActionDTO;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.UnauthorizedException;
import com.employee_leave_tracker.backend.model.employee.Department;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public void initializeLeaveApprovalSteps(LeaveRequest request, Department department) {
        Workflow workflow = workflowService.ensureWorkflowExists(WorkflowType.LEAVE);
        List<WorkflowStep> stepList = workflowService.findAllWorkflowSteps(workflow.getId());
        if (stepList.isEmpty()) {
            throw new ArgumentNotValidException("No workflow steps configured");
        }

        List<LeaveApprovalInstance> instancesList = new ArrayList<>();

        long currentStep = 1;
        for (WorkflowStep step : stepList) {

            Long approverId = resolveApproverId(step.getApproverType(), department);

            if (approverId == null) {
                currentStep++;
                continue;
            }

            LeaveApprovalInstance instance = new LeaveApprovalInstance();
            instance.setLeaveRequest(request);
            instance.setApproverId(approverId);
            instance.setStepOrder(step.getStepOrder());
            instance.setStatus(ApprovalStatus.PENDING);
            instance.setActive(step.getStepOrder() == currentStep);
            instance.setCreatedAt(LocalDateTime.now());

            instancesList.add(instance);

        }


        leaveApprovalInstanceRepo.saveAll(instancesList);
    }

    private Long resolveApproverId(ApproverType type, Department department) {

        // hr department has one layer
        if (department == null) {
            throw new ArgumentNotValidException("Department cannot be null");
        } else if ("HR".equalsIgnoreCase(department.getCode()) && ApproverType.MANAGER.equals(type)) {
            return null;
        }

        return switch (type) {
            case MANAGER -> userAccountRepository
                    .findManagerUserIdByDepartmentId(department.getId())
                    .orElse(null);
            case HR_ADMIN -> userAccountRepository
                    .findManagerUserIdByRole("HR_ADMIN")
                    .orElseThrow(() -> new RuntimeException("No HR/SYSTEM admin found"));
        };
    }


    @Override
    @Transactional
    public void approveLeave(ApprovalActionDTO dto) {

        Long loggedUserId = authUtils.getCurrentUserId();

        LeaveApprovalInstance current = leaveApprovalInstanceRepo
                .findByIdAndIsActiveTrue(dto.approvalInstanceId())
                .orElseThrow(() -> new RuntimeException("No active step found"));

        if (!current.getApproverId().equals(loggedUserId)) {
            throw new AccessDeniedException("Unauthorized approver");
        }

        current.setStatus(ApprovalStatus.APPROVED);
        current.setActive(false);
        current.setComments(dto.comments());
        current.setActionDate(LocalDateTime.now());

        // Find next step
        List<LeaveApprovalInstance> allSteps =
                leaveApprovalInstanceRepo.findByLeaveRequestIdOrderByStepOrderAsc(current.getLeaveRequest().getId());

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
    public void rejectLeave(ApprovalActionDTO dto) {

        Long loggedUserId = authUtils.getCurrentUserId();

        LeaveApprovalInstance current = leaveApprovalInstanceRepo
                .findByIdAndIsActiveTrue(dto.approvalInstanceId())
                .orElseThrow(() -> new RuntimeException("No active step found"));

        if (!current.getApproverId().equals(loggedUserId)) {
            throw new AccessDeniedException("Unauthorized approver");
        }

        current.setStatus(ApprovalStatus.REJECTED);
        current.setActive(false);
        current.setComments(dto.comments());
        current.setActionDate(LocalDateTime.now());

        // Reject all remaining steps
        List<LeaveApprovalInstance> allSteps =
                leaveApprovalInstanceRepo.findByLeaveRequestIdOrderByStepOrderAsc(current.getLeaveRequest().getId());

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


    @Override
    public List<LeaveApproverInstanceResDTO> getAllPendingLeaveRequests(Long approverId) {

        return leaveApprovalInstanceRepo
                .findByApproverIdAndStatusPending(approverId)
                .stream()
                .map(this::buildLeaveApproverInstanceResDTO)
                .toList();
    }

    private LeaveApproverInstanceResDTO buildLeaveApproverInstanceResDTO(LeaveApprovalInstance instance) {

        var leaveRequest = instance.getLeaveRequest();
        var employee = leaveRequest.getEmployee();
        var leaveType = leaveRequest.getLeaveType();

        return LeaveApproverInstanceResDTO.builder()
                .referenceId(instance.getId())
                .leaveId(leaveRequest.getId())
                .employeeName("%s %s (%s)".formatted(
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmployeeCode()))
                .leaveTypeName(leaveType.getName())
                .leaveDuration(leaveRequest.getLeaveDuration())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .totalDays(leaveRequest.getTotalDays())
                .status(leaveRequest.getStatus())
                .reason(leaveRequest.getReason())
                .build();
    }
}
