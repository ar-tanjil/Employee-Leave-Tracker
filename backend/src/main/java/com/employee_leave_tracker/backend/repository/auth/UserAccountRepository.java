package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.UserAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @EntityGraph(attributePaths = "employee")
    Optional<UserAccount> findByUsername(String username);

    boolean existsByEmployeeId(Long employeeId);

    @EntityGraph(attributePaths = "userRoles")
    Optional<UserAccount> findByEmployeeId(Long id);

    @Query("""
                SELECT u.id FROM UserAccount u
                JOIN UserRole ur ON ur.user.id = u.id
                WHERE ur.role.name = :role
                ORDER BY u.id ASC
            """)
    Optional<Long> findFirstByRole(String role);

    @Query("""
                SELECT u.id FROM UserAccount u
                WHERE u.employee.id = (
                    SELECT e.manager.id FROM Employee e WHERE e.id = :employeeId
                )
            """)
    Optional<Long> findManagerUserIdByEmployeeId(Long employeeId);

}
