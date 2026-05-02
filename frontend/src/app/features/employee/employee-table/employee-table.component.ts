import { EmployeeRes, EmployeeTable } from './../../../models/employee.model';
import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { TableComponent } from "../../../shared/components/table/table.component";
import { EmployeeService } from '../employee.service';
import { ColumnDef, TableQueryEvent } from '../../../models/table.models';
import { PaginationParams } from '../../../models/api-response.model';
import { EmployeeFormComponent } from "../employee-form/employee-form.component";
import { EmployeeViewComponent } from "../employee-view/employee-view.component";
import Swal from 'sweetalert2';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-employee-table',
  imports: [TableComponent, EmployeeFormComponent, EmployeeViewComponent],
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

  readonly employee = signal<EmployeeRes | null>(null);

  // state
  readonly loading = signal(false);
  readonly showForm = signal(false);
  readonly showView = signal(false);

  readonly page = computed(() => ({
    data: this.employeeList(),
    total: this.total(),
  }));

  constructor() {
  effect(() => {
    const isVisible = this.showForm() || this.showView();

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
    }

    if (event.sort) {
      params.sortBy = event.sort.key;
      params.sortOrder = event.sort.dir;
    }

    this.employeeService.getEmployees(params).subscribe(response => {
      this.employeeList.set(response.data);
      this.total.set(response.metaData.totalElements);
      this.loading.set(false);
    });;
  }


  openForm(id: number | null): void {
    if (!id) {
      return this.showForm.set(true);
    }

    this.employeeService.getEmployeeById(id!).subscribe({
      next: (emp) => {
        this.employee.set(emp);
        this.showForm.set(true);
      }
    });

  }

  openViewDetails(id: number | null): void {
    if (!id) return;

    this.employeeService.getEmployeeById(id!).subscribe({
      next: (emp) => {
        this.employee.set(emp);
        this.showView.set(true);
      }
    });
  }


  onSaved(): void {
    this.onQueryChange({ page: { page: 1, pageSize: 10 }, sort: null });
    this.showForm.set(false);
    this.employeeService.setSelectedEmployee(null);
  }

  get canDelete() {
    return this.authService.hasRoles(['ROLE_SYSTEM_ADMIN', 'EMPLOYEE:DELETE']);
  }

  async confirmDelete(employee: EmployeeTable): Promise<void> {

    const result = await Swal.fire({
      title: `Delete Employee ${employee.firstName}`,
      text: "Are you sure you want to delete this employee?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Delete',
      cancelButtonText: 'No'
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
        }
      });
    }
  }


  // columns definition
  columnsDef: ColumnDef<EmployeeTable>[] = [
    { key: 'id', label: '#', sortable: true, headerClass: 'w-14' },
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
