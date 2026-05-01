package com.employee_leave_tracker.backend.model.workflow;

import com.employee_leave_tracker.backend.constant.ApproverType;
import com.employee_leave_tracker.backend.model.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workflow_step")
public class WorkflowStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    private Integer stepOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "approver_type")
    private ApproverType approverType;

    private boolean isMandatory;
}