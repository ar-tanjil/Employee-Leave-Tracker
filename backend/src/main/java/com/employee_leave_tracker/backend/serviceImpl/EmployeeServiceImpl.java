package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.employee.*;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.mapper.EmployeeMapper;
import com.employee_leave_tracker.backend.model.Department;
import com.employee_leave_tracker.backend.model.Designation;
import com.employee_leave_tracker.backend.model.Employee;
import com.employee_leave_tracker.backend.repository.DepartmentRepository;
import com.employee_leave_tracker.backend.repository.DesignationRepository;
import com.employee_leave_tracker.backend.repository.EmployeeRepository;
import com.employee_leave_tracker.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    @Override
    @Transactional
    public SuccessResponse<EmployeeResDto> createOrUpdateEmployee(EmployeeReqDto reqDto) {
        Employee employee;

        if (reqDto.id() == null) {
            Department department = departmentRepository.findById(reqDto.departmentId())
                    .orElseThrow(() -> new NoDataFoundException("Department not found"));
            Designation designation = designationRepository.findById(reqDto.designationId())
                    .orElseThrow(() -> new NoDataFoundException("Designation not found"));

            Employee manager = null;
            if (reqDto.managerId() != null) {
                manager = employeeRepository.findById(reqDto.managerId())
                        .orElseThrow(() -> new NoDataFoundException("Manager not found"));

            }

            employee = employeeMapper.toEmployee(reqDto);
            employee.setDepartment(department);
            employee.setDesignation(designation);
            employee.setManager(manager);

            employee = employeeRepository.save(employee);

        } else {
            employee = employeeRepository.findById(reqDto.id())
                    .orElseThrow(() -> new NoDataFoundException("Employee not found"));
            employeeMapper.updateEmployeeFromReqDto(reqDto, employee);
        }

        return new SuccessResponse<>(employeeMapper.toEmployeeResDto(employee));
    }

    @Override
    public SuccessResponse<EmployeeResDto> getEmployeeById(Long id) {
        var response = employeeMapper.toEmployeeResDto(employeeRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("Employee not found")));
        return new SuccessResponse<>(response);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("Employee not found"));

        employee.setIsDeleted(true);
    }

    @Override
    public ListResponse<EmployeeTableResDto> getAllEmployees() {
        var response = employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeTableResDto)
                .toList();
        return new ListResponse<>(response);
    }

    @Override
    public ListResponse<DepartmentResDto> getAllDepartments() {
        var response = departmentRepository.findAllByIsActiveTrue();
        return new ListResponse<>(response);
    }

    @Override
    public ListResponse<DesignationResDto> getAllDesignations() {
        var response = designationRepository.findAllByIsActiveTrue();
        return new ListResponse<>(response);
    }
}
