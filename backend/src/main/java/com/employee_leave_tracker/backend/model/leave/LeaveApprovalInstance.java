package com.employee_leave_tracker.backend.model.leave;

import com.employee_leave_tracker.backend.constant.ApprovalStatus;
import com.employee_leave_tracker.backend.model.workflow.WorkflowStep;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_approval_instance")
public class LeaveApprovalInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "step_definition_id")
    private WorkflowStep stepDefinition;

    private Integer stepOrder;

    @Column(name = "approver_id")
    private Long approverId; // user id

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private String comments;

    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime actionDate;
}