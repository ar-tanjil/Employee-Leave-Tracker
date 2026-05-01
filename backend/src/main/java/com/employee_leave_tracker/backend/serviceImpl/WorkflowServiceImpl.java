package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.ApproverType;
import com.employee_leave_tracker.backend.constant.WorkflowType;
import com.employee_leave_tracker.backend.model.workflow.Workflow;
import com.employee_leave_tracker.backend.model.workflow.WorkflowStep;
import com.employee_leave_tracker.backend.repository.workflow.ApprovalStepDefinitionRepository;
import com.employee_leave_tracker.backend.repository.workflow.ApprovalWorkflowRepository;
import com.employee_leave_tracker.backend.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl implements WorkflowService {

    private final ApprovalWorkflowRepository workflowRepo;
    private final ApprovalStepDefinitionRepository stepRepo;

    @Override
    @Transactional
    public Workflow ensureWorkflowExists(WorkflowType type) {

        return workflowRepo.findByWorkflowTypeAndActiveTrue(type)
                .orElseGet(() -> createDefaultWorkflow(type));
    }


    private Workflow createDefaultWorkflow(WorkflowType type) {

        Workflow workflow = new Workflow();
        workflow.setWorkflowType(type);
        workflow.setName(type.name() + " Default Workflow");
        workflow.setActive(true);

        workflowRepo.save(workflow);

        List<WorkflowStep> steps = new ArrayList<>();

        // Step 1 → Manager
        WorkflowStep managerStep = new WorkflowStep();
        managerStep.setWorkflow(workflow);
        managerStep.setStepOrder(1);
        managerStep.setApproverType(ApproverType.MANAGER);
        steps.add(managerStep);

        // Step 2 → HR Admin
        WorkflowStep hrStep = new WorkflowStep();
        hrStep.setWorkflow(workflow);
        hrStep.setStepOrder(2);
        hrStep.setApproverType(ApproverType.HR_ADMIN);
        steps.add(hrStep);

        stepRepo.saveAll(steps);

        return workflow;
    }


    @Override
    public List<WorkflowStep> findAllWorkflowSteps(Long workflowId) {
        return stepRepo.findByWorkflowIdOrderByStepOrder(workflowId);
    }
}
