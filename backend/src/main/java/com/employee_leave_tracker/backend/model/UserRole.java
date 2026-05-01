package com.employee_leave_tracker.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "role_id"})
        })
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @CreatedDate
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;

    @CreatedBy
    @Column(name = "assigned_by", updatable = false)
    private Long assignedBy;
}
