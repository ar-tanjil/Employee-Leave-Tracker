package com.employee_leave_tracker.backend.repository;

import com.employee_leave_tracker.backend.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
