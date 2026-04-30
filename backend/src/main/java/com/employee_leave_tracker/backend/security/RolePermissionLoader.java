package com.employee_leave_tracker.backend.security;

import com.employee_leave_tracker.backend.projection.UserRoleProjection;
import com.employee_leave_tracker.backend.repository.RolePermissionRepository;
import com.employee_leave_tracker.backend.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RolePermissionLoader {

    private final UserRoleRepository userRoleRepo;
    private final RolePermissionRepository rolePermissionRepo;

    /**
     * Returns flat GrantedAuthority set combining:
     *   ROLE_<roleName>  +  <permissionCode> per role
     */
    @Transactional(readOnly = true)
    public Set<GrantedAuthority> loadAuthorities(Long userAccountId) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        List<UserRoleProjection> userRoles = userRoleRepo.findRolesByUserAccountId(userAccountId);

        for (UserRoleProjection ur : userRoles) {
            // Add role authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + ur.getRoleName()));

            // Add all permissions assigned to this role
            List<String> permissions = rolePermissionRepo.findPermissionCodesByRoleId(ur.getRoleId());
            permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        return authorities;
    }
}
