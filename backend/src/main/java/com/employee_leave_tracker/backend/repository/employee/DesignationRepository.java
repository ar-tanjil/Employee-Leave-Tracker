package com.employee_leave_tracker.backend.repository.employee;

import com.employee_leave_tracker.backend.dto.employee.DesignationResDTO;
import com.employee_leave_tracker.backend.model.employee.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
    Collection<DesignationResDTO> findAllByIsActiveTrue();
}
