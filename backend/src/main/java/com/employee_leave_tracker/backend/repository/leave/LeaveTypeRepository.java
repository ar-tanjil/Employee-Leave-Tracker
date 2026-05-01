package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.model.leave.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    List<LeaveType> findByIsActive(boolean isActive);
}
