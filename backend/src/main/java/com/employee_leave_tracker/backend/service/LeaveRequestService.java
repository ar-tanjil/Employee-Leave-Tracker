package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.leave.LeaveReqDTO;
import com.employee_leave_tracker.backend.dto.leave.LeaveResDTO;

import java.util.List;

public interface LeaveRequestService {

    String applyLeave(LeaveReqDTO dto);

    List<LeaveResDTO> getLeaveRequestsByEmployee();

    void cancelLeave(Long requestId);
}
