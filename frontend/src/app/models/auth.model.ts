
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
