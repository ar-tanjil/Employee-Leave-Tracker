import { inject, Injectable } from "@angular/core";
import { ApiService } from "../../core/services/api.service";
import { EmployeeRes } from "../../models/employee.model";

@Injectable()
export class ProfileService {
  private api = inject(ApiService);

  getUserProfile() {
    return this.api.get<EmployeeRes>(`v1/users/profile`);
  }

  changePassword(payload: {
    oldPassword: string;
    newPassword: string;
  }) {
    return this.api.post(`v1/auth/change-password`, payload);
  }
}
