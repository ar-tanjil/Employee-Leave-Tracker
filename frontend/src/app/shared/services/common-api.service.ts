import { Injectable, inject } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { IDName } from '../../models/common.model';

@Injectable({
  providedIn: 'root',
})
export class CommonApiService {

  private readonly api = inject(ApiService);


  getDepartmentList() {
    return this.api.get<IDName[]>('v1/employees/departments');
  }

  getDesignationList() {
    return this.api.get<IDName[]>('v1/employees/designations')
      .pipe(
      // If your API wraps the data, you might need to map it here
      // For example, if the response is { data: IDName[] }, you would do:
      // map(response => response.data)
    );
  }

}
