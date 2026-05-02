import { Component } from '@angular/core';
import { EmployeeTableComponent } from "./employee-table/employee-table.component";
import { EmployeeService } from './employee.service';

@Component({
  selector: 'app-employee',
  imports: [EmployeeTableComponent],
  providers: [EmployeeService],
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css',
})
export class EmployeeComponent { }
