package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.model.leave.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {

    @Query("""
                SELECT lp FROM LeavePolicy lp
                JOIN FETCH lp.leaveType
                WHERE lp.employmentType = :employmentType
                  AND lp.isActive = true
                  AND lp.effectiveFrom <= CURRENT_DATE
                  AND (lp.effectiveTo IS NULL OR lp.effectiveTo >= CURRENT_DATE)
            """)
    List<LeavePolicy> findAllActivePolicies(String employmentType);


    @Query("SELECT lp FROM LeavePolicy lp " +
            "WHERE lp.leaveType.id = :leaveTypeId " +
            "AND lp.employmentType = :employmentType " +
            "AND lp.isActive = true " +
            "AND lp.effectiveFrom <= CURRENT_DATE " +
            "AND (lp.effectiveTo IS NULL OR lp.effectiveTo >= CURRENT_DATE)")
    Optional<LeavePolicy> findActiveByLeaveTypeIdAndEmploymentType(
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("employmentType") String employmentType
    );

    List<LeavePolicy> findByIsActive(Boolean isActive);

}
