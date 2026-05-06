package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.UserAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @EntityGraph(attributePaths = "employee")
    Optional<UserAccount> findByUsernameAndIsDeletedFalse(String username);

    boolean existsByEmployeeId(Long employeeId);

    @EntityGraph(attributePaths = "userRoles")
    Optional<UserAccount> findByEmployeeId(Long id);

    @Query("""
                SELECT u.id FROM UserAccount u
                JOIN u.userRoles ur
                JOIN Employee e ON e.id = u.employee.id
                WHERE ur.role.name = :role
            """)
    Optional<Long> findManagerUserIdByRole(String role);

    @Query("""
                SELECT u.id FROM UserAccount u
                WHERE u.employee.id = (
                    SELECT e.id FROM Employee e
                    JOIN e.department d
                    JOIN UserAccount ua ON ua.employee.id = e.id
                    JOIN ua.userRoles ur ON ur.role.name = 'MANAGER'
                    WHERE d.id = :departmentId
                )
            """)
    Optional<Long> findManagerUserIdByDepartmentId(Long departmentId);

    @Query("""
            SELECT u
            FROM UserAccount u
            JOIN FETCH Employee e ON u.employee.id = e.id
            JOIN FETCH Department d ON e.department.id = d.id
            WHERE e.id = :employeeId
            """)
    Optional<UserAccount> findWithEmployeeAndDepartmentByEmployeeId(Long employeeId);
}
