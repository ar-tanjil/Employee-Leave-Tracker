package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.leave.LeaveReqDTO;

public interface LeaveRequestService {

    String applyLeave(LeaveReqDTO dto);

    void cancelLeave(Long requestId);
}
