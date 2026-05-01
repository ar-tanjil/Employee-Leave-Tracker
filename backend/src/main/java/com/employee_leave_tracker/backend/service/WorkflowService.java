package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.constant.WorkflowType;
import com.employee_leave_tracker.backend.model.workflow.Workflow;
import com.employee_leave_tracker.backend.model.workflow.WorkflowStep;

import java.util.List;

public interface WorkflowService {
   Workflow ensureWorkflowExists(WorkflowType type);

   List<WorkflowStep> findAllWorkflowSteps(Long workflowId);

}
