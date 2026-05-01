package com.employee_leave_tracker.backend.mapper;

import com.employee_leave_tracker.backend.dto.employee.EmployeeReqDto;
import com.employee_leave_tracker.backend.dto.employee.EmployeeResDto;
import com.employee_leave_tracker.backend.dto.employee.EmployeeTableResDto;
import com.employee_leave_tracker.backend.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    Employee toEmployee(EmployeeReqDto employeeReqDto);

    EmployeeReqDto toEmployeeReqDto(Employee employee);

    EmployeeResDto toEmployeeResDto(Employee employee);

    EmployeeTableResDto toEmployeeTableResDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    void updateEmployeeFromReqDto(EmployeeReqDto reqDto, @MappingTarget Employee employee);
}
