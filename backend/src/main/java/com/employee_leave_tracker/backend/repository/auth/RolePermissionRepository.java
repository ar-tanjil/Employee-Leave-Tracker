package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    @Query("""
        SELECT CONCAT(rp.permission.resource, ':', rp.permission.action)
        FROM   RolePermission rp
        WHERE  rp.role.id = :roleId
    """)
    List<String> findPermissionCodesByRoleId(@Param("roleId") Long roleId);
}