import { EmployeeRes, EmployeeTable } from './../../../models/employee.model';
import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { EmployeeService } from '../employee.service';
import { ColumnDef, TableQueryEvent } from '../../../models/table.models';
import { PaginationParams } from '../../../models/api-response.model';
import { EmployeeFormComponent } from '../employee-form/employee-form.component';
import { EmployeeViewComponent } from '../employee-view/employee-view.component';
import Swal from 'sweetalert2';
import { AuthService } from '../../../core/services/auth.service';
import { finalize } from 'rxjs';
import { TableComponent } from '../../../shared/components/table/table.component';
import { TableCellDirective } from '../../../shared/directives/table-cell.directive';
import { RoleAssignComponent } from '../role-assign/role-assign.component';
import { RoleInfo } from '../../../models/auth.model';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { ProfileComponent } from "../../profile/profile.component";

@Component({
  selector: 'app-employee-table',
  imports: [TableComponent, EmployeeFormComponent, TableCellDirective, RoleAssignComponent, DialogComponent, ProfileComponent],
  templateUrl: './employee-table.component.html',
  styleUrl: './employee-table.component.css',
})
export class EmployeeTableComponent {
  // inject
  private employeeService = inject(EmployeeService);
  private readonly authService = inject(AuthService);

  // data
  private readonly employeeList = signal<EmployeeTable[]>([]);
  private readonly total = signal(0);

  // child component data
  readonly selectedEmployeeId = signal<number | null>(null);
  readonly employee = signal<EmployeeRes | null>(null);
  readonly roleInfo = signal<RoleInfo | null>(null);

  // state
  readonly loading = signal(false);
  readonly showForm = signal(false);
  readonly showView = signal(false);
  readonly showAsignRole = signal(false);

  readonly pageData = computed(() => ({
    data: this.employeeList(),
    total: this.total(),
  }));

  constructor() {
    effect(() => {
      const isVisible = this.showForm() || this.showView() || this.showAsignRole();

      // If both are closed, clear the employee
      if (!isVisible) {
        // Use untracked if you don't want this effect to re-run
        // just because the employee signal changed
        untracked(() => this.employee.set(null));
      }
    });
  }

  // query change handler
  onQueryChange(event: TableQueryEvent): void {
    this.loading.set(true);
    // buid pagenation and sorting params
    let params: PaginationParams = {
      page: event.page.page - 1,
      pageSize: event.page.pageSize,
    };

    if (event.sort) {
      params.sortBy = event.sort.key;
      params.sortOrder = event.sort.dir;
    }

    this.employeeService
      .getEmployees(params)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe((response) => {
        this.employeeList.set(response.data);
        this.total.set(response.metaData.totalElements);
      });
  }

  openForm(id: number | null): void {
    if (!id) {
      return this.showForm.set(true);
    }

    this.employeeService.getEmployeeById(id!).subscribe({
      next: (emp) => {
        this.employee.set(emp);
        this.showForm.set(true);
      },
    });
  }

  openViewDetails(id: number | null): void {
    if (!id) return;
    this.selectedEmployeeId.set(id);

    this.employeeService.getEmployeeById(id!).subscribe({
      next: (emp) => {
        this.employee.set(emp);
        this.showView.set(true);
      },
    });
  }

  onSaved(): void {
    this.onQueryChange({ page: { page: 1, pageSize: 10 }, sort: null });
    this.showForm.set(false);
    this.employeeService.setSelectedEmployee(null);
  }

  openAsignRole(id: number | null): void {
    if (!id) {
      return;
    }
    this.selectedEmployeeId.set(id);
    this.showAsignRole.set(true);
  }

  onAsigned(): void {
    this.showAsignRole.set(false);
    this.employeeService.setSelectedEmployee(null);
  }

  get hasDeletePermission() {
    return this.authService.hasRoles(['ROLE_SYSTEM_ADMIN', 'EMPLOYEE:DELETE']);
  }

  async confirmDelete(employee: EmployeeTable): Promise<void> {
    const result = await Swal.fire({
      title: `Delete Employee ${employee.firstName}`,
      text: 'Are you sure you want to delete this employee?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'No',
    });

    if (result.isConfirmed) {
      // Perform action
      this.employeeService.deleteEmployee(employee.id).subscribe({
        next: () => {
          Swal.fire('Deleted!', 'Employee has been deleted.', 'success');
          this.onQueryChange({ page: { page: 1, pageSize: 10 }, sort: null });
        },
        error: () => {
          Swal.fire('Error!', 'Failed to delete employee.', 'error');
        },
      });
    }
  }

  // columns definition
  columnsDef: ColumnDef<EmployeeTable>[] = [
    {
      key: 'firstName',
      label: 'Name',
      sortable: false,
      class: 'font-medium text-slate-900',
    },
    { key: 'designation', label: 'Designation', sortable: false, class: 'text-slate-500' },
    { key: 'department', label: 'Department', sortable: false },
  ];
}
