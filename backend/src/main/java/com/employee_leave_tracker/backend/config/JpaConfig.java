package com.employee_leave_tracker.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "userIdProvider")
public class JpaConfig {

    @Bean
    public AuditorAware<Long> userIdProvider() {
        return new AuditorAwareImpl();
    }
}