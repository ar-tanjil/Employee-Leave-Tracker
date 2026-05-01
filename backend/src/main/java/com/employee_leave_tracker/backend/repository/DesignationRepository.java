package com.employee_leave_tracker.backend.repository;

import com.employee_leave_tracker.backend.dto.employee.DepartmentResDto;
import com.employee_leave_tracker.backend.dto.employee.DesignationResDto;
import com.employee_leave_tracker.backend.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
    Collection<DesignationResDto> findAllByIsActiveTrue();
}
