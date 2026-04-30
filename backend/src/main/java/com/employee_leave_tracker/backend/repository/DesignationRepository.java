package com.employee_leave_tracker.backend.repository;

import com.employee_leave_tracker.backend.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
}
