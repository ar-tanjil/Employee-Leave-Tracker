package com.employee_leave_tracker.backend.config;

import com.employee_leave_tracker.backend.security.AppUserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> (AppUserPrincipal) auth.getPrincipal())
                .map(AppUserPrincipal::getUserId);
    }
}