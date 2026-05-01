package com.employee_leave_tracker.backend.repository.workflow;

import com.employee_leave_tracker.backend.model.workflow.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalStepDefinitionRepository extends JpaRepository<WorkflowStep, Long> {
    List<WorkflowStep> findByWorkflowIdOrderByStepOrder(Long workflowId);
}
