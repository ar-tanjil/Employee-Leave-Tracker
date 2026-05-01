package com.employee_leave_tracker.backend.config;

import com.employee_leave_tracker.backend.model.auth.Role;
import com.employee_leave_tracker.backend.model.auth.UserAccount;
import com.employee_leave_tracker.backend.model.auth.UserRole;
import com.employee_leave_tracker.backend.repository.auth.RoleRepository;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.auth.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {

        boolean isExist = userRepository.findByUsername("admin")
                .isPresent();

        if (isExist) {
            return;
        }

        UserAccount user = UserAccount.builder()
                .username("admin")
                .passwordHash(passwordEncoder.encode("admin@123#"))
                .status("ACTIVE")
                .isDeleted(false)
                .build();

        userRepository.save(user);

        Role role = roleRepository.findByName("SYSTEM_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        userRoleRepository.save(UserRole.builder()
                .user(user)
                .role(role)
                .build());
    }
}
