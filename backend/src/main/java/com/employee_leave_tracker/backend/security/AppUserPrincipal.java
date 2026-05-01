package com.employee_leave_tracker.backend.security;

import com.employee_leave_tracker.backend.model.auth.UserAccount;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
public class AppUserPrincipal implements UserDetails {

    private final Long userId;
    private final Long employeeId;
    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<GrantedAuthority> authorities;

    public AppUserPrincipal(UserAccount account, Set<GrantedAuthority> authorities) {
        this.userId = account.getId();
        this.employeeId = account.getEmployee() != null ? account.getEmployee().getId() : null;
        this.username = account.getUsername();
        this.password = account.getPasswordHash();
        this.active = account.isActive();
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

