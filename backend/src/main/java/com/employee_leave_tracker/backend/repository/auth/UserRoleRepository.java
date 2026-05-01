package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.UserRole;
import com.employee_leave_tracker.backend.projection.UserRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("""
        SELECT ur.role.id    AS roleId,
               ur.role.name  AS roleName
        FROM   UserRole ur
        WHERE  ur.user.id = :userAccountId
    """)
    List<UserRoleProjection> findRolesByUserAccountId(@Param("userAccountId") Long userAccountId);
}