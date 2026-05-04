package com.employee_leave_tracker.backend.model.auth;

import com.employee_leave_tracker.backend.model.BaseAuditEntity;
import com.employee_leave_tracker.backend.model.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;

    @Column(unique = true)
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRole> userRoles = new ArrayList<>();

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isLocked() {
        return "LOCKED".equals(status);
    }
}
