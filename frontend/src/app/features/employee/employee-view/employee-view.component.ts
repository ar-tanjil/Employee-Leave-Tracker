import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { EmployeeRes } from '../../../models/employee.model';
import { DatePipe } from '@angular/common';
import { EmployeeService } from '../employee.service';

@Component({
  selector: 'app-employee-view',
  imports: [DialogComponent, DatePipe],
  templateUrl: './employee-view.component.html',
  styleUrl: './employee-view.component.css',
})
export class EmployeeViewComponent {

  // inject
  private readonly employeeService = inject(EmployeeService);

  // input
  employee = input<EmployeeRes>(null!);

  // output event to notify parent component when the view modal is closed
  readonly onClose = output<void>();
  readonly onEdit = output<number>();


}
