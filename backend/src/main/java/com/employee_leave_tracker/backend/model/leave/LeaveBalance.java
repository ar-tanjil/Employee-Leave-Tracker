package com.employee_leave_tracker.backend.model.leave;

import com.employee_leave_tracker.backend.model.BaseAuditEntity;
import com.employee_leave_tracker.backend.model.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_balance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "leave_type_id", "year"})
        }
)
public class LeaveBalance extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_policy_id", nullable = false)
    private LeavePolicy leavePolicy;

    private Integer year;

    private Double allocatedDays;
    private Double usedDays;
    private Double pendingDays;
    private Double availableDays;
}
