package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.DashboardDTO;
import com.employee_leave_tracker.backend.dto.employee.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface EmployeeService {

    String createOrUpdateEmployee(EmployeeReqDTO reqDto) throws Exception;

    EmployeeResDTO getEmployeeById(Long id);

    void deleteEmployee(Long id);

    Page<EmployeeTableResDTO> getAllEmployees(Pageable pageable);


    Collection<DepartmentResDTO> getAllDepartments();

    Collection<DesignationResDTO> getAllDesignations();

    DashboardDTO getDashboardData();
}
