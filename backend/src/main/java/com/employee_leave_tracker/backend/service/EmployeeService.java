package com.employee_leave_tracker.backend.service;

import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.employee.*;

public interface EmployeeService {

    SuccessResponse<EmployeeResDto> createOrUpdateEmployee(EmployeeReqDto reqDto);

    SuccessResponse<EmployeeResDto> getEmployeeById(Long id);

    void deleteEmployee(Long id);

    ListResponse<EmployeeTableResDto> getAllEmployees();


    ListResponse<DepartmentResDto> getAllDepartments();

    ListResponse<DesignationResDto> getAllDesignations();

}
