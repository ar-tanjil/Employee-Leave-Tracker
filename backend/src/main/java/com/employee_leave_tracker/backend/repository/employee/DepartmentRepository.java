package com.employee_leave_tracker.backend.repository.employee;

import com.employee_leave_tracker.backend.dto.employee.DepartmentResDTO;
import com.employee_leave_tracker.backend.model.employee.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Collection<DepartmentResDTO> findAllByIsActiveTrue();
}
