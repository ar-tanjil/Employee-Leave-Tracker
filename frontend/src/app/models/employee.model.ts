export interface EmployeeTable {
  id: number;
  firstName: string;
  lastName: string;
  designation: string;
  department: string;
}


export type EmployeeReq = {
  id?: number;
  employeeCode?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  departmentId?: number;
  designationId?: number;
  managerId?: number;
  hireDate?: Date;
  employmentType?: string;
}

export type EmployeeRes = {
  id?: number;
  employeeCode?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  departmentId?: null;
  departmentName?: string;
  designationId?: null;
  designationName?: string;
  managerId?: null;
  managerName?: null;
  hireDate?: Date;
  employmentType?: string;
  avatarUrl?: string;
  location?: string;
}



