package com.employee_leave_tracker.backend.repository.auth;

import com.employee_leave_tracker.backend.model.auth.UserAccount;
import com.employee_leave_tracker.backend.model.auth.UserRole;
import com.employee_leave_tracker.backend.repository.projection.UserRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("""
                SELECT ur.role.id    AS roleId,
                       ur.role.name  AS roleName
                FROM   UserRole ur
                WHERE  ur.user.id = :userAccountId
            """)
    List<UserRoleProjection> findRolesByUserAccountId(@Param("userAccountId") Long userAccountId);

    @Query("""
                SELECT ur
                FROM   UserRole ur
                JOIN FETCH ur.role
                WHERE  ur.user.id = :userId
            """)
    List<UserRole> findByUser(Long userId);


    @Modifying
    @Query("""
                DELETE FROM UserRole ur
                WHERE ur.user.id = :userId
                AND ur.role.id IN :roleIds
            """)
    void deleteByUserIdAndRoleIds(Long userId, Set<Long> roleIds);

    @Query("SELECT DISTINCT ur.role.id FROM UserRole ur WHERE ur.user.id = :userId")
    Set<Long> findRoleIdsByUserId(@Param("userId") Long userId);
}