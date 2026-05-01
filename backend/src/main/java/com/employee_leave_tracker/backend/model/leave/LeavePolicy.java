package com.employee_leave_tracker.backend.model.leave;

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
public class LeavePolicy {

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

    @Column(name = "allow_half_day")
    private boolean allowHalfDay;

    @Column(name = "employment_type", nullable = false)
    private String employmentType;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private boolean isActive;
}