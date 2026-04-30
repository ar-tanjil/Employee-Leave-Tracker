package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.model.Employee;
import com.employee_leave_tracker.backend.model.Role;
import com.employee_leave_tracker.backend.model.UserAccount;
import com.employee_leave_tracker.backend.model.UserRole;
import com.employee_leave_tracker.backend.repository.EmployeeRepository;
import com.employee_leave_tracker.backend.repository.RoleRepository;
import com.employee_leave_tracker.backend.repository.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.UserRoleRepository;
import com.employee_leave_tracker.backend.service.UserProvisioningService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProvisioningServiceImp implements UserProvisioningService {

    private final EmployeeRepository employeeRepo;
    private final UserAccountRepository userAccountRepo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_ROLE = "EMPLOYEE";

    @Override
    public void provisionUserFromEmployee(Long employeeId, String rawPassword) {
        if (userAccountRepo.existsByEmployeeId(employeeId)) {
            throw new IllegalStateException("User account already exists for employee: " + employeeId);
        }

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));

        String username = resolveUsername(employee);
        UserAccount account = UserAccount.builder()
                .employee(employee)
                .username(username)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        UserAccount saved = userAccountRepo.save(account);


        assignDefaultRole(saved);
    }

    /**
     * Assigns the default role to the user account.
     * @param userAccount the user account to which the default role will be assigned
     */
    private void assignDefaultRole(UserAccount userAccount) {
        Role defaultRole = roleRepo.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default EMPLOYEE role not seeded"));

        UserRole userRole = UserRole.builder()
                .user(userAccount)
                .role(defaultRole)
                .assignedAt(LocalDateTime.now())
                .build();

        userRoleRepo.save(userRole);
    }

    /**
     * Resolves the username for the given employee.
     * @param employee the employee for which the username will be resolved
     * @return the resolved username
     */
    private String resolveUsername(Employee employee) {
        return Optional.ofNullable(employee.getEmail())
                .filter(e -> !e.isBlank())
                .orElse(employee.getEmployeeCode().toLowerCase());
    }
}
