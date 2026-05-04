package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.auth.PermissionDTO;
import com.employee_leave_tracker.backend.dto.auth.RoleAssignReqDTO;
import com.employee_leave_tracker.backend.dto.auth.RoleDTO;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.model.auth.Role;
import com.employee_leave_tracker.backend.model.auth.UserAccount;
import com.employee_leave_tracker.backend.model.auth.UserRole;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.repository.auth.RoleRepository;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.auth.UserRoleRepository;
import com.employee_leave_tracker.backend.repository.employee.EmployeeRepository;
import com.employee_leave_tracker.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<RoleDTO> getAllActiveRoles() {
        List<Role> roles = roleRepository.findAllRoles();

        return roles.stream().map(this::mapToRoleDTO).toList();
    }


    @Override
    public List<RoleDTO> getRolesByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        return roleRepository.findAllRolesByEmployeeId(employee.getId())
                .stream().map(role -> mapToRoleDTO(role, employee)).toList();
    }

    private RoleDTO mapToRoleDTO(Role role) {
        return mapToRoleDTO(role, null);
    }

    private RoleDTO mapToRoleDTO(Role role, Employee employee) {
        String fullName = null;
        String employeeCode = null;
        if (employee != null) {
            fullName = employee.getFirstName() + " " + employee.getLastName();
            employeeCode = employee.getEmployeeCode();
        }

        return new RoleDTO(
                role.getId(),
                role.getName(),
                fullName,
                employeeCode,
                role.getDescription(),
                role.getRolePermissions()
                        .stream()
                        .map(rp -> new PermissionDTO(
                                rp.getPermission().getId(),
                                rp.getPermission().getResource() + ":" + rp.getPermission().getAction(),
                                rp.getPermission().getDescription()
                        ))
                        .toList()
        );
    }

    @Override
    @Transactional
    public String assignRoles(RoleAssignReqDTO request) {

        UserAccount user = userAccountRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new NoDataFoundException("User not found"));

        Set<Long> requestedRoleIds = new HashSet<>(request.roleIds());

        List<UserRole> existingUserRoles = userRoleRepository.findByUser(user.getId());

        Set<Long> existingRoleIds = existingUserRoles.stream()
                .map(ur -> ur.getRole().getId())
                .collect(Collectors.toSet());

        // add set
        Set<Long> roleIdsToAdd = new HashSet<>(requestedRoleIds);
        roleIdsToAdd.removeAll(existingRoleIds);

        // remove set
        Set<Long> roleIdsToRemove = new HashSet<>(existingRoleIds);
        roleIdsToRemove.removeAll(requestedRoleIds);


        // remove user role
        if (!roleIdsToRemove.isEmpty()) {
            userRoleRepository.deleteByUserAndRoleIds(user.getId(), roleIdsToRemove);
        }


        // add user role
        if (!roleIdsToAdd.isEmpty()) {

            List<Role> rolesToAdd = roleRepository.findAllById(roleIdsToAdd);

            List<UserRole> newUserRoles = rolesToAdd.stream()
                    .map(role -> UserRole.builder()
                            .user(user)
                            .role(role)
                            .build())
                    .toList();

            userRoleRepository.saveAll(newUserRoles);
        }

        return "Roles updated successfully";
    }
}
