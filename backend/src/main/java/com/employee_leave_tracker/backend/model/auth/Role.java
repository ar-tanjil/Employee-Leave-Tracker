package com.employee_leave_tracker.backend.model.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_system_role", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSystemRole;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<RolePermission> rolePermissions;
}
