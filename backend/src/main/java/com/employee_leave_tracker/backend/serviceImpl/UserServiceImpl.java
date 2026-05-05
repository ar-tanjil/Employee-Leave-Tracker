package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.auth.PermissionDTO;
import com.employee_leave_tracker.backend.dto.auth.RoleAssignReqDTO;
import com.employee_leave_tracker.backend.dto.auth.RoleDTO;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
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

        Set<Long> requestedRoleIds = new HashSet<>(request.roleIds());

        UserAccount user = userAccountRepository.findWithEmployeeAndDepartmentByEmployeeId(request.employeeId())
                .orElseThrow(() -> new NoDataFoundException("User or Employee details not found"));


        Set<Long> existingRoleIds = userRoleRepository.findRoleIdsByUserId(user.getId());

        // add or remove id list
        Set<Long> roleIdsToAdd = requestedRoleIds.stream()
                .filter(id -> !existingRoleIds.contains(id))
                .collect(Collectors.toSet());

        Set<Long> roleIdsToRemove = existingRoleIds.stream()
                .filter(id -> !requestedRoleIds.contains(id))
                .collect(Collectors.toSet());

        // delete
        if (!roleIdsToRemove.isEmpty()) {
            userRoleRepository.deleteByUserIdAndRoleIds(user.getId(), roleIdsToRemove);
        }

        // add
        if (!roleIdsToAdd.isEmpty()) {
            List<Role> rolesToAdd = roleRepository.findAllById(roleIdsToAdd);

            // validate
            validateHrRoleAssignment(user.getEmployee(), rolesToAdd);
            validateManagerRoleAssignment(user.getEmployee());

            List<UserRole> newUserRoles = rolesToAdd.stream()
                    .map(role -> UserRole.builder().user(user).role(role).build())
                    .toList();

            userRoleRepository.saveAll(newUserRoles);
        }

        return "Roles updated successfully";
    }

    private void validateHrRoleAssignment(Employee employee, List<Role> rolesToAdd) {
        boolean isHrAdminRequested = rolesToAdd.stream()
                .anyMatch(role -> "HR_ADMIN".equalsIgnoreCase(role.getName()));

        if (isHrAdminRequested && !"HR".equalsIgnoreCase(employee.getDepartment().getCode())) {
            throw new ArgumentNotValidException("Only HR department members can be assigned the HR_ADMIN role.");
        }
    }

    private void validateManagerRoleAssignment(Employee employee) {
        if ( employeeRepository.existsManagerForDepartment(employee.getDepartment().getId())) {
            throw new ArgumentNotValidException("Department already has a manager");
        }
    }
}
