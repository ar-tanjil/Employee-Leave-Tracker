package com.employee_leave_tracker.backend.repository;

import com.employee_leave_tracker.backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
