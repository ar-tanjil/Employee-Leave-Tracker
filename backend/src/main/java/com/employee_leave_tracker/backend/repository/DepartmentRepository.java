package com.employee_leave_tracker.backend.repository;

import com.employee_leave_tracker.backend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
