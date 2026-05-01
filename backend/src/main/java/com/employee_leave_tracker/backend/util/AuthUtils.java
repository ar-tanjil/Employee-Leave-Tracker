package com.employee_leave_tracker.backend.util;

import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.UnauthorizedException;
import com.employee_leave_tracker.backend.security.AppUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public final class AuthUtils {


    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public AppUserPrincipal getCurrentUser() {
        Authentication auth = getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof AppUserPrincipal userPrincipal) {
            return userPrincipal;
        }

        throw new UnauthorizedException("Invalid user principal");
    }


    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public Long getCurrentUserEmployeeId() {
        Long employeeId = getCurrentUser().getEmployeeId();

        if (employeeId == null) {
            throw new ArgumentNotValidException("Employee ID is not available");
        }

        return employeeId;
    }

    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }


    public boolean isAuthenticated() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated();
    }


    public boolean hasRole(String role) {
        return hasAuthority("ROLE_" + role);
    }


    public boolean hasAuthority(String authority) {
        Authentication auth = getAuthentication();

        if (auth == null) return false;

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.stream()
                .anyMatch(a -> authority.equals(a.getAuthority()));
    }


    public boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) return true;
        }
        return false;
    }


    public boolean hasAnyAuthority(String... authorities) {
        for (String authority : authorities) {
            if (hasAuthority(authority)) return true;
        }
        return false;
    }
}
