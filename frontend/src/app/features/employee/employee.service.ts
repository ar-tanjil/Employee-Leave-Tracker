import { inject, Injectable, signal } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { EmployeeReq, EmployeeRes, EmployeeTable } from '../../models/employee.model';
import { PaginationParams } from '../../models/api-response.model';

@Injectable()
export class EmployeeService {

  private api = inject(ApiService);

  // state
  private _selectedEmployeeId = signal<number | null>(null);
  readonly selectedEmployeeId = this._selectedEmployeeId.asReadonly();

  selectedEmployee() {
    return this._selectedEmployeeId();
  }

  setSelectedEmployee(employee: number | null) {
    this._selectedEmployeeId.set(employee ? employee : null);
  }


  getEmployees(params: PaginationParams) {
    return this.api.getPaged<EmployeeTable>('v1/employees', params);
  }

  getEmployeeById(id: number) {
    return this.api.get<EmployeeRes>(`v1/employees/${id}`);
  }


  saveAndUpdateEmployee(employee: EmployeeReq) {
    return this.api.post<{ message: string }, EmployeeReq>('v1/employees', employee);
  }

  deleteEmployee(id: number) {
    return this.api.delete(`v1/employees/${id}`);
  }

}
