package com.employee_leave_tracker.backend.repository.employee;

import com.employee_leave_tracker.backend.model.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e FROM Employee e
             JOIN FETCH e.department d
             JOIN FETCH e.designation des
             LEFT JOIN FETCH e.manager m
             WHERE e.id = :id
            """)
    Optional<Employee> findEmployeeById(Long id);


    @Query("""
            SELECT e FROM Employee e
             JOIN FETCH e.department d
             JOIN FETCH e.designation des
             WHERE e.isDeleted = false
            """)
    Page<Employee> findAllEmployee(Pageable pageable);
}
