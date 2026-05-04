package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional<Role> findByName(String defaultRole);


   @Query("""
           SELECT distinct r
           FROM Role r
           LEFT JOIN FETCH r.rolePermissions rp
           LEFT JOIN FETCH rp.permission p
           WHERE r.isSystemRole = false
           """)
   List<Role> findAllRoles();

   @Query("""
           SELECT distinct r
           FROM Role r
           LEFT JOIN FETCH r.rolePermissions rp
           LEFT JOIN FETCH rp.permission p
           LEFT JOIN UserRole ur ON r.id = ur.role.id
           LEFT JOIN UserAccount ua ON ur.user.id = ua.id
           WHERE ua.employee.id = :employeeId
           """)
   List<Role> findAllRolesByEmployeeId(Long employeeId);

   List<Role> findAllByIdIn(List<Long> longs);
}
