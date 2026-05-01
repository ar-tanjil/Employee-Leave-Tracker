package com.employee_leave_tracker.backend.repository;

import com.employee_leave_tracker.backend.dto.employee.DepartmentResDto;
import com.employee_leave_tracker.backend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Collection<DepartmentResDto> findAllByIsActiveTrue();
}
