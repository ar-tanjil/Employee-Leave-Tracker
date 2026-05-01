package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.employee.*;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.mapper.EmployeeMapper;
import com.employee_leave_tracker.backend.model.employee.Department;
import com.employee_leave_tracker.backend.model.employee.Designation;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.model.leave.LeaveType;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.employee.DepartmentRepository;
import com.employee_leave_tracker.backend.repository.employee.DesignationRepository;
import com.employee_leave_tracker.backend.repository.employee.EmployeeRepository;
import com.employee_leave_tracker.backend.repository.leave.LeaveTypeRepository;
import com.employee_leave_tracker.backend.service.EmployeeService;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import com.employee_leave_tracker.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final AuthUtils authUtils;
    private final EmployeeMapper employeeMapper;
    private final UserAccountRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceService leaveBalanceService;

    @Override
    @Transactional
    public String createOrUpdateEmployee(EmployeeReqDTO reqDto) {
        Employee employee;
        String message;

        if (reqDto.id() == null) {
            message = "Employee created successfully";
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
            employee.setIsActive(true);
            employee.setIsDeleted(false);

            employeeRepository.save(employee);

        } else {
            message = "Employee updated successfully";
            employee = employeeRepository.findById(reqDto.id())
                    .orElseThrow(() -> new NoDataFoundException("Employee not found"));
            employeeMapper.updateEmployeeFromReqDto(reqDto, employee);
        }

        initializeEmployeeLeaveBalances(employee);

        return message;
    }

    private void initializeEmployeeLeaveBalances(Employee employee) {
        List<LeaveType> activeLeaveTypes = leaveTypeRepository.findByIsActive(true);
        for (LeaveType leaveType : activeLeaveTypes) {
            // This will create balance only if active policy exists
            leaveBalanceService.getOrCreateBalance(employee.getId(), leaveType.getId());
        }
    }

    @Override
    public EmployeeResDTO getEmployeeById(Long id) {
        return employeeMapper.toEmployeeResDto(employeeRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("Employee not found")));
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("Employee not found"));

        employee.setIsDeleted(true);

        userAccountRepository.findByEmployeeId(id)
                .ifPresent(account -> account.setIsDeleted(true));

    }

    @Override
    public Collection<EmployeeTableResDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeTableResDto)
                .toList();
    }

    @Override
    public Collection<DepartmentResDTO> getAllDepartments() {
        return departmentRepository.findAllByIsActiveTrue();

    }

    @Override
    public Collection<DesignationResDTO> getAllDesignations() {
        return designationRepository.findAllByIsActiveTrue();
    }
}
