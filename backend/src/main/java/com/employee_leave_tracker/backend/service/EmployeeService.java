package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.employee.*;

import java.util.Collection;

public interface EmployeeService {

    String createOrUpdateEmployee(EmployeeReqDTO reqDto);

    EmployeeResDTO getEmployeeById(Long id);

    void deleteEmployee(Long id);

    Collection<EmployeeTableResDTO> getAllEmployees();


    Collection<DepartmentResDTO> getAllDepartments();

    Collection<DesignationResDTO> getAllDesignations();

}
