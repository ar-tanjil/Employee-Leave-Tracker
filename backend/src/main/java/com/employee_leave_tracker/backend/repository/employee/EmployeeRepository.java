package com.employee_leave_tracker.backend.repository.employee;

import com.employee_leave_tracker.backend.dto.DashboardDTO;
import com.employee_leave_tracker.backend.model.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e FROM Employee e
             JOIN FETCH e.department d
             JOIN FETCH e.designation des
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

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
            FROM UserAccount u
            JOIN u.employee e
            JOIN e.department d
            JOIN u.userRoles ur
            WHERE d.id = :departmentId 
            AND ur.role.name = 'MANAGER'
            """)
    boolean existsManagerForDepartment(@Param("departmentId") Long departmentId);


    @Query("""
                SELECT new com.employee_leave_tracker.backend.dto.DashboardDTO(
                COUNT(DISTINCT emp.id),
                COUNT(DISTINCT lr.employee.id)
            )
            FROM Employee emp
            LEFT JOIN LeaveRequest lr
                ON lr.employee.id = emp.id
                AND :currentDate BETWEEN lr.startDate AND lr.endDate
            WHERE emp.isDeleted = false
            """)
    DashboardDTO getDashboardData(LocalDate currentDate);

    boolean existsByEmail(String email);
}
