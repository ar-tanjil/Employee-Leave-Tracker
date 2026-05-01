package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.dto.leave.LeaveResDTO;
import com.employee_leave_tracker.backend.model.leave.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
            SELECT COUNT(l) > 0 FROM LeaveRequest l
            WHERE l.employee.id = :empId
            AND l.status != 'REJECTED'
            AND l.status != 'CANCELLED'
            AND :startDate <= l.endDate
            AND :endDate >= l.startDate
            """)
    boolean existsOverlappingRequest(@Param("empId") Long empId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    List<LeaveResDTO> findByEmployeeId(Long employeeId);
}
