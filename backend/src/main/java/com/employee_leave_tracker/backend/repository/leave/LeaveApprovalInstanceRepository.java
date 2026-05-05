package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.model.leave.LeaveApprovalInstance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LeaveApprovalInstanceRepository extends JpaRepository<LeaveApprovalInstance, Long> {

    Optional<LeaveApprovalInstance> findTopByLeaveRequestIdOrderByStepOrderDesc(Long requestId);

    @EntityGraph(attributePaths = {"leaveRequest"})
    Optional<LeaveApprovalInstance> findByIdAndIsActiveTrue(Long referenceId);

    List<LeaveApprovalInstance> findByLeaveRequestIdOrderByStepOrderAsc(Long leaveRequestId);

    @Query("""
            SELECT lai
            FROM LeaveApprovalInstance lai
            JOIN FETCH lai.leaveRequest lr
            JOIN FETCH lr.employee
            JOIN FETCH lr.leaveType
            WHERE lai.approverId = :approverId
            AND lai.isActive = true
            """)
    List<LeaveApprovalInstance> findByApproverIdAndStatusPending(Long approverId);
}
