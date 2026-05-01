package com.employee_leave_tracker.backend.repository.workflow;

import com.employee_leave_tracker.backend.constant.WorkflowType;
import com.employee_leave_tracker.backend.model.workflow.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalWorkflowRepository extends JpaRepository<Workflow, Long> {
    Optional<Workflow> findByDepartmentIdAndWorkflowType(Long departmentId,
                                                         WorkflowType workflowType);

    Optional<Workflow> findByWorkflowTypeAndActiveTrue(WorkflowType type);
}
