package com.employee_leave_tracker.backend.model.leave;

import com.employee_leave_tracker.backend.constant.LeaveDuration;
import com.employee_leave_tracker.backend.constant.LeaveStatus;
import com.employee_leave_tracker.backend.model.BaseAuditEntity;
import com.employee_leave_tracker.backend.model.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_request")
public class LeaveRequest extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestNumber;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Double totalDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_duration", nullable = false)
    private LeaveDuration leaveDuration;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus status;

    @Column(name = "current_approval_step")
    private Integer currentApprovalStep;

    @ManyToOne
    @JoinColumn(name = "leave_policy_id")
    private LeavePolicy leavePolicy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
