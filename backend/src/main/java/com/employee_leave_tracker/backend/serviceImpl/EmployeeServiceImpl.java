package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.dto.DashboardDTO;
import com.employee_leave_tracker.backend.dto.employee.*;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.exception.NoDataFoundException;
import com.employee_leave_tracker.backend.mapper.EmployeeMapper;
import com.employee_leave_tracker.backend.model.employee.Department;
import com.employee_leave_tracker.backend.model.employee.Designation;
import com.employee_leave_tracker.backend.model.employee.Employee;
import com.employee_leave_tracker.backend.repository.auth.UserAccountRepository;
import com.employee_leave_tracker.backend.repository.employee.DepartmentRepository;
import com.employee_leave_tracker.backend.repository.employee.DesignationRepository;
import com.employee_leave_tracker.backend.repository.employee.EmployeeRepository;
import com.employee_leave_tracker.backend.service.EmployeeService;
import com.employee_leave_tracker.backend.service.LeaveBalanceService;
import com.employee_leave_tracker.backend.service.UserProvisioningService;
import com.employee_leave_tracker.backend.util.FileServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final UserProvisioningService userProvisioningService;
    private final FileServiceUtil fileServiceUtil;
    private final UserAccountRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final LeaveBalanceService leaveBalanceService;

    @Override
    @Transactional
    public String createOrUpdateEmployee(EmployeeReqDTO reqDto) throws Exception {
        Employee employee;
        String message;

        if (reqDto.id() == null) {
            message = "Employee created successfully";

            // check department and designation
            Department department = departmentRepository.findById(reqDto.departmentId())
                    .orElseThrow(() -> new NoDataFoundException("Department not found"));
            Designation designation = designationRepository.findById(reqDto.designationId())
                    .orElseThrow(() -> new NoDataFoundException("Designation not found"));

            // check email
            if (employeeRepository.existsByEmail(reqDto.email())) {
                throw new ArgumentNotValidException("Email already exists");
            }

            employee = employeeMapper.toEmployee(reqDto);
            employee.setDepartment(department);
            employee.setDesignation(designation);
            employee.setIsActive(true);
            employee.setIsDeleted(false);

            if (reqDto.image() != null) {
                employee.setImage(fileServiceUtil.convertImageToBase64(reqDto.image()));
            }

            // save
            employeeRepository.save(employee);

            // user creation
            userProvisioningService.provisionUserFromEmployee(employee);

            // leave balance need optimization
            leaveBalanceService.createEmployeeLeaveBalances(employee, employee.getHireDate().getYear());

        } else {
            message = "Employee updated successfully";
            employee = employeeRepository.findById(reqDto.id())
                    .orElseThrow(() -> new NoDataFoundException("Employee not found"));
            employeeMapper.updateEmployeeFromReqDto(reqDto, employee);

            if (reqDto.image() != null) {
                employee.setImage(fileServiceUtil.convertImageToBase64(reqDto.image()));
            }
        }


        return message;
    }


    @Override
    public EmployeeResDTO getEmployeeById(Long id) {
        return employeeMapper.toEmployeeResDto(employeeRepository.findEmployeeById(id)
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
    public Page<EmployeeTableResDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAllEmployee(pageable)
                .map(employeeMapper::toEmployeeTableResDto);
    }

    @Override
    public Collection<DepartmentResDTO> getAllDepartments() {
        return departmentRepository.findAllByIsActiveTrue();

    }

    @Override
    public Collection<DesignationResDTO> getAllDesignations() {
        return designationRepository.findAllByIsActiveTrue();
    }

    @Override
    public DashboardDTO getDashboardData() {
        LocalDate currentDate = LocalDate.now();
        return employeeRepository.getDashboardData(currentDate);

    }
}
