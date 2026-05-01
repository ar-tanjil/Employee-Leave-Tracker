package com.employee_leave_tracker.backend.repository.employee;

import com.employee_leave_tracker.backend.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
