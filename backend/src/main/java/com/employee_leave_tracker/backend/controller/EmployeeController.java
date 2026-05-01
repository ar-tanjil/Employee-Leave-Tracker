package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.CustomResponse;
import com.employee_leave_tracker.backend.dto.ListResponse;
import com.employee_leave_tracker.backend.dto.SuccessResponse;
import com.employee_leave_tracker.backend.dto.employee.EmployeeReqDTO;
import com.employee_leave_tracker.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CustomResponse> getAllEmployees() {
        var response = employeeService.getAllEmployees();
        return ResponseEntity.ok(new ListResponse<>(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('EMPLOYEE:MANAGE')")
    public ResponseEntity<CustomResponse> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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