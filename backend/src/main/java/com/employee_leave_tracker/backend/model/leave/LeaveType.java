package com.employee_leave_tracker.backend.model.leave;

import com.employee_leave_tracker.backend.model.BaseAuditEntity;
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
@Table(name = "leave_type")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_paid", nullable = false, columnDefinition = "boolean default true")
    private Boolean isPaid;

    @Column(name = "requires_attachment")
    private Boolean requiresAttachment;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;
}