package com.employee_leave_tracker.backend.model.leave;

import com.employee_leave_tracker.backend.model.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_policy")
public class LeavePolicy extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    private Integer maxDaysPerYear;
    private Integer maxDaysPerRequest;
    private Integer minDaysNotice;

    private Boolean allowCarryForward;
    private Integer maxCarryForwardDays;
    private Integer carryForwardExpiryMonths;

    private Boolean allowHalfDay;

//    @Enumerated(EnumType.STRING)
//    private AccrualType accrualType;

    private String applicableEmploymentType;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Boolean isActive;
}