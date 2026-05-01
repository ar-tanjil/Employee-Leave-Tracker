package com.employee_leave_tracker.backend.model.workflow;

import com.employee_leave_tracker.backend.constant.WorkflowType;
import com.employee_leave_tracker.backend.model.BaseAuditEntity;
import com.employee_leave_tracker.backend.model.employee.Department;
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
@Table(name = "workflow")
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_type")
    private WorkflowType workflowType;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private Integer totalSteps;

    private boolean isActive;

}
