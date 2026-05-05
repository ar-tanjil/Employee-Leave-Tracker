import { DashboardRes } from './../../models/common.model';
import { inject, Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {

  private readonly api = inject(ApiService);


  getDashboardData(){
    return this.api.get<DashboardRes>("v1/employees/dashboard");
  }

}
