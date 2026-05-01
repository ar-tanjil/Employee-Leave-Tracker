package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.model.leave.LeaveApprovalInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveApprovalInstanceRepository extends JpaRepository<LeaveApprovalInstance, Long> {

    Optional<LeaveApprovalInstance> findTopByLeaveRequestIdOrderByStepOrderDesc(Long requestId);
}
