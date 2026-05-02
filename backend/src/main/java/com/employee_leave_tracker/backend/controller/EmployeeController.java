package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.PageResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.employee.EmployeeReqDTO;
import com.employee_leave_tracker.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Only users with this permission granted via role_permission mapping
//    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW_ALL')")
//    @GetMapping
//    public List<EmployeeSummary> list() { /* ... */ }
//
//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('EMPLOYEE_MANAGE')")
//    @PostMapping("/{id}/provision")
//    public void provision(@PathVariable Long id) { /* ... */ }


    @PostMapping
    public ResponseEntity<CustomResponse> createOrUpdateEmployee(@RequestBody @Valid EmployeeReqDTO reqDto) {

        var response = employeeService.createOrUpdateEmployee(reqDto);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getEmployeeById(@PathVariable Long id) {
        var response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }


    @GetMapping
    public ResponseEntity<CustomResponse> getAllEmployees(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        var response = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(new PageResponse<>(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM_ADMIN') or hasAuthority('EMPLOYEE:MANAGE')")
    public ResponseEntity<CustomResponse> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new SuccessResponse<>("Employee deleted successfully"));
    }


    @GetMapping("/departments")
    public ResponseEntity<CustomResponse> getAllDepartments() {
        var response = employeeService.getAllDepartments();
        return ResponseEntity.ok(new ListResponse<>(response));
    }

    @GetMapping("/designations")
    public ResponseEntity<CustomResponse> getAllDesignations() {
        var response = employeeService.getAllDesignations();
        return ResponseEntity.ok(new ListResponse<>(response));
    }

}