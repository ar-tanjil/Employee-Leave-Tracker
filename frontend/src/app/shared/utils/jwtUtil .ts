import { AuthState, User } from "../../models/auth.model";

export class JwtUtil {

  static decodeToken(token: string): any | null {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(decoded);
    } catch (e) {
      console.error('Invalid JWT token', e);
      return null;
    }
  }

  static buildAuthState(token: string): AuthState {
    const decoded = this.decodeToken(token);

    if (!decoded) {
      return {
        token: null,
        user: null,
        expiresAt: null
      };
    }

    return {
      token: token,
      user: this.extractUser(decoded),
      expiresAt: this.getExpiration(decoded)
    };
  }

  static extractUser(decoded: any): User {
    return {
      username: decoded.sub || decoded.username || '',
      roles: this.extractRoles(decoded),
      email: decoded.email || ''
    };
  }

  static extractRoles(decoded: any): string[] {
    if (decoded.roles) return decoded.roles;
    if (decoded.authorities) return decoded.authorities;

    if (typeof decoded.role === 'string') {
      return [decoded.role];
    }

    return [];
  }

  static getExpiration(decoded: any): number | null {
    if (!decoded.exp) return null;

    return decoded.exp * 1000;
  }

  static isTokenExpired(token: string): boolean {
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;

    return Date.now() > decoded.exp * 1000;
  }
}
