package com.employee_leave_tracker.backend.mapper;

import com.employee_leave_tracker.backend.dto.employee.EmployeeReqDTO;
import com.employee_leave_tracker.backend.dto.employee.EmployeeResDTO;
import com.employee_leave_tracker.backend.dto.employee.EmployeeTableResDTO;
import com.employee_leave_tracker.backend.model.employee.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    Employee toEmployee(EmployeeReqDTO employeeReqDto);

    @Mapping(target = "designationName", source = "designation.name")
    @Mapping(target = "designationId", source = "designation.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "departmentId", source = "department.id")
    EmployeeResDTO toEmployeeResDto(Employee employee);

    @Mapping(target = "designation", source = "designation.name")
    @Mapping(target = "department", source = "department.name")
    EmployeeTableResDTO toEmployeeTableResDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    void updateEmployeeFromReqDto(EmployeeReqDTO reqDto, @MappingTarget Employee employee);
}
