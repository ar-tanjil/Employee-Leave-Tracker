package com.employee_leave_tracker.backend.config;

import com.employee_leave_tracker.backend.model.auth.UserAccount;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class DataSeeder {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .cleanDisabled(false) // Allows flyway.clean()
                .load();
    }

    @Bean
    @Order(1)
    public CommandLineRunner runFlywayAfterHibernate(Flyway flyway) {
        return args -> {
//            // Clean the DB (since you want to wipe it)
//            flyway.clean();
            // Run the migration (Insert your data)
            flyway.migrate();
        };
    }


    @Bean
    @Order(2)
    public CommandLineRunner hashAdminPassword(UserAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            UserAccount user = userRepository.findByUsernameAndIsDeletedFalse("admin")
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));

            if ("admin@123#".equalsIgnoreCase(user.getPasswordHash())) {

                user.setPasswordHash(passwordEncoder.encode("admin@123#"));
                userRepository.save(user);

            }
        };
    }

}
