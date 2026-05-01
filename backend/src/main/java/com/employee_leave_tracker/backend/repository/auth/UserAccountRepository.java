package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.UserAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @EntityGraph(attributePaths = "employee")
    Optional<UserAccount> findByUsername(String username);

    boolean existsByEmployeeId(Long employeeId);

    Optional<UserAccount> findByEmployeeId(Long id);
}
