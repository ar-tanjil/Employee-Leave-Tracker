package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.model.leave.LeaveComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveCommentRepository extends JpaRepository<LeaveComment, Long> {
}
