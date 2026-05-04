
export type AuthState = {
  token: string | null;
  user: User | null;
  expiresAt: number | null;
}

export type User = {
  username: string;
  roles: string[];
  email?: string;
}


export type UserLogin = {
  username: string;
  password: string;
}

export type RoleInfo = {
  id: number;
  name: string;
  employeeName?: string;
  employeeCode?: string;
  description?: string;
  permissions?: PermissionInfo[];
}

export type PermissionInfo = {
  id: number;
  name: string;
  description?: string;
}
