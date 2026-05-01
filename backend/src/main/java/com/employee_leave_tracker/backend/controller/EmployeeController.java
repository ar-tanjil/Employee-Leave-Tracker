package com.employee_leave_tracker.backend.controller;

import com.employee_leave_tracker.backend.dto.employee.EmployeeReqDto;
import com.employee_leave_tracker.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createOrUpdateEmployee(@RequestBody @Valid EmployeeReqDto reqDto) {
        return ResponseEntity.ok(employeeService.createOrUpdateEmployee(reqDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }


    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(employeeService.getAllDepartments());
    }

    @GetMapping("/designations")
    public ResponseEntity<?> getAllDesignations() {
        return ResponseEntity.ok(employeeService.getAllDesignations());
    }

}