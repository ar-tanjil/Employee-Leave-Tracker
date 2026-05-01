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
@Table(name = "leave_balance")
public class LeaveBalance extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    private LeaveType leaveType;

    private Integer year;

    private Double allocatedDays;
    private Double usedDays;
    private Double pendingDays;
    private Double availableDays;
}
