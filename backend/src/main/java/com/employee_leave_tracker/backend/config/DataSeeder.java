package com.employee_leave_tracker.backend.config;

import com.employee_leave_tracker.backend.model.Role;
import com.employee_leave_tracker.backend.model.UserAccount;
import com.employee_leave_tracker.backend.model.UserRole;
import com.employee_leave_tracker.backend.repository.RoleRepository;
import com.employee_leave_tracker.backend.repository.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .createdAt(LocalDateTime.now())
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
