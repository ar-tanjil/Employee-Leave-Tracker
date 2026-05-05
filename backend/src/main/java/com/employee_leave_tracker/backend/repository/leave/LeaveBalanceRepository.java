package com.employee_leave_tracker.backend.repository.leave;

import com.employee_leave_tracker.backend.model.leave.LeaveBalance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    @EntityGraph(attributePaths = {"leavePolicy", "leaveType"})
    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeIdAndYear(Long empId, Long typeId, Integer year);

    boolean existsByEmployeeIdAndLeaveTypeIdAndYear(Long empId, Long typeId, Integer year);

    @Query("""
                SELECT lb.leaveType.id FROM LeaveBalance lb
                WHERE lb.employee.id = :empId
                AND lb.year <= :year
            """)
    List<Long> findLeaveTypeIdsByEmployeeAndYear(Long empId, int year);

    List<LeaveBalance> findByEmployeeIdAndYear(Long employeeId, Integer year);

    @Query("SELECT lb FROM LeaveBalance lb " +
            "WHERE lb.employee.id = :employeeId " +
            "AND lb.year = :year " +
            "AND lb.availableDays > 0")
    List<LeaveBalance> findAvailableBalances(
            @Param("employeeId") Long employeeId,
            @Param("year") Integer year
    );

    @Query("""
            select lb from LeaveBalance lb
            join fetch lb.leaveType
            where lb.employee.id = :employeeId
            and lb.year = :year
            """)
    List<LeaveBalance> findAllByEmployeeIdAndYear(Long employeeId, Integer year);
}
